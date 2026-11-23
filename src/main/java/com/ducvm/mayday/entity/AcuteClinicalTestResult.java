package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "ketquacanlamsangcap", schema = "public")
public class AcuteClinicalTestResult {

	@Id
	@Column(name = "ma_ket_qua_can_lam_sang", nullable = false)
	private Long id;

	@Column(
			name = "loai_ket_qua_can_lam_sang",
			insertable = false,
			updatable = false
	)
	private String resultType;

	@Column(name = "lym", precision = 12, scale = 4)
	private BigDecimal lymphocyteCount;

	@Column(name = "neu", precision = 12, scale = 4)
	private BigDecimal neutrophilCount;

	@Column(name = "plt", precision = 12, scale = 4)
	private BigDecimal plateletCount;

	@Column(name = "tryptase_cap", precision = 12, scale = 4)
	private BigDecimal acuteTryptase;

	@Column(name = "tryptase_nen", precision = 12, scale = 4)
	private BigDecimal baselineTryptase;

	@Column(name = "nlr", insertable = false, updatable = false)
	private BigDecimal neutrophilLymphocyteRatio;

	@Column(name = "plr", insertable = false, updatable = false)
	private BigDecimal plateletLymphocyteRatio;
}
