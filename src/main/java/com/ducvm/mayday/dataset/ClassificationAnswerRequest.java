package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ClassificationAnswerRequest(
		@JsonProperty("question_id") String questionId,

		@JsonProperty("option_value") String optionValue,

		@JsonProperty("answer_value") BigDecimal answerValue
) {}
