package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClassifyMedicalRecordResult(
		@JsonProperty("record_type") String recordType,

		@JsonProperty("template_id") String templateId,

		@JsonProperty("template_name") String templateName
) {}
