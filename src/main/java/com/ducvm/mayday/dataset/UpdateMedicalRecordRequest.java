package com.ducvm.mayday.dataset;

public record UpdateMedicalRecordRequest(
		String status,
		String instructions
) {}
