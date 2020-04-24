package com.mobilewebapp.ws.mobilewebappws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	public UserDto createUser(UserDto userDto) {

		UserEntity existingUser = userRepo.findByEmail(userDto.getEmail());

		if (existingUser != null) {
			throw new RuntimeException("Email already exists!");
		}

		UserEntity newUserEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, newUserEntity);

		// Take care of fields that are not nullable and aren't a part of the UI
		// interaction as of now
//		newUserEntity.setUserId("test");
		newUserEntity.setEncryptedPassword("test");

		// randomly generated userId
		String userId = utils.generateUserId(25);
		newUserEntity.setUserId(userId);
		
		UserEntity savedUserEntity = userRepo.save(newUserEntity);

		UserDto returnUserDto = new UserDto();
		BeanUtils.copyProperties(savedUserEntity, returnUserDto);

		return returnUserDto;
	}

}
