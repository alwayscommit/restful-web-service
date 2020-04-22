package com.mobilewebapp.ws.mobilewebappws.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

	@PostMapping
	public String createUser() {
		return "Created a User...";
	}

	@GetMapping
	public String fetchUser() {
		return "Fetched a User...";
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
