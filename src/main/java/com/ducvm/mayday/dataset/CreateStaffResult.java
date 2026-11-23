package com.ducvm.mayday.dataset;

public record CreateStaffResult(

		Long userId,

		String fullName,

		Long roleId,

		Long branchId,

		Long departmentId

) {
}