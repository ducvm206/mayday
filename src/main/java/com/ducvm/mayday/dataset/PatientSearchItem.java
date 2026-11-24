package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PatientSearchItem(
		@JsonProperty("user_id") Long userId,

		@JsonProperty("full_name") String fullName,

		String phone,

		String gender,

		@JsonProperty("year_of_birth") Integer yearOfBirth
) {}
