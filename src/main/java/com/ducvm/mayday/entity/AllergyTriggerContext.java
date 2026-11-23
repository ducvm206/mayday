package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
		name = "hoancanhkhoiphatdiung",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uq_hoancanhkhoiphatdiung_hoso",
						columnNames = "ma_ho_so"
				)
		}
)
@Getter
@Setter
public class AllergyTriggerContext {
	/**
	 * Allergy trigger context id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_hoan_canh_khoi_phat", nullable = false, updatable = false)
	private Long id;
	/**
	 * Medical record linked to this.
	 */
	@Column(name = "ma_ho_so", nullable = false, unique = true)
	private Long medicalRecordId;

	// Food
	@Column(name = "thuc_an_nghi_ngo", columnDefinition = "text")
	private String suspectedFoodTrigger;

	@Column(name = "thuc_an_thoi_gian")
	private BigDecimal foodExposureTime;

	@Column(name = "thuc_an_tung_bi")
	private Boolean previousFoodReaction;

	@Column(name = "thuc_an_loai_tien_su")
	private String foodHistoryType;

	@Column(name = "thuc_an_tien_su_ghi_ro", columnDefinition = "text")
	private String foodHistoryDetails;

	// Medication
	@Column(name = "thuoc_nghi_ngo", columnDefinition = "text")
	private String suspectedMedicationTrigger;

	@Column(name = "thuoc_thoi_gian")
	private BigDecimal medicationExposureTime;

	@Column(name = "thuoc_tung_bi")
	private Boolean previousMedicationReaction;

	@Column(name = "thuoc_loai_tien_su")
	private String medicationHistoryType;

	@Column(name = "thuoc_tien_su_ghi_ro", columnDefinition = "text")
	private String medicationHistoryDetails;

	// Insects
	@Column(name = "con_trung_nghi_ngo", columnDefinition = "text")
	private String suspectedInsectTrigger;

	@Column(name = "con_trung_thoi_gian")
	private BigDecimal insectExposureTime;

	@Column(name = "con_trung_tung_bi")
	private Boolean previousInsectReaction;

	@Column(name = "con_trung_loai_tien_su")
	private String insectHistoryType;

	@Column(name = "con_trung_tien_su_ghi_ro", columnDefinition = "text")
	private String insectHistoryDetails;

	// Other triggers
	@Column(name = "khac_nghi_ngo", columnDefinition = "text")
	private String otherSuspectedTrigger;

	@Column(name = "khac_thoi_gian")
	private BigDecimal otherExposureTime;

	@Column(name = "khac_tung_bi")
	private Boolean previousOtherReaction;

	@Column(name = "khac_loai_tien_su")
	private String otherHistoryType;

	@Column(name = "khac_tien_su_ghi_ro", columnDefinition = "text")
	private String otherHistoryDetails;
}
