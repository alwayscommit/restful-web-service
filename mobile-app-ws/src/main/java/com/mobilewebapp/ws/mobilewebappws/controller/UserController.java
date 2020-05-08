package com.mobilewebapp.ws.mobilewebappws.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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

import com.mobilewebapp.ws.mobilewebappws.dto.AddressDto;
import com.mobilewebapp.ws.mobilewebappws.dto.UserDto;
import com.mobilewebapp.ws.mobilewebappws.exception.UserServiceException;
import com.mobilewebapp.ws.mobilewebappws.model.request.UserRequest;
import com.mobilewebapp.ws.mobilewebappws.model.response.AddressRest;
import com.mobilewebapp.ws.mobilewebappws.model.response.ErrorMessages;
import com.mobilewebapp.ws.mobilewebappws.model.response.OperationStatusModel;
import com.mobilewebapp.ws.mobilewebappws.model.response.RequestOperationName;
import com.mobilewebapp.ws.mobilewebappws.model.response.RequestOperationStatus;
import com.mobilewebapp.ws.mobilewebappws.model.response.UserRest;
import com.mobilewebapp.ws.mobilewebappws.service.AddressService;
import com.mobilewebapp.ws.mobilewebappws.service.UserService;

@RestController
@RequestMapping("/users") // http://localhost:8080/ this is the root
//context path = http://localhost:8080/mobile-app-ws/
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserRequest userRequest) throws Exception {

		if (userRequest.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		System.out.println("Creating a user...");

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userRequest, UserDto.class);

		// Doesn't do a good job with nested objects, lists, does a shallow copy.
		/*
		 * UserDto userDto = new UserDto(); BeanUtils.copyProperties(userRequest,
		 * userDto);
		 */

		UserDto savedUserDto = userService.createUser(userDto);
		UserRest userResponse = modelMapper.map(savedUserDto, UserRest.class);

		return userResponse;

	}

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest fetchUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);

		return returnValue;
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

	@GetMapping(path = "/{userId}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public Resources<AddressRest> fetchUserAddresses(@PathVariable String userId) {
		List<AddressRest> addressListRest = new ArrayList<AddressRest>();

		List<AddressDto> addressDto = addressService.getAddresses(userId);

		if (addressDto != null && !addressDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressRest>>() {
			}.getType();
			ModelMapper modelMapper = new ModelMapper();
			addressListRest = modelMapper.map(addressDto, listType);

			for (AddressRest addRes : addressListRest) {
				// The response generated by this is different for some reason, as it is within
				// a list maybe
				// Which is why we use HAL - Hypertext Application Language, a simple format
				// that gives consistent and easy way to hyperlink between resources in API
				Link userLink = linkTo(methodOn(UserController.class).fetchUser(userId)).withRel("user");
				addRes.add(userLink);

				Link addressLink = linkTo(
						methodOn(UserController.class).fetchUserAddress(userId, addRes.getAddressId())).withSelfRel();
				addRes.add(addressLink);
			}

		}

		return new Resources<>(addressListRest);
	}

	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public Resource<AddressRest> fetchUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		AddressDto addressDto = addressService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();

		// HATEOAS link formation
		// Link addressLink =
		// linkTo(UserController.class).slash(userId).slash("addresses").slash(addressId).withSelfRel();
		// Instead of hard coding the paths to form API links, you use the methodOn
		// method which does that automatically.

		Link addressLink = linkTo(methodOn(UserController.class).fetchUserAddress(userId, addressId)).withSelfRel();
		Link userLink = linkTo(methodOn(UserController.class).fetchUser(userId)).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).fetchUserAddresses(userId)).withRel("addresses");

		AddressRest returnValue = modelMapper.map(addressDto, AddressRest.class);
		returnValue.add(addressLink);
		returnValue.add(userLink);
		returnValue.add(addressesLink);

		return new Resource<AddressRest>(returnValue);
	}
	
	@GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
        
        boolean isVerified = userService.verifyEmailToken(token);
        
        if(isVerified)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }
 
        return returnValue;
    }
	
	

}
