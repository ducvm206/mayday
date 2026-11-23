package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "ketquacanlamsang", schema = "public")
public class ClinicalTestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_ket_qua_can_lam_sang", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_phien_kham", nullable = false)
	private MedicalVisit medicalVisit;

	@Column(name = "loai_ket_qua_can_lam_sang", nullable = false, length = 3)
	private String resultType;

	@Column(name = "wbc", precision = 12, scale = 4)
	private BigDecimal whiteBloodCellCount;

	@Column(name = "crp", precision = 12, scale = 4)
	private BigDecimal cReactiveProtein;

	@Column(name = "ige_toan_phan", precision = 12, scale = 4)
	private BigDecimal totalIgE;

	@Column(name = "can_lam_sang_khac_ghi_chu", columnDefinition = "text")
	private String otherClinicalTestNotes;

	@Column(name = "xet_nghiem_khac", columnDefinition = "text")
	private String otherTests;

	@Column(name = "thoi_diem_thuc_hien")
	private Instant performedAt;

	@Column(
			name = "thoi_diem_cap_nhat",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant updatedAt;

	@Column(name = "ma_subtype_cap", insertable = false, updatable = false)
	private Long acuteSubtypeId;

	@Column(name = "ma_subtype_man", insertable = false, updatable = false)
	private Long chronicSubtypeId;
}
