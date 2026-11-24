package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetMedicalRecordAnswersResult(
		@JsonProperty("medical_record_id")
		Long medicalRecordId,

		@JsonProperty("record_type")
		String recordType,

		List<MedicalRecordAnswerSection> answers
) {}
