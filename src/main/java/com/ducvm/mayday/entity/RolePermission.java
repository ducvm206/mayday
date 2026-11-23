package com.ducvm.mayday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "vaitroquyenhan", schema = "public")
public class RolePermission {

	@EmbeddedId
	private RolePermissionId id;

	@MapsId("roleId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_vai_tro", nullable = false)
	private Role role;

	@MapsId("permissionId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_quyen_han", nullable = false)
	private Permission permission;

	@Column(
			name = "thoi_diem_gan",
			nullable = false,
			insertable = false,
			updatable = false
	)
	private Instant assignedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_nguoi_gan")
	private Person assignedBy;
}