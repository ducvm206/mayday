package com.ducvm.mayday.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClassificationQuestionOption(
		@JsonProperty("question_option_id") Long questionOptionId,
		@JsonProperty("option_text") String optionText,
		@JsonProperty("option_value") Boolean optionValue
) {}
