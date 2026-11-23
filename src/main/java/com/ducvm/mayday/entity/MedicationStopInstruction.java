package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "chidanngungthuoc", schema = "public")
public class MedicationStopInstruction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_chi_dan_ngung", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_don_thuoc", nullable = false)
	private Prescription prescription;

	@Column(name = "thuoc_can_ngung", nullable = false, length = 255)
	private String medicationToStop;

	@Column(name = "ngung_tu_ngay")
	private LocalDate stopDate;

	@Column(name = "thoi_gian_ngung_ngay")
	private Integer discontinuationDurationDays;

	@Column(name = "muc_dich", length = 100)
	private String purpose;

	@Column(name = "muc_dich_khac_ghi_ro", length = 255)
	private String otherPurpose;
}