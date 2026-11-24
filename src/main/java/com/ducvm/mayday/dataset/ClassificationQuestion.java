package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ClassificationQuestion(
		@JsonProperty("question_id") String questionId,
		@JsonProperty("question_text") String questionText,
		@JsonProperty("question_type") String questionType,
		boolean required,
		List<ClassificationQuestionOption> options
) {}
