package com.ccp.exceptions.email;

@SuppressWarnings("serial")
public class InvalidEmail extends RuntimeException{

	public InvalidEmail(String email) {
		super(email + " is invalid e-mail");
	}
	
}
