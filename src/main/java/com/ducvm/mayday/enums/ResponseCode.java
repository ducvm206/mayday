package com.ducvm.mayday.enums;

public enum ResponseCode {
	SUCCESS(1000, "OK", "Xử lý thành công"),
	OBJECT_NOT_EXISTED(9992, "Post is not existed", "Đối tượng không tồn tại"),
	CODE_VERIFY_INCORRECT(9993, "Code verify is incorrect", "Mã xác thực không đúng"),
	NO_DATA(9994, "No Data or end of list data", "Không có dữ liệu / Hết dữ liệu"),
	USER_NOT_VALIDATED(9995, "User is not validated", "Người dùng chưa xác thực"),
	USER_EXISTED(9996, "User existed.", "Người dùng/Dữ liệu đã tồn tại"),
	METHOD_INVALID(9997, "Method is invalid", "Phương thức không đúng"),
	TOKEN_INVALID(9998, "Token is invalid.", "Sai token"),
	EXCEPTION_ERROR(9999, "Exception error.", "Lỗi exception"),
	DB_CONNECTION_ERROR(1001, "Can not connect to DB.", "Lỗi mất kết nối DB/thực thi SQL"),
	PARAMETER_NOT_ENOUGH(1002, "Parameter is not enough", "Số lượng Parameter không đầy đủ"),
	PARAMETER_TYPE_INVALID(1003, "Parameter type is invalid.", "Kiểu tham số không đúng đắn"),
	PARAMETER_VALUE_INVALID(1004, "Parameter value is invalid.", "Giá trị của tham số không hợp lệ"),
	UNKNOWN_ERROR(1005, "Unknown error.", "Unknown error"),
	FILE_TOO_BIG(1006, "File size is too big.", "Cỡ file vượt mức cho phép"),
	UPLOAD_FILE_FAILED(1007, "Upload File Failed!.", "Upload thất bại"),
	MAX_IMAGES_EXCEEDED(1008, "Maximum number of images.", "Số lượng images vượt quá quy định"),
	NOT_ACCESS(1009, "Not access.", "Không có quyền truy cập tài nguyên"),
	ACTION_ALREADY_DONE(1010, "Action has been done previously by this user.", "Hành động đã được thực hiện trước đây");

	private final Integer code;
	private final String message;
	private final String messageVi;

	ResponseCode(Integer code, String message, String messageVi) {
		this.code = code;
		this.message = message;
		this.messageVi = messageVi;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageVi() {
		return messageVi;
	}

	public static ResponseCode fromCode(int code) {
		for (ResponseCode r : values()) {
			if (r.code == code) {
				return r;
			}
		}
		return null;
	}
}