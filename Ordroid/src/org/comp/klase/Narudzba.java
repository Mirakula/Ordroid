package org.comp.klase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Narudzba {
	private ArrayList<Proizvod> proizvodi;
	private String stanjeNarudzbe;
	private String vrijeme;
	private boolean izabrana;
	private Kupac kupac;
	private Lokacija lokacija;
	private String skladiste;
	private String nacinPlacanja;
	private String tipDokumenta;
	private Program program;
	private int rokPlacanja;
	private int id;
	private int redniBrojStavke;
	private String orginalniBroj;

	Narudzba(String stanje) {
		stanjeNarudzbe = stanje;
		proizvodi = new ArrayList<Proizvod>();
		izabrana = false;
		redniBrojStavke = 1;
	}

	public Narudzba(ArrayList<Proizvod> s, String stanje) {
		proizvodi = new ArrayList<Proizvod>();
		proizvodi = s;
		stanjeNarudzbe = stanje;
		izabrana = false;
		redniBrojStavke = s.size() + 1;
	};

	Narudzba(ArrayList<Proizvod> a, int i, Kupac k, Lokacija l,
			String skladiste, String tipdokumenta, String vrstaPlacanja,
			Program p, String s, int rok, String vrijeme) {
		proizvodi = new ArrayList<Proizvod>();
		proizvodi = a;
		id = i;
		kupac = k;
		program = p;
		lokacija = l;
		tipDokumenta = tipdokumenta;
		this.nacinPlacanja = vrstaPlacanja;
		this.skladiste = skladiste;
		stanjeNarudzbe = s;
		rokPlacanja = rok;
		izabrana = false;
		this.vrijeme = vrijeme;
		redniBrojStavke = a.size() + 1;
		;
	}

	public Narudzba(Kupac k, Lokacija l, String skladiste, String tipdokumenta,
			String vrstaPlacanja, Program p, String s, int rok) {
		proizvodi = new ArrayList<Proizvod>();
		kupac = k;
		program = p;
		lokacija = l;
		tipDokumenta = tipdokumenta;
		nacinPlacanja = vrstaPlacanja;
		this.skladiste = skladiste;
		stanjeNarudzbe = s;
		rokPlacanja = rok;
		izabrana = false;
		redniBrojStavke = 1;
	}

	public Narudzba(Narudzba n) {
		proizvodi = new ArrayList<Proizvod>();
		proizvodi = n.dajArtikleNarudzbe();
		id = n.dajId();
		kupac = n.dajKupca();
		lokacija = n.dajLokaciju();
		program = n.dajProgram();
		tipDokumenta = n.dajTipDokumenta();
		nacinPlacanja = n.dajVrstuPlacanja();
		rokPlacanja = n.dajRokPlacanja();
		skladiste = n.dajSkladiste();
		stanjeNarudzbe = n.dajStanjeNarudzbe();
		izabrana = false;
		vrijeme = n.dajVrijeme();
		redniBrojStavke = proizvodi.size() + 1;
		orginalniBroj = n.DajOrginalniBroj();
	}

	public String dajStanjeNarudzbe() {
		return stanjeNarudzbe;
	}

	public Kupac dajKupca() {
		return kupac;
	}

	public int dajRokPlacanja() {
		return rokPlacanja;
	}

	public Program dajProgram() {
		return program;
	}

	public Lokacija dajLokaciju() {
		return lokacija;
	}

	public String dajVrstuPlacanja() {
		return nacinPlacanja;
	}

	public String dajTipDokumenta() {
		return tipDokumenta;
	}

	public String dajSkladiste() {
		return skladiste;
	}

	public int dajId() {
		return id;
	}

	public String dajVrijeme() {
		return vrijeme;
	}

	public void izaberiNarudzbu(boolean b) {
		izabrana = b;
	}

	public boolean dajJeLiIzabrana() {
		return izabrana;
	}

	public int jeLiVecDodat(Proizvod a) {
		int vrati = -1;
		for (int i = 0; i < proizvodi.size(); i++) {
			if (proizvodi.get(i).dajSifru().equals(a.dajSifru())) {
				vrati = i;
				break;
			}
		}
		return vrati;
	}

	public void izbrisiArtikl(String sifra) {
		for (int i = 0; i < proizvodi.size(); i++) {
			if (proizvodi.get(i).dajSifru().equals(sifra)) {
				proizvodi.remove(i);
				redniBrojStavke -= 1;
				// for(int j=i; j<proizvodi.size(); j++)
				// proizvodi.get(j).postaviRedniBroj(proizvodi.get(j).dajRedniBrojStavke()-1);
				break;
			}
		}
	}

	public String DajOrginalniBroj() {
		return this.orginalniBroj;
	}

	public void postaviOrginalniBroj(String broj) {
		this.orginalniBroj = broj;
	}

	public void dodajArtikl(Proizvod a) {
		int j = jeLiVecDodat(a);
		if (j != -1) {
			izbrisiArtikl(a.dajSifru());
			a.postaviRedniBroj(redniBrojStavke);
			proizvodi.add(a);
			redniBrojStavke += 1;
		} else {
			a.postaviRedniBroj(redniBrojStavke);
			proizvodi.add(a);
			redniBrojStavke += 1;
		}
		if (a.dajNarucenuKolicinu() == 0)
			izbrisiArtikl(a.dajSifru());
	}

	public ArrayList<Proizvod> dajArtikleNarudzbe() {
		return proizvodi;
	}

	public Proizvod dajArtikl(String sifra) {
		Proizvod p = null;
		for (int i = 0; i < proizvodi.size(); i++) {
			if (proizvodi.get(i).dajSifru().equals(sifra))
				p = proizvodi.get(i);
		}
		return p;
	}

	public double dajUkupanIznosNarudzbe() {
		double d = 0;
		DecimalFormat df = new DecimalFormat("0.000");
		if (tipDokumenta.equals("Maloprodaja")) {
			for (int i = 0; i < proizvodi.size(); i++)
				d += proizvodi.get(i).dajIznosMPL();
		} else {
			for (int i = 0; i < proizvodi.size(); i++)
				d += proizvodi.get(i).dajIznosVeleprodaja();
			d += dajPDV_VLP();
		}
		// return d-dajUkupanPopustNarudzbe();
		return d;
	}

	public double dajUkupanIznosNarudzbeBezPDVa() {
		double d = 0;
		DecimalFormat df = new DecimalFormat("0.000");
		for (int i = 0; i < proizvodi.size(); i++)
			d += proizvodi.get(i).dajIznosVeleprodaja();
		// return d-dajUkupanPopustNarudzbe();
		return d;
	}

	public String dajUkupnuVrijednostNarudzbe() {
		double d = 0;
		DecimalFormat df = new DecimalFormat("0.000");
		if (tipDokumenta.equals("Maloprodaja")) {
			for (int i = 0; i < proizvodi.size(); i++)
				d += proizvodi.get(i).dajIznosMPL();
		} else {
			for (int i = 0; i < proizvodi.size(); i++)
				d += proizvodi.get(i).dajIznosVeleprodaja();
		}
		return String.format("%.2f", d);
	}

	public double dajUkupanPopustNarudzbe() {
		double d = 0;
		DecimalFormat df = new DecimalFormat("0.000");
		for (int i = 0; i < proizvodi.size(); i++)
			d += proizvodi.get(i).dajUkupniPopust();
		return d;
	}

	public String dajPDV() {
		DecimalFormat df = new DecimalFormat("0.000");
		double pdv;
		if (tipDokumenta.equals("Maloprodaja")) {
			pdv = dajUkupanIznosNarudzbe() * 17 / 117;
		} else
			pdv = dajUkupanIznosNarudzbeBezPDVa() * 0.17;
		return String.format("%.2f", pdv);
	}

	public double dajPDV_VLP() {
		DecimalFormat df = new DecimalFormat("0.000");
		double pdv;
		pdv = dajUkupanIznosNarudzbeBezPDVa() * 0.17;
		return pdv;
	}

	public void postaviMIS(Lokacija l) {
		this.lokacija = l;
	}

	public void postaviSkladiste(String sklad) {
		this.skladiste = sklad;
	}

	public void postaviRokPlacanja(int rok) {
		this.rokPlacanja = rok;
	}
}
