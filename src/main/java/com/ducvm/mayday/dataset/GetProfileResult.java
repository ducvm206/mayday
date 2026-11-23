package com.ducvm.mayday.dataset;

import java.time.LocalDate;

public record GetProfileResult(
		Long userId,
		String fullName,
		String phone,
		Long roleId,
		String gender,
		String dateOfBirth,
		String career,
		String ethnic,
		String address,
		String ward,
		String city,
		String healthInsuranceNumber,
		String healthInsuranceValidate,
		String nextOfKinName,
		String nextOfKinPhone,
		LocalDate firstVisit
) {
}
