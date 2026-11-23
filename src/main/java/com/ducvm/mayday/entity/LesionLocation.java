package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
		name = "vitritonthuong",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uq_vitri_dot",
						columnNames = {"ma_vi_tri_ton_thuong", "ma_dot_ton_thuong_da"}
				)
		}
)
@Getter
@Setter
public class LesionLocation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
			name = "ma_vi_tri_ton_thuong",
			nullable = false,
			updatable = false
	)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_dot_ton_thuong_da", nullable = false)
	private SkinLesionEpisode skinLesionEpisode;

	@Column(name = "vi_tri_co_the", length = 255)
	private String bodyLocation;

	@Column(name = "thoi_diem_xuat_hien")
	private Instant onsetAt;

	@Column(name = "ten_thuoc_lien_quan", length = 255)
	private String relatedMedication;

	@Column(name = "thoi_gian_dung_thuoc_truoc_khi_xuat_hien", length = 100)
	private String medicationTimeBeforeOnset;

	@Column(name = "hoan_canh_xuat_hien", length = 255)
	private String onsetContext;

	@Column(name = "hoan_canh_xuat_hien_khac", columnDefinition = "text")
	private String otherOnsetContext;

	@Column(name = "don_vi_thoi_gian", length = 50)
	private String timeUnit;

	@Column(
			name = "thoi_diem_tao",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant createdAt;

	@Column(
			name = "thoi_diem_cap_nhat",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant updatedAt;
}

