package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetUsersRequest(

		@JsonProperty("page")
		Integer page,

		@JsonProperty("limit")
		Integer limit,

		@JsonProperty("role_id")
		Long roleId

) {}
