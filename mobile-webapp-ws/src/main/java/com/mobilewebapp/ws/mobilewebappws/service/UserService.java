package com.mobilewebapp.ws.mobilewebappws.service;

import com.mobilewebapp.ws.mobilewebappws.dto.UserDto;
import com.mobilewebapp.ws.mobilewebappws.model.request.UserRequest;

public interface UserService {

	UserDto createUser(UserDto userDto);

}
