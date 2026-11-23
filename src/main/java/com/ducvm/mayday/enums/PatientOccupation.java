package com.ducvm.mayday.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PatientOccupation {
	WORKER("Cong nhan"),
	FARMER("Nong dan"),
	STUDENT("Hoc sinh - Sinh vien"),
	CIVIL_SERVANT("Can bo cong chuc"),
	OTHER("Khac");

	private final String value;
}
