package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
		name = "benhsucacdotbenhtruoc",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uq_benhsucacdotbenhtruoc_hoso",
						columnNames = "ma_ho_so"
				)
		}
)
@Getter
@Setter
public class PreviousDiseaseEpisode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_tien_su", nullable = false, updatable = false)
	private Long id;

	@Column(name = "ma_ho_so", nullable = false, unique = true)
	private Long chronicMedicalRecordId;

	@Column(name = "tung_co_dot_tuong_tu")
	private Boolean hadSimilarPreviousEpisode;

	@Column(name = "tuoi_khoi_phat")
	private Short ageAtOnset;

	@Column(name = "so_dot_truoc")
	private Integer previousEpisodeCount;

	@Column(name = "kieu_dien_bien")
	private String episodeCourseType;

	@Column(name = "bieu_hien_dot_truoc", columnDefinition = "text")
	private String previousEpisodeSymptoms;

	@Column(name = "da_tung_nhap_vien")
	private Boolean previouslyHospitalized;

	@Column(name = "so_lan_nhap_vien")
	private Integer hospitalizationCount;

	@Column(name = "thoi_gian_tu_dot_gan_nhat")
	private BigDecimal timeSinceMostRecentEpisode;

	@Column(name = "dien_bien_cac_dot_truoc", columnDefinition = "text")
	private String previousEpisodeCourseDetails;
}
