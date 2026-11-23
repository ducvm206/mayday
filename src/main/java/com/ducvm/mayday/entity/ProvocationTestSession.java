package com.ducvm.mayday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "phiennghiemphapkichthich", schema = "public")
public class ProvocationTestSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_phien_nghiem_phap", nullable = false)
	private Long id;

	@Column(name = "ma_phien_kham", nullable = false, unique = true)
	private Long chronicMedicalVisitId;

	@Column(name = "ngung_khang_histamine")
	private Boolean stoppedAntihistamines;

	@Column(name = "thoi_gian_ngung_histamine", precision = 8, scale = 2)
	private BigDecimal antihistamineWithdrawalDuration;

	@Column(name = "ngung_corticoid")
	private Boolean stoppedCorticosteroids;

	@Column(name = "thoi_gian_ngung_corticoid", precision = 8, scale = 2)
	private BigDecimal corticosteroidWithdrawalDuration;

	@Column(name = "thoi_diem_cap_nhat", nullable = false)
	private Instant updatedAt;
}
