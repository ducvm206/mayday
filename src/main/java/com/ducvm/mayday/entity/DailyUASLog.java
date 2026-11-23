package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(
		name = "nhatkyuashangngay",
		schema = "public",
		uniqueConstraints = @UniqueConstraint(
				name = "uq_nhatkyuas_benhnhan_ngay",
				columnNames = {"ma_benh_nhan", "ngay_cham_diem"}
		)
)
public class DailyUASLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_nhat_ky_uas", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_chu_ky")
	private UAS7TrackingCycle trackingCycle;

	@Column(name = "ma_ho_so_man")
	private Long chronicMedicalRecordId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_benh_nhan", nullable = false)
	private Patient patient;

	@Column(name = "ngay_cham_diem", nullable = false)
	private LocalDate scoringDate;

	@Column(name = "ngay_thu_may_trong_chu_ky")
	private Short dayInCycle;

	@Column(name = "diem_san_phu_hss", nullable = false)
	private Short whealScore;

	@Column(name = "diem_ngua_iss", nullable = false)
	private Short itchScore;

	@Column(name = "diem_uas_ngay", insertable = false, updatable = false)
	private Short dailyUasScore;

	@Column(name = "trang_thai_dung_thuoc", length = 50)
	private String medicationStatus;

	@Column(name = "trang_thai_ngay", nullable = false, length = 50)
	private String dayStatus = "HOP_LE";

	@Column(
			name = "thoi_diem_cap_nhat",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant updatedAt;

	@Column(
			name = "ngay_bat_dau_chu_ky",
			insertable = false,
			updatable = false
	)
	private LocalDate cycleStartDate;
}
