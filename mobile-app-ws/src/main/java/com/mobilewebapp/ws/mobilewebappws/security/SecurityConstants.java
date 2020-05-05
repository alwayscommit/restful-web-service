package com.mobilewebapp.ws.mobilewebappws.security;

import com.mobilewebapp.ws.mobilewebappws.SpringAppContext;

public class SecurityConstants {

	public static final long EXPIRATION_TIME = 864000000L; //10d in milliseconds
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	public static final String USER_ID = "UserId";
	
	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringAppContext.getBean("appProperties");
		return appProperties.getTokenSecret();
	}

}
