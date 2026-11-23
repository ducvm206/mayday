
package com.ducvm.mayday.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MedicationGroup {
	H1_H2_ANTIHISTAMINES("Khang histamine H1 TH2"),
	BIOLOGICAL_THERAPY("Sinh hoc/dich"),
	IMMUNOSUPPRESSANTS("Uc che mien dich"),
	SYSTEMIC_CORTICOSTEROIDS("Corticosteroid toan than"),
	OTHER("Khac");

	private final String value;
}

