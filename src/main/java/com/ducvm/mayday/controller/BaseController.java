package com.ducvm.mayday.controller;

import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class BaseController {

	public ResponseEntity<Map<String, Object>> respond(
			HttpStatus status, ResponseCode code, String message, Object data) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("code", String.valueOf(code.getCode()));
		body.put("message", message);
		body.put("data", data);
		return ResponseEntity.status(status).body(body);
	}

	public ResponseEntity<Map<String, Object>> handleApiException(ApiException e) {
		ResponseCode code = e.getResponseCode();

		HttpStatus status = switch (code) {
			case USER_EXISTED -> HttpStatus.CONFLICT;
			case OBJECT_NOT_EXISTED -> HttpStatus.NOT_FOUND;
			case PARAMETER_NOT_ENOUGH,
			     PARAMETER_TYPE_INVALID,
			     PARAMETER_VALUE_INVALID -> HttpStatus.BAD_REQUEST;
			default -> HttpStatus.BAD_REQUEST;
		};

		return respond(status, code, code.getMessageVi(), null);
	}

}
