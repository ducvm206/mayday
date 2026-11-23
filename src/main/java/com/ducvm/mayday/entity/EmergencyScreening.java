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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(
		name = "sanglocbaodongdo",
		schema = "public",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uq_sanglocbaodongdo_phien",
						columnNames = "ma_phien_ung_dung"
				)
		}
)
public class EmergencyScreening {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_luot_sang_loc", nullable = false)
	private Long id;

	@Column(
			name = "ma_phien_ung_dung",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private UUID applicationSessionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_benh_nhan")
	private Patient patient;

	@Column(
			name = "phien_ban_bo_cau_hoi",
			nullable = false,
			length = 30
	)
	private String questionnaireVersion = "APP_GD1_V1";

	@Column(name = "co_kho_tho_kho_khe_nghen_hong", nullable = false)
	private boolean breathingDifficulty;

	@Column(name = "co_chong_mat_tuc_nguc_sap_ngat", nullable = false)
	private boolean dizzinessChestPainOrFainting;

	@Column(name = "co_sung_moi_luoi_co_hong", nullable = false)
	private boolean mouthTongueOrThroatSwelling;

	@Column(name = "co_dau_bung_non_lien_tuc", nullable = false)
	private boolean persistentAbdominalPainOrVomiting;

	@Column(
			name = "co_nguy_co_phan_ve",
			insertable = false,
			updatable = false
	)
	private Boolean anaphylaxisRisk;

	@Column(
			name = "ket_qua_dieu_huong",
			insertable = false,
			updatable = false
	)
	private String navigationResult;

	@Column(name = "xac_nhan_muon_goi_cap_cuu")
	private Boolean confirmedEmergencyCall;

	@Column(name = "da_nhan_nut_goi_cap_cuu", nullable = false)
	private boolean emergencyCallButtonPressed = false;

	@Column(name = "so_dien_thoai_cap_cuu", length = 20)
	private String emergencyPhoneNumber;

	@Column(name = "thoi_diem_nhan_nut_goi")
	private Instant emergencyCallButtonPressedAt;

	@Column(name = "thoi_diem_bat_dau", nullable = false)
	private Instant startedAt;

	@Column(name = "thoi_diem_hoan_tat", nullable = false)
	private Instant completedAt;

	@Column(
			name = "thoi_diem_cap_nhat",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant updatedAt;
}
