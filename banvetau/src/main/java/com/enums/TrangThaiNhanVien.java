package com.enums;

public enum TrangThaiNhanVien {
	HOATDONG("Đang hoạt động"), NGUNGHOATDONG("Ngưng hoạt động");

	private final String moTa;

	// Constructor của Enum
	TrangThaiNhanVien(String moTa) {
		this.moTa = moTa;
	}

	public String getMoTa() {
		return moTa;
	}

	// --- SỬA LỖI TẠI ĐÂY: Đổi TrangThaiTau thành TrangThaiNhanVien ---
	public static TrangThaiNhanVien fromString(String text) {
		if (text == null)
			return HOATDONG;

		for (TrangThaiNhanVien b : TrangThaiNhanVien.values()) {
			// So sánh với name (HOATDONG) hoặc moTa (Đang hoạt động) tùy theo cách bạn lưu
			// trong DB
			if (b.name().equalsIgnoreCase(text) || b.moTa.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return HOATDONG; // Giá trị mặc định
	}
}