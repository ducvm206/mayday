package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "nhanvien")
@PrimaryKeyJoinColumn(
		name = "ma_nhan_vien",
		referencedColumnName = "ma_nguoi"
)
public class Employee extends Person {
	/**
	 * Branch where the employee works.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "ma_chi_nhanh",
			nullable = false
	)
	private Branch branch;
}
