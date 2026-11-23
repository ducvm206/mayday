package com.ducvm.mayday.entity;

import com.ducvm.mayday.entity.MedicalVisit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "phienkhamman", schema = "public")
public class ChronicMedicalVisit {

	@Id
	@Column(name = "ma_phien_kham", nullable = false)
	private Long medicalVisitId;

	@Column(name = "loai_benh_an", insertable = false, updatable = false)
	private String medicalRecordType;

	@Column(name = "hien_tai_co_san_phu")
	private Boolean currentlyHasAngioedema;

	@Column(name = "uu_the_ngau_nhien")
	private Boolean spontaneousPredominance;

	@Column(name = "vi_tri_san_phu", columnDefinition = "text")
	private String angioedemaLocation;

	@Column(name = "yeu_to_lam_nang_sa_phu", columnDefinition = "text")
	private String angioedemaAggravatingFactors;

	@Column(name = "hoan_canh_xuat_hien_san_phu", columnDefinition = "text")
	private String angioedemaOccurrenceContext;

	@Column(name = "trong_dot_co_phu_mach")
	private Boolean hadAngioedemaDuringEpisode;

	@Column(name = "hien_tai_co_phu_mach")
	private Boolean currentlyHasAngioedemaDuringEpisode;

	@Column(name = "so_lan_phu_mach_dot_nay")
	private Integer angioedemaEpisodesThisPeriod;

	@Column(name = "hoan_canh_xuat_hien_phu_mach", columnDefinition = "text")
	private String angioedemaEpisodeContext;

	@Column(name = "so_lan_phu_mach_nang_phai_nhap_vien")
	private Integer severeAngioedemaHospitalizations;

	@Column(name = "thoi_gian_ton_tai_phu_mach_co_dung_thuoc", columnDefinition = "text")
	private String angioedemaDurationWithMedication;

	@Column(name = "thoi_gian_ton_tai_phu_mach_khong_dung_thuoc", columnDefinition = "text")
	private String angioedemaDurationWithoutMedication;

	@Column(name = "diem_aas7")
	private Short aas7Score;

	@Column(name = "ben_phu_mach", columnDefinition = "text")
	private String angioedemaSide;

	@Column(name = "thoi_gian_ngau_nhien_tuan", precision = 12, scale = 2)
	private BigDecimal spontaneousDurationWeeks;

	@Column(name = "uu_the_kich_thich")
	private Boolean induciblePredominance;

	@Column(name = "thoi_gian_kich_thich_tuan", precision = 12, scale = 2)
	private BigDecimal inducibleDurationWeeks;

	@Column(name = "uu_the_tuong_duong")
	private Boolean equalPredominance;

	@Column(name = "uu_the_khong_ro")
	private Boolean unknownPredominance;

	@Column(name = "thu_tu_xuat_hien_so_voi_san_phu", columnDefinition = "text")
	private String occurrenceOrderRelativeToAngioedema;

	@Column(name = "phu_mach_1_tuan_gan_day")
	private Boolean angioedemaWithinLastWeek;

	@Column(name = "chan_doan_tai_kham", columnDefinition = "text")
	private String followUpDiagnosis;

	@Column(name = "khoang_cach_xuat_hien", columnDefinition = "text")
	private String occurrenceInterval;

	@Column(name = "phu_mi_mat")
	private Boolean eyelidAngioedema;

	@Column(name = "phu_luoi")
	private Boolean tongueAngioedema;

	@Column(name = "phu_moi")
	private Boolean lipAngioedema;

	@Column(name = "phu_vung_mat_khac")
	private Boolean otherFacialAngioedema;

	@Column(name = "phu_thanh_quan")
	private Boolean laryngealAngioedema;

	@Column(name = "phu_ban_chan")
	private Boolean footAngioedema;

	@Column(name = "phu_sinh_duc")
	private Boolean genitalAngioedema;

	@Column(name = "phu_vi_tri_khac")
	private Boolean otherLocationAngioedema;

	@Column(name = "phu_vi_tri_khac_ghi_ro", columnDefinition = "text")
	private String otherAngioedemaLocationDetails;

	@Column(name = "phu_ban_tay")
	private Boolean handAngioedema;
}
