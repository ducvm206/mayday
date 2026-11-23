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
@Table(name = "chitiettonthuongcaptinh", schema = "public")
public class AcuteLesionDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_chi_tiet_ton_thuong_cap_tinh", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_phien_kham", nullable = false)
	private MedicalVisit medicalVisit;

	@Column(name = "dang_ton_thuong")
	private String lesionType;

	@Column(name = "dang_ton_thuong_khac", columnDefinition = "text")
	private String otherLesionType;

	@Column(name = "kich_thuoc_max_mm")
	private BigDecimal maxSizeMm;

	@Column(name = "kich_thuoc_min_mm")
	private BigDecimal minSizeMm;

	@Column(name = "kich_thuoc_dong_deu_mm")
	private BigDecimal uniformSizeMm;

	@Column(name = "loai_kich_thuoc")
	private String sizeType;

	@Column(name = "thoi_gian_ton_tai_tb")
	private BigDecimal averageDuration;

	@Column(name = "dien_bien", columnDefinition = "text")
	private String progression;

	@Column(name = "so_luong_san_phu")
	private Integer whealCount;

	@Column(name = "phu_mach_ben")
	private Boolean angioedema;

	@Column(name = "vi_tri", columnDefinition = "text")
	private String location;

	@Column(name = "vi_tri_dac_biet_ghi_ro", columnDefinition = "text")
	private String specificLocation;
}
