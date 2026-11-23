package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateRoomRequest(
		String name,

		@JsonProperty("department_id")
		Long departmentId
) {}
