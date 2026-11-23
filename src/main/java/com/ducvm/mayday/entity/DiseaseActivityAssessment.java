package com.ducvm.mayday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "danhgiamucdohoatdongbenh", schema = "public")
public class DiseaseActivityAssessment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_danh_gia_hoat_dong", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_phien_kham_man", nullable = false)
	private ChronicMedicalVisit chronicMedicalVisit;

	@Column(name = "du_du_lieu_7_ngay")
	private Boolean hasCompleteSevenDayData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_nguoi_danh_gia")
	private Person assessor;

	@Column(
			name = "thoi_diem_danh_gia",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant assessmentTime;

	@Column(name = "hss7_san_phu_khong_dung_thuoc")
	private Short hss7AngioedemaWithoutMedication;

	@Column(name = "hss7_san_phu_dung_thuoc_deu")
	private Short hss7AngioedemaWithRegularMedication;

	@Column(name = "hss7_san_phu_dung_thuoc_khong_deu")
	private Short hss7AngioedemaWithIrregularMedication;

	@Column(name = "iss7_ngua_khong_dung_thuoc")
	private Short iss7ItchWithoutMedication;

	@Column(name = "iss7_ngua_dung_thuoc_deu")
	private Short iss7ItchWithRegularMedication;

	@Column(name = "iss7_ngua_dung_thuoc_khong_deu")
	private Short iss7ItchWithIrregularMedication;

	@Column(name = "uas7_tong_khong_dung_thuoc")
	private Short uas7TotalWithoutMedication;

	@Column(name = "uas7_tong_dung_thuoc_deu")
	private Short uas7TotalWithRegularMedication;

	@Column(name = "uas7_tong_dung_thuoc_khong_deu")
	private Short uas7TotalWithIrregularMedication;

	@Column(name = "ghi_chu", columnDefinition = "text")
	private String notes;
}
