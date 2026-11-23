
package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
		name = "khoaphong",
		schema = "public"
)
@Getter
@Setter
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_khoa_phong", nullable = false)
	private Long id;

	@Column(name = "ten_khoa_phong", nullable = false, length = 150)
	private String name;

	@Column(name = "loai_khoa", nullable = false, length = 50)
	private String departmentType;

	@Column(name = "mo_ta", columnDefinition = "text")
	private String description;

	@Column(name = "ma_chi_nhanh", nullable = false)
	private Long branchId;
}

