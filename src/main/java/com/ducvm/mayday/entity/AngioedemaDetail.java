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
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "chitiettonthuongsanphu", schema = "public")
public class AngioedemaDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_chi_tiet_ton_thuong", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_phien_kham", nullable = false)
	private MedicalVisit medicalVisit;

	@Column(name = "dang_ton_thuong")
	private String lesionType;

	@Column(name = "kich_thuoc")
	private BigDecimal size;

	@Column(name = "thoi_gian_ton_tai")
	private BigDecimal duration;

	@Column(name = "thoi_diem")
	private Instant occurrenceTime;

	@Column(name = "tinh_trang_dung_thuoc")
	private String medicationStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_dac_diem_san_phu")
	private AngioedemaCharacteristic angioedemaCharacteristic;

	@Column(name = "so_luong")
	private Integer quantity;

	@Column(name = "vi_tri", columnDefinition = "text")
	private String location;
}
