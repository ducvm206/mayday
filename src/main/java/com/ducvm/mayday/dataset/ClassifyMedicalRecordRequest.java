package com.ducvm.mayday.dataset;

import java.util.List;

public record ClassifyMedicalRecordRequest(
		List<ClassificationAnswerRequest> answers
) {}
