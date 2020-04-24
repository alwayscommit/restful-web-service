package com.mobilewebapp.ws.mobilewebappws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.mobilewebapp.ws.mobilewebappws.dto.UserDto;

public interface UserService extends UserDetailsService{

	UserDto createUser(UserDto userDto);

}
