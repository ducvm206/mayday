
package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
		name = "tinhtrangnhiemtrung",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uq_tinhtrangnhiemtrung_hoso",
						columnNames = "ma_ho_so"
				)
		}
)
@Getter
@Setter
public class InfectionStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
			name = "ma_tinh_trang_nhiem_trung",
			nullable = false,
			updatable = false
	)
	private Long id;

	@Column(name = "ma_ho_so", nullable = false, unique = true)
	private Long acuteMedicalRecordId;

	// Respiratory infection symptoms
	@Column(name = "ho_hap_ho")
	private Boolean respiratoryCough;

	@Column(name = "ho_hap_dau_hong")
	private Boolean respiratorySoreThroat;

	@Column(name = "ho_hap_hat_hoi")
	private Boolean respiratorySneezing;

	@Column(name = "ho_hap_so_mui")
	private Boolean respiratoryRunnyNose;

	@Column(name = "ho_hap_thoi_gian")
	private BigDecimal respiratoryDuration;

	@Column(name = "ho_hap_khoang_cach")
	private BigDecimal respiratoryTimeSinceOccurrence;

	// Urinary tract infection symptoms
	@Column(name = "tiet_nieu_tieu_rat")
	private Boolean urinaryPainfulUrination;

	@Column(name = "tiet_nieu_tieu_buot")
	private Boolean urinaryBurningUrination;

	@Column(name = "tiet_nieu_thoi_gian")
	private BigDecimal urinaryDuration;

	@Column(name = "tiet_nieu_khoang_cach")
	private BigDecimal urinaryTimeSinceOccurrence;

	// Skin infection symptoms
	@Column(name = "da_mun")
	private Boolean skinAcne;

	@Column(name = "da_nhot")
	private Boolean skinBoils;

	@Column(name = "da_thoi_gian")
	private BigDecimal skinDuration;

	@Column(name = "da_khoang_cach")
	private BigDecimal skinTimeSinceOccurrence;

	// Other infection symptoms
	@Column(name = "khac_ghi_ro", columnDefinition = "text")
	private String otherSymptoms;

	@Column(name = "khac_thoi_gian")
	private BigDecimal otherDuration;

	@Column(name = "khac_khoang_cach")
	private BigDecimal otherTimeSinceOccurrence;
}
