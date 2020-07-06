package org.comp.klase;

import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Vector;

import org.comp.aktivnosti.OrdroidActivity;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.thoughtworks.xstream.XStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

public class WebServis {

	private static String NAMESPACE = "http://tempuri.org/";
	private static String URL1 = "http://";
	//private static String URL3 = "/WSOrderCom/Service1.asmx";
	private static String URL3 = "/WSOrderCom/Service1.asmx";
	public static Context cont;

	// metoda koja daje listu svih firmi sa servera
	public static ArrayList<Kupac> DajKupce(String ipAdresa, String korIme,
			String program) {
		String dajKupce = "DajKupce";
		String akcijaDajKupce = "http://tempuri.org/DajKupce";
		ArrayList<Kupac> kupci = new ArrayList<Kupac>();
		try {
			SoapObject request = new SoapObject(NAMESPACE, dajKupce);
			request.addProperty("userID", korIme);
			request.addProperty("orderTypeID", program);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1
					+ ipAdresa + URL3);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(akcijaDajKupce, envelope);
			KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
			SoapObject ss1 = (SoapObject) ks.getProperty(0);
			for (int i = 0; i < ss1.getPropertyCount(); i++) {
				SoapObject ss = (SoapObject) ss1.getProperty(i);
				Kupac k = new Kupac(ss.getProperty(0).toString(), ss
						.getProperty(1).toString(), ss.getProperty(2)
						.toString(), ss.getProperty(3).toString(), ss
						.getProperty(4).toString(), ss.getProperty(5)
						.toString(), ss.getProperty(6).toString(), ss
						.getProperty(7).toString(), ss.getProperty(8)
						.toString(), ss.getProperty(9).toString(), ss
						.getProperty(10).toString(), ss.getProperty(11)
						.toString(), ss.getProperty(12).toString(), ss
						.getProperty(13).toString(), ss.getProperty(14)
						.toString(), ss.getProperty(15).toString());
				kupci.add(k);
			}
		} catch (Exception e) {
			Log.e("Greška:  " + e.toString().toString() + "\n\n" + "Poruka:  "
					+ e.getMessage(), "sad");
			;
		}
		return kupci;
	}

	// metoda koja daje listu svih lokacija za odredjeni prodajni program sa
	// servera
	public static ArrayList<Lokacija> DajLokacije(String ipAdresa,
			String korIme, String program) {
		String dajLokacije = "DajLokacije";
		String akcijaDajLokacije = "http://tempuri.org/DajLokacije";
		ArrayList<Lokacija> lokacije = new ArrayList<Lokacija>();
		try {
			SoapObject request = new SoapObject(NAMESPACE, dajLokacije);
			request.addProperty("userID", korIme);
			request.addProperty("orderTypeID", program);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1
					+ ipAdresa + URL3);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(akcijaDajLokacije, envelope);
			KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
			SoapObject ss1 = (SoapObject) ks.getProperty(0);
			for (int i = 0; i < ss1.getPropertyCount(); i++) {
				SoapObject ss = (SoapObject) ss1.getProperty(i);
				ArrayList<String> podaci = new ArrayList<String>();
				for (int k = 0; k < ss.getPropertyCount(); k++) {
					if (!ss.getProperty(k).toString().equals("anyType{}"))
						podaci.add(ss.getProperty(k).toString());
					else
						podaci.add("");
				}
				Lokacija l = new Lokacija(podaci.get(0), podaci.get(1),
						podaci.get(2), podaci.get(3), podaci.get(4),
						podaci.get(5), podaci.get(6), podaci.get(7),
						podaci.get(8), podaci.get(9), podaci.get(10),
						podaci.get(11), podaci.get(12));
				lokacije.add(l);
			}
		} catch (Exception e) {
			Log.e("Greška:  " + e.toString().toString() + "\n\n" + "Poruka:  "
					+ e.getMessage(), "sad");
			;
		}
		return lokacije;
	}

	// metoda koja daje listu svih proizvoda sa akcijskim popustima sa
	// servera
	public static ArrayList<AkcijskiPopusti> DajAkcijskePopuste(String ipAdresa) {
		String dajPopuste = "DajAkcijskePopuste";
		String akcijaDajPopuste = "http://tempuri.org/DajAkcijskePopuste";
		ArrayList<AkcijskiPopusti> popusti = new ArrayList<AkcijskiPopusti>();
		try {
			SoapObject request = new SoapObject(NAMESPACE, dajPopuste);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1
					+ ipAdresa + URL3);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(akcijaDajPopuste, envelope);
			KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
			SoapObject ss1 = (SoapObject) ks.getProperty(0);
			for (int i = 0; i < ss1.getPropertyCount(); i++) {
				SoapObject ss = (SoapObject) ss1.getProperty(i);
				AkcijskiPopusti p = new AkcijskiPopusti(ss.getProperty(0)
						.toString(), ss.getProperty(1).toString(), ss
						.getProperty(2).toString(), ss.getProperty(3)
						.toString(), ss.getProperty(4).toString(), ss
						.getProperty(5).toString(), ss.getProperty(6)
						.toString());
				popusti.add(p);
			}
		} catch (Exception e) {
			Log.e("Greška:  " + e.toString().toString() + "\n\n" + "Poruka:  "
					+ e.getMessage(), "sad");
			;
		}
		return popusti;
	}

	// metoda koja daje listu svih proizvoda za odredjeni prodajni program sa
	// servera
	public static ArrayList<Proizvod> DajProizvode(String ipAdresa,
			String program) {
		String dajProizvode = "DajProizvode";
		String akcijaDajProizvode = "http://tempuri.org/DajProizvode";
		ArrayList<Proizvod> proizvodi = new ArrayList<Proizvod>();
		try {
			SoapObject request = new SoapObject(NAMESPACE, dajProizvode);
			request.addProperty("OrderTypeID", program);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1
					+ ipAdresa + URL3);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(akcijaDajProizvode, envelope);
			KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
			SoapObject ss1 = (SoapObject) ks.getProperty(0);
			for (int i = 0; i < ss1.getPropertyCount(); i++) {
				SoapObject ss = (SoapObject) ss1.getProperty(i);
				ArrayList<String> podaci = new ArrayList<String>();
				for (int k = 0; k < ss.getPropertyCount(); k++) {
					if (!ss.getProperty(k).toString().equals("anyType{}"))
						podaci.add(ss.getProperty(k).toString());
					else
						podaci.add("");
				}
				Proizvod p = new Proizvod(podaci.get(0), podaci.get(1),
						podaci.get(2), podaci.get(3), podaci.get(4),
						podaci.get(5), podaci.get(6), Double.valueOf(podaci
								.get(7)), podaci.get(8), podaci.get(9),
						podaci.get(10), podaci.get(11));
				proizvodi.add(p);
			}
		} catch (Exception e) {
			Log.e("Greška:  " + e.toString().toString() + "\n\n" + "Poruka:  "
					+ e.getMessage(), "sad");
			;
		}
		return proizvodi;
	}

	// metoda koja daje listu svih prodajnih programa sa servera
	public static ArrayList<Program> DajPrograme(String ipAdresa, String korIme) {
		String dajGrupe = "DajPrograme";
		String akcijaDajGrupe = "http://tempuri.org/DajPrograme";
		ArrayList<Program> grupe = new ArrayList<Program>();
		try {
			SoapObject request = new SoapObject(NAMESPACE, dajGrupe);
			request.addProperty("userID", korIme);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1
					+ ipAdresa + URL3);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(akcijaDajGrupe, envelope);
			KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
			SoapObject ss1 = (SoapObject) ks.getProperty(0);
			for (int i = 0; i < ss1.getPropertyCount(); i++) {
				SoapObject ss = (SoapObject) ss1.getProperty(i);
				Program g = new Program(ss.getProperty(0).toString(), ss
						.getProperty(1).toString());
				grupe.add(g);
			}
		} catch (Exception e) {
			Log.e("Greška:  " + e.toString().toString() + "\n\n" + "Poruka:  "
					+ e.getMessage(), "sad");
			;
		}
		return grupe;
	}

	// metoda koja daje listu svih grupa prodajnih programa sa servera
	public static ArrayList<Grupa> DajGrupe(String ipAdresa, String idPrograma) {
		String dajGrupe = "DajGrupe";
		String akcijaDajGrupe = "http://tempuri.org/DajGrupe";
		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		try {
			SoapObject request = new SoapObject(NAMESPACE, dajGrupe);
			request.addProperty("orderTypeID", idPrograma);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1
					+ ipAdresa + URL3);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(akcijaDajGrupe, envelope);
			KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
			SoapObject ss1 = (SoapObject) ks.getProperty(0);
			for (int i = 0; i < ss1.getPropertyCount(); i++) {
				SoapObject ss = (SoapObject) ss1.getProperty(i);
				Grupa g = new Grupa(ss.getProperty(0).toString(), ss
						.getProperty(1).toString(), ss.getProperty(2)
						.toString(), ss.getProperty(3).toString());
				grupe.add(g);
			}
		} catch (Exception e) {
			Log.e("Greška:  " + e.toString().toString() + "\n\n" + "Poruka:  "
					+ e.getMessage(), "sad");
			;
		}
		return grupe;
	}
	
	// metoda koja daje listu svih grupa prodajnih programa sa servera
	public static ArrayList<Rok_placanja> DajRokovePlacanja(String ipAdresa, String korIme,
				String program) {
			String dajRokovePlacanja = "DajRokovePlacanja";
			String akcijaDajRokovePlacanja = "http://tempuri.org/DajRokovePlacanja";
			ArrayList<Rok_placanja> rokoviPlacanja = new ArrayList<Rok_placanja>();
			try {
				SoapObject request = new SoapObject(NAMESPACE, dajRokovePlacanja);
				request.addProperty("userID", korIme);
				request.addProperty("orderTypeID", program);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1
						+ ipAdresa + URL3);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(akcijaDajRokovePlacanja, envelope);
				KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
				SoapObject ss1 = (SoapObject) ks.getProperty(0);
				for (int i = 0; i < ss1.getPropertyCount(); i++) {
					SoapObject ss = (SoapObject) ss1.getProperty(i);
					Rok_placanja rp = new Rok_placanja(ss.getProperty(0).toString(),ss.getProperty(1).toString(), ss.getProperty(2).toString(), ss.getProperty(3).toString());
					rokoviPlacanja.add(rp);
				}
			} catch (Exception e) {
				Log.e("Greška:  " + e.toString().toString() + "\n\n" + "Poruka:  "
						+ e.getMessage(), "sad");
				;
			}
			return rokoviPlacanja;
		}
	
	

	// metoda koja daje listu svih prodajnih programa sa servera
	public static Korisnik ProvjeriKorisnika(String ipAdresa, String username,
			String password) {
		String provjeriKoris = "ProvjeriKorisnika";
		String akcijaprovjeriKorisnika = "http://tempuri.org/ProvjeriKorisnika";
		Korisnik k = null;
		try {
			SoapObject request = new SoapObject(NAMESPACE, provjeriKoris);
			request.addProperty("userID", username);
			request.addProperty("password", password);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1
					+ ipAdresa + URL3);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(akcijaprovjeriKorisnika, envelope);
			KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
			SoapObject ss = (SoapObject) ks.getProperty(0);
			k = new Korisnik(ss.getProperty(0).toString(), ss.getProperty(1)
					.toString(), ss.getProperty(2).toString(), ss
					.getProperty(3).toString(), ss.getProperty(4).toString());
		} catch (Exception e) {
			Log.e("Greška:  " + e.toString().toString() + "\n\n" + "Poruka:  "
					+ e.getMessage(), "sad");
			;
		}
		return k;
	}

	// metoda koja daje listu svih grupa prodajnih programa sa servera
	@SuppressLint("LongLogTag")
	public static ArrayList<Integer> posaljiNarudzbu(String ipAdresa,
			ArrayList<Narudzba> narudzbe, String korIme) {
		BazaPodataka baza;
		baza = new BazaPodataka(cont);
		ArrayList<Integer> vrati = new ArrayList<Integer>();
		String posaljiNarudzbu = "PosaljiNarudzbu";
		String akcijaPosaljiNarudzbu = "http://tempuri.org/PosaljiNarudzbu";
		DecimalFormat df = new DecimalFormat("0.000");
		XStream xstream = new XStream();
		xstream.alias("stavka", Stavke.class);
		xstream.alias("stavke", ListaStavki.class);
		xstream.addImplicitCollection(ListaStavki.class, "list");
		for (int k = 0; k < narudzbe.size(); k++)
			vrati.add(0);
		for (int i = 0; i < narudzbe.size(); i++) {
			ArrayList<Stavke> stavkeNarudzbe = new ArrayList<Stavke>();
			ArrayList<Proizvod> proizvodi = new ArrayList<Proizvod>();
			proizvodi = narudzbe.get(i).dajArtikleNarudzbe();
			for (int j = 0; j < proizvodi.size(); j++) {
				double cijena = 0;

				if (narudzbe.get(i).dajTipDokumenta().equals("Maloprodaja"))
					cijena = Double.valueOf(proizvodi.get(j).dajMLPcijenu());
				else
					cijena = Double.valueOf(proizvodi.get(j).dajCijenu());
				// stavkeNarudzbe.add(new
				// Stavke(proizvodi.get(j).dajRedniBrojStavke(),
				// proizvodi.get(j).dajSifru(),
				// String.valueOf(proizvodi.get(j).dajNarucenuKolicinu()),
				// df.format(cijena), "", "0", "0",
				// df.format(proizvodi.get(j).dajPopust())));
				stavkeNarudzbe.add(new Stavke(j + 1, proizvodi.get(j)
						.dajSifru(), String.valueOf(proizvodi.get(j)
						.dajNarucenuKolicinu()), df.format(cijena), "", "0.000",
						"0", df.format(proizvodi.get(j).dajPopust()), df
								.format(proizvodi.get(j).dajRabatKupca())));
				Log.e("df.format(proizvodi.get(j).dajRabatKupca()))",
						df.format(proizvodi.get(j).dajRabatKupca()));
			}

			try {
				String xml = xstream.toXML(stavkeNarudzbe);
				Log.e("XML stavke", xml);
				SoapObject request = new SoapObject(NAMESPACE, posaljiNarudzbu);
				request.addProperty("id", narudzbe.get(i).dajId());
				request.addProperty("brjori", "");
				request.addProperty("brjotp", "");
				request.addProperty("posmat", narudzbe.get(i).dajSkladiste());
				request.addProperty("primat", narudzbe.get(i).dajKupca()
						.dajSifru());
				request.addProperty(
						"primis",
						Integer.valueOf(narudzbe.get(i).dajLokaciju()
								.dajIdLokacije()));
				request.addProperty("datrea", narudzbe.get(i).dajVrijeme());
				request.addProperty("rokpla", narudzbe.get(i).dajRokPlacanja());
				request.addProperty("tiprac", 0);
				Log.e("PRIMIS ", narudzbe.get(i).dajLokaciju()
						.dajIdLokacijeKupca());
				if (narudzbe.get(i).dajTipDokumenta().equals("Maloprodaja"))
					request.addProperty("tipnar", 49);
				else
					request.addProperty("tipnar", 35);
				if (narudzbe.get(i).dajVrstuPlacanja().equals("Gotovina")) {
					if (narudzbe.get(i).dajTipDokumenta().equals("Maloprodaja"))
						request.addProperty("nacpla", 2);
					else
						request.addProperty("nacpla", 3);
				} else
					request.addProperty("nacpla", 1);
				request.addProperty("vriskl", String.format("%.2f", narudzbe
						.get(i).dajUkupanIznosNarudzbe()));
				request.addProperty("indskl", 0);
				request.addProperty("datdok", narudzbe.get(i).dajVrijeme());
				request.addProperty("usenam", korIme);
				request.addProperty("rabkup", 0);
				request.addProperty("xml", xml);
				request.addProperty("narkup", narudzbe.get(i)
						.DajOrginalniBroj());
				request.addProperty("brojStavki", stavkeNarudzbe.size());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				if (OrdroidActivity.imaLiKonekcije(cont)) {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							URL1 + ipAdresa + URL3);
					androidHttpTransport.debug = true;
					androidHttpTransport.call(akcijaPosaljiNarudzbu, envelope);
					KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
					int rezultat;
					rezultat = Integer.parseInt(ks.getProperty(0).toString());
					if (rezultat == 1)
						baza.promjeniStatusNarudzbeUPoslata(narudzbe.get(i));
					vrati.set(i, Integer.parseInt(ks.getProperty(0).toString()));
				} else
					break;
			} catch (Exception e) {
				Log.e("Greška:  " + e.toString().toString() + "\n\n"
						+ "Poruka:  " + e.getMessage(), "sad");
				;
			}
		}
		return vrati;
	}
}
