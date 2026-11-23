package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "nguoi")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

	/**
	 * Person id.
	 */
	@Id
	@Column(name = "ma_nguoi", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Person name.
	 */
	@Column(name = "ho_ten", nullable = false, length = 150)
	private String fullName;

	/**
	 * Date of birth.
	 */
	@Column(name = "ngay_sinh")
	private LocalDate dateOfBirth;

	/**
	 * Sex: "Nam" or "Nu".
	 */
	@Column(name = "gioi_tinh", length = 3)
	private String sex;

	/**
	 * Phone number (10 digits; not unique because family members may share).
	 */
	@Column(name = "so_dien_thoai", nullable = false, length = 10)
	private String phone;

	/**
	 * City of residence.
	 */
	@Column(name = "dia_chi_tinh_tp", length = 100)
	private String city;

	/**
	 * Ward of residence.
	 */
	@Column(name = "dia_chi_xa_phuong", length = 100)
	private String ward;

	/**
	 * Home address.
	 */
	@Column(name = "dia_chi_so_nha_thon_pho", length = 255)
	private String address;

	/**
	 * Creation date. Owned by the database (DEFAULT now(), trigger protect_tao).
	 */
	@Column(name = "thoi_diem_tao", nullable = false, insertable = false, updatable = false)
	private Instant createdAt;

	/**
	 * Last modified date. Owned by the database (trigger trg_nguoi_cap_nhat).
	 */
	@Column(name = "thoi_diem_cap_nhat", nullable = false, insertable = false, updatable = false)
	private Instant updatedAt;

}
