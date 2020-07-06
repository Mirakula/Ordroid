package org.comp.klase;

public class Korisnik {

	private String id;
	private String imeIprezime;
	private String korisnickoIme;
	private String lozinka;
	private String lokacija;
	
	Korisnik(String id, String imeIprezime, String korIme, String lozinka, String lokacija){
		this.id=id;
		this.imeIprezime=imeIprezime;
		this.korisnickoIme=korIme;
		this.lozinka=lozinka;
		this.lokacija=lokacija;
	}
	
	Korisnik(Korisnik k) {
		this.id=k.dajID();
		this.imeIprezime=k.dajImeIPrezime();
		this.korisnickoIme=k.dajKorisnickoIme();
		this.lozinka=k.dajLozinku();
		this.lokacija=k.dajLokaciju();
	}
	
	public String dajID(){ return id; }
	public String dajImeIPrezime() {return imeIprezime; }
	public String dajLozinku() {return lozinka;}
	public String dajLokaciju() { return lokacija; }
	public String dajKorisnickoIme() { return korisnickoIme;}
}
