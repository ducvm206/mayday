package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AssignPermissionsRequest(
		@JsonProperty("role_id") Long roleId,
		List<Long> permissions
) {}
