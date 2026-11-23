package com.ducvm.mayday.exceptions;

public class UserExistedException extends RuntimeException {
	public UserExistedException(String message) { super(message); }
}
