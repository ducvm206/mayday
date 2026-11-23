package com.ducvm.mayday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "loainghiemphap", schema = "public")
public class ProvocationTestType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_nghiem_phap", nullable = false)
	private Long id;

	@Column(name = "ten_nghiem_phap", nullable = false, length = 150)
	private String name;

	@Column(name = "phuong_phap_thuc_hien", columnDefinition = "text")
	private String procedure;

	@Column(name = "thoi_gian_chuan", precision = 10, scale = 2)
	private BigDecimal standardDuration;

	@Column(name = "thoi_diem_tao", nullable = false, insertable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "thoi_diem_cap_nhat", nullable = false, insertable = false, updatable = false)
	private Instant updatedAt;

	@Column(name = "la_can_nguyen_khac", nullable = false)
	private Boolean otherCause;
}