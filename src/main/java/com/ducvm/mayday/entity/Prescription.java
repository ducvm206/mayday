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
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "donthuoc", schema = "public")
public class Prescription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_don_thuoc", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_ho_so", nullable = false)
	private MedicalRecord medicalRecord;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_phien_kham", nullable = false)
	private MedicalVisit medicalVisit;

	@Column(name = "ngay_ke_don", nullable = false)
	private LocalDate prescriptionDate;

	@Column(name = "trang_thai_don", length = 50)
	private String status;

	@Column(name = "ghi_chu_dieu_tri", columnDefinition = "text")
	private String treatmentNotes;

	@Column(name = "ngung_thuoc_lan_sau")
	private Boolean discontinueMedicationNextTime;

	@Column(name = "thuoc_can_ngung_truoc_tai_kham", columnDefinition = "text")
	private String medicationsToStopBeforeFollowUp;

	@Column(name = "thoi_diem_tao", nullable = false, insertable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "thoi_diem_cap_nhat", nullable = false, insertable = false, updatable = false)
	private Instant updatedAt;
}
