package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "benhnhan")
@PrimaryKeyJoinColumn(
		name = "ma_benh_nhan",
		referencedColumnName = "ma_nguoi"
)
public class Patient extends Person {

	/**
	 * HIS number (unique when present; enforced by DB unique index).
	 */
	@Column(name = "ma_his", length = 50)
	private String hisNumber;

	/**
	 * Occupation.
	 */
	@Column(name = "nghe_nghiep", length = 40)
	private String occupation;

	/**
	 * Ethnicity.
	 */
	@Column(name = "dan_toc", length = 50)
	private String ethnicity;

	/**
	 * Medical insurance number.
	 */
	@Column(name = "bhyt_so_the", length = 30)
	private String medicalInsuranceNumber;

	/**
	 * Medical insurance expiry.
	 */
	@Column(name = "bhyt_gia_tri_den")
	private LocalDate medicalInsuranceExpiry;

	/**
	 * Family member name and address.
	 */
	@Column(name = "nguoi_nha_ho_ten_dia_chi", columnDefinition = "text")
	private String familyNameAndAddress;

	/**
	 * Family member phone (10 digits).
	 */
	@Column(name = "nguoi_nha_so_dien_thoai", length = 10)
	private String familyPhoneNumber;
}
