package com.ducvm.mayday.util;

public class Constant {

	public static final String FULL_NAME_REGEX = "^.{1,63}$";

	public static final String PHONE_NUMBER_REGEX = "^0[35789][0-9]{8}$";

	public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,32}$";

	public static final long OTP_EXPIRY_SECONDS = 300;

	public static final long OTP_RESEND_COOLDOWN_SECONDS = 60;

	public static final int PASSWORD_MIN_LENGTH = 8;

	public static final int PASSWORD_MAX_LENGTH = 32;

}
