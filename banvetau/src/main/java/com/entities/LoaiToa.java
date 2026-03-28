package com.entities;

import java.util.Objects;

public class LoaiToa {
	private String maLoaiToa;
	private String tenLoaiToa;
	private String moTa;

	// 1. Constructor không tham số
	public LoaiToa() {
	}

	// 2. Constructor đầy đủ tham số
	public LoaiToa(String maLoaiToa, String tenLoaiToa, String moTa) {
		this.maLoaiToa = maLoaiToa;
		this.tenLoaiToa = tenLoaiToa;
		this.moTa = moTa;
	}

	// 3. Constructor với mã (Dùng để tìm kiếm hoặc tạo liên kết nhanh)
	public LoaiToa(String maLoaiToa) {
		this.maLoaiToa = maLoaiToa;
	}

	// 4. Getter và Setter
	public String getMaLoaiToa() {
		return maLoaiToa;
	}

	public void setMaLoaiToa(String maLoaiToa) {
		this.maLoaiToa = maLoaiToa;
	}

	public String getTenLoaiToa() {
		return tenLoaiToa;
	}

	public void setTenLoaiToa(String tenLoaiToa) {
		this.tenLoaiToa = tenLoaiToa;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	// 5. Override toString để hiển thị trên các ComboBox hoặc Log
	@Override
	public String toString() {
		return tenLoaiToa;
	}

	// 6. Override equals và hashCode (Quan trọng khi làm việc với Collection/List)
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		LoaiToa loaiToa = (LoaiToa) o;
		return Objects.equals(maLoaiToa, loaiToa.maLoaiToa);
	}

	@Override
	public int hashCode() {
		return Objects.hash(maLoaiToa);
	}
}