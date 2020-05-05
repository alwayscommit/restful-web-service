package com.mobilewebapp.ws.mobilewebappws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.mobilewebapp.ws.mobilewebappws.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDto);

	UserDto getUser(String email);

	UserDto getUserByUserId(String id);

	UserDto updateUser(String id, UserDto userDto);

	void deleteUser(String id);

	List<UserDto> getUsers(int page, int limit);

}
