package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public record MedicalRecordAnswerRequest(
		@JsonProperty("question_id")
		String questionId,

		@JsonProperty("question_option_id")
		Long questionOptionId,

		@JsonProperty("answer_text")
		String answerText,

		@JsonProperty("answer_value")
		JsonNode answerValue
) {}
