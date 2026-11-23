package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
		name = "tiensubanthan",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uq_tiensubanthan_hoso",
						columnNames = "ma_ho_so"
				)
		}
)
@Getter
@Setter
public class PersonalMedicalHistory {
	/**
	 * Personal medical history ID.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_tien_su_ban_than", nullable = false, updatable = false)
	private Long id;
	/**
	 * Medical record associated with this history.
	 */
	@Column(name = "ma_ho_so", nullable = false, unique = true)
	private Long medicalRecordId;

	@Column(name = "tien_su_co_dia")
	private Boolean atopicDermatitisHistory;

	@Column(name = "tien_su_co_dia_ghi_ro", columnDefinition = "text")
	private String atopicDermatitisHistoryDetails;

	@Column(name = "tien_su_tuyen_giap")
	private Boolean thyroidDiseaseHistory;

	@Column(name = "tien_su_tuyen_giap_ghi_ro", columnDefinition = "text")
	private String thyroidDiseaseHistoryDetails;

	@Column(name = "tien_su_tu_mien")
	private Boolean autoimmuneDiseaseHistory;

	@Column(name = "tien_su_tu_mien_ghi_ro", columnDefinition = "text")
	private String autoimmuneDiseaseHistoryDetails;

	@Column(name = "tien_su_di_ung")
	private Boolean allergyHistory;

	@Column(name = "tien_su_di_ung_ghi_ro", columnDefinition = "text")
	private String allergyHistoryDetails;

	@Column(name = "tien_su_phan_ve")
	private Boolean anaphylaxisHistory;

	@Column(name = "tien_su_phan_ve_ghi_ro", columnDefinition = "text")
	private String anaphylaxisHistoryDetails;

	@Column(name = "tien_su_viem_nhiem_man")
	private Boolean chronicInfectionHistory;

	@Column(name = "tien_su_viem_nhiem_man_ghi_ro", columnDefinition = "text")
	private String chronicInfectionHistoryDetails;

	@Column(name = "tien_su_benh_khac")
	private Boolean otherDiseaseHistory;

	@Column(name = "tien_su_benh_khac_ghi_ro", columnDefinition = "text")
	private String otherDiseaseHistoryDetails;
}
