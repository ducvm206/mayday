package com.ducvm.mayday.dataset;

import java.util.List;
import java.util.Map;

public record MedicalRecordAnswerSection(
		String section,
		List<Map<String, Object>> values
) {}
