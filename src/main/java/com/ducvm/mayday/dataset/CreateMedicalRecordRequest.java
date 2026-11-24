package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateMedicalRecordRequest(
		@JsonProperty("patient_id")
		Long patientId,

		@JsonProperty("template_id")
		String templateId,

		List<MedicalRecordAnswerRequest> answers
) {}
