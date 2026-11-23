package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
		name = "taikhoan",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uq_tai_khoan_nguoi",
						columnNames = "ma_nguoi"
				)
		}
)
@Getter
@Setter
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_tai_khoan", nullable = false)
	private Long id;

	/**
	 * Owning person. 1:0..1 — a person has at most one account
	 * (UNIQUE constraint on taikhoan.ma_nguoi).
	 */
	@Column(name = "ma_nguoi", nullable = false, unique = true)
	private Long personId;


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_vai_tro", nullable = false)
	private Role role;

	/**
	 * Login name = the account's phone number (10 digits).
	 * uq_tai_khoan_ten_dang_nhap ON lower(ten_dang_nhap).
	 */
	@Column(name = "ten_dang_nhap", nullable = false, length = 100)
	private String username;

	/**
	 * Password hash — never store the plain password.
	 */
	@Column(name = "mat_khau_ma_hoa", nullable = false, columnDefinition = "text")
	private String encodedPassword;

	@Column(name = "trang_thai_hoat_dong", nullable = false)
	private Boolean active = true;

	/**
	 * Owned by DB (DEFAULT now(), trigger trg_taikhoan_cap_nhat).
	 */
	@Column(name = "thoi_diem_tao", nullable = false, insertable = false, updatable = false)
	private Instant createdAt;

	/**
	 * Owned by DB (trigger trg_taikhoan_cap_nhat).
	 */
	@Column(name = "thoi_diem_cap_nhat", nullable = false, insertable = false, updatable = false)
	private Instant updatedAt;
}