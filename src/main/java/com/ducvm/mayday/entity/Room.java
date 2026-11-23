
package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "phongkham")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_phong_kham", nullable = false)
	private Long id;

	@Column(name = "ma_chi_nhanh", nullable = false)
	private Long branchId;

	@Column(name = "ten_phong_kham", nullable = false, length = 150)
	private String name;

}