package com.entities;

public class Tuyen {
	private String maTuyen;
	private Ga gaDen;
	private Ga gaDi;
	public String getMaTuyen() {
		return maTuyen;
	}
	public void setMaTuyen(String maTuyen) {
		this.maTuyen = maTuyen;
	}
	public Ga getGaDen() {
		return gaDen;
	}
	public void setGaDen(Ga gaDen) {
		this.gaDen = gaDen;
	}
	public Ga getGaDi() {
		return gaDi;
	}
	public void setGaDi(Ga gaDi) {
		this.gaDi = gaDi;
	}
	@Override
	public String toString() {
		return "Tuyen [maTuyen=" + maTuyen + ", gaDen=" + gaDen + ", gaDi=" + gaDi + "]";
	}
	
	
}
