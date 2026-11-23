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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "phienkham", schema = "public")
@Getter
@Setter
public class MedicalVisit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_phien_kham", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_ho_so", nullable = false)
	private MedicalRecord medicalRecord;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_bac_si", nullable = false)
	private Employee doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_phong_kham")
	private Room room;

	@Column(name = "loai_benh_an", nullable = false, insertable = false, updatable = false)
	private String medicalRecordType;

	@Column(name = "ma_subtype_cap", insertable = false, updatable = false)
	private Long acuteSubtypeId;

	@Column(name = "ma_subtype_man", insertable = false, updatable = false)
	private Long chronicSubtypeId;

	@Column(name = "ngay_kham", nullable = false)
	private LocalDate visitDate;

	@Column(name = "lan_kham_thu", nullable = false)
	private Integer visitNumber;

	@Column(name = "loai_phien_kham", columnDefinition = "text")
	private String visitType;

	@Column(name = "luu_huyet_thanh")
	private Boolean hasSerumBlistering;

	@Column(name = "sot")
	private Boolean hasFever;

	@Column(name = "sot_nhiet_do_c", precision = 4, scale = 1)
	private BigDecimal feverTemperatureCelsius;

	@Column(name = "ngay_hen_tai_kham")
	private LocalDate followUpDate;

	@Column(name = "tom_tat_buoi_kham", columnDefinition = "text")
	private String visitSummary;

	@Column(name = "huong_xu_tri_lan_sau", columnDefinition = "text")
	private String nextVisitPlan;

	@Column(name = "dan_do", columnDefinition = "text")
	private String instructions;

}
