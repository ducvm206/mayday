package com.ducvm.mayday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
		name = "dacdiemsanphu",
		schema = "public",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "dacdiemsanphu_ten_dac_diem_key",
						columnNames = "ten_dac_diem"
				)
		}
)
public class AngioedemaCharacteristic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_dac_diem_san_phu", nullable = false)
	private Long id;

	@Column(name = "ten_dac_diem", nullable = false, length = 150)
	private String name;

	@Column(name = "mo_ta", columnDefinition = "text")
	private String description;
}
