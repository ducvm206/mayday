package com.ducvm.mayday.dataset;

public record GetUsersResult (
		Long userId,

		String fullName,

		String phone,

		Long roleId,

		Integer isActive
) {
}
