package com.ducvm.mayday.dataset;

import java.util.List;

public record UpdateMedicalRecordAnswersRequest(
		List<MedicalRecordSectionUpdate> answers
) {}
