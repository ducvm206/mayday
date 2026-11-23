package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AssignEmployeeResult(

		@JsonProperty("user_id")
		Long userId,

		@JsonProperty("branch_id")
		Long branchId,

		@JsonProperty("department_id")
		Long departmentId,

		@JsonProperty("room_id")
		Long roomId

) {}
