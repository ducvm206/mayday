package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProfileRequest(

		@JsonProperty("full_name")
		String fullName,

		@JsonProperty("gender")
		String gender,

		@JsonProperty("date_of_birth")
		String dateOfBirth,

		@JsonProperty("career")
		String career,

		@JsonProperty("ethnic")
		String ethnic,

		@JsonProperty("hamlet_address")
		String hamletAddress,

		@JsonProperty("commune_address")
		String communeAddress,

		@JsonProperty("city_address")
		String cityAddress,

		@JsonProperty("health_insurance_number")
		String healthInsuranceNumber,

		@JsonProperty("health_insurance_validate")
		String healthInsuranceValidate,

		@JsonProperty("next_of_kin_phone")
		String nextOfKinPhone
) {
}
