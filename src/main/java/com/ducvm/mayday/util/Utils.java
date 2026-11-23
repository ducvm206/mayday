package com.ducvm.mayday.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Utils {

	private static final DateTimeFormatter DATE_FORMAT =
			DateTimeFormatter.ofPattern("dd/MM/uuuu")
					.withResolverStyle(ResolverStyle.STRICT);

	/** Returns null if the input is null, blank or not a real date. */
	public static LocalDate convertStringToDate(String date) {
		if (date == null || date.isBlank()) {
			return null;
		}
		try {
			return LocalDate.parse(date.trim(), DATE_FORMAT);
		} catch (DateTimeParseException e) {
			return null;
		}
	}
}