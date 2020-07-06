package org.comp.klase;

public class Kupac {
	private String sifra;
	private String naziv;
	private String adresa;
	private String ZIP;
	private String mjesto;
	private String telefon;
	private String kupacIDbroj;
	private String kupacVATbroj;
	private String ukupniKredit;
	private String dospjeliKredit;
	private String posljednjaUplata;
	private String prihod;
	private String brojRacuna;
	private String kupacSudBroj;
	private String osnovniPopust;
	private String idProgram;
	private boolean izabran;
	
	Kupac(String sifra, String naziv, String adresa, String ZIP, String mjesto, String telefon, String kupacIDbroj, String kupacVATbroj, String ukupniKredit, String dospjeliKredit, String posljednjaUplata, String prihod, String brojRacuna, String kupacSudBroj, String osnovniPopust, String idProgram ){
		this.sifra=sifra;
		this.naziv=naziv;
		this.adresa=adresa;
		this.ZIP=ZIP;
		this.mjesto=mjesto;
		this.telefon=telefon;
		this.kupacIDbroj=kupacIDbroj;
		this.kupacVATbroj=kupacVATbroj;
		this.ukupniKredit=ukupniKredit;
		this.dospjeliKredit=dospjeliKredit;
		this.posljednjaUplata=posljednjaUplata;
		this.prihod=prihod;
		this.brojRacuna=brojRacuna;
		this.kupacSudBroj=kupacSudBroj;
		this.osnovniPopust=osnovniPopust;
		this.idProgram=idProgram;
		this.izabran=false;
	}
	
	public String dajSifru(){ return sifra; }
	public String dajNaziv() {return naziv; }
	public String dajAdresu() {return adresa; }
	public String dajZIP() {return ZIP; }
	public String dajMjesto() {return mjesto; }
	public String dajTelefon() {return telefon; }
	public String dajKupacIdbroj() {return kupacIDbroj; }
	public String dajKupacVATbroj() {return kupacVATbroj; }
	public String dajUkupniKredit() {return ukupniKredit; }
	public String dajDospjeliKredit() {return dospjeliKredit; }
	public String dajPosljednjuUplatu() {return posljednjaUplata; }
	public String dajPrihod() {return prihod; }
	public String dajBrojRacuna() {return brojRacuna; }
	public String dajKupacSudBroj() {return kupacSudBroj; }
	public String dajOsnovniPopust() { return osnovniPopust; }
	public String dajIdPrograma() { return idProgram; }
	
	public boolean dajJeLiIzabran() { return izabran; }
	
	public void postaviRabat()	{ osnovniPopust="0.00"; }
	public void postaviRabatZaProgram(String rabat)	{ osnovniPopust=rabat; }
	public void izaberiKupca(boolean b) { this.izabran=b; }

}
