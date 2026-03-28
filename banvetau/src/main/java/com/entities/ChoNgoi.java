package com.entities;

public class ChoNgoi {
	private String maCho;
	private String tenCho;
	private String maToa;
	private int trangThai; // Or boolean, depending on your DB (0: Empty, 1: Booked)

	// Default Constructor
	public ChoNgoi() {
	}

	// Full Constructor
	public ChoNgoi(String maCho, String tenCho, String maToa, int trangThai) {
		this.maCho = maCho;
		this.tenCho = tenCho;
		this.maToa = maToa;
		this.trangThai = trangThai;
	}

	// Getters and Setters
	public String getMaCho() {
		return maCho;
	}

	public void setMaCho(String maCho) {
		this.maCho = maCho;
	}

	public String getTenCho() {
		return tenCho;
	}

	public void setTenCho(String tenCho) {
		this.tenCho = tenCho;
	}

	public String getMaToa() {
		return maToa;
	}

	public void setMaToa(String maToa) {
		this.maToa = maToa;
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "ChoNgoi{" + "maCho='" + maCho + '\'' + ", tenCho='" + tenCho + '\'' + ", maToa='" + maToa + '\''
				+ ", trangThai=" + trangThai + '}';
	}
}