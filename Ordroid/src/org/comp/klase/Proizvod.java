package org.comp.klase;

import java.text.DecimalFormat;

import android.app.Application;
import android.util.Log;

public class Proizvod {

	private String sifra;
	private String id_grupa;
	private String naziv;
	private String EAN;
	private String mjera;
	private String cijena;
	private String cijena_mlp;
	private String osnovica;
	private String kupdob;
	private double popust;
	private double rabatKupca;
	private String dugme;
	private String id_program;
	private String kolicina;
	private boolean izabran;
	private double narucenaKolicina;
	private int redniBroj;
	private double ukupniPopustNaStavku;

	Proizvod(String sifra, String id_grupa, String naziv, String EAN,
			String mjera, String cijena, String cijena_mlp, double popust,
			String dugme, String id_program, String kolicina, String kupdob) {
		this.sifra = sifra;
		this.id_grupa = id_grupa;
		this.naziv = naziv;
		this.EAN = EAN;
		this.mjera = mjera;
		this.cijena = cijena;
		this.cijena_mlp = cijena_mlp;
		this.popust = popust;
		this.dugme = dugme;
		this.id_program = id_program;
		this.kolicina = kolicina;
		this.izabran = false;
		this.narucenaKolicina = 0.000;
		this.ukupniPopustNaStavku = 0;
		this.kupdob=kupdob;
	}

	Proizvod(int redBroj, String sifra, double kolicina, String cijena,
			double rabat, boolean maloprodaja) {
		this.sifra = sifra;
		if (maloprodaja)
			this.cijena_mlp = cijena;
		else
			this.cijena = cijena;
		this.popust = rabat;
		this.narucenaKolicina = kolicina;
		this.izabran = false;
		this.redniBroj = redBroj;

	}

	public String dajSifru() {
		return sifra;
	}
	
	public String dajKupdob(){
		return kupdob;
	}

	public String dajIdGrupe() {
		return id_grupa;
	}

	public String dajNaziv() {
		return naziv;
	}

	public String dajEAN() {
		return EAN;
	}

	public String dajMjeru() {
		return mjera;
	}

	public String dajCijenu() {
		return cijena;
	}

	public String dajMLPcijenu() {
		return cijena_mlp;
	}

	public double dajPopust() {
		return popust;
	}

	public String dajDugme() {
		return dugme;
	}

	public String dajIDprogram() {
		return id_program;
	}

	public String dajKolicinu() {
		return kolicina;
	}
	
	public double dajRabatKupca(){
		return rabatKupca;
	}

	public void postaviIdGrupe(String idGrupe) {
		id_grupa = idGrupe;
	}

	public void postaviNaziv(String naz) {
		naziv = naz;
	}

	public void postaviEAN(String ean) {
		EAN = ean;
	}

	public void postaviMjeru(String mjera) {
		mjera = mjera;
	}

	public void postaviCijenu(String cij) {
		cijena = cij;
	}

	public void postaviMLPcijenu(String mlpCij) {
		cijena_mlp = mlpCij;
	}

	public void postaviDugme(String dugme) {
		dugme = dugme;
	}

	public void postaviIDprogram(String idProgram) {
		id_program = idProgram;
	}

	public void postaviKolicinu(String kol) {
		kolicina = kol;
	}

	public void postaviOsnovicu(String osn) {
		osnovica = osn;
	}

	public double dajNarucenuKolicinu() {
		return narucenaKolicina;
	}

	public boolean dajJeLiIzabran() {
		return izabran;
	}

	public void izaberiProizvod(boolean b) {
		izabran = b;
	}

	public int dajRedniBrojStavke() {
		return redniBroj;
	}

	public void postaviRedniBroj(int r) {
		redniBroj = r;
	}

	public void postaviPopust(double p) {
		this.popust = p;
	}
	
	public void postaviRabatKupca(double rabat){
		this.rabatKupca=rabat;
	}

	public void postaviVrijednostPopusta(double p) {
		this.ukupniPopustNaStavku = p;
	}

	public String dajUkupnuVrijednostPopusta() {
		DecimalFormat df = new DecimalFormat("0.000");
		return df.format(ukupniPopustNaStavku);
	}

	public double dajUkupniPopust() {
		return ukupniPopustNaStavku;
	}

	public double dajIznosMPL() {
		double vrati = 0;
		//vrati = Double.valueOf(cijena_mlp) * narucenaKolicina;
		vrati=Double.valueOf(dajOsnovicuMLP())*narucenaKolicina;
		return vrati;
	};

	public double dajIznosVeleprodaja() {
		double vrati = 0;
		//vrati = Double.valueOf(cijena) * narucenaKolicina;
		vrati=Double.valueOf(dajOsnovicuVLP())*narucenaKolicina;
		return vrati;
	};

	public void postaviNarucenuKolicinu(double kol) {
		narucenaKolicina = kol;
	}

	public String dajOsnovicuMLP() {
		Log.d("TAG", "MLP Cijena: " + cijena_mlp);
		double osn=0.00;
		osn= (narucenaKolicina*Double.valueOf(cijena_mlp)-ukupniPopustNaStavku)/narucenaKolicina;

		return String.format("%.2f", osn);
	}
	
	public String dajOsnovicuVLP() {
		double osn=0.00;
		osn= (narucenaKolicina*Double.valueOf(cijena)-ukupniPopustNaStavku)/narucenaKolicina;

		return String.format("%.2f", osn);
	}
}
