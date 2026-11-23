package com.ducvm.mayday.exceptions;

import com.ducvm.mayday.enums.ResponseCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

	private final ResponseCode responseCode;

	public ApiException(ResponseCode responseCode) {
		super(responseCode.getMessageVi());
		this.responseCode = responseCode;
	}
}
