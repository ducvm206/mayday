package com.ducvm.mayday.entity;

import com.ducvm.mayday.entity.Account;
import com.ducvm.mayday.entity.MedicalRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tepdinhkem")
@Getter
@Setter
public class Attachment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ma_tep_dinh_kem", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ma_ho_so", nullable = false)
	private MedicalRecord medicalRecord;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ma_tai_khoan")
	private Account account;

	@Column(name = "ten_tep_goc", nullable = false, length = 255)
	private String originalFileName;

	@Column(name = "duong_dan_luu_tru", nullable = false, length = 500)
	private String storagePath;

	@Column(name = "duong_dan_url", length = 500)
	private String url;

	@Column(name = "dinh_dang_tep", length = 50)
	private String fileFormat;

	@Column(name = "loai_phuong_tien", length = 50)
	private String mediaType;

	@Column(name = "dung_luong_byte")
	private Long fileSizeBytes;

	@Column(name = "phan_loai_muc_dich", length = 100)
	private String purposeCategory;

	@Column(name = "mo_ta_tep", columnDefinition = "text")
	private String description;

	@Column(name = "diem_do_mo", precision = 6, scale = 2)
	private BigDecimal blurScore;

	@Column(name = "thoi_diem_tao", nullable = false,
			insertable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "thoi_diem_cap_nhat", nullable = false,
			insertable = false, updatable = false)
	private Instant updatedAt;
}
