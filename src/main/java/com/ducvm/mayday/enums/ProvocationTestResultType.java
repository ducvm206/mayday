package com.ducvm.mayday.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProvocationTestResultType {

	/**
	 * Positive test result.
	 */
	POSITIVE("Duong tinh"),

	/**
	 * Negative test result.
	 */
	NEGATIVE("Am tinh");

	private final String result;
}
