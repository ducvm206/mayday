package com.ducvm.mayday.dataset;


import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatePatientRequest(
		@JsonProperty("full_name")
		String fullName,

		String phone,

		String password,

		String gender,

		@JsonProperty("year_of_birth")
		Integer yearOfBirth,

		String career,

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

		@JsonProperty("next_of_kin_name")
		String nextOfKinName,

		@JsonProperty("next_of_kin_phone")
		String nextOfKinPhone
) {}

