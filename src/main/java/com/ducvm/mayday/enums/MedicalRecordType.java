package com.ducvm.mayday.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MedicalRecordType {
	/**
	 * Acute record type.
	 */
	ACUTE("CAP"),
	/**
	 * Chronic record type.
	 */
	CHRONIC("MAN");

	private final String type;

}
