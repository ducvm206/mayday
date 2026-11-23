
package com.ducvm.mayday.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum Sex {
	MALE("Nam", "male"),
	FEMALE("Nu", "female"),
	OTHER("Khac", "other");

	private final String value;     // stored in nguoi.gioi_tinh
	private final String apiValue;  // used in the API

	public static Optional<Sex> fromApiValue(String v) {
		if (v == null) return Optional.empty();
		return Arrays.stream(values())
				.filter(s -> s.apiValue.equalsIgnoreCase(v.trim()))
				.findFirst();
	}

	public static Sex fromDbValue(String v) {
		return Arrays.stream(values())
				.filter(s -> s.value.equals(v))
				.findFirst().orElse(null);
	}
}

