package org.comp.klase;

public class AkcijskiPopusti {
	private String sifmat;
	private String kupdob;
	private String sifpre;
	private String primis;
	private String datcij;
	private String datpre;
	private String rabsta;
	
	public AkcijskiPopusti(String sifmat, String kupdob, String sifpre, String primis, String datcij, String datpre, String rabsta){
		this.sifmat=sifmat;
		this.kupdob= kupdob;
		this.sifpre=sifpre;
		this.primis=primis;
		this.datcij=datcij;
		this.datpre=datpre;
		this.rabsta=rabsta;
	}
	
	public String DajSifmat() {
		return sifmat;
	}
	public void PostaviSifmat(String sifmat) {
		this.sifmat = sifmat;
	}
	public String DajKupdob() {
		return kupdob;
	}
	public void PostaviKupdob(String kupdob) {
		this.kupdob = kupdob;
	}
	public String DajSifpre() {
		return sifpre;
	}
	public void PostaviSifpre(String sifpre) {
		this.sifpre = sifpre;
	}
	public String DajPrimis() {
		return primis;
	}
	public void PostaviPrimis(String primis) {
		this.primis = primis;
	}
	public String DajDatcij() {
		return datcij;
	}
	public void PostaviDatcij(String datcij) {
		this.datcij = datcij;
	}
	public String DajDatpre() {
		return datpre;
	}
	public void PostaviDatpre(String datpre) {
		this.datpre = datpre;
	}
	
	public String DajRabsta() {
		return rabsta;
	}
	public void PostaviRabsta(String rabsta) {
		this.rabsta = rabsta;
	}

}