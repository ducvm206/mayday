package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chinhanh")
public class Branch {

	/**
	 * Branch id.
	 */
	@Id
	@Column(name = "ma_chi_nhanh", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Branch name.
	 */
	@Column(name = "ten_chi_nhanh", nullable = false, length = 150)
	private String name;

	/**
	 * Branch address.
	 */
	@Column(name = "dia_chi", nullable = false, columnDefinition = "text")
	private String address;

	/**
	 * Branch hotline.
	 */
	@Column(name = "so_dien_thoai", length = 20)
	private String hotline;

	/**
	 * Branch active status.
	 */
	@Column(name = "trang_thai", nullable = false)
	private Boolean status = true;
}
