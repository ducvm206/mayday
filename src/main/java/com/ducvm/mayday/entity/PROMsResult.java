package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "ketquaproms", schema = "public")
public class PROMsResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_ket_qua_proms", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_phien_kham_man", nullable = false)
	private ChronicMedicalVisit chronicMedicalVisit;

	@Column(name = "uct_diem")
	private Short uctScore;

	@Column(name = "aect_diem")
	private Short aectScore;

	@Column(name = "act_diem_legacy")
	private Short actLegacyScore;

	@Column(name = "dlqi_diem")
	private Short dlqiScore;

	@Column(name = "cu_q2ol_diem", precision = 5, scale = 2)
	private BigDecimal cuQ2olScore;

	@Column(
			name = "thoi_diem_cap_nhat",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant updatedAt;
}
