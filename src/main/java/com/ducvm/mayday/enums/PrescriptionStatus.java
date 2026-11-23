package com.ducvm.mayday.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrescriptionStatus {

	/**
	 * Prescription is currently being followed.
	 */
	IN_TREATMENT("DANG_DIEU_TRI"),

	/**
	 * Treatment has been completed.
	 */
	COMPLETED("HOAN_THANH"),

	/**
	 * Prescription has been cancelled.
	 */
	CANCELLED("HUY");

	private final String status;
}
