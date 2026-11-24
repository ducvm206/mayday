package com.ducvm.mayday.util;

import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.exceptions.ApiException;

import java.math.BigDecimal;
import java.util.Set;

public final class MedicalRecordClassification {

	private MedicalRecordClassification() {}

	public static final String PREVIOUSLY_EXAMINED =
			"PREVIOUSLY_EXAMINED";

	public static final String URTICARIA_DURATION_WEEKS =
			"URTICARIA_DURATION_WEEKS";

	public static final String ACUTE = "CAP";
	public static final String CHRONIC = "MAN";

	public static final BigDecimal CHRONIC_THRESHOLD_WEEKS =
			BigDecimal.valueOf(6);

	public static final String ACUTE_TEMPLATE_ID = "TMPL_URT_ACUTE";
	public static final String CHRONIC_TEMPLATE_ID = "TMPL_URT_CHRONIC";

	public static final String ACUTE_TEMPLATE_NAME =
			"Mẫu bệnh án mày đay cấp";

	public static final String CHRONIC_TEMPLATE_NAME =
			"Mẫu bệnh án mày đay mạn";

	public static final Set<String> QUESTION_IDS = Set.of(
			PREVIOUSLY_EXAMINED,
			URTICARIA_DURATION_WEEKS
	);

	public static boolean isValidBooleanOption(String value) {
		return "true".equalsIgnoreCase(value)
				|| "false".equalsIgnoreCase(value);
	}

	public static String getRecordTypeFromTemplate(String templateId) {
		if (ACUTE_TEMPLATE_ID.equals(templateId)) {
			return ACUTE;
		}

		if (CHRONIC_TEMPLATE_ID.equals(templateId)) {
			return CHRONIC;
		}

		throw new ApiException(ResponseCode.OBJECT_NOT_EXISTED);
	}
}
