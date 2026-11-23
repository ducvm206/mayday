package com.ducvm.mayday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "thuoc", schema = "public")
public class Medication {

	@Id
	@Column(name = "ma_thuoc", nullable = false, length = 50)
	private String id;

	@Column(name = "ten_thuoc", nullable = false, length = 255)
	private String name;

	@Column(name = "hoat_chat", length = 255)
	private String activeIngredient;

	@Column(name = "nhom_dieu_tri", length = 100)
	private String treatmentGroup;

	@Column(
			name = "thoi_diem_tao",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant createdAt;

	@Column(
			name = "thoi_diem_cap_nhat",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant updatedAt;
}
