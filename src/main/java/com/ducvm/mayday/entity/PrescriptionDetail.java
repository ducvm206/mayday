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
@Table(name = "chitietdonthuoc", schema = "public")
public class PrescriptionDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_chi_tiet_don_thuoc", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_don_thuoc", nullable = false)
	private Prescription prescription;

	@Column(name = "ma_thuoc", length = 50)
	private String medicationId;

	@Column(name = "ten_thuoc_bo_sung", length = 255)
	private String additionalMedicationName;

	@Column(name = "lieu_dung", nullable = false, length = 100)
	private String dosage;

	@Column(name = "thoi_gian_dung", nullable = false, length = 100)
	private String usageDuration;

	@Column(name = "ma_ke_hoach_dieu_tri", length = 50)
	private String treatmentPlanId;

	@Column(name = "thoi_diem_tao", nullable = false, insertable = false, updatable = false)
	private Instant createdAt;
}
