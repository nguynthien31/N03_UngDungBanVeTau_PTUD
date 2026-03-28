package com.entities;

import com.enums.TrangThaiNhanVien;

public class NhanVien {
	private String maNV;
	private String tenNV;
	private String chucVu;
	private String matKhau;
	private TrangThaiNhanVien trangThaiNV;

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getTenNV() {
		return tenNV;
	}

	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}

	public NhanVien(String maNV, String tenNV) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public TrangThaiNhanVien getTrangThaiNV() {
		return trangThaiNV;
	}

	public void setTrangThaiNV(TrangThaiNhanVien trangThaiNV) {
		this.trangThaiNV = trangThaiNV;
	}

	public NhanVien() {
		super();
	}

	public NhanVien(String maNV, String tenNV, String chucVu, String matKhau, TrangThaiNhanVien trangThaiNV) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.chucVu = chucVu;
		this.matKhau = matKhau;
		this.trangThaiNV = trangThaiNV;
	}

	@Override
	public String toString() {
		return "NhanVien [maNV=" + maNV + ", tenNV=" + tenNV + ", chucVu=" + chucVu + ", matKhau=" + matKhau
				+ ", trangThaiNV=" + trangThaiNV + "]";
	}

}
