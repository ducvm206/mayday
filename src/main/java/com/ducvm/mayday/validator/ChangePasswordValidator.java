package com.ducvm.mayday.validator;

import com.ducvm.mayday.dataset.ChangePasswordRequest;
import com.ducvm.mayday.util.Constant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChangePasswordValidator {

	public Map<String, String> validate(ChangePasswordRequest req) {
		Map<String, String> errors = new HashMap<>();

		if (req == null) {
			errors.put("request", "Request body is required");
			return errors;
		}

		if (isEmpty(req.getOldPassword())) {
			errors.put("old_password", "Old password is required");
		}

		String np = req.getNewPassword();
		if (isEmpty(np)) {
			errors.put("new_password", "New password is required");
		} else if (np.length() < Constant.PASSWORD_MIN_LENGTH
				|| np.length() > Constant.PASSWORD_MAX_LENGTH) {
			errors.put("new_password", "New password must be "
					+ Constant.PASSWORD_MIN_LENGTH + "-"
					+ Constant.PASSWORD_MAX_LENGTH + " characters");
		}

		if (isEmpty(req.getConfirmPassword())) {
			errors.put("confirm_password", "Confirm password is required");
		} else if (!errors.containsKey("new_password")
				&& !req.getConfirmPassword().equals(np)) {
			errors.put("confirm_password", "Confirm password does not match new password");
		}

		return errors;
	}

	private boolean isEmpty(String v) {
		return v == null || v.isBlank();
	}
}
