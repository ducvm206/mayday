package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStaffRequest implements PersonRequest {

	@JsonProperty("full_name")
	private String fullName;

	@JsonProperty("phone")
	private String phone;

	@JsonProperty("password")
	private String password;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("role_id")
	private Long roleId;

	@JsonProperty("branch_id")
	private Long branchId;

	@JsonProperty("department_id")
	private Long departmentId;

	@JsonProperty("room_id")
	private Long roomId;
}
