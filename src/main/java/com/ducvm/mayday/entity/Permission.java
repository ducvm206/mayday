package com.ducvm.mayday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(
		name = "quyenhan",
		schema = "public"
)
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_quyen_han", nullable = false)
	private Long id;

	@Column(name = "ten_quyen_han", nullable = false, length = 100)
	private String name;

	@Column(name = "nhom_quyen", length = 100)
	private String permissionGroup;

	@Column(name = "mo_ta", columnDefinition = "text")
	private String description;

	/**
	 * Owned by DB (trigger trg_quyenhan_cap_nhat).
	 */
	@Column(name = "thoi_diem_tao", nullable = false, insertable = false, updatable = false)
	private Instant createdAt;

	/**
	 * Owned by DB (trigger trg_quyenhan_cap_nhat).
	 */
	@Column(name = "thoi_diem_cap_nhat", nullable = false, insertable = false, updatable = false)
	private Instant updatedAt;

	/**
	 * Role–permission assignments. Explicit entity preserves audit columns.
	 */
	@OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<RolePermission> roles = new HashSet<>();
}
