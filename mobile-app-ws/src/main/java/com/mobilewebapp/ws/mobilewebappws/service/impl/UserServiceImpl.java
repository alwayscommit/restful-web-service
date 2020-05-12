package com.mobilewebapp.ws.mobilewebappws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobilewebapp.ws.mobilewebappws.dto.AddressDto;
import com.mobilewebapp.ws.mobilewebappws.dto.UserDto;
import com.mobilewebapp.ws.mobilewebappws.io.entity.UserEntity;
import com.mobilewebapp.ws.mobilewebappws.model.response.ErrorMessages;
import com.mobilewebapp.ws.mobilewebappws.repo.UserRepository;
import com.mobilewebapp.ws.mobilewebappws.service.UserService;
import com.mobilewebapp.ws.mobilewebappws.shared.AmazonSES;
import com.mobilewebapp.ws.mobilewebappws.util.Utils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private Utils utils;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDto createUser(UserDto userDto) {

		UserEntity existingUser = userRepo.findByEmail(userDto.getEmail());

		if (existingUser != null) {
			throw new RuntimeException("Email already exists!");
		}

		for (int i = 0; i < userDto.getAddresses().size(); i++) {
			AddressDto address = userDto.getAddresses().get(i);
			address.setUserDetails(userDto);
			address.setAddressId(utils.generateAddressId(30));
			userDto.getAddresses().set(i, address);
		}

		ModelMapper modelMapper = new ModelMapper();
		UserEntity newUserEntity = modelMapper.map(userDto, UserEntity.class);

		// randomly generated userId
		String userId = utils.generateUserId(25);
		newUserEntity.setUserId(userId);
		newUserEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
		newUserEntity.setEmailVerificationToken(utils.generateEmailVerfToken(userId));
		newUserEntity.setEmailVerificationStatus(false);
		UserEntity savedUserEntity = userRepo.save(newUserEntity);

		UserDto returnUserDto = modelMapper.map(savedUserEntity, UserDto.class);

		new AmazonSES().sendVerificationEmail(returnUserDto);
		
		return returnUserDto;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepo.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
//		 return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new
//		 ArrayList<>());
//		passing of userEntity.getEmailVerificationStatus() will prevent the user from logging
//		in if the email is not verified
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
				userEntity.getEmailVerificationStatus(), true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepo.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}

	@Override
	public UserDto getUserByUserId(String id) {
		UserEntity userEntity = userRepo.findByUserId(id);

		if (userEntity == null) {
			throw new UsernameNotFoundException("User with id " + id + " not found!");
		}

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}

	@Override
	public UserDto updateUser(String id, UserDto userDto) {
		UserEntity userEntity = userRepo.findByUserId(id);
		if (userEntity == null) {
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());

		UserEntity savedUserEntity = userRepo.save(userEntity);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(savedUserEntity, returnValue);

		return returnValue;
	}

	@Override
	public void deleteUser(String id) {
		UserEntity userEntity = userRepo.findByUserId(id);
		if (userEntity == null) {
			throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		userRepo.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnUserDtoList = new ArrayList<UserDto>();

		if (page > 0) {
			page = page - 1;
		}

		Pageable pageReq = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = userRepo.findAll(pageReq);
		List<UserEntity> users = usersPage.getContent();

		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnUserDtoList.add(userDto);
		}

		return returnUserDtoList;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		boolean returnVerified = false;

		// Find user by token
		UserEntity userEntity = userRepo.findUserByEmailVerificationToken(token);

		if (userEntity != null) {
			boolean hastokenExpired = Utils.hasTokenExpired(token);
			if (!hastokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepo.save(userEntity);
				returnVerified = true;
			}
		}

		return returnVerified;
	}

}
