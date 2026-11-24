
package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "hosobenhan")
@Getter
@Setter
public class MedicalRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_ho_so", nullable = false, updatable = false)
	private Long id;

	@Column(name = "ma_benh_nhan", nullable = false)
	private Long patientId;

	@Column(name = "loai_benh_an", nullable = false, length = 10)
	private String recordType;

	@Column(name = "thoi_diem_kham_lan_dau")
	private Instant firstVisitTime;

	@Column(name = "ngay_bat_dau_dieu_tri_ngoai_tru")
	private LocalDate outpatientTreatmentStartDate;

	@Column(name = "ngay_ket_thuc_dieu_tri_ngoai_tru")
	private LocalDate outpatientTreatmentEndDate;

	@Column(name = "chan_doan_tuyen_truoc", columnDefinition = "text")
	private String referralDiagnosis;

	@Column(
			name = "chan_doan_ban_dau_phong_kham",
			columnDefinition = "text"
	)
	private String initialClinicDiagnosis;

	@Column(
			name = "ket_qua_dieu_tri_bao_hiem",
			columnDefinition = "text"
	)
	private String insuranceTreatmentOutcome;

	@Column(name = "thoi_gian_khoi_phat_tuan", precision = 12, scale = 2)
	private BigDecimal onsetDurationWeeks;

	@Column(name = "trang_thai", nullable = false, columnDefinition = "text")
	private String status = "draft";

	@Column(name = "tien_su_gia_dinh", columnDefinition = "text")
	private String familyHistoryStatus;

	@Column(name = "tien_su_gia_dinh_ghi_ro", columnDefinition = "text")
	private String familyHistoryDetails;

	@Column(name = "da_dieu_tri_truoc_kham")
	private Boolean previouslyTreated;
}