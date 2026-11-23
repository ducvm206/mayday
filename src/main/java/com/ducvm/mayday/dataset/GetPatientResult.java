package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetPatientResult(

		@JsonProperty("user_id")
		Long userId,

		@JsonProperty("full_name")
		String fullName,

		@JsonProperty("phone")
		String phone,

		@JsonProperty("role_id")
		Long roleId,

		@JsonProperty("gender")
		String gender,

		@JsonProperty("year_of_birth")
		Integer yearOfBirth,

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

		@JsonProperty("work_place")
		String workPlace,

		@JsonProperty("health_insurance_number")
		String healthInsuranceNumber,

		@JsonProperty("health_insurance_validate")
		String healthInsuranceValidate,

		@JsonProperty("next_of_kin_name")
		String nextOfKinName,

		@JsonProperty("next_of_kin_phone")
		String nextOfKinPhone,

		@JsonProperty("first_visit")
		String firstVisit
) {}
