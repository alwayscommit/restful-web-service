package com.mobilewebapp.ws.mobilewebappws.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobilewebapp.ws.mobilewebappws.dto.UserDto;
import com.mobilewebapp.ws.mobilewebappws.exception.UserServiceException;
import com.mobilewebapp.ws.mobilewebappws.model.request.UserRequest;
import com.mobilewebapp.ws.mobilewebappws.model.response.ErrorMessages;
import com.mobilewebapp.ws.mobilewebappws.model.response.OperationStatusModel;
import com.mobilewebapp.ws.mobilewebappws.model.response.RequestOperationName;
import com.mobilewebapp.ws.mobilewebappws.model.response.RequestOperationStatus;
import com.mobilewebapp.ws.mobilewebappws.model.response.UserRest;
import com.mobilewebapp.ws.mobilewebappws.service.UserService;

@RestController
@RequestMapping("users") // http://localhost:8080/ this is the root
//context path = http://localhost:8080/mobile-app-ws/
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

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userRequest, userDto);

		UserDto savedUserDto = userService.updateUser(id, userDto);

		UserRest userResponse = new UserRest();
		BeanUtils.copyProperties(savedUserDto, userResponse);

		return userResponse;
	}

	@DeleteMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);
		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnUserList = new ArrayList<UserRest>();

		List<UserDto> users = userService.getUsers(page, limit);

		for (UserDto user : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(user, userModel);
			returnUserList.add(userModel);
		}

		return returnUserList;
	}

}
