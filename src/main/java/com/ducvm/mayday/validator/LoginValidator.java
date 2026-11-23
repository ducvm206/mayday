package com.ducvm.mayday.validator;

import com.ducvm.mayday.dataset.LoginRequest;
import com.ducvm.mayday.util.Constant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class LoginValidator {
	public Map<String, String> validate(LoginRequest req) {
		Map<String, String> errors = new HashMap<>();

		if (req == null) {
			errors.put("request", "Request body is required");
			return errors;
		}


		// Phone number
		if (isEmpty(req.getPhone())) {
			errors.put("phoneNumber", "Phone number is required");
		} else if (!matches(req.getPhone(), Constant.PHONE_NUMBER_REGEX)) {
			errors.put("phoneNumber", "Phone number must contain exactly 10 or 11 digits");
		}

		// Password
		if (isEmpty(req.getPassword())) {
			errors.put("password", "Password is required");
		}

		return errors;
	}

	private boolean isEmpty(String value) {
		return value == null || value.isBlank();
	}

	private boolean matches(String value, String regex) {
		return Pattern.matches(regex, value);
	}
}
