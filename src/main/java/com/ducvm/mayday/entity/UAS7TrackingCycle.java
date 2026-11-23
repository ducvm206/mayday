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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(
		name = "chukytheodoiuas7",
		schema = "public",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uq_chuky_benhnhan_ngay",
						columnNames = {
								"ma_chu_ky",
								"ma_benh_nhan",
								"ngay_bat_dau"
						}
				)
		}
)
public class UAS7TrackingCycle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_chu_ky", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_benh_nhan", nullable = false)
	private Patient patient;

	@Column(name = "ngay_bat_dau", nullable = false)
	private LocalDate startDate;

	@Column(name = "ngay_ket_thuc", nullable = false)
	private LocalDate endDate;

	@Column(name = "trang_thai_chu_ky", length = 50)
	private String status = "DANG_THEO_DOI";

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

