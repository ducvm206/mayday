package com.ducvm.mayday.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Roles {

	ADMIN(1, "admin"),

	DOCTOR(2, "bac_si"),

	NURSE(3, "nurse"),

	PATIENT(4, "benh_nhan");

	private final int id;
	private final String name;

	public static Roles fromId(int id) {
		return Arrays.stream(values())
				.filter(role -> role.getId() == id)
				.findFirst()
				.orElseThrow(() ->
						new IllegalArgumentException("Invalid role ID: " + id));
	}
}
