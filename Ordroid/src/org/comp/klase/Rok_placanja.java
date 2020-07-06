package org.comp.klase;

public class Rok_placanja {
	public String nv_brjtin;
    public String ba_kupdob;
    public String nr_rabkup;
    public String nr_rokpla;
	
	public Rok_placanja(String nv_brjtin, String ba_kupdob, String nr_rabkup, String nr_rokpla) {
		super();
		this.nv_brjtin = nv_brjtin;
		this.ba_kupdob = ba_kupdob;
		this.nr_rabkup = nr_rabkup;
		this.nr_rokpla = nr_rokpla;
	}

	public String getNa_kratin() {
		return nv_brjtin;
	}

	public void setNa_kratin(String na_kratin) {
		this.nv_brjtin = na_kratin;
	}

	public String getBa_kupdob() {
		return ba_kupdob;
	}

	public void setBa_kupdob(String ba_kupdob) {
		this.ba_kupdob = ba_kupdob;
	}

	public String getNr_rabkup() {
		return nr_rabkup;
	}

	public void setNr_rabkup(String nr_rabkup) {
		this.nr_rabkup = nr_rabkup;
	}

	public String getNr_rokpla() {
		return nr_rokpla;
	}

	public void setNr_rokpla(String nr_rokpla) {
		this.nr_rokpla = nr_rokpla;
	}
	
}
