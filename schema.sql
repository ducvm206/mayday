-- public.chinhanh definition

-- Drop table

-- DROP TABLE public.chinhanh;

CREATE TABLE public.chinhanh (
                                 ma_chi_nhanh int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                 ten_chi_nhanh varchar(150) NOT NULL,
                                 dia_chi text NOT NULL,
                                 so_dien_thoai varchar(20) NULL,
                                 trang_thai bool DEFAULT true NOT NULL,
                                 CONSTRAINT ck_chi_nhanh_dia_chi CHECK ((btrim(dia_chi) <> ''::text)),
                                 CONSTRAINT ck_chi_nhanh_sdt CHECK ((btrim((so_dien_thoai)::text) <> ''::text)),
                                 CONSTRAINT ck_chi_nhanh_ten CHECK ((btrim((ten_chi_nhanh)::text) <> ''::text)),
                                 CONSTRAINT pk_chi_nhanh PRIMARY KEY (ma_chi_nhanh)
);
COMMENT ON TABLE public.chinhanh IS 'Chi nhánh phòng khám.';


-- public.dacdiemsanphu definition

-- Drop table

-- DROP TABLE public.dacdiemsanphu;

CREATE TABLE public.dacdiemsanphu (
                                      ma_dac_diem_san_phu int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                      ten_dac_diem varchar(150) NOT NULL,
                                      mo_ta text NULL,
                                      CONSTRAINT dacdiemsanphu_pkey PRIMARY KEY (ma_dac_diem_san_phu),
                                      CONSTRAINT dacdiemsanphu_ten_dac_diem_check CHECK ((btrim((ten_dac_diem)::text) <> ''::text)),
                                      CONSTRAINT dacdiemsanphu_ten_dac_diem_key UNIQUE (ten_dac_diem)
);


-- public.loainghiemphap definition

-- Drop table

-- DROP TABLE public.loainghiemphap;

CREATE TABLE public.loainghiemphap (
                                       ma_nghiem_phap int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                       ten_nghiem_phap varchar(150) NOT NULL,
                                       phuong_phap_thuc_hien text NULL,
                                       thoi_gian_chuan numeric(10, 2) NULL, -- Quy ước: phút. NULL nếu chưa xác định.
                                       thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       la_can_nguyen_khac bool DEFAULT false NOT NULL,
                                       CONSTRAINT ck_loai_nghiem_phap_ten CHECK ((btrim((ten_nghiem_phap)::text) <> ''::text)),
                                       CONSTRAINT ck_loai_nghiem_phap_thoi_gian CHECK ((thoi_diem_cap_nhat >= thoi_diem_tao)),
                                       CONSTRAINT ck_loai_nghiem_phap_thoi_gian_chuan CHECK (((thoi_gian_chuan >= (0)::numeric) AND (thoi_gian_chuan < 'Infinity'::numeric))),
                                       CONSTRAINT pk_loai_nghiem_phap PRIMARY KEY (ma_nghiem_phap),
                                       CONSTRAINT uq_loai_nghiem_phap_ten UNIQUE (ten_nghiem_phap),
                                       CONSTRAINT uq_nghiemphap_phanloai UNIQUE (ma_nghiem_phap, la_can_nguyen_khac)
);
COMMENT ON TABLE public.loainghiemphap IS 'Danh mục loại nghiệm pháp kích thích.';

-- Column comments

COMMENT ON COLUMN public.loainghiemphap.thoi_gian_chuan IS 'Quy ước: phút. NULL nếu chưa xác định.';

-- Table Triggers

create trigger trg_loainghiemphap_cap_nhat before
    update
    on
        public.loainghiemphap for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.nguoi definition

-- Drop table

-- DROP TABLE public.nguoi;

CREATE TABLE public.nguoi (
                              ma_nguoi int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                              ho_ten varchar(150) NOT NULL,
                              ngay_sinh date NULL,
                              gioi_tinh varchar(3) NULL,
                              so_dien_thoai varchar(10) NOT NULL, -- 10 chữ số; không UNIQUE vì người thân có thể dùng chung số.
                              dia_chi_tinh_tp varchar(100) NULL,
                              dia_chi_xa_phuong varchar(100) NULL,
                              dia_chi_so_nha_thon_pho varchar(255) NULL,
                              thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              CONSTRAINT ck_nguoi_gioi_tinh CHECK (((gioi_tinh)::text = ANY ((ARRAY['Nam'::character varying, 'Nu'::character varying])::text[]))),
	CONSTRAINT ck_nguoi_ho_ten CHECK ((btrim((ho_ten)::text) <> ''::text)),
	CONSTRAINT ck_nguoi_ngay_sinh CHECK ((ngay_sinh <= CURRENT_DATE)),
	CONSTRAINT ck_nguoi_sdt CHECK (((so_dien_thoai)::text ~ '^[0-9]{10}$'::text)),
	CONSTRAINT ck_nguoi_thoi_gian CHECK ((thoi_diem_cap_nhat >= thoi_diem_tao)),
	CONSTRAINT pk_nguoi PRIMARY KEY (ma_nguoi)
);
COMMENT ON TABLE public.nguoi IS 'Supertype: thông tin chung của bệnh nhân và nhân viên.';

-- Column comments

COMMENT ON COLUMN public.nguoi.so_dien_thoai IS '10 chữ số; không UNIQUE vì người thân có thể dùng chung số.';

-- Table Triggers

create trigger trg_nguoi_cap_nhat before
    update
    on
        public.nguoi for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.quyenhan definition

-- Drop table

-- DROP TABLE public.quyenhan;

CREATE TABLE public.quyenhan (
                                 ma_quyen_han int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                 ten_quyen_han varchar(100) NOT NULL,
                                 nhom_quyen varchar(100) NULL,
                                 mo_ta text NULL,
                                 thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 CONSTRAINT ck_quyen_han_nhom CHECK ((btrim((nhom_quyen)::text) <> ''::text)),
                                 CONSTRAINT ck_quyen_han_ten CHECK ((btrim((ten_quyen_han)::text) <> ''::text)),
                                 CONSTRAINT ck_quyen_han_thoi_gian CHECK ((thoi_diem_cap_nhat >= thoi_diem_tao)),
                                 CONSTRAINT pk_quyen_han PRIMARY KEY (ma_quyen_han),
                                 CONSTRAINT uq_quyen_han_ten UNIQUE (ten_quyen_han)
);
COMMENT ON TABLE public.quyenhan IS 'Danh mục quyền hạn ứng dụng.';

-- Table Triggers

create trigger trg_quyenhan_cap_nhat before
    update
    on
        public.quyenhan for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.thuoc definition

-- Drop table

-- DROP TABLE public.thuoc;

CREATE TABLE public.thuoc (
                              ma_thuoc varchar(50) NOT NULL,
                              ten_thuoc varchar(255) NOT NULL,
                              hoat_chat varchar(255) NULL,
                              nhom_dieu_tri varchar(100) NULL,
                              thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              CONSTRAINT ck_thuoc_nhom_dieu_tri CHECK (((nhom_dieu_tri IS NULL) OR ((nhom_dieu_tri)::text = ANY ((ARRAY['Khang histamine H1 TH2'::character varying, 'Sinh hoc/dich'::character varying, 'Uc che mien dich'::character varying, 'Corticosteroid toan than'::character varying, 'Khac'::character varying])::text[])))),
	CONSTRAINT ck_thuoc_ten CHECK ((btrim((ten_thuoc)::text) <> ''::text)),
	CONSTRAINT pk_thuoc PRIMARY KEY (ma_thuoc)
);
COMMENT ON TABLE public.thuoc IS 'Danh mục thuốc điều trị.';

-- Table Triggers

create trigger trg_thuoc_cap_nhat before
    update
    on
        public.thuoc for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.vaitro definition

-- Drop table

-- DROP TABLE public.vaitro;

CREATE TABLE public.vaitro (
                               ma_vai_tro int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                               ten_vai_tro varchar(50) NOT NULL,
                               ten_hien_thi varchar(100) NOT NULL,
                               mo_ta_vai_tro text NULL,
                               thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               CONSTRAINT ck_vai_tro_hien_thi CHECK ((btrim((ten_hien_thi)::text) <> ''::text)),
                               CONSTRAINT ck_vai_tro_ten CHECK ((btrim((ten_vai_tro)::text) <> ''::text)),
                               CONSTRAINT ck_vai_tro_thoi_gian CHECK ((thoi_diem_cap_nhat >= thoi_diem_tao)),
                               CONSTRAINT pk_vai_tro PRIMARY KEY (ma_vai_tro),
                               CONSTRAINT uq_vai_tro_ten UNIQUE (ten_vai_tro)
);
COMMENT ON TABLE public.vaitro IS 'Vai trò hệ thống (Admin, Bác sĩ, ...).';

-- Table Triggers

create trigger trg_vaitro_cap_nhat before
    update
    on
        public.vaitro for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.benhnhan definition

-- Drop table

-- DROP TABLE public.benhnhan;

CREATE TABLE public.benhnhan (
                                 ma_benh_nhan int8 NOT NULL,
                                 ma_his varchar(50) NULL, -- Mã HIS dạng chuỗi; NULL nếu chưa đồng bộ HIS.
                                 nghe_nghiep varchar(40) NULL,
                                 dan_toc varchar(50) NULL,
                                 bhyt_so_the varchar(30) NULL,
                                 bhyt_gia_tri_den date NULL,
                                 nguoi_nha_ho_ten_dia_chi text NULL,
                                 nguoi_nha_so_dien_thoai varchar(10) NULL,
                                 CONSTRAINT ck_benh_nhan_bhyt CHECK ((btrim((bhyt_so_the)::text) <> ''::text)),
                                 CONSTRAINT ck_benh_nhan_ma_his CHECK ((btrim((ma_his)::text) <> ''::text)),
                                 CONSTRAINT ck_benh_nhan_nghe_nghiep CHECK (((nghe_nghiep)::text = ANY ((ARRAY['Cong nhan'::character varying, 'Nong dan'::character varying, 'Hoc sinh - Sinh vien'::character varying, 'Can bo cong chuc'::character varying, 'Khac'::character varying])::text[]))),
	CONSTRAINT ck_benh_nhan_sdt_nguoi_nha CHECK (((nguoi_nha_so_dien_thoai)::text ~ '^[0-9]{10}$'::text)),
	CONSTRAINT pk_benh_nhan PRIMARY KEY (ma_benh_nhan),
	CONSTRAINT uq_benh_nhan_ma_his UNIQUE (ma_his),
	CONSTRAINT fk_benh_nhan_nguoi FOREIGN KEY (ma_benh_nhan) REFERENCES public.nguoi(ma_nguoi) ON DELETE RESTRICT
);
COMMENT ON TABLE public.benhnhan IS 'Subtype của Nguoi; ma_benh_nhan đồng thời là PK và FK tới Nguoi.';

-- Column comments

COMMENT ON COLUMN public.benhnhan.ma_his IS 'Mã HIS dạng chuỗi; NULL nếu chưa đồng bộ HIS.';


-- public.chukytheodoiuas7 definition

-- Drop table

-- DROP TABLE public.chukytheodoiuas7;

CREATE TABLE public.chukytheodoiuas7 (
                                         ma_chu_ky int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                         ma_benh_nhan int8 NOT NULL,
                                         ngay_bat_dau date NOT NULL,
                                         ngay_ket_thuc date NOT NULL,
                                         trang_thai_chu_ky varchar(50) DEFAULT 'DANG_THEO_DOI'::character varying NULL,
                                         thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         CONSTRAINT ck_chukyuas7_ngay CHECK ((ngay_ket_thuc = (ngay_bat_dau + 6))),
                                         CONSTRAINT pk_chukytheodoiuas7 PRIMARY KEY (ma_chu_ky),
                                         CONSTRAINT uq_chuky_benhnhan_ngay UNIQUE (ma_chu_ky, ma_benh_nhan, ngay_bat_dau),
                                         CONSTRAINT fk_chukyuas7_benhnhan FOREIGN KEY (ma_benh_nhan) REFERENCES public.benhnhan(ma_benh_nhan) ON DELETE RESTRICT
);
CREATE INDEX idx_chukyuas7_benhnhan ON public.chukytheodoiuas7 USING btree (ma_benh_nhan);
COMMENT ON TABLE public.chukytheodoiuas7 IS 'Chu kỳ theo dõi UAS7 (7 ngày).';

-- Table Triggers

create trigger trg_chukyuas7_cap_nhat before
    update
    on
        public.chukytheodoiuas7 for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.khoaphong definition

-- Drop table

-- DROP TABLE public.khoaphong;

CREATE TABLE public.khoaphong (
                                  ma_khoa_phong int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                  ten_khoa_phong varchar(150) NOT NULL,
                                  loai_khoa varchar(50) NOT NULL,
                                  mo_ta text NULL,
                                  ma_chi_nhanh int8 NOT NULL,
                                  CONSTRAINT ck_khoa_phong_loai CHECK ((btrim((loai_khoa)::text) <> ''::text)),
                                  CONSTRAINT ck_khoa_phong_ten CHECK ((btrim((ten_khoa_phong)::text) <> ''::text)),
                                  CONSTRAINT pk_khoa_phong PRIMARY KEY (ma_khoa_phong),
                                  CONSTRAINT uq_khoa_phong_chi_nhanh_ten UNIQUE (ma_chi_nhanh, ten_khoa_phong),
                                  CONSTRAINT fk_khoa_phong_chi_nhanh FOREIGN KEY (ma_chi_nhanh) REFERENCES public.chinhanh(ma_chi_nhanh) ON DELETE RESTRICT
);


-- public.nhanvien definition

-- Drop table

-- DROP TABLE public.nhanvien;

CREATE TABLE public.nhanvien (
                                 ma_nhan_vien int8 NOT NULL,
                                 ma_chi_nhanh int8 NOT NULL,
                                 CONSTRAINT pk_nhan_vien PRIMARY KEY (ma_nhan_vien),
                                 CONSTRAINT fk_nhan_vien_chi_nhanh FOREIGN KEY (ma_chi_nhanh) REFERENCES public.chinhanh(ma_chi_nhanh) ON DELETE RESTRICT,
                                 CONSTRAINT fk_nhan_vien_nguoi FOREIGN KEY (ma_nhan_vien) REFERENCES public.nguoi(ma_nguoi) ON DELETE RESTRICT
);
CREATE INDEX idx_nhan_vien_chi_nhanh ON public.nhanvien USING btree (ma_chi_nhanh);
COMMENT ON TABLE public.nhanvien IS 'Subtype của Nguoi; mỗi nhân viên thuộc một chi nhánh.';


-- public.phongkham definition

-- Drop table

-- DROP TABLE public.phongkham;

CREATE TABLE public.phongkham (
                                  ma_phong_kham int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                  ten_phong_kham varchar(150) NOT NULL,
                                  ma_chi_nhanh int8 NOT NULL,
                                  CONSTRAINT phongkham_pkey PRIMARY KEY (ma_phong_kham),
                                  CONSTRAINT phongkham_ten_phong_kham_check CHECK ((btrim((ten_phong_kham)::text) <> ''::text)),
                                  CONSTRAINT uq_phongkham_chi_nhanh_ten UNIQUE (ma_chi_nhanh, ten_phong_kham),
                                  CONSTRAINT fk_phongkham_chi_nhanh FOREIGN KEY (ma_chi_nhanh) REFERENCES public.chinhanh(ma_chi_nhanh) ON DELETE RESTRICT
);


-- public.sanglocbaodongdo definition

-- Drop table

-- DROP TABLE public.sanglocbaodongdo;

CREATE TABLE public.sanglocbaodongdo (
                                         ma_luot_sang_loc int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                         ma_phien_ung_dung uuid DEFAULT gen_random_uuid() NOT NULL, -- Mã UUID duy nhất để ứng dụng gửi lại an toàn khi retry cùng một lượt sàng lọc.
                                         ma_benh_nhan int8 NULL,
                                         phien_ban_bo_cau_hoi varchar(30) DEFAULT 'APP_GD1_V1'::character varying NOT NULL,
                                         co_kho_tho_kho_khe_nghen_hong bool NOT NULL, -- Khó thở, thở khò khè hoặc cảm giác nghẹn họng.
                                         co_chong_mat_tuc_nguc_sap_ngat bool NOT NULL, -- Chóng mặt, xây xẩm mặt mày, tức ngực hoặc cảm thấy sắp ngất.
                                         co_sung_moi_luoi_co_hong bool NOT NULL, -- Sưng nề nhanh ở môi, lưỡi hoặc cổ họng.
                                         co_dau_bung_non_lien_tuc bool NOT NULL, -- Đau bụng quặn hoặc nôn mửa liên tục.
                                         co_nguy_co_phan_ve bool GENERATED ALWAYS AS (co_kho_tho_kho_khe_nghen_hong OR co_chong_mat_tuc_nguc_sap_ngat OR co_sung_moi_luoi_co_hong OR co_dau_bung_non_lien_tuc) STORED NULL,
                                         ket_qua_dieu_huong varchar(30) GENERATED ALWAYS AS (
                                             CASE
                                                 WHEN co_kho_tho_kho_khe_nghen_hong OR co_chong_mat_tuc_nguc_sap_ngat OR co_sung_moi_luoi_co_hong OR co_dau_bung_non_lien_tuc THEN 'NGUY_CO_PHAN_VE'::text
                                                 ELSE 'CHUYEN_BUOC_1'::text
                                                 END) STORED NULL,
                                         xac_nhan_muon_goi_cap_cuu bool NULL,
                                         da_nhan_nut_goi_cap_cuu bool DEFAULT false NOT NULL, -- Chỉ ghi nhận thao tác yêu cầu mở cuộc gọi; không khẳng định cuộc gọi đã kết nối.
                                         so_dien_thoai_cap_cuu varchar(20) NULL,
                                         thoi_diem_nhan_nut_goi timestamptz NULL,
                                         thoi_diem_bat_dau timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         thoi_diem_hoan_tat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         CONSTRAINT ck_sanglocbaodongdo_goi_cap_cuu CHECK (((da_nhan_nut_goi_cap_cuu AND co_nguy_co_phan_ve AND (xac_nhan_muon_goi_cap_cuu IS TRUE) AND (thoi_diem_nhan_nut_goi IS NOT NULL) AND (so_dien_thoai_cap_cuu IS NOT NULL) AND (btrim((so_dien_thoai_cap_cuu)::text) <> ''::text)) OR ((NOT da_nhan_nut_goi_cap_cuu) AND (thoi_diem_nhan_nut_goi IS NULL) AND (so_dien_thoai_cap_cuu IS NULL)))),
                                         CONSTRAINT ck_sanglocbaodongdo_phien_ban CHECK ((btrim((phien_ban_bo_cau_hoi)::text) <> ''::text)),
                                         CONSTRAINT ck_sanglocbaodongdo_thoi_gian CHECK ((thoi_diem_hoan_tat >= thoi_diem_bat_dau)),
                                         CONSTRAINT ck_sanglocbaodongdo_xac_nhan CHECK (((co_nguy_co_phan_ve AND (xac_nhan_muon_goi_cap_cuu IS NOT NULL)) OR ((NOT co_nguy_co_phan_ve) AND (xac_nhan_muon_goi_cap_cuu IS NULL)))),
                                         CONSTRAINT pk_sanglocbaodongdo PRIMARY KEY (ma_luot_sang_loc),
                                         CONSTRAINT uq_sanglocbaodongdo_phien UNIQUE (ma_phien_ung_dung),
                                         CONSTRAINT fk_sanglocbaodongdo_benhnhan FOREIGN KEY (ma_benh_nhan) REFERENCES public.benhnhan(ma_benh_nhan) ON DELETE RESTRICT
);
CREATE INDEX idx_sanglocbaodongdo_benhnhan ON public.sanglocbaodongdo USING btree (ma_benh_nhan) WHERE (ma_benh_nhan IS NOT NULL);
CREATE INDEX idx_sanglocbaodongdo_nguyco_thoigian ON public.sanglocbaodongdo USING btree (thoi_diem_hoan_tat DESC) WHERE co_nguy_co_phan_ve;
COMMENT ON TABLE public.sanglocbaodongdo IS 'Mỗi dòng là một lượt sàng lọc báo động đỏ đã hoàn tất trước đăng nhập hoặc trước bước định danh.';

-- Column comments

COMMENT ON COLUMN public.sanglocbaodongdo.ma_phien_ung_dung IS 'Mã UUID duy nhất để ứng dụng gửi lại an toàn khi retry cùng một lượt sàng lọc.';
COMMENT ON COLUMN public.sanglocbaodongdo.co_kho_tho_kho_khe_nghen_hong IS 'Khó thở, thở khò khè hoặc cảm giác nghẹn họng.';
COMMENT ON COLUMN public.sanglocbaodongdo.co_chong_mat_tuc_nguc_sap_ngat IS 'Chóng mặt, xây xẩm mặt mày, tức ngực hoặc cảm thấy sắp ngất.';
COMMENT ON COLUMN public.sanglocbaodongdo.co_sung_moi_luoi_co_hong IS 'Sưng nề nhanh ở môi, lưỡi hoặc cổ họng.';
COMMENT ON COLUMN public.sanglocbaodongdo.co_dau_bung_non_lien_tuc IS 'Đau bụng quặn hoặc nôn mửa liên tục.';
COMMENT ON COLUMN public.sanglocbaodongdo.da_nhan_nut_goi_cap_cuu IS 'Chỉ ghi nhận thao tác yêu cầu mở cuộc gọi; không khẳng định cuộc gọi đã kết nối.';

-- Table Triggers

create trigger trg_sanglocbaodongdo_cap_nhat before
    update
    on
        public.sanglocbaodongdo for each row execute function fn_cap_nhat_thoi_gian();


-- public.taikhoan definition

-- Drop table

-- DROP TABLE public.taikhoan;

CREATE TABLE public.taikhoan (
                                 ma_tai_khoan int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                 ma_nguoi int8 NOT NULL,
                                 ma_vai_tro int8 NOT NULL,
                                 ten_dang_nhap varchar(100) NOT NULL,
                                 mat_khau_ma_hoa text NOT NULL, -- Lưu password hash, không lưu mật khẩu rõ.
                                 trang_thai_hoat_dong bool DEFAULT true NOT NULL,
                                 thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 CONSTRAINT ck_tai_khoan_mat_khau CHECK ((btrim(mat_khau_ma_hoa) <> ''::text)),
                                 CONSTRAINT ck_tai_khoan_ten CHECK (((btrim((ten_dang_nhap)::text) <> ''::text) AND ((ten_dang_nhap)::text = btrim((ten_dang_nhap)::text)))),
	CONSTRAINT ck_tai_khoan_thoi_gian CHECK ((thoi_diem_cap_nhat >= thoi_diem_tao)),
	CONSTRAINT pk_tai_khoan PRIMARY KEY (ma_tai_khoan),
	CONSTRAINT uq_tai_khoan_nguoi UNIQUE (ma_nguoi),
	CONSTRAINT fk_tai_khoan_nguoi FOREIGN KEY (ma_nguoi) REFERENCES public.nguoi(ma_nguoi) ON DELETE RESTRICT,
	CONSTRAINT fk_tai_khoan_vai_tro FOREIGN KEY (ma_vai_tro) REFERENCES public.vaitro(ma_vai_tro) ON DELETE RESTRICT
);
CREATE INDEX idx_tai_khoan_vai_tro ON public.taikhoan USING btree (ma_vai_tro);
CREATE UNIQUE INDEX uq_tai_khoan_ten_dang_nhap ON public.taikhoan USING btree (lower((ten_dang_nhap)::text));
COMMENT ON TABLE public.taikhoan IS 'Tài khoản đăng nhập; mỗi người tối đa một tài khoản.';

-- Column comments

COMMENT ON COLUMN public.taikhoan.mat_khau_ma_hoa IS 'Lưu password hash, không lưu mật khẩu rõ.';

-- Table Triggers

create trigger trg_taikhoan_cap_nhat before
    update
    on
        public.taikhoan for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.vaitroquyenhan definition

-- Drop table

-- DROP TABLE public.vaitroquyenhan;

CREATE TABLE public.vaitroquyenhan (
                                       ma_vai_tro int8 NOT NULL,
                                       ma_quyen_han int8 NOT NULL,
                                       thoi_diem_gan timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       ma_nguoi_gan int8 NULL, -- Tham chiếu Nguoi; NULL khi hệ thống khởi tạo.
                                       CONSTRAINT pk_vai_tro_quyen_han PRIMARY KEY (ma_vai_tro, ma_quyen_han),
                                       CONSTRAINT fk_vtqh_nguoi_gan FOREIGN KEY (ma_nguoi_gan) REFERENCES public.nguoi(ma_nguoi) ON DELETE RESTRICT,
                                       CONSTRAINT fk_vtqh_quyen_han FOREIGN KEY (ma_quyen_han) REFERENCES public.quyenhan(ma_quyen_han) ON DELETE CASCADE,
                                       CONSTRAINT fk_vtqh_vai_tro FOREIGN KEY (ma_vai_tro) REFERENCES public.vaitro(ma_vai_tro) ON DELETE CASCADE
);
CREATE INDEX idx_vtqh_nguoi_gan ON public.vaitroquyenhan USING btree (ma_nguoi_gan);
CREATE INDEX idx_vtqh_quyen_han ON public.vaitroquyenhan USING btree (ma_quyen_han);
COMMENT ON TABLE public.vaitroquyenhan IS 'Thực thể kết hợp N:M giữa VaiTro và QuyenHan.';

-- Column comments

COMMENT ON COLUMN public.vaitroquyenhan.ma_nguoi_gan IS 'Tham chiếu Nguoi; NULL khi hệ thống khởi tạo.';


-- public.anhchuptonthuong definition

-- Drop table

-- DROP TABLE public.anhchuptonthuong;

CREATE TABLE public.anhchuptonthuong (
                                         ma_anh int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                         ma_tep_dinh_kem int8 NOT NULL,
                                         ma_dot_ton_thuong_da int8 NULL,
                                         ma_vi_tri_ton_thuong int8 NULL,
                                         thoi_diem_chup timestamptz NOT NULL,
                                         moc_tien_trien varchar(50) NULL,
                                         diem_ngua_nrs int2 NULL,
                                         diem_dau_nrs int2 NULL,
                                         diem_bong_rat_nrs int2 NULL,
                                         so_luong_not_san int4 NULL,
                                         chi_so_auas_cuc_bo numeric(5, 2) NULL,
                                         url_anh varchar(500) NULL,
                                         thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         CONSTRAINT ck_anh_bong_rat_nrs CHECK (((diem_bong_rat_nrs >= 0) AND (diem_bong_rat_nrs <= 10))),
                                         CONSTRAINT ck_anh_dau_nrs CHECK (((diem_dau_nrs >= 0) AND (diem_dau_nrs <= 10))),
                                         CONSTRAINT ck_anh_ngua_nrs CHECK (((diem_ngua_nrs >= 0) AND (diem_ngua_nrs <= 10))),
                                         CONSTRAINT ck_anh_so_luong CHECK ((so_luong_not_san >= 0)),
                                         CONSTRAINT ck_anh_vitri_can_dot CHECK (((ma_vi_tri_ton_thuong IS NULL) OR (ma_dot_ton_thuong_da IS NOT NULL))),
                                         CONSTRAINT pk_anhchuptonthuong PRIMARY KEY (ma_anh)
);
CREATE INDEX idx_anhchup_dot ON public.anhchuptonthuong USING btree (ma_dot_ton_thuong_da);
CREATE INDEX idx_anhchup_ma_tep ON public.anhchuptonthuong USING btree (ma_tep_dinh_kem);
CREATE INDEX idx_anhchup_vitri ON public.anhchuptonthuong USING btree (ma_vi_tri_ton_thuong);
COMMENT ON TABLE public.anhchuptonthuong IS 'Ảnh chụp tổn thương liên kết với tệp đính kèm.';

-- Table Triggers

create trigger trg_anhchuptonthuong_cap_nhat before
    update
    on
        public.anhchuptonthuong for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.benhanmaydaycap definition

-- Drop table

-- DROP TABLE public.benhanmaydaycap;

CREATE TABLE public.benhanmaydaycap (
                                        ma_ho_so int8 NOT NULL,
                                        loai_benh_an text GENERATED ALWAYS AS ('CAP'::text) STORED NULL,
	nhap_vien bool NULL,
	cd_soc_phan_ve bool NULL,
	cd_phan_ve bool NULL,
	cd_may_day_cap_nang bool NULL,
	cd_may_day_cap_thong_thuong bool NULL,
	tien_su_benh_nen_da text NULL,
	tien_su_benh_nen_ho_hap text NULL,
	tien_su_benh_nen_tuan_hoan text NULL,
	tien_su_benh_tim_mach_ghi_ro text NULL,
	tien_su_benh_tieu_hoa_man_tinh_ghi_ro text NULL,
	tien_su_benh_nen_tieu_hoa text NULL,
	nhom_yeu_to_nghi_ngo text NULL,
	co_nghi_ngo_dot_nay bool NULL,
	ten_yeu_to_nghi_ngo text NULL,
	loai_tien_su text NULL,
	da_tung_phan_ung_truoc_day bool NULL,
	thoi_gian_tiep_xuc_den_khoi_phat interval NULL,
	thoi_gian_keo_dai_tb interval NULL,
	dot_gan_nhat_cach interval NULL,
	tien_su_phan_ve_ghi_ro text NULL,
	tien_su_di_ung_ghi_ro text NULL,
	da_dieu_tri_benh_dot_nay bool NULL,
	tuoi_lan_dau_bi_benh int4 NULL,
	so_lan_may_day_cap_truoc int4 NULL,
	thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
	benh_phu text NULL,
	CONSTRAINT ck_benhanmaydaycap_dot_gan_nhat CHECK ((dot_gan_nhat_cach >= '00:00:00'::interval)),
	CONSTRAINT ck_benhanmaydaycap_keo_dai CHECK ((thoi_gian_keo_dai_tb >= '00:00:00'::interval)),
	CONSTRAINT ck_benhanmaydaycap_so_lan CHECK ((so_lan_may_day_cap_truoc >= 0)),
	CONSTRAINT ck_benhanmaydaycap_tiep_xuc CHECK ((thoi_gian_tiep_xuc_den_khoi_phat >= '00:00:00'::interval)),
	CONSTRAINT ck_benhanmaydaycap_tuoi CHECK ((tuoi_lan_dau_bi_benh >= 0)),
	CONSTRAINT pk_benhanmaydaycap PRIMARY KEY (ma_ho_so)
);
COMMENT ON TABLE public.benhanmaydaycap IS 'Thông tin chuyên biệt của bệnh án mề đay cấp.';

-- Table Triggers

create trigger trg_benhanmaydaycap_cap_nhat before
    update
    on
        public.benhanmaydaycap for each row execute function fn_cap_nhat_thoi_gian();


-- public.benhanmaydayman definition

-- Drop table

-- DROP TABLE public.benhanmaydayman;

CREATE TABLE public.benhanmaydayman (
                                        ma_ho_so int8 NOT NULL,
                                        loai_benh_an text GENERATED ALWAYS AS ('MAN'::text) STORED NULL,
	chan_doan_chinh text NULL,
	phan_nhom_csu text NULL,
	chung_da_ve_noi bool NULL,
	may_day_cholinergic bool NULL,
	may_day_do_lanh bool NULL,
	may_day_adrenergic bool NULL,
	may_day_do_nuoc bool NULL,
	may_day_do_anh_sang bool NULL,
	may_day_cam_ung_khac bool NULL,
	may_day_cam_ung_khac_ghi_ro text NULL,
	bieu_hien_qua_trinh_benh text NULL,
	CONSTRAINT ck_benhanmaydayman_bieu_hien CHECK ((bieu_hien_qua_trinh_benh = ANY (ARRAY['Chi san phu'::text, 'Chi phu mach'::text, 'San phu + phu mach'::text]))),
	CONSTRAINT ck_benhanmaydayman_chan_doan CHECK ((chan_doan_chinh = ANY (ARRAY['CSU'::text, 'CIndU'::text, 'CSU + CIndU'::text, 'Phu mach don thuan'::text]))),
	CONSTRAINT ck_benhanmaydayman_csu CHECK ((phan_nhom_csu = ANY (ARRAY['CSU Type I'::text, 'CSU Type IIb'::text, 'Chong lap Type I + Type IIb'::text, 'Khong xac dinh'::text]))),
	CONSTRAINT ck_detail_may_day_cam_ung_khac CHECK ((((may_day_cam_ung_khac IS TRUE) AND (may_day_cam_ung_khac_ghi_ro IS NOT NULL) AND (btrim(may_day_cam_ung_khac_ghi_ro) <> ''::text)) OR ((NOT (may_day_cam_ung_khac IS TRUE)) AND (may_day_cam_ung_khac_ghi_ro IS NULL)))),
	CONSTRAINT pk_benhanmaydayman PRIMARY KEY (ma_ho_so)
);
COMMENT ON TABLE public.benhanmaydayman IS 'Thông tin chuyên biệt của bệnh án mề đay mạn.';


-- public.benhsucacdotbenhtruoc definition

-- Drop table

-- DROP TABLE public.benhsucacdotbenhtruoc;

CREATE TABLE public.benhsucacdotbenhtruoc (
                                              ma_tien_su int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                              ma_ho_so int8 NOT NULL,
                                              tung_co_dot_tuong_tu bool NULL,
                                              tuoi_khoi_phat int2 NULL,
                                              so_dot_truoc int4 NULL,
                                              kieu_dien_bien varchar NULL,
                                              bieu_hien_dot_truoc text NULL,
                                              da_tung_nhap_vien bool NULL,
                                              so_lan_nhap_vien int4 NULL,
                                              thoi_gian_tu_dot_gan_nhat numeric NULL,
                                              dien_bien_cac_dot_truoc text NULL,
                                              CONSTRAINT ck_benh_su_so_dot CHECK ((so_dot_truoc >= 0)),
                                              CONSTRAINT ck_benh_su_so_lan_nhap_vien CHECK ((so_lan_nhap_vien >= 0)),
                                              CONSTRAINT ck_benh_su_thoi_gian CHECK (((thoi_gian_tu_dot_gan_nhat >= (0)::numeric) AND (thoi_gian_tu_dot_gan_nhat < 'Infinity'::numeric))),
                                              CONSTRAINT ck_benh_su_tuoi CHECK ((tuoi_khoi_phat >= 0)),
                                              CONSTRAINT pk_benhsucacdotbenhtruoc PRIMARY KEY (ma_tien_su),
                                              CONSTRAINT uq_benhsucacdotbenhtruoc_hoso UNIQUE (ma_ho_so)
);
COMMENT ON TABLE public.benhsucacdotbenhtruoc IS 'Bản tổng hợp bệnh sử các đợt trước của bệnh án mạn.';


-- public.chidanngungthuoc definition

-- Drop table

-- DROP TABLE public.chidanngungthuoc;

CREATE TABLE public.chidanngungthuoc (
                                         ma_chi_dan_ngung int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                         ma_don_thuoc int8 NOT NULL,
                                         thuoc_can_ngung varchar(255) NOT NULL,
                                         ngung_tu_ngay date NULL,
                                         thoi_gian_ngung_ngay int4 NULL,
                                         muc_dich varchar(100) NULL,
                                         muc_dich_khac_ghi_ro varchar(255) NULL,
                                         CONSTRAINT ck_chidan_muc_dich CHECK (((muc_dich IS NULL) OR ((muc_dich)::text = ANY ((ARRAY['Test huyet thanh tu than'::character varying, 'Test kich thich'::character varying, 'Khac'::character varying])::text[])))),
	CONSTRAINT ck_chidan_thoi_gian CHECK ((thoi_gian_ngung_ngay >= 0)),
	CONSTRAINT ck_ngung_mucdich_detail CHECK ((((NOT ((muc_dich)::text IS DISTINCT FROM 'Khac'::text)) AND (muc_dich_khac_ghi_ro IS NOT NULL) AND (btrim((muc_dich_khac_ghi_ro)::text) <> ''::text)) OR ((NOT (NOT ((muc_dich)::text IS DISTINCT FROM 'Khac'::text))) AND (muc_dich_khac_ghi_ro IS NULL)))),
	CONSTRAINT pk_chidanngungthuoc PRIMARY KEY (ma_chi_dan_ngung)
);
CREATE INDEX idx_chidanngungthuoc_ma_don_thuoc ON public.chidanngungthuoc USING btree (ma_don_thuoc);
COMMENT ON TABLE public.chidanngungthuoc IS 'Chỉ dẫn ngừng thuốc trước tái khám.';


-- public.chitietdonthuoc definition

-- Drop table

-- DROP TABLE public.chitietdonthuoc;

CREATE TABLE public.chitietdonthuoc (
                                        ma_chi_tiet_don_thuoc int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                        ma_don_thuoc int8 NOT NULL,
                                        ma_thuoc varchar(50) NULL,
                                        ten_thuoc_bo_sung varchar(255) NULL,
                                        lieu_dung varchar(100) NOT NULL,
                                        thoi_gian_dung varchar(100) NOT NULL,
                                        ma_ke_hoach_dieu_tri varchar(50) NULL, -- Mã tham chiếu kế hoạch từ hệ thống ngoài, không phải FK nội bộ; NULL nếu không sử dụng.
                                        thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                        CONSTRAINT ck_ctdt_lieu_dung CHECK ((btrim((lieu_dung)::text) <> ''::text)),
                                        CONSTRAINT ck_ctdt_ma_ke_hoach_dieu_tri CHECK ((btrim((ma_ke_hoach_dieu_tri)::text) <> ''::text)),
                                        CONSTRAINT ck_ctdt_mot_thuoc CHECK (((ma_thuoc IS NOT NULL) <> (ten_thuoc_bo_sung IS NOT NULL))),
                                        CONSTRAINT ck_ctdt_ten_thuoc_bo_sung CHECK ((btrim((ten_thuoc_bo_sung)::text) <> ''::text)),
                                        CONSTRAINT ck_ctdt_thoi_gian_dung CHECK ((btrim((thoi_gian_dung)::text) <> ''::text)),
                                        CONSTRAINT pk_chitietdonthuoc PRIMARY KEY (ma_chi_tiet_don_thuoc)
);
CREATE INDEX idx_chitietdonthuoc_ma_don_thuoc ON public.chitietdonthuoc USING btree (ma_don_thuoc);
CREATE INDEX idx_chitietdonthuoc_ma_thuoc ON public.chitietdonthuoc USING btree (ma_thuoc);
COMMENT ON TABLE public.chitietdonthuoc IS 'Chi tiết từng thuốc trong đơn.';

-- Column comments

COMMENT ON COLUMN public.chitietdonthuoc.ma_ke_hoach_dieu_tri IS 'Mã tham chiếu kế hoạch từ hệ thống ngoài, không phải FK nội bộ; NULL nếu không sử dụng.';


-- public.chitietketquanghiemphap definition

-- Drop table

-- DROP TABLE public.chitietketquanghiemphap;

CREATE TABLE public.chitietketquanghiemphap (
                                                ma_chi_tiet_ket_qua_nghiem_phap int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                                ma_phien_nghiem_phap int8 NOT NULL,
                                                loai_nghiem_phap int8 NOT NULL, -- FK tới loainghiemphap.ma_nghiem_phap.
                                                ket_qua varchar(12) NULL,
                                                nguong_gia_tri_so numeric(12, 4) NULL,
                                                nguong_gia_tri_chu text NULL,
                                                don_vi_do varchar(30) NULL, -- Quy ước: diem, phut, degC, kg, nm.
                                                diem_ngua_nrs int2 NULL,
                                                diem_bong_rat_nrs int2 NULL,
                                                diem_dau_nrs int2 NULL,
                                                can_nguyen_khac_ten text NULL,
                                                la_can_nguyen_khac bool DEFAULT false NOT NULL, -- Phải khớp danh mục loại nghiệm pháp; TRUE bắt buộc can_nguyen_khac_ten.
                                                CONSTRAINT ck_chi_tiet_kqnp_bong_nrs CHECK (((diem_bong_rat_nrs >= 0) AND (diem_bong_rat_nrs <= 10))),
                                                CONSTRAINT ck_chi_tiet_kqnp_can_nguyen CHECK ((btrim(can_nguyen_khac_ten) <> ''::text)),
                                                CONSTRAINT ck_chi_tiet_kqnp_dau_nrs CHECK (((diem_dau_nrs >= 0) AND (diem_dau_nrs <= 10))),
                                                CONSTRAINT ck_chi_tiet_kqnp_don_vi CHECK ((((nguong_gia_tri_so IS NULL) AND (nguong_gia_tri_chu IS NULL)) OR (don_vi_do IS NOT NULL))),
                                                CONSTRAINT ck_chi_tiet_kqnp_don_vi_val CHECK ((btrim((don_vi_do)::text) <> ''::text)),
                                                CONSTRAINT ck_chi_tiet_kqnp_ket_qua CHECK (((ket_qua)::text = ANY ((ARRAY['Duong tinh'::character varying, 'Am tinh'::character varying])::text[]))),
	CONSTRAINT ck_chi_tiet_kqnp_ngua_nrs CHECK (((diem_ngua_nrs >= 0) AND (diem_ngua_nrs <= 10))),
	CONSTRAINT ck_chi_tiet_kqnp_nguong CHECK (((nguong_gia_tri_so IS NULL) OR (nguong_gia_tri_chu IS NULL))),
	CONSTRAINT ck_chi_tiet_kqnp_nguong_chu CHECK ((btrim(nguong_gia_tri_chu) <> ''::text)),
	CONSTRAINT ck_chi_tiet_kqnp_nguong_khong_am CHECK ((((don_vi_do)::text <> ALL ((ARRAY['phut'::character varying, 'kg'::character varying, 'nm'::character varying, 'diem'::character varying])::text[])) OR (nguong_gia_tri_so >= (0)::numeric))),
	CONSTRAINT ck_chi_tiet_kqnp_nguong_so CHECK ((nguong_gia_tri_so < 'Infinity'::numeric)),
	CONSTRAINT ck_kqnp_can_nguyen_detail CHECK ((((la_can_nguyen_khac IS TRUE) AND (can_nguyen_khac_ten IS NOT NULL) AND (btrim(can_nguyen_khac_ten) <> ''::text)) OR ((NOT (la_can_nguyen_khac IS TRUE)) AND (can_nguyen_khac_ten IS NULL)))),
	CONSTRAINT pk_chi_tiet_kqnp PRIMARY KEY (ma_chi_tiet_ket_qua_nghiem_phap)
);
CREATE INDEX idx_chi_tiet_kqnp_loai ON public.chitietketquanghiemphap USING btree (loai_nghiem_phap);
CREATE INDEX idx_chi_tiet_kqnp_phien ON public.chitietketquanghiemphap USING btree (ma_phien_nghiem_phap);
COMMENT ON TABLE public.chitietketquanghiemphap IS 'Chi tiết kết quả từng loại nghiệm pháp.';

-- Column comments

COMMENT ON COLUMN public.chitietketquanghiemphap.loai_nghiem_phap IS 'FK tới loainghiemphap.ma_nghiem_phap.';
COMMENT ON COLUMN public.chitietketquanghiemphap.don_vi_do IS 'Quy ước: diem, phut, degC, kg, nm.';
COMMENT ON COLUMN public.chitietketquanghiemphap.la_can_nguyen_khac IS 'Phải khớp danh mục loại nghiệm pháp; TRUE bắt buộc can_nguyen_khac_ten.';


-- public.chitiettonthuongcaptinh definition

-- Drop table

-- DROP TABLE public.chitiettonthuongcaptinh;

CREATE TABLE public.chitiettonthuongcaptinh (
                                                ma_chi_tiet_ton_thuong_cap_tinh int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                                ma_phien_kham int8 NOT NULL,
                                                dang_ton_thuong varchar NULL,
                                                dang_ton_thuong_khac text NULL,
                                                kich_thuoc_max_mm numeric NULL, -- Đơn vị: mm.
                                                kich_thuoc_min_mm numeric NULL, -- Đơn vị: mm.
                                                kich_thuoc_dong_deu_mm numeric NULL, -- Đơn vị: mm.
                                                loai_kich_thuoc varchar NULL,
                                                thoi_gian_ton_tai_tb numeric NULL,
                                                dien_bien text NULL,
                                                so_luong_san_phu int4 NULL,
                                                phu_mach_ben bool NULL,
                                                vi_tri text NULL,
                                                vi_tri_dac_biet_ghi_ro text NULL,
                                                CONSTRAINT ck_dang_tonthuong_khac CHECK ((((NOT ((dang_ton_thuong)::text IS DISTINCT FROM 'Khac'::text)) AND (dang_ton_thuong_khac IS NOT NULL) AND (btrim(dang_ton_thuong_khac) <> ''::text)) OR ((NOT (NOT ((dang_ton_thuong)::text IS DISTINCT FROM 'Khac'::text))) AND (dang_ton_thuong_khac IS NULL)))),
	CONSTRAINT ck_ton_thuong_cap_kich_thuoc_dong_deu CHECK (((kich_thuoc_dong_deu_mm >= (0)::numeric) AND (kich_thuoc_dong_deu_mm < 'Infinity'::numeric))),
	CONSTRAINT ck_ton_thuong_cap_kich_thuoc_max CHECK (((kich_thuoc_max_mm >= (0)::numeric) AND (kich_thuoc_max_mm < 'Infinity'::numeric))),
	CONSTRAINT ck_ton_thuong_cap_kich_thuoc_min CHECK (((kich_thuoc_min_mm >= (0)::numeric) AND (kich_thuoc_min_mm < 'Infinity'::numeric))),
	CONSTRAINT ck_ton_thuong_cap_min_max CHECK ((kich_thuoc_max_mm >= kich_thuoc_min_mm)),
	CONSTRAINT ck_ton_thuong_cap_so_luong CHECK ((so_luong_san_phu >= 0)),
	CONSTRAINT ck_ton_thuong_cap_thoi_gian CHECK (((thoi_gian_ton_tai_tb >= (0)::numeric) AND (thoi_gian_ton_tai_tb < 'Infinity'::numeric))),
	CONSTRAINT pk_chitiettonthuongcaptinh PRIMARY KEY (ma_chi_tiet_ton_thuong_cap_tinh)
);
CREATE INDEX idx_ton_thuong_cap_phien ON public.chitiettonthuongcaptinh USING btree (ma_phien_kham);
COMMENT ON TABLE public.chitiettonthuongcaptinh IS 'Chi tiết tổn thương ghi nhận trong phiên khám cấp.';

-- Column comments

COMMENT ON COLUMN public.chitiettonthuongcaptinh.kich_thuoc_max_mm IS 'Đơn vị: mm.';
COMMENT ON COLUMN public.chitiettonthuongcaptinh.kich_thuoc_min_mm IS 'Đơn vị: mm.';
COMMENT ON COLUMN public.chitiettonthuongcaptinh.kich_thuoc_dong_deu_mm IS 'Đơn vị: mm.';


-- public.chitiettonthuongsanphu definition

-- Drop table

-- DROP TABLE public.chitiettonthuongsanphu;

CREATE TABLE public.chitiettonthuongsanphu (
                                               ma_chi_tiet_ton_thuong int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                               ma_phien_kham int8 NOT NULL,
                                               dang_ton_thuong varchar NULL,
                                               kich_thuoc numeric NULL,
                                               thoi_gian_ton_tai numeric NULL,
                                               thoi_diem timestamptz NULL,
                                               tinh_trang_dung_thuoc varchar NULL,
                                               ma_dac_diem_san_phu int8 NULL,
                                               so_luong int4 NULL,
                                               vi_tri text NULL,
                                               CONSTRAINT ck_san_phu_kich_thuoc CHECK (((kich_thuoc >= (0)::numeric) AND (kich_thuoc < 'Infinity'::numeric))),
                                               CONSTRAINT ck_san_phu_so_luong CHECK ((so_luong >= 0)),
                                               CONSTRAINT ck_san_phu_thoi_gian CHECK (((thoi_gian_ton_tai >= (0)::numeric) AND (thoi_gian_ton_tai < 'Infinity'::numeric))),
                                               CONSTRAINT pk_chitiettonthuongsanphu PRIMARY KEY (ma_chi_tiet_ton_thuong)
);
CREATE INDEX idx_chitiettonthuongsanphu_ma_dac_diem_san_phu ON public.chitiettonthuongsanphu USING btree (ma_dac_diem_san_phu);
CREATE INDEX idx_san_phu_phien ON public.chitiettonthuongsanphu USING btree (ma_phien_kham);
COMMENT ON TABLE public.chitiettonthuongsanphu IS 'Chi tiết tổn thương sẩn phù trong phiên khám mạn.';


-- public.danhgiamucdohoatdongbenh definition

-- Drop table

-- DROP TABLE public.danhgiamucdohoatdongbenh;

CREATE TABLE public.danhgiamucdohoatdongbenh (
                                                 ma_danh_gia_hoat_dong int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                                 ma_phien_kham_man int8 NOT NULL,
                                                 du_du_lieu_7_ngay bool DEFAULT true NULL,
                                                 ma_nguoi_danh_gia int8 NULL, -- Người thực hiện đánh giá, tham chiếu nguoi; có thể là nhân viên hoặc bệnh nhân.
                                                 thoi_diem_danh_gia timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                                 hss7_san_phu_khong_dung_thuoc int2 NULL,
                                                 hss7_san_phu_dung_thuoc_deu int2 NULL,
                                                 hss7_san_phu_dung_thuoc_khong_deu int2 NULL,
                                                 iss7_ngua_khong_dung_thuoc int2 NULL,
                                                 iss7_ngua_dung_thuoc_deu int2 NULL,
                                                 iss7_ngua_dung_thuoc_khong_deu int2 NULL,
                                                 uas7_tong_khong_dung_thuoc int2 NULL,
                                                 uas7_tong_dung_thuoc_deu int2 NULL,
                                                 uas7_tong_dung_thuoc_khong_deu int2 NULL,
                                                 ghi_chu text NULL,
                                                 CONSTRAINT ck_danhgia_hss7_1 CHECK (((hss7_san_phu_khong_dung_thuoc >= 0) AND (hss7_san_phu_khong_dung_thuoc <= 21))),
                                                 CONSTRAINT ck_danhgia_hss7_2 CHECK (((hss7_san_phu_dung_thuoc_deu >= 0) AND (hss7_san_phu_dung_thuoc_deu <= 21))),
                                                 CONSTRAINT ck_danhgia_hss7_3 CHECK (((hss7_san_phu_dung_thuoc_khong_deu >= 0) AND (hss7_san_phu_dung_thuoc_khong_deu <= 21))),
                                                 CONSTRAINT ck_danhgia_iss7_1 CHECK (((iss7_ngua_khong_dung_thuoc >= 0) AND (iss7_ngua_khong_dung_thuoc <= 21))),
                                                 CONSTRAINT ck_danhgia_iss7_2 CHECK (((iss7_ngua_dung_thuoc_deu >= 0) AND (iss7_ngua_dung_thuoc_deu <= 21))),
                                                 CONSTRAINT ck_danhgia_iss7_3 CHECK (((iss7_ngua_dung_thuoc_khong_deu >= 0) AND (iss7_ngua_dung_thuoc_khong_deu <= 21))),
                                                 CONSTRAINT ck_danhgia_uas7_1 CHECK (((uas7_tong_khong_dung_thuoc >= 0) AND (uas7_tong_khong_dung_thuoc <= 42))),
                                                 CONSTRAINT ck_danhgia_uas7_2 CHECK (((uas7_tong_dung_thuoc_deu >= 0) AND (uas7_tong_dung_thuoc_deu <= 42))),
                                                 CONSTRAINT ck_danhgia_uas7_3 CHECK (((uas7_tong_dung_thuoc_khong_deu >= 0) AND (uas7_tong_dung_thuoc_khong_deu <= 42))),
                                                 CONSTRAINT pk_danhgiamucdohoatdongbenh PRIMARY KEY (ma_danh_gia_hoat_dong)
);
CREATE INDEX idx_danhgia_phienkhamman ON public.danhgiamucdohoatdongbenh USING btree (ma_phien_kham_man);
CREATE INDEX idx_danhgiamucdohoatdongbenh_ma_nguoi_danh_gia ON public.danhgiamucdohoatdongbenh USING btree (ma_nguoi_danh_gia);
COMMENT ON TABLE public.danhgiamucdohoatdongbenh IS 'Đánh giá mức độ hoạt động bệnh theo phiên khám mạn.';

-- Column comments

COMMENT ON COLUMN public.danhgiamucdohoatdongbenh.ma_nguoi_danh_gia IS 'Người thực hiện đánh giá, tham chiếu nguoi; có thể là nhân viên hoặc bệnh nhân.';


-- public.dauhieusinhton definition

-- Drop table

-- DROP TABLE public.dauhieusinhton;

CREATE TABLE public.dauhieusinhton (
                                       ma_dau_hieu_sinh_ton int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                       ma_phien_kham int8 NOT NULL,
                                       ma_bac_si int8 NULL,
                                       thoi_diem timestamptz NULL,
                                       tri_giac varchar NULL,
                                       nhiet_do numeric NULL, -- Đơn vị: độ C.
                                       spo2 numeric NULL, -- Độ bão hòa oxy ngoại vi, đơn vị: %.
                                       mach numeric NULL, -- Đơn vị: lần/phút.
                                       huyet_ap_tam_thu numeric NULL, -- Đơn vị: mmHg.
                                       huyet_ap_tam_truong numeric NULL, -- Đơn vị: mmHg.
                                       nhip_tho numeric NULL, -- Đơn vị: lần/phút.
                                       bat_thuong_co_quan_khac bool NULL,
                                       bat_thuong_co_quan_khac_ghi_ro text NULL,
                                       thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       CONSTRAINT ck_detail_bat_thuong_co_quan_khac CHECK ((((bat_thuong_co_quan_khac IS TRUE) AND (bat_thuong_co_quan_khac_ghi_ro IS NOT NULL) AND (btrim(bat_thuong_co_quan_khac_ghi_ro) <> ''::text)) OR ((NOT (bat_thuong_co_quan_khac IS TRUE)) AND (bat_thuong_co_quan_khac_ghi_ro IS NULL)))),
                                       CONSTRAINT ck_dhst_bat_thuong_ghi_ro CHECK ((btrim(bat_thuong_co_quan_khac_ghi_ro) <> ''::text)),
                                       CONSTRAINT ck_dhst_huyet_ap_tam_thu CHECK (((huyet_ap_tam_thu >= (0)::numeric) AND (huyet_ap_tam_thu < 'Infinity'::numeric))),
                                       CONSTRAINT ck_dhst_huyet_ap_tam_truong CHECK (((huyet_ap_tam_truong >= (0)::numeric) AND (huyet_ap_tam_truong < 'Infinity'::numeric))),
                                       CONSTRAINT ck_dhst_mach CHECK (((mach >= (0)::numeric) AND (mach < 'Infinity'::numeric))),
                                       CONSTRAINT ck_dhst_nhiet_do CHECK (((nhiet_do > '-Infinity'::numeric) AND (nhiet_do < 'Infinity'::numeric))),
                                       CONSTRAINT ck_dhst_nhip_tho CHECK (((nhip_tho >= (0)::numeric) AND (nhip_tho < 'Infinity'::numeric))),
                                       CONSTRAINT ck_dhst_spo2 CHECK (((spo2 >= (0)::numeric) AND (spo2 <= (100)::numeric))),
                                       CONSTRAINT pk_dauhieusinhton PRIMARY KEY (ma_dau_hieu_sinh_ton)
);
CREATE INDEX idx_dhst_bacsi ON public.dauhieusinhton USING btree (ma_bac_si);
CREATE INDEX idx_dhst_phien_thoi_diem ON public.dauhieusinhton USING btree (ma_phien_kham, thoi_diem);
COMMENT ON TABLE public.dauhieusinhton IS 'Các lần đo dấu hiệu sinh tồn trong phiên khám.';

-- Column comments

COMMENT ON COLUMN public.dauhieusinhton.nhiet_do IS 'Đơn vị: độ C.';
COMMENT ON COLUMN public.dauhieusinhton.spo2 IS 'Độ bão hòa oxy ngoại vi, đơn vị: %.';
COMMENT ON COLUMN public.dauhieusinhton.mach IS 'Đơn vị: lần/phút.';
COMMENT ON COLUMN public.dauhieusinhton.huyet_ap_tam_thu IS 'Đơn vị: mmHg.';
COMMENT ON COLUMN public.dauhieusinhton.huyet_ap_tam_truong IS 'Đơn vị: mmHg.';
COMMENT ON COLUMN public.dauhieusinhton.nhip_tho IS 'Đơn vị: lần/phút.';

-- Table Triggers

create trigger trg_dauhieusinhton_cap_nhat before
    update
    on
        public.dauhieusinhton for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.donthuoc definition

-- Drop table

-- DROP TABLE public.donthuoc;

CREATE TABLE public.donthuoc (
                                 ma_don_thuoc int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                 ma_ho_so int8 NOT NULL,
                                 ngay_ke_don date DEFAULT CURRENT_DATE NOT NULL,
                                 trang_thai_don varchar(50) DEFAULT 'DANG_DIEU_TRI'::character varying NULL,
                                 ghi_chu_dieu_tri text NULL,
                                 ngung_thuoc_lan_sau bool DEFAULT false NULL,
                                 thuoc_can_ngung_truoc_tai_kham text NULL,
                                 thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 ma_phien_kham int8 NOT NULL,
                                 CONSTRAINT ck_donthuoc_ngung_detail CHECK ((((ngung_thuoc_lan_sau IS TRUE) AND (thuoc_can_ngung_truoc_tai_kham IS NOT NULL) AND (btrim(thuoc_can_ngung_truoc_tai_kham) <> ''::text)) OR ((NOT (ngung_thuoc_lan_sau IS TRUE)) AND (thuoc_can_ngung_truoc_tai_kham IS NULL)))),
                                 CONSTRAINT ck_donthuoc_trang_thai CHECK (((trang_thai_don)::text = ANY ((ARRAY['DANG_DIEU_TRI'::character varying, 'HOAN_THANH'::character varying, 'HUY'::character varying])::text[]))),
	CONSTRAINT pk_donthuoc PRIMARY KEY (ma_don_thuoc)
);
CREATE INDEX idx_donthuoc_ma_ho_so ON public.donthuoc USING btree (ma_ho_so);
CREATE INDEX idx_donthuoc_ma_phien_kham ON public.donthuoc USING btree (ma_phien_kham);
COMMENT ON TABLE public.donthuoc IS 'Đơn thuốc theo hồ sơ bệnh án.';

-- Table Triggers

create trigger trg_donthuoc_cap_nhat before
    update
    on
        public.donthuoc for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.dottonthuongda definition

-- Drop table

-- DROP TABLE public.dottonthuongda;

CREATE TABLE public.dottonthuongda (
                                       ma_dot_ton_thuong_da int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                       ma_benh_nhan int8 NOT NULL,
                                       ma_ho_so_man int8 NULL,
                                       ten_dot_ton_thuong_da varchar(255) NULL,
                                       thoi_diem_xuat_hien timestamptz NULL,
                                       hoan_canh_xuat_hien varchar(255) NULL,
                                       hoan_canh_xuat_hien_khac text NULL,
                                       ten_thuoc_dung_gan_thoi_diem_xuat_hien varchar(255) NULL,
                                       thoi_gian_dung_thuoc_truoc_khi_xuat_hien varchar(100) NULL,
                                       don_vi_thoi_gian varchar(50) NULL,
                                       chi_tiet_dot_ton_thuong text NULL,
                                       thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       CONSTRAINT ck_dottonthuongda_hoancanh CHECK ((((NOT ((hoan_canh_xuat_hien)::text IS DISTINCT FROM 'Khac'::text)) AND (hoan_canh_xuat_hien_khac IS NOT NULL) AND (btrim(hoan_canh_xuat_hien_khac) <> ''::text)) OR ((NOT (NOT ((hoan_canh_xuat_hien)::text IS DISTINCT FROM 'Khac'::text))) AND (hoan_canh_xuat_hien_khac IS NULL)))),
	CONSTRAINT pk_dottonthuongda PRIMARY KEY (ma_dot_ton_thuong_da)
);
CREATE INDEX idx_dottonthuong_benhnhan ON public.dottonthuongda USING btree (ma_benh_nhan);
CREATE INDEX idx_dottonthuong_hoso_man ON public.dottonthuongda USING btree (ma_ho_so_man);
COMMENT ON TABLE public.dottonthuongda IS 'Đợt tổn thương da của bệnh nhân.';

-- Table Triggers

create trigger trg_dottonthuongda_cap_nhat before
    update
    on
        public.dottonthuongda for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.hoancanhkhoiphatdiung definition

-- Drop table

-- DROP TABLE public.hoancanhkhoiphatdiung;

CREATE TABLE public.hoancanhkhoiphatdiung (
                                              ma_hoan_canh_khoi_phat int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                              ma_ho_so int8 NOT NULL,
                                              thuc_an_nghi_ngo text NULL,
                                              thuc_an_thoi_gian numeric NULL,
                                              thuc_an_tung_bi bool NULL,
                                              thuc_an_loai_tien_su varchar NULL,
                                              thuc_an_tien_su_ghi_ro text NULL,
                                              thuoc_nghi_ngo text NULL,
                                              thuoc_thoi_gian numeric NULL,
                                              thuoc_tung_bi bool NULL,
                                              thuoc_loai_tien_su varchar NULL,
                                              thuoc_tien_su_ghi_ro text NULL,
                                              con_trung_nghi_ngo text NULL,
                                              con_trung_thoi_gian numeric NULL,
                                              con_trung_tung_bi bool NULL,
                                              con_trung_loai_tien_su varchar NULL,
                                              con_trung_tien_su_ghi_ro text NULL,
                                              khac_nghi_ngo text NULL,
                                              khac_thoi_gian numeric NULL,
                                              khac_tung_bi bool NULL,
                                              khac_loai_tien_su varchar NULL,
                                              khac_tien_su_ghi_ro text NULL,
                                              CONSTRAINT ck_hoan_canh_con_trung_thoi_gian CHECK (((con_trung_thoi_gian >= (0)::numeric) AND (con_trung_thoi_gian < 'Infinity'::numeric))),
                                              CONSTRAINT ck_hoan_canh_khac_thoi_gian CHECK (((khac_thoi_gian >= (0)::numeric) AND (khac_thoi_gian < 'Infinity'::numeric))),
                                              CONSTRAINT ck_hoan_canh_thuc_an_thoi_gian CHECK (((thuc_an_thoi_gian >= (0)::numeric) AND (thuc_an_thoi_gian < 'Infinity'::numeric))),
                                              CONSTRAINT ck_hoan_canh_thuoc_thoi_gian CHECK (((thuoc_thoi_gian >= (0)::numeric) AND (thuoc_thoi_gian < 'Infinity'::numeric))),
                                              CONSTRAINT ck_hoancanh_con_trung_tiensu CHECK ((((con_trung_tung_bi IS TRUE) AND (con_trung_tien_su_ghi_ro IS NOT NULL) AND (btrim(con_trung_tien_su_ghi_ro) <> ''::text)) OR ((NOT (con_trung_tung_bi IS TRUE)) AND (con_trung_tien_su_ghi_ro IS NULL)))),
                                              CONSTRAINT ck_hoancanh_khac_tiensu CHECK ((((khac_tung_bi IS TRUE) AND (khac_tien_su_ghi_ro IS NOT NULL) AND (btrim(khac_tien_su_ghi_ro) <> ''::text)) OR ((NOT (khac_tung_bi IS TRUE)) AND (khac_tien_su_ghi_ro IS NULL)))),
                                              CONSTRAINT ck_hoancanh_thuc_an_tiensu CHECK ((((thuc_an_tung_bi IS TRUE) AND (thuc_an_tien_su_ghi_ro IS NOT NULL) AND (btrim(thuc_an_tien_su_ghi_ro) <> ''::text)) OR ((NOT (thuc_an_tung_bi IS TRUE)) AND (thuc_an_tien_su_ghi_ro IS NULL)))),
                                              CONSTRAINT ck_hoancanh_thuoc_tiensu CHECK ((((thuoc_tung_bi IS TRUE) AND (thuoc_tien_su_ghi_ro IS NOT NULL) AND (btrim(thuoc_tien_su_ghi_ro) <> ''::text)) OR ((NOT (thuoc_tung_bi IS TRUE)) AND (thuoc_tien_su_ghi_ro IS NULL)))),
                                              CONSTRAINT pk_hoancanhkhoiphatdiung PRIMARY KEY (ma_hoan_canh_khoi_phat),
                                              CONSTRAINT uq_hoancanhkhoiphatdiung_hoso UNIQUE (ma_ho_so)
);
COMMENT ON TABLE public.hoancanhkhoiphatdiung IS 'Hoàn cảnh khởi phát dị ứng ghi nhận theo hồ sơ.';


-- public.hosobenhan definition

-- Drop table

-- DROP TABLE public.hosobenhan;

CREATE TABLE public.hosobenhan (
                                   ma_ho_so int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                   ma_benh_nhan int8 NOT NULL,
                                   loai_benh_an text NOT NULL,
                                   ma_subtype_cap int8 GENERATED ALWAYS AS (
                                       CASE
                                           WHEN loai_benh_an = 'CAP'::text THEN ma_ho_so
                                           ELSE NULL::bigint
                                           END) STORED NULL,
                                   ma_subtype_man int8 GENERATED ALWAYS AS (
                                       CASE
                                           WHEN loai_benh_an = 'MAN'::text THEN ma_ho_so
                                           ELSE NULL::bigint
                                           END) STORED NULL,
                                   thoi_diem_kham_lan_dau timestamptz NULL,
                                   ngay_bat_dau_dieu_tri_ngoai_tru date NULL,
                                   ngay_ket_thuc_dieu_tri_ngoai_tru date NULL,
                                   chan_doan_tuyen_truoc text NULL,
                                   chan_doan_ban_dau_phong_kham text NULL,
                                   ket_qua_dieu_tri_bao_hiem text NULL,
                                   thoi_gian_khoi_phat_tuan numeric(12, 2) NULL,
                                   trang_thai text DEFAULT 'draft'::text NOT NULL,
                                   tien_su_gia_dinh text NULL,
                                   tien_su_gia_dinh_ghi_ro text NULL,
                                   da_dieu_tri_truoc_kham bool NULL,
                                   CONSTRAINT ck_hoso_giadinh_detail CHECK ((((NOT (tien_su_gia_dinh IS DISTINCT FROM 'Co'::text)) AND (tien_su_gia_dinh_ghi_ro IS NOT NULL) AND (btrim(tien_su_gia_dinh_ghi_ro) <> ''::text)) OR ((NOT (NOT (tien_su_gia_dinh IS DISTINCT FROM 'Co'::text))) AND (tien_su_gia_dinh_ghi_ro IS NULL)))),
                                   CONSTRAINT ck_hosobenhan_ket_qua CHECK ((ket_qua_dieu_tri_bao_hiem = ANY (ARRAY['Khoi benh'::text, 'Do'::text, 'Khong do'::text, 'Nang hon'::text, 'Chet'::text, 'Bien chung'::text]))),
                                   CONSTRAINT ck_hosobenhan_loai CHECK ((loai_benh_an = ANY (ARRAY['CAP'::text, 'MAN'::text]))),
                                   CONSTRAINT ck_hosobenhan_ngay_dieu_tri CHECK ((ngay_ket_thuc_dieu_tri_ngoai_tru >= ngay_bat_dau_dieu_tri_ngoai_tru)),
                                   CONSTRAINT ck_hosobenhan_thoi_gian CHECK (((thoi_gian_khoi_phat_tuan >= (0)::numeric) AND (thoi_gian_khoi_phat_tuan < 'Infinity'::numeric))),
                                   CONSTRAINT ck_hosobenhan_tien_su_gia_dinh CHECK ((tien_su_gia_dinh = ANY (ARRAY['Co'::text, 'Khong'::text, 'Khong ro'::text]))),
                                   CONSTRAINT ck_hosobenhan_trang_thai CHECK ((trang_thai = ANY (ARRAY['draft'::text, 'pending'::text, 'in_progress'::text, 'completed'::text, 'cancelled'::text]))),
                                   CONSTRAINT pk_hosobenhan PRIMARY KEY (ma_ho_so),
                                   CONSTRAINT uq_hoso_benhnhan UNIQUE (ma_ho_so, ma_benh_nhan),
                                   CONSTRAINT uq_hosobenhan_ma_loai UNIQUE (ma_ho_so, loai_benh_an)
);
CREATE INDEX idx_hosobenhan_benhnhan ON public.hosobenhan USING btree (ma_benh_nhan);
CREATE INDEX idx_hosobenhan_subtype_cap ON public.hosobenhan USING btree (ma_subtype_cap) WHERE (ma_subtype_cap IS NOT NULL);
CREATE INDEX idx_hosobenhan_subtype_man ON public.hosobenhan USING btree (ma_subtype_man) WHERE (ma_subtype_man IS NOT NULL);
COMMENT ON TABLE public.hosobenhan IS 'Hồ sơ bệnh án của bệnh nhân; phân loại cấp hoặc mạn.';


-- public.ketquacanlamsang definition

-- Drop table

-- DROP TABLE public.ketquacanlamsang;

CREATE TABLE public.ketquacanlamsang (
                                         ma_ket_qua_can_lam_sang int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                         ma_phien_kham int8 NOT NULL,
                                         loai_ket_qua_can_lam_sang varchar(3) NOT NULL,
                                         wbc numeric(12, 4) NULL, -- Đơn vị: G/L
                                         crp numeric(12, 4) NULL, -- Đơn vị: mg/L
                                         ige_toan_phan numeric(12, 4) NULL, -- Đơn vị: IU/mL
                                         can_lam_sang_khac_ghi_chu text NULL,
                                         xet_nghiem_khac text NULL,
                                         thoi_diem_thuc_hien timestamptz NULL,
                                         thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                         ma_subtype_cap int8 GENERATED ALWAYS AS (
                                             CASE
                                                 WHEN loai_ket_qua_can_lam_sang::text = 'CAP'::text THEN ma_ket_qua_can_lam_sang
                                                 ELSE NULL::bigint
                                                 END) STORED NULL,
                                         ma_subtype_man int8 GENERATED ALWAYS AS (
                                             CASE
                                                 WHEN loai_ket_qua_can_lam_sang::text = 'MAN'::text THEN ma_ket_qua_can_lam_sang
                                                 ELSE NULL::bigint
                                                 END) STORED NULL,
                                         CONSTRAINT ck_kqcls_crp CHECK (((crp >= (0)::numeric) AND (crp < 'Infinity'::numeric))),
                                         CONSTRAINT ck_kqcls_ige CHECK (((ige_toan_phan >= (0)::numeric) AND (ige_toan_phan < 'Infinity'::numeric))),
                                         CONSTRAINT ck_kqcls_loai CHECK (((loai_ket_qua_can_lam_sang)::text = ANY ((ARRAY['CAP'::character varying, 'MAN'::character varying])::text[]))),
	CONSTRAINT ck_kqcls_wbc CHECK (((wbc >= (0)::numeric) AND (wbc < 'Infinity'::numeric))),
	CONSTRAINT pk_kqcls PRIMARY KEY (ma_ket_qua_can_lam_sang),
	CONSTRAINT uq_kqcls_ma_loai UNIQUE (ma_ket_qua_can_lam_sang, loai_ket_qua_can_lam_sang)
);
CREATE INDEX idx_ketquacanlamsang_subtype_cap ON public.ketquacanlamsang USING btree (ma_subtype_cap) WHERE (ma_subtype_cap IS NOT NULL);
CREATE INDEX idx_ketquacanlamsang_subtype_man ON public.ketquacanlamsang USING btree (ma_subtype_man) WHERE (ma_subtype_man IS NOT NULL);
CREATE INDEX idx_kqcls_phien_kham ON public.ketquacanlamsang USING btree (ma_phien_kham);
COMMENT ON TABLE public.ketquacanlamsang IS 'Supertype kết quả cận lâm sàng.';

-- Column comments

COMMENT ON COLUMN public.ketquacanlamsang.wbc IS 'Đơn vị: G/L';
COMMENT ON COLUMN public.ketquacanlamsang.crp IS 'Đơn vị: mg/L';
COMMENT ON COLUMN public.ketquacanlamsang.ige_toan_phan IS 'Đơn vị: IU/mL';

-- Table Triggers

create trigger trg_kqcls_cap_nhat before
    update
    on
        public.ketquacanlamsang for each row execute function fn_cap_nhat_thoi_gian();


-- public.ketquacanlamsangcap definition

-- Drop table

-- DROP TABLE public.ketquacanlamsangcap;

CREATE TABLE public.ketquacanlamsangcap (
                                            ma_ket_qua_can_lam_sang int8 NOT NULL,
                                            loai_ket_qua_can_lam_sang varchar(3) GENERATED ALWAYS AS ('CAP'::character varying(3)) STORED NULL,
	lym numeric(12, 4) NULL, -- Đơn vị: G/L
	neu numeric(12, 4) NULL, -- Đơn vị: G/L
	plt numeric(12, 4) NULL, -- Đơn vị: G/L
	tryptase_cap numeric(12, 4) NULL, -- Đơn vị: ng/mL
	tryptase_nen numeric(12, 4) NULL, -- Đơn vị: ng/mL
	nlr numeric GENERATED ALWAYS AS (round(neu / NULLIF(lym, 0::numeric), 6)) STORED NULL, -- Tỷ số NEU/LYM, tự tính.
	plr numeric GENERATED ALWAYS AS (round(plt / NULLIF(lym, 0::numeric), 6)) STORED NULL, -- Tỷ số PLT/LYM, tự tính.
	CONSTRAINT ck_kqcls_cap_lym CHECK (((lym >= (0)::numeric) AND (lym < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_cap_neu CHECK (((neu >= (0)::numeric) AND (neu < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_cap_plt CHECK (((plt >= (0)::numeric) AND (plt < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_cap_tryptase_cap CHECK (((tryptase_cap >= (0)::numeric) AND (tryptase_cap < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_cap_tryptase_nen CHECK (((tryptase_nen >= (0)::numeric) AND (tryptase_nen < 'Infinity'::numeric))),
	CONSTRAINT pk_kqcls_cap PRIMARY KEY (ma_ket_qua_can_lam_sang)
);
COMMENT ON TABLE public.ketquacanlamsangcap IS 'Subtype cấp; NLR và PLR tự tính từ NEU, PLT, LYM.';

-- Column comments

COMMENT ON COLUMN public.ketquacanlamsangcap.lym IS 'Đơn vị: G/L';
COMMENT ON COLUMN public.ketquacanlamsangcap.neu IS 'Đơn vị: G/L';
COMMENT ON COLUMN public.ketquacanlamsangcap.plt IS 'Đơn vị: G/L';
COMMENT ON COLUMN public.ketquacanlamsangcap.tryptase_cap IS 'Đơn vị: ng/mL';
COMMENT ON COLUMN public.ketquacanlamsangcap.tryptase_nen IS 'Đơn vị: ng/mL';
COMMENT ON COLUMN public.ketquacanlamsangcap.nlr IS 'Tỷ số NEU/LYM, tự tính.';
COMMENT ON COLUMN public.ketquacanlamsangcap.plr IS 'Tỷ số PLT/LYM, tự tính.';

-- Table Triggers

create trigger trg_kqcls_cap_cap_nhat_cha after
    insert
    or
update
    on
    public.ketquacanlamsangcap for each row execute function fn_cap_nhat_cha_kqcls();


-- public.ketquacanlamsangman definition

-- Drop table

-- DROP TABLE public.ketquacanlamsangman;

CREATE TABLE public.ketquacanlamsangman (
                                            ma_ket_qua_can_lam_sang int8 NOT NULL,
                                            loai_ket_qua_can_lam_sang varchar(3) GENERATED ALWAYS AS ('MAN'::character varying(3)) STORED NULL,
	test_huyet_thanh_tu_than varchar(12) NULL,
	ige_khang_tpo_ket_qua varchar(12) NULL,
	bat_ket_qua varchar(12) NULL,
	ige_khang_il24_ket_qua varchar(12) NULL,
	asst_duong_kinh_mm numeric(12, 4) NULL, -- Đơn vị: mm
	eo numeric(12, 4) NULL, -- Đơn vị: G/L
	ba_bach_cau numeric(12, 4) NULL, -- Đơn vị: G/L
	anti_tpo numeric(12, 4) NULL, -- Đơn vị: IU/mL
	tsh numeric(12, 4) NULL, -- Đơn vị: micro-IU/mL
	ft4 numeric(12, 4) NULL, -- Đơn vị: pmol/L
	ft3 numeric(12, 4) NULL, -- Đơn vị: pmol/L
	mau_lang_2h numeric(12, 4) NULL, -- Đơn vị: mm
	mau_lang_1h numeric(12, 4) NULL, -- Đơn vị: mm
	ana_hep2 varchar(12) NULL,
	sieu_am_tuyen_giap text NULL,
	CONSTRAINT ck_kqcls_man_ana CHECK (((ana_hep2)::text = ANY ((ARRAY['Duong tinh'::character varying, 'Am tinh'::character varying])::text[]))),
	CONSTRAINT ck_kqcls_man_anti_tpo CHECK (((anti_tpo >= (0)::numeric) AND (anti_tpo < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_man_asst CHECK (((asst_duong_kinh_mm >= (0)::numeric) AND (asst_duong_kinh_mm < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_man_asst_logic CHECK (((asst_duong_kinh_mm IS NULL) OR (NOT ((test_huyet_thanh_tu_than)::text IS DISTINCT FROM 'Duong tinh'::text)))),
	CONSTRAINT ck_kqcls_man_ba CHECK (((ba_bach_cau >= (0)::numeric) AND (ba_bach_cau < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_man_bat CHECK (((bat_ket_qua)::text = ANY ((ARRAY['Duong tinh'::character varying, 'Am tinh'::character varying, 'Chua lam'::character varying])::text[]))),
	CONSTRAINT ck_kqcls_man_eo CHECK (((eo >= (0)::numeric) AND (eo < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_man_ft3 CHECK (((ft3 >= (0)::numeric) AND (ft3 < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_man_ft4 CHECK (((ft4 >= (0)::numeric) AND (ft4 < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_man_ige_il24 CHECK (((ige_khang_il24_ket_qua)::text = ANY ((ARRAY['Duong tinh'::character varying, 'Am tinh'::character varying, 'Chua lam'::character varying])::text[]))),
	CONSTRAINT ck_kqcls_man_ige_tpo CHECK (((ige_khang_tpo_ket_qua)::text = ANY ((ARRAY['Duong tinh'::character varying, 'Am tinh'::character varying, 'Chua lam'::character varying])::text[]))),
	CONSTRAINT ck_kqcls_man_ml_1h CHECK (((mau_lang_1h >= (0)::numeric) AND (mau_lang_1h < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_man_ml_2h CHECK (((mau_lang_2h >= (0)::numeric) AND (mau_lang_2h < 'Infinity'::numeric))),
	CONSTRAINT ck_kqcls_man_test_ht CHECK (((test_huyet_thanh_tu_than)::text = ANY ((ARRAY['Duong tinh'::character varying, 'Am tinh'::character varying, 'Chua lam'::character varying])::text[]))),
	CONSTRAINT ck_kqcls_man_tsh CHECK (((tsh >= (0)::numeric) AND (tsh < 'Infinity'::numeric))),
	CONSTRAINT pk_kqcls_man PRIMARY KEY (ma_ket_qua_can_lam_sang)
);
COMMENT ON TABLE public.ketquacanlamsangman IS 'Subtype mạn; IgE kháng TPO định tính khác anti_tpo định lượng.';

-- Column comments

COMMENT ON COLUMN public.ketquacanlamsangman.asst_duong_kinh_mm IS 'Đơn vị: mm';
COMMENT ON COLUMN public.ketquacanlamsangman.eo IS 'Đơn vị: G/L';
COMMENT ON COLUMN public.ketquacanlamsangman.ba_bach_cau IS 'Đơn vị: G/L';
COMMENT ON COLUMN public.ketquacanlamsangman.anti_tpo IS 'Đơn vị: IU/mL';
COMMENT ON COLUMN public.ketquacanlamsangman.tsh IS 'Đơn vị: micro-IU/mL';
COMMENT ON COLUMN public.ketquacanlamsangman.ft4 IS 'Đơn vị: pmol/L';
COMMENT ON COLUMN public.ketquacanlamsangman.ft3 IS 'Đơn vị: pmol/L';
COMMENT ON COLUMN public.ketquacanlamsangman.mau_lang_2h IS 'Đơn vị: mm';
COMMENT ON COLUMN public.ketquacanlamsangman.mau_lang_1h IS 'Đơn vị: mm';

-- Table Triggers

create trigger trg_kqcls_man_cap_nhat_cha after
    insert
    or
update
    on
    public.ketquacanlamsangman for each row execute function fn_cap_nhat_cha_kqcls();


-- public.ketquaproms definition

-- Drop table

-- DROP TABLE public.ketquaproms;

CREATE TABLE public.ketquaproms (
                                    ma_ket_qua_proms int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                    ma_phien_kham_man int8 NOT NULL,
                                    uct_diem int2 NULL,
                                    aect_diem int2 NULL,
                                    act_diem_legacy int2 NULL,
                                    dlqi_diem int2 NULL,
                                    cu_q2ol_diem numeric(5, 2) NULL,
                                    thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                    CONSTRAINT ck_proms_act CHECK (((act_diem_legacy >= 0) AND (act_diem_legacy <= 16))),
                                    CONSTRAINT ck_proms_aect CHECK (((aect_diem >= 0) AND (aect_diem <= 16))),
                                    CONSTRAINT ck_proms_cu_q2ol CHECK ((cu_q2ol_diem >= (0)::numeric)),
                                    CONSTRAINT ck_proms_dlqi CHECK (((dlqi_diem >= 0) AND (dlqi_diem <= 30))),
                                    CONSTRAINT ck_proms_uct CHECK (((uct_diem >= 0) AND (uct_diem <= 16))),
                                    CONSTRAINT pk_ketquaproms PRIMARY KEY (ma_ket_qua_proms)
);
CREATE INDEX idx_ketquaproms_phienkhamman ON public.ketquaproms USING btree (ma_phien_kham_man);
COMMENT ON TABLE public.ketquaproms IS 'Kết quả đo lường PROMs (UCT, AE-CT, DLQI, CU-Q2oL).';

-- Table Triggers

create trigger trg_ketquaproms_cap_nhat before
    update
    on
        public.ketquaproms for each row execute function fn_cap_nhat_thoi_gian();


-- public.nhatkyaashangngay definition

-- Drop table

-- DROP TABLE public.nhatkyaashangngay;

CREATE TABLE public.nhatkyaashangngay (
                                          ma_nhat_ky_aas int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                          ma_benh_nhan int8 NOT NULL,
                                          ma_ho_so_man int8 NULL,
                                          ngay_theo_doi date NOT NULL,
                                          co_dot_sung_phu bool DEFAULT false NOT NULL,
                                          thoi_diem_xuat_hien varchar(50) NULL,
                                          muc_kho_chiu int2 NULL,
                                          han_che_hoat_dong int2 NULL,
                                          anh_huong_ngoai_hinh int2 NULL,
                                          do_nang_tong_the int2 NULL,
                                          diem_aas_ngay int2 GENERATED ALWAYS AS (
                                              CASE
                                              WHEN co_dot_sung_phu = true THEN (muc_kho_chiu + han_che_hoat_dong + anh_huong_ngoai_hinh + do_nang_tong_the)::integer
    ELSE 0
END) STORED NULL,
	thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
	CONSTRAINT ck_aas_day_du_diem CHECK (((co_dot_sung_phu AND (muc_kho_chiu IS NOT NULL) AND (han_che_hoat_dong IS NOT NULL) AND (anh_huong_ngoai_hinh IS NOT NULL) AND (do_nang_tong_the IS NOT NULL)) OR ((NOT co_dot_sung_phu) AND (muc_kho_chiu IS NULL) AND (han_che_hoat_dong IS NULL) AND (anh_huong_ngoai_hinh IS NULL) AND (do_nang_tong_the IS NULL)))),
	CONSTRAINT ck_aas_do_nang CHECK (((do_nang_tong_the >= 0) AND (do_nang_tong_the <= 3))),
	CONSTRAINT ck_aas_han_che CHECK (((han_che_hoat_dong >= 0) AND (han_che_hoat_dong <= 3))),
	CONSTRAINT ck_aas_kho_chiu CHECK (((muc_kho_chiu >= 0) AND (muc_kho_chiu <= 3))),
	CONSTRAINT ck_aas_ngoai_hinh CHECK (((anh_huong_ngoai_hinh >= 0) AND (anh_huong_ngoai_hinh <= 3))),
	CONSTRAINT pk_nhatkyaashangngay PRIMARY KEY (ma_nhat_ky_aas),
	CONSTRAINT uq_nhatkyaas_benhnhan_ngay UNIQUE (ma_benh_nhan, ngay_theo_doi)
);
CREATE INDEX idx_nhatkyaas_hoso_man ON public.nhatkyaashangngay USING btree (ma_ho_so_man);
COMMENT ON TABLE public.nhatkyaashangngay IS 'Nhật ký AAS hằng ngày (phù mạch).';

-- Table Triggers

create trigger trg_nhatkyaashangngay_cap_nhat before
    update
    on
        public.nhatkyaashangngay for each row execute function fn_cap_nhat_thoi_gian();


-- public.nhatkyuashangngay definition

-- Drop table

-- DROP TABLE public.nhatkyuashangngay;

CREATE TABLE public.nhatkyuashangngay (
                                          ma_nhat_ky_uas int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                          ma_chu_ky int8 NULL,
                                          ma_ho_so_man int8 NULL,
                                          ma_benh_nhan int8 NOT NULL,
                                          ngay_cham_diem date NOT NULL,
                                          ngay_thu_may_trong_chu_ky int2 NULL,
                                          diem_san_phu_hss int2 NOT NULL,
                                          diem_ngua_iss int2 NOT NULL,
                                          diem_uas_ngay int2 GENERATED ALWAYS AS (diem_san_phu_hss + diem_ngua_iss) STORED NULL,
                                          trang_thai_dung_thuoc varchar(50) NULL,
                                          trang_thai_ngay varchar(50) DEFAULT 'HOP_LE'::character varying NOT NULL,
                                          thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                          ngay_bat_dau_chu_ky date GENERATED ALWAYS AS (ngay_cham_diem - (ngay_thu_may_trong_chu_ky::integer - 1)) STORED NULL,
                                          CONSTRAINT ck_nhatkyuas_hss CHECK (((diem_san_phu_hss >= 0) AND (diem_san_phu_hss <= 3))),
                                          CONSTRAINT ck_nhatkyuas_iss CHECK (((diem_ngua_iss >= 0) AND (diem_ngua_iss <= 3))),
                                          CONSTRAINT ck_nhatkyuas_thu_may CHECK (((ngay_thu_may_trong_chu_ky >= 1) AND (ngay_thu_may_trong_chu_ky <= 7))),
                                          CONSTRAINT ck_uas_chuky_ngay CHECK (((ma_chu_ky IS NULL) = (ngay_thu_may_trong_chu_ky IS NULL))),
                                          CONSTRAINT ck_uas_trangthai CHECK (((trang_thai_ngay)::text = ANY ((ARRAY['HOP_LE'::character varying, 'KHONG_HOP_LE'::character varying])::text[]))),
	CONSTRAINT pk_nhatkyuashangngay PRIMARY KEY (ma_nhat_ky_uas),
	CONSTRAINT uq_nhatkyuas_benhnhan_ngay UNIQUE (ma_benh_nhan, ngay_cham_diem)
);
CREATE INDEX idx_nhatkyuas_chuky ON public.nhatkyuashangngay USING btree (ma_chu_ky);
CREATE INDEX idx_nhatkyuas_hoso_man ON public.nhatkyuashangngay USING btree (ma_ho_so_man);
COMMENT ON TABLE public.nhatkyuashangngay IS 'Nhật ký UAS hằng ngày của bệnh nhân.';

-- Table Triggers

create trigger trg_nhatkyuashangngay_cap_nhat before
    update
    on
        public.nhatkyuashangngay for each row execute function fn_cap_nhat_thoi_gian();


-- public.phienkham definition

-- Drop table

-- DROP TABLE public.phienkham;

CREATE TABLE public.phienkham (
                                  ma_phien_kham int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                  ma_ho_so int8 NOT NULL,
                                  ma_bac_si int8 NOT NULL,
                                  ma_phong_kham int8 NULL, -- FK tới danh mục phòng khám; NULL khi chưa phân phòng.
                                  loai_benh_an text NOT NULL,
                                  ma_subtype_cap int8 GENERATED ALWAYS AS (
                                      CASE
                                          WHEN loai_benh_an = 'CAP'::text THEN ma_phien_kham
                                          ELSE NULL::bigint
                                          END) STORED NULL,
                                  ma_subtype_man int8 GENERATED ALWAYS AS (
                                      CASE
                                          WHEN loai_benh_an = 'MAN'::text THEN ma_phien_kham
                                          ELSE NULL::bigint
                                          END) STORED NULL,
                                  ngay_kham date NOT NULL,
                                  lan_kham_thu int4 NOT NULL,
                                  loai_phien_kham text NULL,
                                  luu_huyet_thanh bool NULL,
                                  sot bool NULL,
                                  sot_nhiet_do_c numeric(4, 1) NULL, -- Đơn vị: độ C.
                                  ngay_hen_tai_kham date NULL,
                                  tom_tat_buoi_kham text NULL,
                                  huong_xu_tri_lan_sau text NULL,
                                  dan_do text NULL,
                                  CONSTRAINT ck_phienkham_lan_kham CHECK ((lan_kham_thu > 0)),
                                  CONSTRAINT ck_phienkham_loai CHECK ((loai_benh_an = ANY (ARRAY['CAP'::text, 'MAN'::text]))),
                                  CONSTRAINT ck_phienkham_ngay_hen CHECK ((ngay_hen_tai_kham >= ngay_kham)),
                                  CONSTRAINT ck_phienkham_nhiet_do CHECK (((sot_nhiet_do_c > (0)::numeric) AND (sot_nhiet_do_c < 'Infinity'::numeric))),
                                  CONSTRAINT pk_phienkham PRIMARY KEY (ma_phien_kham),
                                  CONSTRAINT uq_phien_hoso UNIQUE (ma_phien_kham, ma_ho_so),
                                  CONSTRAINT uq_phienkham_lan_kham UNIQUE (ma_ho_so, lan_kham_thu),
                                  CONSTRAINT uq_phienkham_ma_loai UNIQUE (ma_phien_kham, loai_benh_an)
);
CREATE INDEX idx_phienkham_bacsi ON public.phienkham USING btree (ma_bac_si);
CREATE INDEX idx_phienkham_hoso_ngay ON public.phienkham USING btree (ma_ho_so, ngay_kham);
CREATE INDEX idx_phienkham_phong ON public.phienkham USING btree (ma_phong_kham);
CREATE INDEX idx_phienkham_subtype_cap ON public.phienkham USING btree (ma_subtype_cap) WHERE (ma_subtype_cap IS NOT NULL);
CREATE INDEX idx_phienkham_subtype_man ON public.phienkham USING btree (ma_subtype_man) WHERE (ma_subtype_man IS NOT NULL);
COMMENT ON TABLE public.phienkham IS 'Các lần khám thuộc một hồ sơ bệnh án.';

-- Column comments

COMMENT ON COLUMN public.phienkham.ma_phong_kham IS 'FK tới danh mục phòng khám; NULL khi chưa phân phòng.';
COMMENT ON COLUMN public.phienkham.sot_nhiet_do_c IS 'Đơn vị: độ C.';


-- public.phienkhamcap definition

-- Drop table

-- DROP TABLE public.phienkhamcap;

CREATE TABLE public.phienkhamcap (
                                     ma_phien_kham int8 NOT NULL,
                                     loai_benh_an text GENERATED ALWAYS AS ('CAP'::text) STORED NULL,
	trieu_chung_da text NULL,
	trieu_chung_ho_hap text NULL,
	so_voi_thuong_ngay_ho_hap text NULL,
	trieu_chung_tuan_hoan text NULL,
	so_voi_thuong_ngay_tuan_hoan text NULL,
	trieu_chung_tieu_hoa text NULL,
	so_voi_thuong_ngay_tieu_hoa text NULL,
	co_san_phu bool NULL,
	co_anh_san_phu bool NULL,
	so_lan_non int4 NULL,
	CONSTRAINT ck_phienkhamcap_so_lan_non CHECK ((so_lan_non >= 0)),
	CONSTRAINT pk_phienkhamcap PRIMARY KEY (ma_phien_kham)
);
COMMENT ON TABLE public.phienkhamcap IS 'Thông tin chuyên biệt của phiên khám cấp.';


-- public.phienkhamman definition

-- Drop table

-- DROP TABLE public.phienkhamman;

CREATE TABLE public.phienkhamman (
                                     ma_phien_kham int8 NOT NULL,
                                     loai_benh_an text GENERATED ALWAYS AS ('MAN'::text) STORED NULL,
	hien_tai_co_san_phu bool NULL,
	uu_the_ngau_nhien bool NULL,
	vi_tri_san_phu text NULL,
	yeu_to_lam_nang_sa_phu text NULL,
	hoan_canh_xuat_hien_san_phu text NULL,
	trong_dot_co_phu_mach bool NULL,
	hien_tai_co_phu_mach bool NULL,
	so_lan_phu_mach_dot_nay int4 NULL,
	hoan_canh_xuat_hien_phu_mach text NULL,
	so_lan_phu_mach_nang_phai_nhap_vien int4 NULL,
	thoi_gian_ton_tai_phu_mach_co_dung_thuoc text NULL,
	thoi_gian_ton_tai_phu_mach_khong_dung_thuoc text NULL,
	diem_aas7 int2 NULL, -- Điểm AAS7, từ 0 đến 105.
	ben_phu_mach text NULL,
	thoi_gian_ngau_nhien_tuan numeric(12, 2) NULL,
	uu_the_kich_thich bool NULL,
	thoi_gian_kich_thich_tuan numeric(12, 2) NULL,
	uu_the_tuong_duong bool NULL,
	uu_the_khong_ro bool NULL,
	thu_tu_xuat_hien_so_voi_san_phu text NULL,
	phu_mach_1_tuan_gan_day bool NULL,
	chan_doan_tai_kham text NULL,
	khoang_cach_xuat_hien text NULL,
	phu_mi_mat bool NULL,
	phu_luoi bool NULL,
	phu_moi bool NULL,
	phu_vung_mat_khac bool NULL,
	phu_thanh_quan bool NULL,
	phu_ban_chan bool NULL,
	phu_sinh_duc bool NULL,
	phu_vi_tri_khac bool NULL,
	phu_vi_tri_khac_ghi_ro text NULL,
	phu_ban_tay bool NULL,
	CONSTRAINT ck_detail_phu_vi_tri_khac CHECK ((((phu_vi_tri_khac IS TRUE) AND (phu_vi_tri_khac_ghi_ro IS NOT NULL) AND (btrim(phu_vi_tri_khac_ghi_ro) <> ''::text)) OR ((NOT (phu_vi_tri_khac IS TRUE)) AND (phu_vi_tri_khac_ghi_ro IS NULL)))),
	CONSTRAINT ck_phienkhamman_aas7 CHECK (((diem_aas7 >= 0) AND (diem_aas7 <= 105))),
	CONSTRAINT ck_phienkhamman_so_lan_nhap_vien CHECK ((so_lan_phu_mach_nang_phai_nhap_vien >= 0)),
	CONSTRAINT ck_phienkhamman_so_lan_phu CHECK ((so_lan_phu_mach_dot_nay >= 0)),
	CONSTRAINT ck_phienkhamman_tuan_kich_thich CHECK (((thoi_gian_kich_thich_tuan >= (0)::numeric) AND (thoi_gian_kich_thich_tuan < 'Infinity'::numeric))),
	CONSTRAINT ck_phienkhamman_tuan_ngau_nhien CHECK (((thoi_gian_ngau_nhien_tuan >= (0)::numeric) AND (thoi_gian_ngau_nhien_tuan < 'Infinity'::numeric))),
	CONSTRAINT pk_phienkhamman PRIMARY KEY (ma_phien_kham)
);
COMMENT ON TABLE public.phienkhamman IS 'Thông tin chuyên biệt của phiên khám mạn.';

-- Column comments

COMMENT ON COLUMN public.phienkhamman.diem_aas7 IS 'Điểm AAS7, từ 0 đến 105.';


-- public.phiennghiemphapkichthich definition

-- Drop table

-- DROP TABLE public.phiennghiemphapkichthich;

CREATE TABLE public.phiennghiemphapkichthich (
                                                 ma_phien_nghiem_phap int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                                 ma_phien_kham int8 NOT NULL,
                                                 ngung_khang_histamine bool NULL,
                                                 thoi_gian_ngung_histamine numeric(8, 2) NULL, -- Đơn vị: ngày.
                                                 ngung_corticoid bool NULL,
                                                 thoi_gian_ngung_corticoid numeric(8, 2) NULL, -- Đơn vị: ngày.
                                                 thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                                 CONSTRAINT ck_phien_np_corticoid CHECK (((thoi_gian_ngung_corticoid IS NULL) OR (ngung_corticoid IS TRUE))),
                                                 CONSTRAINT ck_phien_np_corticoid_val CHECK (((thoi_gian_ngung_corticoid >= (0)::numeric) AND (thoi_gian_ngung_corticoid < 'Infinity'::numeric))),
                                                 CONSTRAINT ck_phien_np_histamine CHECK (((thoi_gian_ngung_histamine IS NULL) OR (ngung_khang_histamine IS TRUE))),
                                                 CONSTRAINT ck_phien_np_histamine_val CHECK (((thoi_gian_ngung_histamine >= (0)::numeric) AND (thoi_gian_ngung_histamine < 'Infinity'::numeric))),
                                                 CONSTRAINT pk_phien_nghiem_phap PRIMARY KEY (ma_phien_nghiem_phap),
                                                 CONSTRAINT uq_phien_nghiem_phap_phien_kham UNIQUE (ma_phien_kham)
);
COMMENT ON TABLE public.phiennghiemphapkichthich IS 'Phiên nghiệm pháp kích thích (1:1 với phiên khám mạn).';

-- Column comments

COMMENT ON COLUMN public.phiennghiemphapkichthich.thoi_gian_ngung_histamine IS 'Đơn vị: ngày.';
COMMENT ON COLUMN public.phiennghiemphapkichthich.thoi_gian_ngung_corticoid IS 'Đơn vị: ngày.';

-- Table Triggers

create trigger trg_phiennp_cap_nhat before
    update
    on
        public.phiennghiemphapkichthich for each row execute function fn_cap_nhat_thoi_gian();


-- public.tepdinhkem definition

-- Drop table

-- DROP TABLE public.tepdinhkem;

CREATE TABLE public.tepdinhkem (
                                   ma_tep_dinh_kem int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                   ma_ho_so int8 NOT NULL,
                                   ma_tai_khoan int8 NULL,
                                   ten_tep_goc varchar(255) NOT NULL,
                                   duong_dan_luu_tru varchar(500) NOT NULL,
                                   duong_dan_url varchar(500) NULL,
                                   dinh_dang_tep varchar(50) NULL,
                                   loai_phuong_tien varchar(50) NULL,
                                   dung_luong_byte int8 NULL,
                                   phan_loai_muc_dich varchar(100) NULL,
                                   mo_ta_tep text NULL,
                                   diem_do_mo numeric(6, 2) NULL,
                                   thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                   thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                   CONSTRAINT ck_tep_dung_luong CHECK ((dung_luong_byte > 0)),
                                   CONSTRAINT ck_tep_duong_dan CHECK ((btrim((duong_dan_luu_tru)::text) <> ''::text)),
                                   CONSTRAINT ck_tep_ten_goc CHECK ((btrim((ten_tep_goc)::text) <> ''::text)),
                                   CONSTRAINT pk_tepdinhkem PRIMARY KEY (ma_tep_dinh_kem)
);
CREATE INDEX idx_tepdinhkem_ma_ho_so ON public.tepdinhkem USING btree (ma_ho_so);
CREATE INDEX idx_tepdinhkem_tai_khoan ON public.tepdinhkem USING btree (ma_tai_khoan);
COMMENT ON TABLE public.tepdinhkem IS 'Tệp đính kèm (ảnh, tài liệu) theo hồ sơ.';

-- Table Triggers

create trigger trg_tepdinhkem_cap_nhat before
    update
    on
        public.tepdinhkem for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.tiensubanthan definition

-- Drop table

-- DROP TABLE public.tiensubanthan;

CREATE TABLE public.tiensubanthan (
                                      ma_tien_su_ban_than int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                      ma_ho_so int8 NOT NULL,
                                      tien_su_co_dia bool NULL,
                                      tien_su_co_dia_ghi_ro text NULL,
                                      tien_su_tuyen_giap bool NULL,
                                      tien_su_tuyen_giap_ghi_ro text NULL,
                                      tien_su_tu_mien bool NULL,
                                      tien_su_tu_mien_ghi_ro text NULL,
                                      tien_su_di_ung bool NULL,
                                      tien_su_di_ung_ghi_ro text NULL,
                                      tien_su_phan_ve bool NULL,
                                      tien_su_phan_ve_ghi_ro text NULL,
                                      tien_su_viem_nhiem_man bool NULL,
                                      tien_su_viem_nhiem_man_ghi_ro text NULL,
                                      tien_su_benh_khac bool NULL,
                                      tien_su_benh_khac_ghi_ro text NULL,
                                      CONSTRAINT ck_detail_tien_su_benh_khac CHECK ((((tien_su_benh_khac IS TRUE) AND (tien_su_benh_khac_ghi_ro IS NOT NULL) AND (btrim(tien_su_benh_khac_ghi_ro) <> ''::text)) OR ((NOT (tien_su_benh_khac IS TRUE)) AND (tien_su_benh_khac_ghi_ro IS NULL)))),
                                      CONSTRAINT ck_detail_tien_su_co_dia CHECK ((((tien_su_co_dia IS TRUE) AND (tien_su_co_dia_ghi_ro IS NOT NULL) AND (btrim(tien_su_co_dia_ghi_ro) <> ''::text)) OR ((NOT (tien_su_co_dia IS TRUE)) AND (tien_su_co_dia_ghi_ro IS NULL)))),
                                      CONSTRAINT ck_detail_tien_su_di_ung CHECK ((((tien_su_di_ung IS TRUE) AND (tien_su_di_ung_ghi_ro IS NOT NULL) AND (btrim(tien_su_di_ung_ghi_ro) <> ''::text)) OR ((NOT (tien_su_di_ung IS TRUE)) AND (tien_su_di_ung_ghi_ro IS NULL)))),
                                      CONSTRAINT ck_detail_tien_su_phan_ve CHECK ((((tien_su_phan_ve IS TRUE) AND (tien_su_phan_ve_ghi_ro IS NOT NULL) AND (btrim(tien_su_phan_ve_ghi_ro) <> ''::text)) OR ((NOT (tien_su_phan_ve IS TRUE)) AND (tien_su_phan_ve_ghi_ro IS NULL)))),
                                      CONSTRAINT ck_detail_tien_su_tu_mien CHECK ((((tien_su_tu_mien IS TRUE) AND (tien_su_tu_mien_ghi_ro IS NOT NULL) AND (btrim(tien_su_tu_mien_ghi_ro) <> ''::text)) OR ((NOT (tien_su_tu_mien IS TRUE)) AND (tien_su_tu_mien_ghi_ro IS NULL)))),
                                      CONSTRAINT ck_detail_tien_su_tuyen_giap CHECK ((((tien_su_tuyen_giap IS TRUE) AND (tien_su_tuyen_giap_ghi_ro IS NOT NULL) AND (btrim(tien_su_tuyen_giap_ghi_ro) <> ''::text)) OR ((NOT (tien_su_tuyen_giap IS TRUE)) AND (tien_su_tuyen_giap_ghi_ro IS NULL)))),
                                      CONSTRAINT ck_detail_tien_su_viem_nhiem_man CHECK ((((tien_su_viem_nhiem_man IS TRUE) AND (tien_su_viem_nhiem_man_ghi_ro IS NOT NULL) AND (btrim(tien_su_viem_nhiem_man_ghi_ro) <> ''::text)) OR ((NOT (tien_su_viem_nhiem_man IS TRUE)) AND (tien_su_viem_nhiem_man_ghi_ro IS NULL)))),
                                      CONSTRAINT ck_tsbt_benh_khac_ghi_ro CHECK ((btrim(tien_su_benh_khac_ghi_ro) <> ''::text)),
                                      CONSTRAINT ck_tsbt_co_dia_ghi_ro CHECK ((btrim(tien_su_co_dia_ghi_ro) <> ''::text)),
                                      CONSTRAINT ck_tsbt_di_ung_ghi_ro CHECK ((btrim(tien_su_di_ung_ghi_ro) <> ''::text)),
                                      CONSTRAINT ck_tsbt_phan_ve_ghi_ro CHECK ((btrim(tien_su_phan_ve_ghi_ro) <> ''::text)),
                                      CONSTRAINT ck_tsbt_tu_mien_ghi_ro CHECK ((btrim(tien_su_tu_mien_ghi_ro) <> ''::text)),
                                      CONSTRAINT ck_tsbt_tuyen_giap_ghi_ro CHECK ((btrim(tien_su_tuyen_giap_ghi_ro) <> ''::text)),
                                      CONSTRAINT ck_tsbt_viem_nhiem_man_ghi_ro CHECK ((btrim(tien_su_viem_nhiem_man_ghi_ro) <> ''::text)),
                                      CONSTRAINT pk_tiensubanthan PRIMARY KEY (ma_tien_su_ban_than),
                                      CONSTRAINT uq_tiensubanthan_hoso UNIQUE (ma_ho_so)
);
COMMENT ON TABLE public.tiensubanthan IS 'Tiền sử bản thân ghi nhận theo hồ sơ bệnh án.';


-- public.tinhtrangnhiemtrung definition

-- Drop table

-- DROP TABLE public.tinhtrangnhiemtrung;

CREATE TABLE public.tinhtrangnhiemtrung (
                                            ma_tinh_trang_nhiem_trung int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                            ma_ho_so int8 NOT NULL,
                                            ho_hap_ho bool NULL,
                                            ho_hap_dau_hong bool NULL,
                                            ho_hap_hat_hoi bool NULL,
                                            ho_hap_so_mui bool NULL,
                                            ho_hap_thoi_gian numeric NULL,
                                            ho_hap_khoang_cach numeric NULL,
                                            tiet_nieu_tieu_rat bool NULL,
                                            tiet_nieu_tieu_buot bool NULL,
                                            tiet_nieu_thoi_gian numeric NULL,
                                            tiet_nieu_khoang_cach numeric NULL,
                                            da_mun bool NULL,
                                            da_nhot bool NULL,
                                            da_thoi_gian numeric NULL,
                                            da_khoang_cach numeric NULL,
                                            khac_ghi_ro text NULL,
                                            khac_thoi_gian numeric NULL,
                                            khac_khoang_cach numeric NULL,
                                            CONSTRAINT ck_nhiem_trung_da_khoang_cach CHECK (((da_khoang_cach >= (0)::numeric) AND (da_khoang_cach < 'Infinity'::numeric))),
                                            CONSTRAINT ck_nhiem_trung_da_thoi_gian CHECK (((da_thoi_gian >= (0)::numeric) AND (da_thoi_gian < 'Infinity'::numeric))),
                                            CONSTRAINT ck_nhiem_trung_ho_hap_khoang_cach CHECK (((ho_hap_khoang_cach >= (0)::numeric) AND (ho_hap_khoang_cach < 'Infinity'::numeric))),
                                            CONSTRAINT ck_nhiem_trung_ho_hap_thoi_gian CHECK (((ho_hap_thoi_gian >= (0)::numeric) AND (ho_hap_thoi_gian < 'Infinity'::numeric))),
                                            CONSTRAINT ck_nhiem_trung_khac_ghi_ro CHECK ((btrim(khac_ghi_ro) <> ''::text)),
                                            CONSTRAINT ck_nhiem_trung_khac_khoang_cach CHECK (((khac_khoang_cach >= (0)::numeric) AND (khac_khoang_cach < 'Infinity'::numeric))),
                                            CONSTRAINT ck_nhiem_trung_khac_thoi_gian CHECK (((khac_thoi_gian >= (0)::numeric) AND (khac_thoi_gian < 'Infinity'::numeric))),
                                            CONSTRAINT ck_nhiem_trung_tiet_nieu_khoang_cach CHECK (((tiet_nieu_khoang_cach >= (0)::numeric) AND (tiet_nieu_khoang_cach < 'Infinity'::numeric))),
                                            CONSTRAINT ck_nhiem_trung_tiet_nieu_thoi_gian CHECK (((tiet_nieu_thoi_gian >= (0)::numeric) AND (tiet_nieu_thoi_gian < 'Infinity'::numeric))),
                                            CONSTRAINT pk_tinhtrangnhiemtrung PRIMARY KEY (ma_tinh_trang_nhiem_trung),
                                            CONSTRAINT uq_tinhtrangnhiemtrung_hoso UNIQUE (ma_ho_so)
);
COMMENT ON TABLE public.tinhtrangnhiemtrung IS 'Tình trạng nhiễm trùng ghi nhận trong bệnh án cấp.';


-- public.vitritonthuong definition

-- Drop table

-- DROP TABLE public.vitritonthuong;

CREATE TABLE public.vitritonthuong (
                                       ma_vi_tri_ton_thuong int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
                                       ma_dot_ton_thuong_da int8 NOT NULL,
                                       vi_tri_co_the varchar(255) NULL,
                                       thoi_diem_xuat_hien timestamptz NULL,
                                       ten_thuoc_lien_quan varchar(255) NULL,
                                       thoi_gian_dung_thuoc_truoc_khi_xuat_hien varchar(100) NULL,
                                       hoan_canh_xuat_hien varchar(255) NULL,
                                       hoan_canh_xuat_hien_khac text NULL,
                                       don_vi_thoi_gian varchar(50) NULL,
                                       thoi_diem_tao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       thoi_diem_cap_nhat timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       CONSTRAINT ck_vitritonthuong_hoancanh CHECK ((((NOT ((hoan_canh_xuat_hien)::text IS DISTINCT FROM 'Khac'::text)) AND (hoan_canh_xuat_hien_khac IS NOT NULL) AND (btrim(hoan_canh_xuat_hien_khac) <> ''::text)) OR ((NOT (NOT ((hoan_canh_xuat_hien)::text IS DISTINCT FROM 'Khac'::text))) AND (hoan_canh_xuat_hien_khac IS NULL)))),
	CONSTRAINT pk_vitritonthuong PRIMARY KEY (ma_vi_tri_ton_thuong),
	CONSTRAINT uq_vitri_dot UNIQUE (ma_vi_tri_ton_thuong, ma_dot_ton_thuong_da)
);
CREATE INDEX idx_vitritonthuong_dot ON public.vitritonthuong USING btree (ma_dot_ton_thuong_da);
COMMENT ON TABLE public.vitritonthuong IS 'Vị trí tổn thương trên cơ thể theo đợt.';

-- Table Triggers

create trigger trg_vitritonthuong_cap_nhat before
    update
    on
        public.vitritonthuong for each row execute function fn_cap_nhat_thoi_gian('protect_tao');


-- public.anhchuptonthuong foreign keys

ALTER TABLE public.anhchuptonthuong ADD CONSTRAINT fk_anh_vitri_cung_dot FOREIGN KEY (ma_vi_tri_ton_thuong,ma_dot_ton_thuong_da) REFERENCES public.vitritonthuong(ma_vi_tri_ton_thuong,ma_dot_ton_thuong_da) ON DELETE RESTRICT;
ALTER TABLE public.anhchuptonthuong ADD CONSTRAINT fk_anhtonthuong_dottonthuongda FOREIGN KEY (ma_dot_ton_thuong_da) REFERENCES public.dottonthuongda(ma_dot_ton_thuong_da) ON DELETE RESTRICT;
ALTER TABLE public.anhchuptonthuong ADD CONSTRAINT fk_anhtonthuong_tepdinhkem FOREIGN KEY (ma_tep_dinh_kem) REFERENCES public.tepdinhkem(ma_tep_dinh_kem) ON DELETE RESTRICT;
ALTER TABLE public.anhchuptonthuong ADD CONSTRAINT fk_anhtonthuong_vitritonthuong FOREIGN KEY (ma_vi_tri_ton_thuong) REFERENCES public.vitritonthuong(ma_vi_tri_ton_thuong) ON DELETE RESTRICT;


-- public.benhanmaydaycap foreign keys

ALTER TABLE public.benhanmaydaycap ADD CONSTRAINT fk_benhanmaydaycap_hoso FOREIGN KEY (ma_ho_so,loai_benh_an) REFERENCES public.hosobenhan(ma_ho_so,loai_benh_an) DEFERRABLE INITIALLY DEFERRED;


-- public.benhanmaydayman foreign keys

ALTER TABLE public.benhanmaydayman ADD CONSTRAINT fk_benhanmaydayman_hoso FOREIGN KEY (ma_ho_so,loai_benh_an) REFERENCES public.hosobenhan(ma_ho_so,loai_benh_an) DEFERRABLE INITIALLY DEFERRED;


-- public.benhsucacdotbenhtruoc foreign keys

ALTER TABLE public.benhsucacdotbenhtruoc ADD CONSTRAINT fk_benhsucacdotbenhtruoc_benh_an_man FOREIGN KEY (ma_ho_so) REFERENCES public.benhanmaydayman(ma_ho_so) ON DELETE RESTRICT;


-- public.chidanngungthuoc foreign keys

ALTER TABLE public.chidanngungthuoc ADD CONSTRAINT fk_chidanngungthuoc_donthuoc FOREIGN KEY (ma_don_thuoc) REFERENCES public.donthuoc(ma_don_thuoc) ON DELETE RESTRICT;


-- public.chitietdonthuoc foreign keys

ALTER TABLE public.chitietdonthuoc ADD CONSTRAINT fk_chitietdonthuoc_donthuoc FOREIGN KEY (ma_don_thuoc) REFERENCES public.donthuoc(ma_don_thuoc) ON DELETE RESTRICT;
ALTER TABLE public.chitietdonthuoc ADD CONSTRAINT fk_chitietdonthuoc_thuoc FOREIGN KEY (ma_thuoc) REFERENCES public.thuoc(ma_thuoc) ON DELETE RESTRICT;


-- public.chitietketquanghiemphap foreign keys

ALTER TABLE public.chitietketquanghiemphap ADD CONSTRAINT fk_chi_tiet_kqnp_loai FOREIGN KEY (loai_nghiem_phap) REFERENCES public.loainghiemphap(ma_nghiem_phap) ON DELETE RESTRICT;
ALTER TABLE public.chitietketquanghiemphap ADD CONSTRAINT fk_chi_tiet_kqnp_phien FOREIGN KEY (ma_phien_nghiem_phap) REFERENCES public.phiennghiemphapkichthich(ma_phien_nghiem_phap) ON DELETE RESTRICT;
ALTER TABLE public.chitietketquanghiemphap ADD CONSTRAINT fk_kqnp_phanloai FOREIGN KEY (loai_nghiem_phap,la_can_nguyen_khac) REFERENCES public.loainghiemphap(ma_nghiem_phap,la_can_nguyen_khac) ON DELETE RESTRICT;


-- public.chitiettonthuongcaptinh foreign keys

ALTER TABLE public.chitiettonthuongcaptinh ADD CONSTRAINT fk_chitiettonthuongcaptinh_phien_cap FOREIGN KEY (ma_phien_kham) REFERENCES public.phienkhamcap(ma_phien_kham) ON DELETE RESTRICT;


-- public.chitiettonthuongsanphu foreign keys

ALTER TABLE public.chitiettonthuongsanphu ADD CONSTRAINT fk_chitiettonthuongsanphu_phien_man FOREIGN KEY (ma_phien_kham) REFERENCES public.phienkhamman(ma_phien_kham) ON DELETE RESTRICT;
ALTER TABLE public.chitiettonthuongsanphu ADD CONSTRAINT fk_sanphu_dacdiem FOREIGN KEY (ma_dac_diem_san_phu) REFERENCES public.dacdiemsanphu(ma_dac_diem_san_phu) ON DELETE RESTRICT;


-- public.danhgiamucdohoatdongbenh foreign keys

ALTER TABLE public.danhgiamucdohoatdongbenh ADD CONSTRAINT fk_danhgia_nguoi FOREIGN KEY (ma_nguoi_danh_gia) REFERENCES public.nguoi(ma_nguoi) ON DELETE RESTRICT;
ALTER TABLE public.danhgiamucdohoatdongbenh ADD CONSTRAINT fk_danhgiahoatdong_phienkhamman FOREIGN KEY (ma_phien_kham_man) REFERENCES public.phienkhamman(ma_phien_kham) ON DELETE RESTRICT;


-- public.dauhieusinhton foreign keys

ALTER TABLE public.dauhieusinhton ADD CONSTRAINT fk_dauhieusinhton_bacsi FOREIGN KEY (ma_bac_si) REFERENCES public.nhanvien(ma_nhan_vien) ON DELETE RESTRICT;
ALTER TABLE public.dauhieusinhton ADD CONSTRAINT fk_dauhieusinhton_phienkham FOREIGN KEY (ma_phien_kham) REFERENCES public.phienkham(ma_phien_kham) ON DELETE RESTRICT;


-- public.donthuoc foreign keys

ALTER TABLE public.donthuoc ADD CONSTRAINT fk_donthuoc_hosobenhan FOREIGN KEY (ma_ho_so) REFERENCES public.hosobenhan(ma_ho_so) ON DELETE RESTRICT;
ALTER TABLE public.donthuoc ADD CONSTRAINT fk_donthuoc_phien_hoso FOREIGN KEY (ma_phien_kham,ma_ho_so) REFERENCES public.phienkham(ma_phien_kham,ma_ho_so) ON DELETE RESTRICT;


-- public.dottonthuongda foreign keys

ALTER TABLE public.dottonthuongda ADD CONSTRAINT fk_dottonthuongda_benhanman FOREIGN KEY (ma_ho_so_man) REFERENCES public.benhanmaydayman(ma_ho_so) ON DELETE RESTRICT;
ALTER TABLE public.dottonthuongda ADD CONSTRAINT fk_dottonthuongda_benhnhan FOREIGN KEY (ma_benh_nhan) REFERENCES public.benhnhan(ma_benh_nhan) ON DELETE RESTRICT;
ALTER TABLE public.dottonthuongda ADD CONSTRAINT fk_dottonthuongda_cung_benhnhan FOREIGN KEY (ma_ho_so_man,ma_benh_nhan) REFERENCES public.hosobenhan(ma_ho_so,ma_benh_nhan) ON DELETE RESTRICT;


-- public.hoancanhkhoiphatdiung foreign keys

ALTER TABLE public.hoancanhkhoiphatdiung ADD CONSTRAINT fk_hoancanhkhoiphatdiung_hoso FOREIGN KEY (ma_ho_so) REFERENCES public.hosobenhan(ma_ho_so) ON DELETE RESTRICT;


-- public.hosobenhan foreign keys

ALTER TABLE public.hosobenhan ADD CONSTRAINT fk_hoso_bat_buoc_cap FOREIGN KEY (ma_subtype_cap) REFERENCES public.benhanmaydaycap(ma_ho_so) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.hosobenhan ADD CONSTRAINT fk_hoso_bat_buoc_man FOREIGN KEY (ma_subtype_man) REFERENCES public.benhanmaydayman(ma_ho_so) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.hosobenhan ADD CONSTRAINT fk_hosobenhan_benhnhan FOREIGN KEY (ma_benh_nhan) REFERENCES public.benhnhan(ma_benh_nhan) ON DELETE RESTRICT;


-- public.ketquacanlamsang foreign keys

ALTER TABLE public.ketquacanlamsang ADD CONSTRAINT fk_kqcls_bat_buoc_cap FOREIGN KEY (ma_subtype_cap) REFERENCES public.ketquacanlamsangcap(ma_ket_qua_can_lam_sang) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.ketquacanlamsang ADD CONSTRAINT fk_kqcls_bat_buoc_man FOREIGN KEY (ma_subtype_man) REFERENCES public.ketquacanlamsangman(ma_ket_qua_can_lam_sang) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.ketquacanlamsang ADD CONSTRAINT fk_kqcls_phien_kham FOREIGN KEY (ma_phien_kham,loai_ket_qua_can_lam_sang) REFERENCES public.phienkham(ma_phien_kham,loai_benh_an) ON DELETE RESTRICT;


-- public.ketquacanlamsangcap foreign keys

ALTER TABLE public.ketquacanlamsangcap ADD CONSTRAINT fk_kqcls_cap_cha FOREIGN KEY (ma_ket_qua_can_lam_sang,loai_ket_qua_can_lam_sang) REFERENCES public.ketquacanlamsang(ma_ket_qua_can_lam_sang,loai_ket_qua_can_lam_sang) ON DELETE CASCADE;


-- public.ketquacanlamsangman foreign keys

ALTER TABLE public.ketquacanlamsangman ADD CONSTRAINT fk_kqcls_man_cha FOREIGN KEY (ma_ket_qua_can_lam_sang,loai_ket_qua_can_lam_sang) REFERENCES public.ketquacanlamsang(ma_ket_qua_can_lam_sang,loai_ket_qua_can_lam_sang) ON DELETE CASCADE;


-- public.ketquaproms foreign keys

ALTER TABLE public.ketquaproms ADD CONSTRAINT fk_ketquaproms_phienkhamman FOREIGN KEY (ma_phien_kham_man) REFERENCES public.phienkhamman(ma_phien_kham) ON DELETE RESTRICT;


-- public.nhatkyaashangngay foreign keys

ALTER TABLE public.nhatkyaashangngay ADD CONSTRAINT fk_nhatkyaas_benhanman FOREIGN KEY (ma_ho_so_man) REFERENCES public.benhanmaydayman(ma_ho_so) ON DELETE RESTRICT;
ALTER TABLE public.nhatkyaashangngay ADD CONSTRAINT fk_nhatkyaas_benhnhan FOREIGN KEY (ma_benh_nhan) REFERENCES public.benhnhan(ma_benh_nhan) ON DELETE RESTRICT;
ALTER TABLE public.nhatkyaashangngay ADD CONSTRAINT fk_nhatkyaashangngay_cung_benhnhan FOREIGN KEY (ma_ho_so_man,ma_benh_nhan) REFERENCES public.hosobenhan(ma_ho_so,ma_benh_nhan) ON DELETE RESTRICT;


-- public.nhatkyuashangngay foreign keys

ALTER TABLE public.nhatkyuashangngay ADD CONSTRAINT fk_nhatkyuas_benhanman FOREIGN KEY (ma_ho_so_man) REFERENCES public.benhanmaydayman(ma_ho_so) ON DELETE RESTRICT;
ALTER TABLE public.nhatkyuashangngay ADD CONSTRAINT fk_nhatkyuas_benhnhan FOREIGN KEY (ma_benh_nhan) REFERENCES public.benhnhan(ma_benh_nhan) ON DELETE RESTRICT;
ALTER TABLE public.nhatkyuashangngay ADD CONSTRAINT fk_nhatkyuas_chuky FOREIGN KEY (ma_chu_ky) REFERENCES public.chukytheodoiuas7(ma_chu_ky) ON DELETE RESTRICT;
ALTER TABLE public.nhatkyuashangngay ADD CONSTRAINT fk_nhatkyuashangngay_cung_benhnhan FOREIGN KEY (ma_ho_so_man,ma_benh_nhan) REFERENCES public.hosobenhan(ma_ho_so,ma_benh_nhan) ON DELETE RESTRICT;
ALTER TABLE public.nhatkyuashangngay ADD CONSTRAINT fk_uas_chuky_benhnhan_ngay FOREIGN KEY (ma_chu_ky,ma_benh_nhan,ngay_bat_dau_chu_ky) REFERENCES public.chukytheodoiuas7(ma_chu_ky,ma_benh_nhan,ngay_bat_dau) ON DELETE RESTRICT;


-- public.phienkham foreign keys

ALTER TABLE public.phienkham ADD CONSTRAINT fk_phien_bat_buoc_cap FOREIGN KEY (ma_subtype_cap) REFERENCES public.phienkhamcap(ma_phien_kham) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.phienkham ADD CONSTRAINT fk_phien_bat_buoc_man FOREIGN KEY (ma_subtype_man) REFERENCES public.phienkhamman(ma_phien_kham) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE public.phienkham ADD CONSTRAINT fk_phienkham_bacsi FOREIGN KEY (ma_bac_si) REFERENCES public.nhanvien(ma_nhan_vien) ON DELETE RESTRICT;
ALTER TABLE public.phienkham ADD CONSTRAINT fk_phienkham_hoso FOREIGN KEY (ma_ho_so,loai_benh_an) REFERENCES public.hosobenhan(ma_ho_so,loai_benh_an);
ALTER TABLE public.phienkham ADD CONSTRAINT fk_phienkham_phong FOREIGN KEY (ma_phong_kham) REFERENCES public.phongkham(ma_phong_kham) ON DELETE RESTRICT;


-- public.phienkhamcap foreign keys

ALTER TABLE public.phienkhamcap ADD CONSTRAINT fk_phienkhamcap_phienkham FOREIGN KEY (ma_phien_kham,loai_benh_an) REFERENCES public.phienkham(ma_phien_kham,loai_benh_an) DEFERRABLE INITIALLY DEFERRED;


-- public.phienkhamman foreign keys

ALTER TABLE public.phienkhamman ADD CONSTRAINT fk_phienkhamman_phienkham FOREIGN KEY (ma_phien_kham,loai_benh_an) REFERENCES public.phienkham(ma_phien_kham,loai_benh_an) DEFERRABLE INITIALLY DEFERRED;


-- public.phiennghiemphapkichthich foreign keys

ALTER TABLE public.phiennghiemphapkichthich ADD CONSTRAINT fk_phien_np_phien_kham_man FOREIGN KEY (ma_phien_kham) REFERENCES public.phienkhamman(ma_phien_kham) ON DELETE RESTRICT;


-- public.tepdinhkem foreign keys

ALTER TABLE public.tepdinhkem ADD CONSTRAINT fk_tepdinhkem_hosobenhan FOREIGN KEY (ma_ho_so) REFERENCES public.hosobenhan(ma_ho_so) ON DELETE RESTRICT;
ALTER TABLE public.tepdinhkem ADD CONSTRAINT fk_tepdinhkem_taikhoan FOREIGN KEY (ma_tai_khoan) REFERENCES public.taikhoan(ma_tai_khoan) ON DELETE SET NULL;


-- public.tiensubanthan foreign keys

ALTER TABLE public.tiensubanthan ADD CONSTRAINT fk_tiensubanthan_hoso FOREIGN KEY (ma_ho_so) REFERENCES public.hosobenhan(ma_ho_so) ON DELETE RESTRICT;


-- public.tinhtrangnhiemtrung foreign keys

ALTER TABLE public.tinhtrangnhiemtrung ADD CONSTRAINT fk_tinhtrangnhiemtrung_benh_an_cap FOREIGN KEY (ma_ho_so) REFERENCES public.benhanmaydaycap(ma_ho_so) ON DELETE RESTRICT;


-- public.vitritonthuong foreign keys

ALTER TABLE public.vitritonthuong ADD CONSTRAINT fk_vitritonthuong_dottonthuongda FOREIGN KEY (ma_dot_ton_thuong_da) REFERENCES public.dottonthuongda(ma_dot_ton_thuong_da) ON DELETE RESTRICT;