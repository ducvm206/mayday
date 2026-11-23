package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateRoomResult(
		Long id,
		String name,

		@JsonProperty("department_id")
		Long departmentId
) {}
