package com.ducvm.mayday.validator;

import com.ducvm.mayday.dataset.PersonRequest;
import com.ducvm.mayday.enums.Sex;
import com.ducvm.mayday.util.Constant;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PersonValidator {

	public Map<String, String> validate(PersonRequest req) {

		Map<String, String> errors = new LinkedHashMap<>();

		if (req == null) {
			errors.put("request", "Request body is required");
			return errors;
		}

		String name = req.getFullName();

		if (isEmpty(name)) {
			errors.put("full_name", "Full name is required");
		} else if (!name.trim().matches(Constant.FULL_NAME_REGEX)) {
			errors.put("full_name", "Full name must contain fewer than 64 characters");
		}

		String phone = req.getPhone();

		if (isEmpty(phone)) {
			errors.put("phone", "Phone number is required");
		} else if (!phone.trim().matches(Constant.PHONE_NUMBER_REGEX)) {
			errors.put("phone", "Phone number must be a valid Vietnamese number (10 digits)");
		}

		String pw = req.getPassword();

		if (isEmpty(pw)) {
			errors.put("password", "Password is required");
		} else if (pw.length() < Constant.PASSWORD_MIN_LENGTH
				|| pw.length() > Constant.PASSWORD_MAX_LENGTH) {
			errors.put("password", "Password must be "
					+ Constant.PASSWORD_MIN_LENGTH + "-"
					+ Constant.PASSWORD_MAX_LENGTH + " characters");
		}

		if (!isEmpty(req.getGender())
				&& Sex.fromApiValue(req.getGender()).isEmpty()) {
			errors.put("gender", "Gender must be 'male', 'female' or 'other'");
		}

		return errors;
	}

	private boolean isEmpty(String v) {
		return v == null || v.isBlank();
	}
}
