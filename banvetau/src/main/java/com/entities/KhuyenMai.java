package com.entities;

import java.time.LocalDate;

public class KhuyenMai {
	private String maKM;
	private String tenKM;
	private double tyLeGiam; // Ví dụ: 0.1 tương ứng với giảm 10%
	private LocalDate ngayBatDau;
	private LocalDate ngayKetThuc;
	private String moTa;

	// Constructor đầy đủ tham số
	public KhuyenMai(String maKM, String tenKM, double tyLeGiam, LocalDate ngayBatDau, LocalDate ngayKetThuc,
			String moTa) {
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.tyLeGiam = tyLeGiam;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.moTa = moTa;
	}

	// Constructor mặc định
	public KhuyenMai() {
	}

	// Constructor với mã (dùng để truy vấn nhanh)
	public KhuyenMai(String maKM) {
		this.maKM = maKM;
	}

	// Getter và Setter
	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public double getTyLeGiam() {
		return tyLeGiam;
	}

	public void setTyLeGiam(double tyLeGiam) {
		this.tyLeGiam = tyLeGiam;
	}

	public LocalDate getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(LocalDate ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public LocalDate getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(LocalDate ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	/**
	 * Kiểm tra xem khuyến mãi có còn hiệu lực ở thời điểm hiện tại hay không
	 */
	public boolean isConHieuLuc() {
		LocalDate nay = LocalDate.now();
		return (nay.isEqual(ngayBatDau) || nay.isAfter(ngayBatDau))
				&& (nay.isEqual(ngayKetThuc) || nay.isBefore(ngayKetThuc));
	}

	@Override
	public String toString() {
		return "KhuyenMai [maKM=" + maKM + ", tenKM=" + tenKM + ", tyLeGiam=" + (tyLeGiam * 100) + "%]";
	}
}