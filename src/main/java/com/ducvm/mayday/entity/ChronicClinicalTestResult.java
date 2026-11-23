package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "ketquacanlamsangman", schema = "public")
public class ChronicClinicalTestResult {

	@Id
	@Column(name = "ma_ket_qua_can_lam_sang", nullable = false)
	private Long id;

	@Column(
			name = "loai_ket_qua_can_lam_sang",
			insertable = false,
			updatable = false
	)
	private String resultType;

	@Column(name = "test_huyet_thanh_tu_than", length = 12)
	private String autologousSerumTest;

	@Column(name = "ige_khang_tpo_ket_qua", length = 12)
	private String antiTpoIgEResult;

	@Column(name = "bat_ket_qua", length = 12)
	private String basophilActivationTestResult;

	@Column(name = "ige_khang_il24_ket_qua", length = 12)
	private String antiIl24IgEResult;

	@Column(name = "asst_duong_kinh_mm", precision = 12, scale = 4)
	private BigDecimal asstDiameterMm;

	@Column(name = "eo", precision = 12, scale = 4)
	private BigDecimal eosinophilCount;

	@Column(name = "ba_bach_cau", precision = 12, scale = 4)
	private BigDecimal basophilCount;

	@Column(name = "anti_tpo", precision = 12, scale = 4)
	private BigDecimal antiTpo;

	@Column(name = "tsh", precision = 12, scale = 4)
	private BigDecimal tsh;

	@Column(name = "ft4", precision = 12, scale = 4)
	private BigDecimal freeT4;

	@Column(name = "ft3", precision = 12, scale = 4)
	private BigDecimal freeT3;

	@Column(name = "mau_lang_2h", precision = 12, scale = 4)
	private BigDecimal esr2Hours;

	@Column(name = "mau_lang_1h", precision = 12, scale = 4)
	private BigDecimal esr1Hour;

	@Column(name = "ana_hep2", length = 12)
	private String anaHep2;

	@Column(name = "sieu_am_tuyen_giap", columnDefinition = "text")
	private String thyroidUltrasound;
}