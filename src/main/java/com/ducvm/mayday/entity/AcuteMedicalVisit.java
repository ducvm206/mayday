package com.ducvm.mayday.entity;

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

@Entity
@Getter
@Setter
@Table(name = "phienkhamcap", schema = "public")
public class AcuteMedicalVisit {

	@Id
	@Column(name = "ma_phien_kham", nullable = false)
	private Long medicalVisitId;

	@Column(name = "loai_benh_an", insertable = false, updatable = false)
	private String medicalRecordType;

	@Column(name = "trieu_chung_da", columnDefinition = "text")
	private String skinSymptoms;

	@Column(name = "trieu_chung_ho_hap", columnDefinition = "text")
	private String respiratorySymptoms;

	@Column(name = "so_voi_thuong_ngay_ho_hap", columnDefinition = "text")
	private String respiratoryComparisonToNormal;

	@Column(name = "trieu_chung_tuan_hoan", columnDefinition = "text")
	private String circulatorySymptoms;

	@Column(name = "so_voi_thuong_ngay_tuan_hoan", columnDefinition = "text")
	private String circulatoryComparisonToNormal;

	@Column(name = "trieu_chung_tieu_hoa", columnDefinition = "text")
	private String digestiveSymptoms;

	@Column(name = "so_voi_thuong_ngay_tieu_hoa", columnDefinition = "text")
	private String digestiveComparisonToNormal;

	@Column(name = "co_san_phu")
	private Boolean hasAngioedema;

	@Column(name = "co_anh_san_phu")
	private Boolean hasAngioedemaPhoto;

	@Column(name = "so_lan_non")
	private Integer vomitingCount;
}
