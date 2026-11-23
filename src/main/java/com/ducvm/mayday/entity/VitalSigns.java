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
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "dauhieusinhton", schema = "public")
public class VitalSigns {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_dau_hieu_sinh_ton", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_phien_kham", nullable = false)
	private MedicalVisit medicalVisit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_bac_si")
	private Employee doctor;

	@Column(name = "thoi_diem")
	private Instant recordedAt;

	@Column(name = "tri_giac")
	private String consciousness;

	@Column(name = "nhiet_do")
	private BigDecimal temperature;

	@Column(name = "spo2")
	private BigDecimal oxygenSaturation;

	@Column(name = "mach")
	private BigDecimal pulse;

	@Column(name = "huyet_ap_tam_thu")
	private BigDecimal systolicBloodPressure;

	@Column(name = "huyet_ap_tam_truong")
	private BigDecimal diastolicBloodPressure;

	@Column(name = "nhip_tho")
	private BigDecimal respiratoryRate;

	@Column(name = "bat_thuong_co_quan_khac")
	private Boolean otherOrganAbnormality;

	@Column(name = "bat_thuong_co_quan_khac_ghi_ro", columnDefinition = "text")
	private String otherOrganAbnormalityDetails;

	@Column(
			name = "thoi_diem_tao",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant createdAt;

	@Column(
			name = "thoi_diem_cap_nhat",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant updatedAt;
}