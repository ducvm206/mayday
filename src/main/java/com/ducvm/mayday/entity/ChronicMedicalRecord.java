package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "benhanmaydayman")
@Getter
@Setter
public class ChronicMedicalRecord {
	/**
	 * Medical record ID, shared with the parent medical record.
	 */
	@Id
	@Column(name = "ma_ho_so", nullable = false)
	private Long id;
	/**
	 * Main diagnosis.
	 */
	@Column(name = "chan_doan_chinh", columnDefinition = "text")
	private String mainDiagnosis;
	/**
	 * CSU subgroup.
	 */
	@Column(name = "phan_nhom_csu", columnDefinition = "text")
	private String csuSubtype;
	/**
	 * Whether the condition presents with wheals.
	 */
	@Column(name = "chung_da_ve_noi")
	private Boolean hasWheals;

	@Column(name = "may_day_cholinergic")
	private Boolean cholinergicUrticaria;

	@Column(name = "may_day_do_lanh")
	private Boolean coldUrticaria;

	@Column(name = "may_day_adrenergic")
	private Boolean adrenergicUrticaria;

	@Column(name = "may_day_do_nuoc")
	private Boolean aquagenicUrticaria;

	@Column(name = "may_day_do_anh_sang")
	private Boolean solarUrticaria;

	@Column(name = "may_day_cam_ung_khac")
	private Boolean hasOtherInducibleUrticaria;

	@Column(
			name = "may_day_cam_ung_khac_ghi_ro",
			columnDefinition = "text"
	)
	private String otherInducibleUrticariaDetails;
	/**
	 * Clinical presentation during the disease course.
	 */
	@Column(name = "bieu_hien_qua_trinh_benh", columnDefinition = "text")
	private String diseaseCoursePresentation;
}
