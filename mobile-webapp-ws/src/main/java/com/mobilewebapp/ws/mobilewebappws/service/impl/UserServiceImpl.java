package com.mobilewebapp.ws.mobilewebappws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobilewebapp.ws.mobilewebappws.dto.UserDto;
import com.mobilewebapp.ws.mobilewebappws.io.entity.UserEntity;
import com.mobilewebapp.ws.mobilewebappws.repo.UserRepository;
import com.mobilewebapp.ws.mobilewebappws.service.UserService;
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

		UserEntity newUserEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, newUserEntity);

		newUserEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

		// randomly generated userId
		String userId = utils.generateUserId(25);
		newUserEntity.setUserId(userId);

		UserEntity savedUserEntity = userRepo.save(newUserEntity);

		UserDto returnUserDto = new UserDto();
		BeanUtils.copyProperties(savedUserEntity, returnUserDto);

		return returnUserDto;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepo.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

}
