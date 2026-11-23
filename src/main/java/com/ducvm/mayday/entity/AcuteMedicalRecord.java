package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "benhanmaydaycap")
@Getter
@Setter
public class AcuteMedicalRecord {
	/**
	 * Medical record ID, shared with the parent medical record.
	 */
	@Id
	@Column(name = "ma_ho_so", nullable = false)
	private Long id;
	/**
	 * Whether the patient was hospitalized.
	 */
	@Column(name = "nhap_vien")
	private Boolean hospitalized;

	@Column(name = "cd_soc_phan_ve")
	private Boolean diagnosedAnaphylacticShock;

	@Column(name = "cd_phan_ve")
	private Boolean diagnosedAnaphylaxis;

	@Column(name = "cd_may_day_cap_nang")
	private Boolean diagnosedSevereAcuteUrticaria;

	@Column(name = "cd_may_day_cap_thong_thuong")
	private Boolean diagnosedTypicalAcuteUrticaria;

	@Column(name = "tien_su_benh_nen_da", columnDefinition = "text")
	private String skinDiseaseHistory;

	@Column(name = "tien_su_benh_nen_ho_hap", columnDefinition = "text")
	private String respiratoryDiseaseHistory;

	@Column(name = "tien_su_benh_nen_tuan_hoan", columnDefinition = "text")
	private String circulatoryDiseaseHistory;

	@Column(name = "tien_su_benh_tim_mach_ghi_ro", columnDefinition = "text")
	private String cardiovascularHistoryDetails;

	@Column(name = "tien_su_benh_tieu_hoa_man_tinh_ghi_ro", columnDefinition = "text")
	private String chronicDigestiveDiseaseHistoryDetails;

	@Column(name = "tien_su_benh_nen_tieu_hoa", columnDefinition = "text")
	private String digestiveDiseaseHistory;

	@Column(name = "nhom_yeu_to_nghi_ngo", columnDefinition = "text")
	private String suspectedTriggerCategory;

	@Column(name = "co_nghi_ngo_dot_nay")
	private Boolean hasSuspectedTriggerThisEpisode;

	@Column(name = "ten_yeu_to_nghi_ngo", columnDefinition = "text")
	private String suspectedTriggerName;

	@Column(name = "loai_tien_su", columnDefinition = "text")
	private String historyType;

	@Column(name = "da_tung_phan_ung_truoc_day")
	private Boolean hadPreviousReaction;

	@JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
	@Column(name = "thoi_gian_tiep_xuc_den_khoi_phat", columnDefinition = "interval")
	private Duration exposureToOnsetDuration;

	@JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
	@Column(name = "thoi_gian_keo_dai_tb", columnDefinition = "interval")
	private Duration averageEpisodeDuration;

	@JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
	@Column(name = "dot_gan_nhat_cach", columnDefinition = "interval")
	private Duration timeSinceMostRecentEpisode;

	@Column(name = "tien_su_phan_ve_ghi_ro", columnDefinition = "text")
	private String anaphylaxisHistoryDetails;

	@Column(name = "tien_su_di_ung_ghi_ro", columnDefinition = "text")
	private String allergyHistoryDetails;

	@Column(name = "da_dieu_tri_benh_dot_nay")
	private Boolean treatedDuringCurrentEpisode;

	@Column(name = "tuoi_lan_dau_bi_benh")
	private Integer ageAtFirstOnset;

	@Column(name = "so_lan_may_day_cap_truoc")
	private Integer previousAcuteUrticariaEpisodeCount;

	@Column(name = "thoi_diem_cap_nhat", nullable = false)
	private Instant updatedAt;

	@Column(name = "benh_phu", columnDefinition = "text")
	private String comorbidities;
}
