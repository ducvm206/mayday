package com.ducvm.mayday.dataset;

import java.util.Map;

public record MedicalRecordSectionUpdate(
		String section,
		Map<String, Object> values
) {}
