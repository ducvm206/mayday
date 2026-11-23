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
import jakarta.persistence.Index;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
		name = "anhchuptonthuong",
		indexes = {
				@Index(name = "idx_anhchup_dot", columnList = "ma_dot_ton_thuong_da"),
				@Index(name = "idx_anhchup_ma_tep", columnList = "ma_tep_dinh_kem"),
				@Index(name = "idx_anhchup_vitri", columnList = "ma_vi_tri_ton_thuong")
		}
)
@Getter
@Setter
public class SkinLesionPhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_anh", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_tep_dinh_kem", nullable = false)
	private Attachment attachment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_dot_ton_thuong_da")
	private SkinLesionEpisode skinLesionEpisode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_vi_tri_ton_thuong")
	private LesionLocation lesionLocation;

	@Column(name = "thoi_diem_chup", nullable = false)
	private Instant capturedAt;

	@Column(name = "moc_tien_trien", length = 50)
	private String progressionStage;

	@Column(name = "diem_ngua_nrs")
	private Short itchScore;

	@Column(name = "diem_dau_nrs")
	private Short painScore;

	@Column(name = "diem_bong_rat_nrs")
	private Short burningScore;

	@Column(name = "so_luong_not_san")
	private Integer whealCount;

	@Column(name = "chi_so_auas_cuc_bo", precision = 5, scale = 2)
	private BigDecimal localAuasScore;

	@Column(name = "url_anh", length = 500)
	private String imageUrl;

	@Column(name = "thoi_diem_tao", nullable = false, insertable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "thoi_diem_cap_nhat", nullable = false, insertable = false, updatable = false)
	private Instant updatedAt;
}
