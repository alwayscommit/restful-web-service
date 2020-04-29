package com.mobilewebapp.ws.mobilewebappws.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobilewebapp.ws.mobilewebappws.dto.UserDto;
import com.mobilewebapp.ws.mobilewebappws.exception.UserServiceException;
import com.mobilewebapp.ws.mobilewebappws.model.request.UserRequest;
import com.mobilewebapp.ws.mobilewebappws.model.response.ErrorMessages;
import com.mobilewebapp.ws.mobilewebappws.model.response.UserRest;
import com.mobilewebapp.ws.mobilewebappws.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserRequest userRequest) throws Exception {

		if (userRequest.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		System.out.println("Creating a user...");

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userRequest, userDto);

		UserDto savedUserDto = userService.createUser(userDto);

		UserRest userResponse = new UserRest();
		BeanUtils.copyProperties(savedUserDto, userResponse);

		return userResponse;

	}

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest fetchUser(@PathVariable String id) {
		UserRest userResponse = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, userResponse);
		return userResponse;
	}

	@DeleteMapping
	public String deleteUser() {
		return "Deleted a User...";
	}

	@PutMapping
	public String updateUser() {
		return "Updated a User...";
	}

}
