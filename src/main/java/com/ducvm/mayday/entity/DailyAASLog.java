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
		name = "nhatkyaashangngay",
		schema = "public",
		uniqueConstraints = @UniqueConstraint(
				name = "uq_nhatkyaas_benhnhan_ngay",
				columnNames = {"ma_benh_nhan", "ngay_theo_doi"}
		)
)
public class DailyAASLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_nhat_ky_aas", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_benh_nhan", nullable = false)
	private Patient patient;

	@Column(name = "ma_ho_so_man")
	private Long chronicMedicalRecordId;

	@Column(name = "ngay_theo_doi", nullable = false)
	private LocalDate trackingDate;

	@Column(name = "co_dot_sung_phu", nullable = false)
	private Boolean hasAngioedemaAttack = false;

	@Column(name = "thoi_diem_xuat_hien", length = 50)
	private String occurrenceTime;

	@Column(name = "muc_kho_chiu")
	private Short discomfortLevel;

	@Column(name = "han_che_hoat_dong")
	private Short activityLimitation;

	@Column(name = "anh_huong_ngoai_hinh")
	private Short appearanceImpact;

	@Column(name = "do_nang_tong_the")
	private Short overallSeverity;

	@Column(name = "diem_aas_ngay", insertable = false, updatable = false)
	private Short dailyAasScore;

	@Column(
			name = "thoi_diem_cap_nhat",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant updatedAt;
}
