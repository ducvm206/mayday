package com.ducvm.mayday.repository;

import com.ducvm.mayday.enums.ResponseCode;
import com.ducvm.mayday.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MedicalRecordAnswerRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final Map<String, Set<String>> ALLOWED_COLUMNS = Map.of(
			"acute_details", Set.of(
					"nhap_vien",
					"cd_soc_phan_ve",
					"cd_phan_ve",
					"cd_may_day_cap_nang",
					"cd_may_day_cap_thong_thuong",
					"tien_su_benh_nen_da",
					"tien_su_benh_nen_ho_hap",
					"tien_su_benh_nen_tuan_hoan",
					"tien_su_benh_tim_mach_ghi_ro",
					"tien_su_benh_tieu_hoa_man_tinh_ghi_ro",
					"tien_su_benh_nen_tieu_hoa",
					"nhom_yeu_to_nghi_ngo",
					"co_nghi_ngo_dot_nay",
					"ten_yeu_to_nghi_ngo",
					"loai_tien_su",
					"da_tung_phan_ung_truoc_day",
					"thoi_gian_tiep_xuc_den_khoi_phat",
					"thoi_gian_keo_dai_tb",
					"dot_gan_nhat_cach",
					"tien_su_phan_ve_ghi_ro",
					"tien_su_di_ung_ghi_ro",
					"da_dieu_tri_benh_dot_nay",
					"tuoi_lan_dau_bi_benh",
					"so_lan_may_day_cap_truoc",
					"benh_phu"
			),
			"chronic_details", Set.of(
					"chan_doan_chinh",
					"phan_nhom_csu",
					"chung_da_ve_noi",
					"may_day_cholinergic",
					"may_day_do_lanh",
					"may_day_adrenergic",
					"may_day_do_nuoc",
					"may_day_do_anh_sang",
					"may_day_cam_ung_khac",
					"may_day_cam_ung_khac_ghi_ro",
					"bieu_hien_qua_trinh_benh"
			),
			"personal_history", Set.of(
					"tien_su_co_dia",
					"tien_su_co_dia_ghi_ro",
					"tien_su_tuyen_giap",
					"tien_su_tuyen_giap_ghi_ro",
					"tien_su_tu_mien",
					"tien_su_tu_mien_ghi_ro",
					"tien_su_di_ung",
					"tien_su_di_ung_ghi_ro",
					"tien_su_phan_ve",
					"tien_su_phan_ve_ghi_ro",
					"tien_su_viem_nhiem_man",
					"tien_su_viem_nhiem_man_ghi_ro",
					"tien_su_benh_khac",
					"tien_su_benh_khac_ghi_ro"
			),
			"allergy_triggers", Set.of(
					"thuc_an_nghi_ngo", "thuc_an_thoi_gian",
					"thuc_an_tung_bi", "thuc_an_loai_tien_su",
					"thuc_an_tien_su_ghi_ro",
					"thuoc_nghi_ngo", "thuoc_thoi_gian",
					"thuoc_tung_bi", "thuoc_loai_tien_su",
					"thuoc_tien_su_ghi_ro",
					"con_trung_nghi_ngo", "con_trung_thoi_gian",
					"con_trung_tung_bi", "con_trung_loai_tien_su",
					"con_trung_tien_su_ghi_ro",
					"khac_nghi_ngo", "khac_thoi_gian",
					"khac_tung_bi", "khac_loai_tien_su",
					"khac_tien_su_ghi_ro"
			),
			"previous_episodes", Set.of(
					"tung_co_dot_tuong_tu",
					"tuoi_khoi_phat",
					"so_dot_truoc",
					"kieu_dien_bien",
					"bieu_hien_dot_truoc",
					"da_tung_nhap_vien",
					"so_lan_nhap_vien",
					"thoi_gian_tu_dot_gan_nhat",
					"dien_bien_cac_dot_truoc"
			),
			"infection_status", Set.of(
					"ho_hap_ho",
					"ho_hap_dau_hong",
					"ho_hap_hat_hoi",
					"ho_hap_so_mui",
					"ho_hap_thoi_gian",
					"ho_hap_khoang_cach",
					"tiet_nieu_tieu_rat",
					"tiet_nieu_tieu_buot",
					"tiet_nieu_thoi_gian",
					"tiet_nieu_khoang_cach",
					"da_mun",
					"da_nhot",
					"da_thoi_gian",
					"da_khoang_cach",
					"khac_ghi_ro",
					"khac_thoi_gian",
					"khac_khoang_cach"
			)
	);

	private static final Map<String, String> SECTION_TABLES = Map.of(
			"acute_details", "benhanmaydaycap",
			"chronic_details", "benhanmaydayman",
			"personal_history", "tiensubanthan",
			"allergy_triggers", "hoancanhkhoiphatdiung",
			"previous_episodes", "benhsucacdotbenhtruoc",
			"infection_status", "tinhtrangnhiemtrung"
	);

	public List<Map<String, Object>> findAcuteDetails(Long medicalRecordId) {
		return jdbcTemplate.queryForList(
				"SELECT * FROM benhanmaydaycap WHERE ma_ho_so = ?",
				medicalRecordId
		);
	}

	public List<Map<String, Object>> findChronicDetails(Long medicalRecordId) {
		return jdbcTemplate.queryForList(
				"SELECT * FROM benhanmaydayman WHERE ma_ho_so = ?",
				medicalRecordId
		);
	}

	public List<Map<String, Object>> findPersonalHistory(Long medicalRecordId) {
		return jdbcTemplate.queryForList(
				"SELECT * FROM tiensubanthan WHERE ma_ho_so = ?",
				medicalRecordId
		);
	}

	public List<Map<String, Object>> findAllergyTriggers(Long medicalRecordId) {
		return jdbcTemplate.queryForList(
				"SELECT * FROM hoancanhkhoiphatdiung WHERE ma_ho_so = ?",
				medicalRecordId
		);
	}

	public List<Map<String, Object>> findPreviousEpisodes(Long medicalRecordId) {
		return jdbcTemplate.queryForList(
				"SELECT * FROM benhsucacdotbenhtruoc WHERE ma_ho_so = ?",
				medicalRecordId
		);
	}

	public List<Map<String, Object>> findInfectionStatus(Long medicalRecordId) {
		return jdbcTemplate.queryForList(
				"SELECT * FROM tinhtrangnhiemtrung WHERE ma_ho_so = ?",
				medicalRecordId
		);
	}

	public void upsertSection(
			Long medicalRecordId,
			String section,
			Map<String, Object> values) {

		String table = SECTION_TABLES.get(section);
		Set<String> allowedColumns = ALLOWED_COLUMNS.get(section);

		if (table == null || allowedColumns == null) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		if (values == null || values.isEmpty()
				|| !allowedColumns.containsAll(values.keySet())) {
			throw new ApiException(ResponseCode.PARAMETER_VALUE_INVALID);
		}

		List<String> columns = new ArrayList<>(values.keySet());

		String columnNames = String.join(", ", columns);

		String placeholders = String.join(
				", ",
				Collections.nCopies(columns.size(), "?")
		);

		String updateClause = columns.stream()
				.map(column -> column + " = EXCLUDED." + column)
				.collect(Collectors.joining(", "));

		String sql = """
            INSERT INTO %s (ma_ho_so, %s)
            VALUES (?, %s)
            ON CONFLICT (ma_ho_so)
            DO UPDATE SET %s
            """.formatted(table, columnNames, placeholders, updateClause);

		List<Object> parameters = new ArrayList<>();
		parameters.add(medicalRecordId);

		for (String column : columns) {
			parameters.add(values.get(column));
		}

		jdbcTemplate.update(sql, parameters.toArray());
	}
}
