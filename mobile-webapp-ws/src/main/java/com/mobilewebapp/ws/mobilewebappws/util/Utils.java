package com.mobilewebapp.ws.mobilewebappws.util;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();
	private String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public String generateUserId(int length) {
		return generateUserIdString(length);
	}

	private String generateUserIdString(int length) {
		StringBuilder string = new StringBuilder();

		for (int i = 0; i < length; i++) {
			string.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(string);
	}

}
