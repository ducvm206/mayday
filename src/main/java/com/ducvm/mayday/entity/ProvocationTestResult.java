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

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "chitietketquanghiemphap", schema = "public")
public class ProvocationTestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_chi_tiet_ket_qua_nghiem_phap", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_phien_nghiem_phap", nullable = false)
	private ProvocationTestSession provocationTestSession;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "loai_nghiem_phap", nullable = false)
	private ProvocationTestType provocationTestType;

	@Column(name = "ket_qua", length = 12)
	private String result;

	@Column(name = "nguong_gia_tri_so", precision = 12, scale = 4)
	private BigDecimal numericThreshold;

	@Column(name = "nguong_gia_tri_chu", columnDefinition = "text")
	private String textThreshold;

	@Column(name = "don_vi_do", length = 30)
	private String unit;

	@Column(name = "diem_ngua_nrs")
	private Short itchNrsScore;

	@Column(name = "diem_bong_rat_nrs")
	private Short burningNrsScore;

	@Column(name = "diem_dau_nrs")
	private Short painNrsScore;

	@Column(name = "can_nguyen_khac_ten", columnDefinition = "text")
	private String otherCauseName;

	@Column(name = "la_can_nguyen_khac", nullable = false)
	private Boolean otherCause;
}
