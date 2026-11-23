package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
		name = "vaitro"
)
@Getter
@Setter
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_vai_tro", nullable = false)
	private Long id;

	@Column(name = "ten_vai_tro", nullable = false, length = 50)
	private String name;

	@Column(name = "ten_hien_thi", nullable = false, length = 100)
	private String displayName;

	@Column(name = "mo_ta_vai_tro", columnDefinition = "text")
	private String description;

	/**
	 * Owned by DB (trigger trg_vaitro_cap_nhat).
	 */
	@Column(name = "thoi_diem_tao", nullable = false, insertable = false, updatable = false)
	private Instant createdAt;

	/**
	 * Owned by DB (trigger trg_vaitro_cap_nhat).
	 */
	@Column(name = "thoi_diem_cap_nhat", nullable = false, insertable = false, updatable = false)
	private Instant updatedAt;

	/**
	 * Role–permission assignments. Use the explicit RolePermission entity
	 * (not @ManyToMany) so the audit columns thoi_diem_gan / ma_nguoi_gan
	 * are preserved.
	 */
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<RolePermission> permissions = new HashSet<>();
}
