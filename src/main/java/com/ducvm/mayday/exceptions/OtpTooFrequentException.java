package com.ducvm.mayday.exceptions;

public class OtpTooFrequentException extends RuntimeException {
	public OtpTooFrequentException() { super("OTP requested too frequently"); }
}
