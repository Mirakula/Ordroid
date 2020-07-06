package org.comp.klase;

public class Program {
	private String sifra;
	private String naziv;
	private boolean izabran;
	
	public Program(String s, String n){
		this.sifra=s;
		this.naziv=n;
		izabran=false;
	}
	
	public Program(Program p){
		this.sifra=p.sifra;
		this.naziv=p.naziv;
		this.izabran=false;
	}
	
	public String dajSifru() {return sifra;}
	public String dajNaziv() {return naziv;}
	public void izaberi(boolean b) { izabran=b; }
	public boolean DajJeLiIzabran() { return izabran; }
}
