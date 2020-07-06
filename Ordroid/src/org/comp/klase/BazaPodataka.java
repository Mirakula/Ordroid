/*
 * Ova klasa sluzi za komunikaciju sa bazom odataka na samom uredjaju (SQLITE)
 */
package org.comp.klase;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.comp.aktivnosti.OrdroidActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

public class BazaPodataka extends SQLiteOpenHelper {

	// putanja do baze podataka na uredjaju
	private static final String DB_PATH = "/data/data/org.comp.paket/databases/";

	// naziv baze podataka na uredjaju
	private static final String DB_NAME = "komerdroid.db";
	private static final int DATABASE_VERSION = 15;
	private final Context myContext;

	public BazaPodataka(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}

	// metoda koja se poziva prilikom kreiranja baze
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE Logovanje (_id INTEGER PRIMARY KEY AUTOINCREMENT, korisnicko_ime TEXT, lozinka TEXT);");
			db.execSQL("CREATE TABLE Korisnik (_id INTEGER PRIMARY KEY AUTOINCREMENT, sifra TEXT, ime_prezime TEXT, korisnicko_ime TEXT, lozinka TEXT, lokacija TEXT);");
			db.execSQL("CREATE TABLE Postavke (_id INTEGER PRIMARY KEY AUTOINCREMENT, adresa TEXT, datum_zadnjeg_osvjezavanja_popusta TEXT);");
			db.execSQL("CREATE TABLE Kupci (_id INTEGER PRIMARY KEY AUTOINCREMENT, sifra TEXT, naziv TEXT, adresa TEXT, ZIP TEXT, mjesto TEXT, telefon TEXT, kupac_ID_broj TEXT, kupac_VAT_broj TEXT, ukupni_kredit TEXT, dospjeli_kredit TEXT, posljednja_uplata TEXT, prihod TEXT, broj_racuna TEXT, kupac_sud_broj TEXT, osnovni_popust TEXT, id_program TEXT);");
			db.execSQL("CREATE TABLE Grupe (_id INTEGER PRIMARY KEY AUTOINCREMENT, sifra TEXT, skracenica TEXT, dugme TEXT, id_program TEXT);");
			db.execSQL("CREATE TABLE Proizvodi (_id INTEGER PRIMARY KEY AUTOINCREMENT, sifra TEXT, id_grupa TEXT, naziv TEXT, EAN TEXT, mjera TEXT, cijena TEXT, cijena_mlp TEXT, popust TEXT, dugme TEXT, id_program TEXT, kolicina TEXT, kupdob TEXT);");
			db.execSQL("CREATE TABLE Programi (_id INTEGER PRIMARY KEY AUTOINCREMENT, sifra TEXT, naziv TEXT, aktivan TEXT);");
			db.execSQL("CREATE TABLE Finansije (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_kupac TEXT, ukupni_kredit TEXT, uplaceni_kredit TEXT, zadnja_uplata TEXT, prihod TEXT);");
			db.execSQL("CREATE TABLE narudzbe (id INTEGER PRIMARY KEY AUTOINCREMENT, brjori TEXT, brjotp TEXT, posmat TEXT, primat TEXT, primis TEXT, datrea TEXT, nacpla TEXT, valpla TEXT, rokpla TEXT, tiprac TEXT, tipnar TEXT, demdin TEXT, vriskl TEXT, indskl TEXT, staslg TEXT, datdok TEXT, usenam TEXT, radmod TEXT, rabkup TEXT, izvdok TEXT, siflok TEXT, tip TEXT, id_program TEXT, narkup TEXT);");
			db.execSQL("CREATE TABLE stavke (id INTEGER PRIMARY KEY AUTOINCREMENT, redbrj TEXT, sifmat TEXT, kolmat TEXT, cijmat TEXT, sifpro TEXT, brjcij TEXT, tipmts TEXT, staslg TEXT, rabsta TEXT, siflok TEXT, id_narudzba INTEGER, popust TEXT, rabat TEXT);");
			db.execSQL("CREATE TABLE Popusti (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_kupca TEXT, popust TEXT);");
			db.execSQL("CREATE TABLE Akcijski_popusti (_id INTEGER PRIMARY KEY AUTOINCREMENT, sifmat TEXT, kupdob TEXT, sifpre TEXT, primis TEXT, datcij TEXT, datpre TEXT, rabsta TEXT);");
			db.execSQL("CREATE TABLE Lokacije (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_lokacija_kupca TEXT, id_kupca TEXT, id_lokacije TEXT, naziv_lokacije TEXT, adresa_lokacije TEXT, ZIP_lokacije TEXT, mjesto_lokacije TEXT, ime_kontakt_osobe TEXT, telefon_kontakt_osobe TEXT, tip_lokacije TEXT, trgovina_na_malo_broj TEXT, novac_broj TEXT, id_program TEXT);");
			db.execSQL("CREATE TABLE Rok_Placanja (_id INTEGER PRIMARY KEY AUTOINCREMENT, nr_brjtin TEXT, ba_kupdob TEXT, nr_rabkup DOUBLE, nr_rokpla TEXT);");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Greška", "Kreiranje");
		}
	}

	// metoda koja se poziva prilikom nadogradnje baze
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("UPDATE", "updating baze");
		db.execSQL("DROP TABLE IF EXISTS Korisnik;");
		db.execSQL("DROP TABLE IF EXISTS Postavke;");
		db.execSQL("DROP TABLE IF EXISTS Kupci;");
		db.execSQL("DROP TABLE IF EXISTS Proizvodi;");
		db.execSQL("DROP TABLE IF EXISTS Programi;");
		db.execSQL("DROP TABLE IF EXISTS Finansije;");
		db.execSQL("DROP TABLE IF EXISTS Popusti;");
		db.execSQL("DROP TABLE IF EXISTS Grupe;");
		db.execSQL("DROP TABLE IF EXISTS Lokacije;");
		db.execSQL("DROP TABLE IF EXISTS narudzbe;");
		db.execSQL("DROP TABLE IF EXISTS stavke;");
		db.execSQL("DROP TABLE IF EXISTS Logovanje;");
		db.execSQL("DROP TABLE IF EXISTS Akcijski_popusti;");
		db.execSQL("DROP TABLE IF EXISTS Rok_Placanja;");
		onCreate(db);
	}

	public void postaviIpAdresuServisa(String ip) {
		getWritableDatabase().delete("Postavke", "1=1", null);
		ContentValues cv = new ContentValues();
		cv.put("adresa", ip);
		Log.e("ip je", ip);
		getWritableDatabase().insert("Postavke", null, cv);
		close();
	}

	public void postaviDatumOsvjezavanjaPopusta() {
		Calendar vrijeme = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String datum = formatter.format(vrijeme.getTime());
		ContentValues cv = new ContentValues();
		cv.put("datum_zadnjeg_osvjezavanja_popusta", datum);
		getWritableDatabase().update("Postavke", cv, "1=1", null);
		close();
	}

	public String dajAkcijskiPopust(String proizvod, String kupac, String mis,
			String kupdob) {
		String popust = "0.00";
		Cursor cursor = getWritableDatabase()
				.query("Akcijski_popusti",
						new String[] { "rabsta", "datcij", "datpre" },
						"(sifmat=? OR sifmat=0) AND (sifpre=? OR sifpre=0) AND (primis=? OR primis=0) AND (kupdob=? OR kupdob=0)",
						new String[] { proizvod, kupac, mis, kupdob }, null,
						null, null);
		if (cursor.moveToFirst()) {
			do {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				java.util.Date datumCij = null;
				java.util.Date datumPre = null;
				java.util.Date danasnji = null;
				try {

					datumCij = formatter.parse(cursor.getString(1));
					datumPre = formatter.parse(cursor.getString(2));
					java.util.Date danas = Calendar.getInstance().getTime();
					danas.setHours(0);
					danas.setMinutes(0);
					danas.setSeconds(0);
					danasnji = danas;
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if ((danasnji.after(datumCij) || formatter.format(danasnji)
						.equals(formatter.format(datumCij)))
						&& (danasnji.before(datumPre) || formatter.format(
								danasnji).equals(formatter.format(datumPre)))) {
					int duzinaRabata = cursor.getString(0).length();
					popust = cursor.getString(0).substring(0, duzinaRabata - 1);
				}
			} while (cursor.moveToNext());
		} else
			popust = "0.00";
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return popust;
	}

	public boolean jeLiLogovan() {
		// openDataBase();
		boolean b = false;
		Cursor cursor = getWritableDatabase().rawQuery(
				"SELECT * FROM Logovanje", null);
		if (cursor.moveToFirst()) {
			b = true;
		} else
			b = false;
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return b;
	}

	public String dajKorisnickoIme() {
		// openDataBase();
		String vrati = "";
		Cursor cursor = getWritableDatabase().rawQuery(
				"SELECT korisnicko_ime FROM Logovanje", null);
		if (cursor.moveToFirst()) {
			vrati = cursor.getString(0);
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return vrati;
	}

	public void Logovanje(String username, String sifra) {
		if (!username.equals("admin") && !sifra.equals("postavke")) {
			getWritableDatabase().delete("Logovanje", "1=1", null);
			ContentValues cv = new ContentValues();
			cv.put("korisnicko_ime", username);
			cv.put("lozinka", sifra);
			getWritableDatabase().insert("Logovanje", null, cv);
			close();
		}
	}

	public void Odjava() {
		getWritableDatabase().delete("Logovanje", "1=1", null);
		close();
	}

	public void deleteNarudzba(int id) {
		getWritableDatabase().delete("stavke", "id_narudzba=?",
				new String[] { String.valueOf(id) });
		getWritableDatabase().delete("narudzbe", "id=?",
				new String[] { String.valueOf(id) });
		close();
	}

	public void insertNarudzba(Narudzba n, String tip) {
		DecimalFormat df = new DecimalFormat("0.000");
		try {
			// Log.e("RABAT U BAZI",
			// n.dajKupca().dajNaziv()+" "+n.dajKupca().dajOsnovniPopust());
			// openDataBase();
			String korIme = "";
			korIme = dajKorisnickoIme();
			ContentValues cv = new ContentValues();
			cv.put("nacpla", n.dajVrstuPlacanja());
			cv.put("usenam", korIme);
			cv.put("vriskl", df.format(n.dajUkupanIznosNarudzbe()));
			cv.put("rokpla", String.valueOf(n.dajRokPlacanja()));
			Calendar vrijeme = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss");
			String dateNow = formatter.format(vrijeme.getTime());
			cv.put("datrea", dateNow);
			cv.put("datdok", dateNow);
			cv.put("tiprac", n.dajTipDokumenta());
			cv.put("tipnar", n.dajTipDokumenta());
			cv.put("staslg", "1");
			cv.put("rabkup", n.dajKupca().dajOsnovniPopust());
			cv.put("valpla", "KM");
			cv.put("primat", n.dajKupca().dajSifru());
			cv.put("posmat", n.dajSkladiste());
			cv.put("primis", n.dajLokaciju().dajIdLokacijeKupca());
			cv.put("demdin", "1.0000");
			cv.put("radmod", "RUR");
			cv.put("indskl", "7");
			cv.put("tip", tip);
			cv.put("brjori", "a");
			cv.put("brjotp", "a");
			cv.put("izvdok", "a");
			cv.put("siflok", "a");
			cv.put("id_program", n.dajProgram().dajSifru());
			cv.put("narkup", n.DajOrginalniBroj());
			getWritableDatabase().insert("narudzbe", null, cv);
		} catch (SQLException e3) {
			throw e3;
		} finally {
			close();
		}
	}

	public void deleteZakljuceneNarudzbe(int id) {
		getWritableDatabase().delete("stavke", "id_narudzba=?",
				new String[] { String.valueOf(id) });
		getWritableDatabase().delete("narudzbe", "id=?",
				new String[] { String.valueOf(id) });
		close();
	}

	public void insertStavkiNarudzbe(ArrayList<Proizvod> stavke,
			boolean maloprodaja) {
		String narudzbaId = "";
		narudzbaId = dajIdNarudzbe();
		try {
			DecimalFormat df = new DecimalFormat("0.000");
			DecimalFormat dk = new DecimalFormat("0.00");
			for (int i = 0; i < stavke.size(); i++) {
				double iznos = 0;
				String iznosString = df.format(iznos);
				ContentValues cv = new ContentValues();
				double cijena = 0;
				if (maloprodaja) {
					iznos = stavke.get(i).dajIznosMPL();
					cijena = Double.valueOf(stavke.get(i).dajMLPcijenu());
				} else {
					iznos = stavke.get(i).dajIznosVeleprodaja();
					cijena = Double.valueOf(stavke.get(i).dajCijenu());
				}

				cv.put("redbrj",
						String.valueOf(stavke.get(i).dajRedniBrojStavke()));
				cv.put("sifmat", stavke.get(i).dajSifru());
				cv.put("cijmat", df.format(cijena));
				cv.put("kolmat", dk.format(stavke.get(i).dajNarucenuKolicinu()));
				cv.put("staslg", "1");
				cv.put("brjcij", "1");
				cv.put("rabsta", stavke.get(i).dajPopust());
				cv.put("tipmts", "");
				cv.put("sifpro", "");
				cv.put("siflok", "");
				cv.put("popust", df.format(stavke.get(i).dajUkupniPopust()));
				cv.put("rabat", df.format(stavke.get(i).dajRabatKupca()));
				cv.put("id_narudzba", narudzbaId);
				getWritableDatabase().insert("stavke", null, cv);
			}
		} catch (SQLException e3) {
			throw e3;
		} finally {
			close();
		}
	}

	public ArrayList<Narudzba> dajNarudzbe(String tip) {
		DecimalFormat df = new DecimalFormat("0.000");
		ArrayList<Narudzba> narudzbe = new ArrayList<Narudzba>();
		ArrayList<Proizvod> stavke = null;
		Calendar vrijeme = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String dateNow = formatter.format(vrijeme.getTime());
		String komercijalista = dajKorisnickoIme();
		try {
			Cursor cursor = getWritableDatabase().query(
					"narudzbe",
					new String[] { "id", "brjori", "brjotp", "posmat",
							"primat", "primis", "datrea", "nacpla", "valpla",
							"rokpla", "tiprac", "tipnar", "demdin", "vriskl",
							"indskl", "staslg", "datdok", "usenam", "radmod",
							"rabkup", "izvdok", "siflok", "id_program", "tip",
							"narkup" }, "tip=? AND usenam=?",
					new String[] { tip, komercijalista }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					String idNarudzbe = cursor.getString(0);
					String tip_nar = cursor.getString(23);
					String datum = cursor.getString(6);
					String provjera = datum.substring(0, 10);
					if (!provjera.equals(dateNow)
							&& !tip_nar.equals("zakljucena")) {
						getWritableDatabase().delete("stavke", "id_narudzba=?",
								new String[] { idNarudzbe });
						getWritableDatabase().delete("narudzbe", "id=?",
								new String[] { idNarudzbe });
						continue;
					}
					Cursor cursor1 = getWritableDatabase().query(
							"stavke",
							new String[] { "redbrj", "sifmat", "kolmat",
									"cijmat", "sifpro", "brjcij", "tipmts",
									"staslg", "rabsta", "siflok", "popust",
									"rabat" }, "id_narudzba=?",
							new String[] { idNarudzbe }, null, null, null);
					stavke = new ArrayList<Proizvod>();
					if (cursor1.moveToFirst()) {
						do {
							String cijena = "";
							boolean mlp = false;
							if (cursor.getString(10).equals("Maloprodaja"))
								mlp = true;
							Proizvod ppp = new Proizvod(cursor1.getInt(0), cursor1.getString(1), cursor1.getDouble(2),	cursor1.getString(3), cursor1.getDouble(8),	mlp);
							Proizvod ppp1 = dajProizvod(cursor1.getString(1));
							if (!mlp)
								ppp.postaviCijenu(ppp1.dajCijenu());
							else
								ppp.postaviMLPcijenu(ppp1.dajMLPcijenu());
								ppp.postaviDugme(ppp1.dajDugme());
								ppp.postaviEAN(ppp1.dajEAN());
								ppp.postaviIdGrupe(ppp1.dajIdGrupe());
								ppp.postaviIDprogram(ppp1.dajIDprogram());
								ppp.postaviKolicinu(ppp1.dajKolicinu());
								ppp.postaviMjeru(ppp1.dajMjeru());
								ppp.postaviNaziv(ppp1.dajNaziv());
								ppp.postaviVrijednostPopusta(cursor1.getDouble(10));
								ppp.postaviRabatKupca(cursor1.getDouble(11));
								stavke.add(ppp);
						} while (cursor1.moveToNext());
					}
					if (cursor1 != null && !cursor1.isClosed()) {
						cursor1.close();
					}
					Kupac kkk = dajKupcaSifra(cursor.getString(4),
							cursor.getString(22));
					if (cursor.getString(10).equals("Maloprodaja"))
						kkk.postaviRabat();
					Lokacija lll = dajLokacijuSifra(cursor.getString(5));
					Program ppp = dajProgramSifra(cursor.getString(22));
					Narudzba nar = new Narudzba(stavke, cursor.getInt(0), kkk,
							lll, cursor.getString(3), cursor.getString(10),
							cursor.getString(7), ppp, "otvorena",
							cursor.getInt(9), cursor.getString(6));
					nar.postaviOrginalniBroj(cursor.getString(24));
					narudzbe.add(nar);
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (SQLException e3) {
			throw e3;
		} finally {
			close();
		}
		if (narudzbe.size() == 0)
			return null;
		else
			return narudzbe;
	}

	public Narudzba dajNarudzbu(int id) {
		DecimalFormat df = new DecimalFormat("0.000");
		Narudzba narudzba = null;
		ArrayList<Proizvod> stavke = null;
		try {
			Cursor cursor = getWritableDatabase().query(
					"narudzbe",
					new String[] { "id", "brjori", "brjotp", "posmat",
							"primat", "primis", "datrea", "nacpla", "valpla",
							"rokpla", "tiprac", "tipnar", "demdin", "vriskl",
							"indskl", "staslg", "datdok", "usenam", "radmod",
							"rabkup", "izvdok", "siflok", "id_program",
							"narkup" }, "id=?",
					new String[] { String.valueOf(id) }, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					// String idNarudzbe=cursor.getString(0);
					Cursor cursor1 = getWritableDatabase().query(
							"stavke",
							new String[] { "redbrj", "sifmat", "kolmat",
									"cijmat", "sifpro", "brjcij", "tipmts",
									"staslg", "rabsta", "siflok", "popust",
									"rabat" }, "id_narudzba=?",
							new String[] { String.valueOf(id) }, null, null,
							null);
					stavke = new ArrayList<Proizvod>();
					if (cursor1.moveToFirst()) {
						do {
							String cijena = "";
							boolean mlp = false;
							if (cursor.getString(10).equals("Maloprodaja"))
								mlp = true;
							Proizvod ppp = new Proizvod(cursor1.getInt(0),
									cursor1.getString(1), cursor1.getDouble(2),
									cursor1.getString(3), cursor1.getDouble(8),
									mlp);
							Proizvod ppp1 = dajProizvod(cursor1.getString(1));
							if (!mlp)
								ppp.postaviCijenu(ppp1.dajCijenu());
							else
								ppp.postaviMLPcijenu(ppp1.dajMLPcijenu());
							ppp.postaviDugme(ppp1.dajDugme());
							ppp.postaviEAN(ppp1.dajEAN());
							ppp.postaviIdGrupe(ppp1.dajIdGrupe());
							ppp.postaviIDprogram(ppp1.dajIDprogram());
							ppp.postaviKolicinu(ppp1.dajKolicinu());
							ppp.postaviMjeru(ppp1.dajMjeru());
							ppp.postaviNaziv(ppp1.dajNaziv());
							ppp.postaviVrijednostPopusta(cursor1.getDouble(10));
							ppp.postaviRabatKupca(cursor1.getDouble(11));
							stavke.add(ppp);
						} while (cursor1.moveToNext());
					}
					if (cursor1 != null && !cursor1.isClosed()) {
						cursor1.close();
					}
					Kupac kkk = dajKupcaSifra(cursor.getString(4),
							cursor.getString(22));
					if (cursor.getString(10).equals("Maloprodaja"))
						kkk.postaviRabat();
					Lokacija lll = dajLokacijuSifra(cursor.getString(5));
					Program ppp = dajProgramSifra(cursor.getString(22));
					narudzba = new Narudzba(stavke, cursor.getInt(0), kkk, lll,
							cursor.getString(3), cursor.getString(10),
							cursor.getString(7), ppp, "otvorena",
							cursor.getInt(9), cursor.getString(6));
					narudzba.postaviOrginalniBroj(cursor.getString(23));
					break;
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (SQLException e3) {
			throw e3;
		} finally {
			close();
		}
		if (narudzba == null)
			return null;
		else
			return narudzba;
	}

	public void promjeniStatusNarudzbe(ArrayList<Narudzba> narudzbe) {

		try {
			for (int i = 0; i < narudzbe.size(); i++) {
				ContentValues cv = new ContentValues();
				cv.put("tip", "poslana");
				Calendar vrijeme = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				String dateNow = formatter.format(vrijeme.getTime());
				cv.put("datrea", dateNow);
				getWritableDatabase()
						.update("narudzbe",
								cv,
								"id=?",
								new String[] { String.valueOf(narudzbe.get(i)
										.dajId()) });
			}
		} catch (Exception e) {
			Log.e("greska promjena statusa", "greska");
		} finally {
			close();
		}
	}

	public void promjeniStatusNarudzbeUPoslata(Narudzba narudzba) {

		try {
			ContentValues cv = new ContentValues();
			cv.put("tip", "poslana");
			getWritableDatabase().update("narudzbe", cv, "id=?",
					new String[] { String.valueOf(narudzba.dajId()) });
		} catch (Exception e) {
			Log.e("greska promjena statusa", "greska");
		} finally {
			close();
		}
	}

	public ArrayList<Kupac> dajKupceZaProgram(String sifraPrograma) {
		// openDataBase();
		ArrayList<Kupac> kupci = new ArrayList<Kupac>();
		Cursor cursor = getWritableDatabase().query(
				"Kupci",
				new String[] { "sifra", "naziv", "adresa", "ZIP", "mjesto",
						"telefon", "kupac_ID_broj", "kupac_VAT_broj",
						"ukupni_kredit", "dospjeli_kredit",
						"posljednja_uplata", "prihod", "broj_racuna",
						"kupac_sud_broj", "osnovni_popust", "id_program" },
				"id_program=?", new String[] { sifraPrograma }, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				kupci.add(new Kupac(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3), cursor
								.getString(4), cursor.getString(5), cursor
								.getString(6), cursor.getString(7), cursor
								.getString(8), cursor.getString(9), cursor
								.getString(10), cursor.getString(11), cursor
								.getString(12), cursor.getString(13), cursor
								.getString(14), cursor.getString(15)));
				Log.e("KUPAC U BAZI11",
						cursor.getString(1) + "  " + cursor.getString(14));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return kupci;
	}

	public ArrayList<String> dajNaziveKupacaZaProgram(String sifraPrograma) {
		// openDataBase();
		ArrayList<String> kupci = new ArrayList<String>();
		Cursor cursor = getWritableDatabase().rawQuery(
				"SELECT DISTINCT naziv FROM Kupci WHERE id_program=?",
				new String[] { sifraPrograma });
		if (cursor.moveToFirst()) {
			do {
				kupci.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return kupci;
	}

	public String dajSifruKupca(String naziv) {
		// openDataBase();
		String sifra = "";
		Cursor cursor = getWritableDatabase()
				.query("Kupci", new String[] { "sifra" }, "naziv=?", new String[] {naziv},
						null, null, null);
		if (cursor.moveToFirst()) {
			do {
					sifra = cursor.getString(0);
					break;				
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return sifra;
	}

	public Kupac dajKupca(String naziv, String pp) {
		// openDataBase();
		Kupac kupac = null;
		String program = dajSifruPrograma(pp);
		Cursor cursor = getWritableDatabase().query(
				"Kupci",
				new String[] { "sifra", "naziv", "adresa", "ZIP", "mjesto",
						"telefon", "kupac_ID_broj", "kupac_VAT_broj",
						"ukupni_kredit", "dospjeli_kredit",
						"posljednja_uplata", "prihod", "broj_racuna",
						"kupac_sud_broj", "osnovni_popust", "id_program" },
				"naziv=? AND id_program=?", new String[] { naziv, program },
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				kupac = new Kupac(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7),
						cursor.getString(8), cursor.getString(9),
						cursor.getString(10), cursor.getString(11),
						cursor.getString(12), cursor.getString(13),
						cursor.getString(14), cursor.getString(15));
				Log.e("KUPAC U BAZI",
						cursor.getString(1) + "  " + cursor.getString(14));
				Log.e("KUPAC U BAZI OPET",
						kupac.dajNaziv() + "  " + kupac.dajOsnovniPopust());
				break;

			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();

		return kupac;
	}

	public Kupac dajKupcaSifra(String sifra, String id_prog) {
		// openDataBase();
		Kupac kupac = null;
		Cursor cursor = getWritableDatabase().query(
				"Kupci",
				new String[] { "sifra", "naziv", "adresa", "ZIP", "mjesto",
						"telefon", "kupac_ID_broj", "kupac_VAT_broj",
						"ukupni_kredit", "dospjeli_kredit",
						"posljednja_uplata", "prihod", "broj_racuna",
						"kupac_sud_broj", "osnovni_popust", "id_program" },
				"sifra=? AND id_program=?", new String[] { sifra, id_prog },
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				kupac = new Kupac(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7),
						cursor.getString(8), cursor.getString(9),
						cursor.getString(10), cursor.getString(11),
						cursor.getString(12), cursor.getString(13),
						cursor.getString(14), cursor.getString(15));
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return kupac;
	}

	public ArrayList<Grupa> dajGrupeZaProgram(String sifraPrograma) {
		// openDataBase();
		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		Cursor cursor = getWritableDatabase()
				.rawQuery(
						"SELECT DISTINCT sifra, skracenica, dugme, id_program FROM Grupe WHERE id_program=? GROUP BY sifra ORDER BY skracenica;",
						new String[] { sifraPrograma });
		if (cursor.moveToFirst()) {
			do {
				grupe.add(new Grupa(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3)));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return grupe;
	}

	public ArrayList<String> dajNaziveGrupeZaProgram(String sifraPrograma) {
		// openDataBase();
		ArrayList<String> grupe = new ArrayList<String>();
		Cursor cursor = getWritableDatabase().query("Grupe",
				new String[] { "skracenica" }, "id_program=?",
				new String[] { sifraPrograma }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				grupe.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return grupe;
	}

	public ArrayList<Proizvod> dajProizvodeZaProgram(String sifraPrograma) {
		// openDataBase();
		ArrayList<Proizvod> proizvodi = new ArrayList<Proizvod>();
		Cursor cursor = getWritableDatabase().query(
				"Proizvodi",
				new String[] { "sifra", "id_grupa", "naziv", "EAN", "mjera",
						"cijena", "cijena_mlp", "popust", "dugme",
						"id_program", "kolicina", "kupdob" }, "id_program=?",
				new String[] { sifraPrograma }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				proizvodi.add(new Proizvod(cursor.getString(0), cursor
						.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4), cursor
								.getString(5), cursor.getString(6), cursor
								.getDouble(7), cursor.getString(8), cursor
								.getString(9), cursor.getString(10), cursor
								.getString(11)));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return proizvodi;
	}

	public Proizvod dajProizvod(String sifra) {
		// openDataBase();
		Proizvod proizvod = null;
		Cursor cursor = getWritableDatabase().query(
				"Proizvodi",
				new String[] { "sifra", "id_grupa", "naziv", "EAN", "mjera",
						"cijena", "cijena_mlp", "popust", "dugme",
						"id_program", "kolicina", "kupdob" }, "sifra=?",
				new String[] { sifra }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				proizvod = new Proizvod(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getDouble(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						cursor.getString(11));
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return proizvod;
	}

	public ArrayList<Proizvod> dajProizvodeZaGrupu(ArrayList<Grupa> grupe,
			String program) {
		// openDataBase();
		// String sifraGrupe="";
		// sifraGrupe=dajSifruGrupe(nazivGrupe);
		// Log.e("SUBSTRING", sifra.substring(0,5));
		String poredjenje = "(";
		for (int i = 0; i < grupe.size(); i++) {
			if (i == grupe.size() - 1)
				poredjenje += "id_grupa='" + grupe.get(i).dajIdGrupe() + "'";
			else
				poredjenje += "id_grupa='" + grupe.get(i).dajIdGrupe() + "'"
						+ " OR ";
		}
		poredjenje += ")";

		ArrayList<Proizvod> proizvodi = new ArrayList<Proizvod>();
		String poredjenje1 = "";
		if (poredjenje.length() > 0)
			poredjenje1 = " AND id_program=" + program + " ";
		else
			poredjenje1 = "id_program=" + program + " ";
		Cursor cursor = getWritableDatabase()
				.rawQuery(
						"SELECT DISTINCT sifra, id_grupa, naziv, EAN, mjera, cijena, cijena_mlp, popust, dugme,id_program, kolicina, kupdob FROM Proizvodi WHERE "
								+ poredjenje
								+ poredjenje1
								+ "GROUP BY sifra ORDER BY naziv", null);
		if (cursor.moveToFirst()) {
			do {
				proizvodi.add(new Proizvod(cursor.getString(0), cursor
						.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4), cursor
								.getString(5), cursor.getString(6), cursor
								.getDouble(7), cursor.getString(8), cursor
								.getString(9), cursor.getString(10), cursor
								.getString(11)));
				// Log.e("SUBSTRING 1", cursor.)
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return proizvodi;
	}

	public ArrayList<Grupa> dajDugmad(String sifraPrograma, String dugme,
			boolean prviNivo) {
		// openDataBase();
		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		Cursor cursor = null;
		if (dugme.equals("0") && !prviNivo)
			cursor = getWritableDatabase()
					.rawQuery(
							"SELECT DISTINCT sifra,skracenica, dugme,id_program FROM Grupe WHERE id_program=? and (dugme=? OR dugme=? OR dugme=? OR dugme=?)ORDER BY dugme",
							new String[] { sifraPrograma, "1", "2", "3", "4" });
		else if (dugme.equals("1") && !prviNivo)
			cursor = getWritableDatabase()
					.rawQuery(
							"SELECT DISTINCT sifra,skracenica, dugme,id_program FROM Grupe WHERE id_program=? and (dugme=? OR dugme=? OR dugme=? OR dugme=?)ORDER BY dugme",
							new String[] { sifraPrograma, "11", "12", "13",
									"14" });
		else if (dugme.equals("2") && !prviNivo)
			cursor = getWritableDatabase()
					.rawQuery(
							"SELECT DISTINCT sifra,skracenica, dugme,id_program FROM Grupe WHERE id_program=? and (dugme=? OR dugme=? OR dugme=? OR dugme=?)ORDER BY dugme",
							new String[] { sifraPrograma, "21", "22", "23",
									"24" });
		else if (dugme.equals("3") && !prviNivo)
			cursor = getWritableDatabase()
					.rawQuery(
							"SELECT DISTINCT sifra,skracenica, dugme,id_program FROM Grupe WHERE id_program=? and (dugme=? OR dugme=? OR dugme=? OR dugme=?)ORDER BY dugme",
							new String[] { sifraPrograma, "31", "32", "33",
									"34" });
		else if (dugme.equals("4") && !prviNivo)
			cursor = getWritableDatabase()
					.rawQuery(
							"SELECT DISTINCT sifra,skracenica, dugme,id_program FROM Grupe WHERE id_program=? and (dugme=? OR dugme=? OR dugme=? OR dugme=?)ORDER BY dugme",
							new String[] { sifraPrograma, "41", "42", "43",
									"44" });
		else
			cursor = getWritableDatabase()
					.rawQuery(
							"SELECT DISTINCT sifra,skracenica, dugme,id_program FROM Grupe WHERE id_program=? and (dugme=? OR dugme=? OR dugme=? OR dugme=?)ORDER BY dugme",
							new String[] { sifraPrograma, dugme });
		if (cursor.moveToFirst()) {
			do {
				grupe.add(new Grupa(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3)));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		if (dugme.length() == 1 && !prviNivo) {
			Set s = new TreeSet(new Comparator<Grupa>() {
				@Override
				public int compare(Grupa lhs, Grupa rhs) {
					// TODO Auto-generated method stub
					if (lhs.dajSkracenicu().equals(rhs.dajSkracenicu()))
						return 0;
					return 1;
				}
			});
			s.addAll(grupe);
			grupe.clear();
			grupe.addAll(s);
		}
		return grupe;
	}

	public ArrayList<Lokacija> dajLokacijeZaKupca(String sifraKupca) {
		// openDataBase();
		ArrayList<Lokacija> lokacije = new ArrayList<Lokacija>();
		Cursor cursor = getWritableDatabase()
				.rawQuery(
						"SELECT DISTINCT id_lokacija_kupca, id_kupca, id_lokacije, naziv_lokacije, adresa_lokacije, ZIP_lokacije, mjesto_lokacije, ime_kontakt_osobe, telefon_kontakt_osobe, tip_lokacije, trgovina_na_malo_broj, novac_broj, id_program FROM Lokacije WHERE id_kupca=? GROUP BY id_lokacije ORDER BY naziv_lokacije",
						new String[] { sifraKupca });
		if (cursor.moveToFirst()) {
			do {
				lokacije.add(new Lokacija(cursor.getString(0), cursor
						.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4), cursor
								.getString(5), cursor.getString(6), cursor
								.getString(7), cursor.getString(8), cursor
								.getString(9), cursor.getString(10), cursor
								.getString(11), cursor.getString(12)));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return lokacije;
	}

	public Lokacija dajLokaciju(String naziv, String kupac) {
		// openDataBase();
		Lokacija lokacija = null;
		Cursor cursor = getWritableDatabase().query(
				"Lokacije",
				new String[] { "id_lokacija_kupca", "id_kupca", "id_lokacije",
						"naziv_lokacije", "adresa_lokacije", "ZIP_lokacije",
						"mjesto_lokacije", "ime_kontakt_osobe",
						"telefon_kontakt_osobe", "tip_lokacije",
						"trgovina_na_malo_broj", "novac_broj", "id_program" },
				"naziv_lokacije=? AND id_kupca=?",
				new String[] { naziv, kupac }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				lokacija = new Lokacija(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						cursor.getString(11), cursor.getString(12));
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return lokacija;
	}

	public Lokacija dajLokacijuSifra(String sifra) {
		// openDataBase();
		Lokacija lokacija = null;
		Cursor cursor = getWritableDatabase()
				.query("Lokacije",
						new String[] { "id_lokacija_kupca", "id_kupca",
								"id_lokacije", "naziv_lokacije",
								"adresa_lokacije", "ZIP_lokacije",
								"mjesto_lokacije", "ime_kontakt_osobe",
								"telefon_kontakt_osobe", "tip_lokacije",
								"trgovina_na_malo_broj", "novac_broj",
								"id_program" }, "id_lokacija_kupca=?",
						new String[] { sifra }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				lokacija = new Lokacija(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						cursor.getString(11), cursor.getString(12));
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return lokacija;
	}

	public ArrayList<String> dajNaziveLokacijaZaKupca(String naziv) {
		// openDataBase();
		ArrayList<String> lokacije = new ArrayList<String>();
		String sifra = dajSifruKupca(naziv);
		Cursor cursor = getWritableDatabase()
				.rawQuery(
						"SELECT DISTINCT naziv_lokacije FROM Lokacije WHERE id_kupca=? GROUP BY id_lokacije ORDER BY naziv_lokacije",
						new String[] { sifra });
		if (cursor.moveToFirst()) {
			do {
				lokacije.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return lokacije;
	}

	public String dajSifruKomercijaliste(String username) {
		String vrati = "";
		Cursor cursor = getReadableDatabase().query("Korisnik",
				new String[] { "sifra" }, "korisnicko_ime=?",
				new String[] { username }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				vrati = cursor.getString(0);
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return vrati;
	}

	public String dajIdNarudzbe() {
		// openDataBase();
		String vrati = "";
		Cursor cursor = getReadableDatabase().query("narudzbe",
				new String[] { "max(id)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				vrati = cursor.getString(0);
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return vrati;
	}

	public String dajIpAdresuServera() {
		// openDataBase();
		String vrati = "";
		Cursor cursor = getWritableDatabase().query("Postavke",
				new String[] { "adresa" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				vrati = cursor.getString(0);
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return vrati;
	}

	public boolean osvjeziAkcijskePopuste() {
		String vrati = "";
		Cursor cursor = getWritableDatabase().query("Postavke",
				new String[] { "datum_zadnjeg_osvjezavanja_popusta" }, null,
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				vrati = cursor.getString(0);
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		Calendar vrijeme = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date datum = vrijeme.getTime();

		java.util.Date osvjezavano = null;
		try {
			osvjezavano = formatter.parse(vrati);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!formatter.format(datum).equals(formatter.format(osvjezavano)))
			return true;
		else
			return false;
	}

	// metoda koja preko web servisa cita sve kupce iz glavne baze i upisuje ih
	// u bazu
	// podataka na uredjaju
	public void ucitajKupce(String program) {
		// openDataBase();
		ArrayList<Kupac> kupci = new ArrayList<Kupac>();
		String username = "";
		username = dajKorisnickoIme();
		if (OrdroidActivity.imaLiKonekcije(myContext)) {
			try {
				kupci = WebServis.DajKupce(dajIpAdresuServera(), username,
						program);
				for (int i = 0; i < kupci.size(); i++) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("sifra", kupci.get(i).dajSifru());
					initialValues.put("naziv", kupci.get(i).dajNaziv());
					initialValues.put("adresa", kupci.get(i).dajAdresu());
					initialValues.put("ZIP", kupci.get(i).dajZIP());
					initialValues.put("mjesto", kupci.get(i).dajMjesto());
					initialValues.put("telefon", kupci.get(i).dajTelefon());
					initialValues.put("kupac_ID_broj", kupci.get(i)
							.dajKupacIdbroj());
					initialValues.put("kupac_VAT_broj", kupci.get(i)
							.dajKupacVATbroj());
					initialValues.put("ukupni_kredit", kupci.get(i)
							.dajUkupniKredit());
					initialValues.put("dospjeli_kredit", kupci.get(i)
							.dajDospjeliKredit());
					initialValues.put("posljednja_uplata", kupci.get(i)
							.dajPosljednjuUplatu());
					initialValues.put("prihod", kupci.get(i).dajPrihod());
					initialValues.put("broj_racuna", kupci.get(i)
							.dajBrojRacuna());
					initialValues.put("kupac_sud_broj", kupci.get(i)
							.dajKupacSudBroj());
					initialValues.put("osnovni_popust", kupci.get(i)
							.dajOsnovniPopust());
					initialValues.put("id_program", kupci.get(i)
							.dajIdPrograma());
					getWritableDatabase().insert("Kupci", null, initialValues);
				}
				close();
			} catch (Exception e) {
				Toast.makeText(myContext,
						"Greška: Nije moguće uspostaviti vezu sa serverom!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(myContext, "Nema konekcije na server!",
					Toast.LENGTH_LONG).show();
		}
	}

	public void izbrisiKupce(String program) {
		// openDataBase();
		getWritableDatabase().delete("Kupci", "id_program=?",
				new String[] { program });
		close();
	}

	// metoda koja preko web servisa cita sve akcijske popuste iz glavne baze i
	// upisuje ih
	// u bazu
	// podataka na uredjaju
	public void ucitajAkcijskePopuste() {
		// openDataBase();
		izbrisiAkcijskePopuste();
		ArrayList<AkcijskiPopusti> popusti = new ArrayList<AkcijskiPopusti>();
		if (OrdroidActivity.imaLiKonekcije(myContext)) {
			try {
				popusti = WebServis.DajAkcijskePopuste(dajIpAdresuServera());
				for (int i = 0; i < popusti.size(); i++) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("sifmat", popusti.get(i).DajSifmat());
					initialValues.put("kupdob", popusti.get(i).DajKupdob());
					initialValues.put("sifpre", popusti.get(i).DajSifpre());
					initialValues.put("primis", popusti.get(i).DajPrimis());
					initialValues.put("datcij", popusti.get(i).DajDatcij());
					initialValues.put("datpre", popusti.get(i).DajDatpre());
					initialValues.put("rabsta", popusti.get(i).DajRabsta());
					getWritableDatabase().insert("Akcijski_popusti", null,
							initialValues);
				}
				close();
			} catch (Exception e) {
				Toast.makeText(myContext,
						"Greška: Nije moguće uspostaviti vezu sa serverom!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(myContext, "Nema konekcije na server!",
					Toast.LENGTH_LONG).show();
		}
		postaviDatumOsvjezavanjaPopusta();
	}

	public void izbrisiAkcijskePopuste() {
		// openDataBase();
		try {
			getWritableDatabase().delete("Akcijski_popusti", "1=1", null);
			close();
		} catch (Exception re) {
		}

	}

	// metoda koja preko web servisa cita sva mjesta isporuka iz glavne baze i
	// upisuje ih u bazu
	// podataka na uredjaju
	public void ucitajLokacije(String program) {
		// openDataBase();
		ArrayList<Lokacija> lokacije = new ArrayList<Lokacija>();
		String userName = "";
		userName = dajKorisnickoIme();
		if (OrdroidActivity.imaLiKonekcije(myContext)) {
			try {
				lokacije = WebServis.DajLokacije(dajIpAdresuServera(),
						userName, program);
				for (int i = 0; i < lokacije.size(); i++) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("id_lokacija_kupca", lokacije.get(i)
							.dajIdLokacijeKupca());
					initialValues.put("id_kupca", lokacije.get(i).dajIdKupca());
					initialValues.put("id_lokacije", lokacije.get(i)
							.dajIdLokacije());
					initialValues.put("naziv_lokacije", lokacije.get(i)
							.dajNazivLokacije());
					initialValues.put("adresa_lokacije", lokacije.get(i)
							.dajAdresuLokacije());
					initialValues.put("ZIP_lokacije", lokacije.get(i)
							.dajZIPlokacije());
					initialValues.put("mjesto_lokacije", lokacije.get(i)
							.dajMjestoLokacije());
					initialValues.put("ime_kontakt_osobe", lokacije.get(i)
							.dajImeKontaktOsobe());
					initialValues.put("telefon_kontakt_osobe", lokacije.get(i)
							.dajTelefonKontaktOsobe());
					initialValues.put("tip_lokacije", lokacije.get(i)
							.dajTipLokacije());
					initialValues.put("trgovina_na_malo_broj", lokacije.get(i)
							.dajTrgovinaNaMaloBroj());
					initialValues.put("novac_broj", lokacije.get(i)
							.dajNovacBroj());
					initialValues.put("id_program", lokacije.get(i)
							.dajIdPrograma());
					getWritableDatabase().insert("Lokacije", null,
							initialValues);
				}
				close();
			} catch (Exception e) {
				Toast.makeText(myContext,
						"Greška: Nije moguće uspostaviti vezu sa serverom!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(myContext, "Nema konekcije na server!",
					Toast.LENGTH_LONG).show();
		}
	}
	
	
	// metoda koja preko web servisa cita rokove placanja iz glavne baze i
		// upisuje ih u bazu
		// podataka na uredjaju
	public void ucitajRokovePlacanja(String program) {
			// openDataBase();
			ArrayList<Rok_placanja> rokoviPlacanja= new ArrayList<Rok_placanja>();
			String userName = "";
			userName = dajKorisnickoIme();
			if (OrdroidActivity.imaLiKonekcije(myContext)) {
				try {
					rokoviPlacanja = WebServis.DajRokovePlacanja(dajIpAdresuServera(),
							userName, program);
					for (int i = 0; i < rokoviPlacanja.size(); i++) {
						ContentValues initialValues = new ContentValues();
						initialValues.put("nr_brjtin", rokoviPlacanja.get(i).getNa_kratin());
						initialValues.put("ba_kupdob", rokoviPlacanja.get(i).getBa_kupdob());
						initialValues.put("nr_rabkup", rokoviPlacanja.get(i).getNr_rabkup());
						initialValues.put("nr_rokpla", rokoviPlacanja.get(i).getNr_rokpla());
						getWritableDatabase().insert("Rok_placanja", null,
								initialValues);
						Log.d("kupac rok ", rokoviPlacanja.get(i).getBa_kupdob() + " " + rokoviPlacanja.get(i).getNr_rokpla());
					}
					close();
				} catch (Exception e) {
					Toast.makeText(myContext,
							"Greška: Nije moguće uspostaviti vezu sa serverom!",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(myContext, "Nema konekcije na server!",
						Toast.LENGTH_LONG).show();
			}
	}
	
	public int dajRokPlacanjaZaFirmu(String ba_kupdob, String na_kratin) {
		int vrati = 0;
			Cursor cursor = getWritableDatabase()
					.query("Rok_placanja",
							new String[] { "nr_rokpla"},
							"nr_brjtin=? AND ba_kupdob=?",
							new String[] { na_kratin, ba_kupdob}, null,
							null, null);
			if (cursor.moveToFirst()) {
				do {
					vrati = Integer.parseInt(cursor.getString(0));
					break;
				} while (cursor.moveToNext());
			}
			else {
				Log.d("Rok placanja", "vraca se default 10");
				vrati = 0;
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			close();
			Log.d("ROK PLACANJA: ","" + vrati);
			return vrati;
	}
	

	public void izbrisiLokacije(String program) {
		// openDataBase();
		getWritableDatabase().delete("Lokacije", "id_program=?",
				new String[] { program });
		close();
	}

	// metoda koja preko web servisa cita sve rpoizvode za neki prodajni program
	// iz glavne baze i upisuje ih u bazu
	// podataka na uredjaju
	public void ucitajProizvode(String program) {
		// openDataBase();
		DecimalFormat df = new DecimalFormat("0.000");
		ArrayList<Proizvod> proizvodi = new ArrayList<Proizvod>();
		if (OrdroidActivity.imaLiKonekcije(myContext)) {
			try {
				proizvodi = WebServis.DajProizvode(dajIpAdresuServera(),
						program);
				for (int i = 0; i < proizvodi.size(); i++) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("sifra", proizvodi.get(i).dajSifru());
					initialValues
							.put("id_grupa", proizvodi.get(i).dajIdGrupe());
					initialValues.put("naziv", proizvodi.get(i).dajNaziv());
					initialValues.put("EAN", proizvodi.get(i).dajEAN());
					initialValues.put("mjera", proizvodi.get(i).dajMjeru());
					initialValues.put("cijena", proizvodi.get(i).dajCijenu());
					initialValues.put("cijena_mlp", proizvodi.get(i)
							.dajMLPcijenu());
					initialValues.put("popust", proizvodi.get(i).dajPopust());
					initialValues.put("dugme", proizvodi.get(i).dajDugme());
					initialValues.put("id_program", proizvodi.get(i)
							.dajIDprogram());
					initialValues.put("kupdob", proizvodi.get(i).dajKupdob());
					initialValues.put("kolicina", String.valueOf(proizvodi.get(i)
							.dajKolicinu()));
					getWritableDatabase().insert("Proizvodi", null,
							initialValues);
				}
				close();
			} catch (Exception e) {
				Toast.makeText(myContext,
						"Greška: Nije moguće uspostaviti vezu sa serverom!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(myContext, "Nema konekcije na server!",
					Toast.LENGTH_LONG).show();
		}
	}

	// metoda koja preko web servisa cita sve grupe proizvoda za prodajni
	// program iz glavne baze i upisuje ih u bazu
	// podataka na uredjaju
	public void ucitajGrupe(String program) {
		// openDataBase();
		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		if (OrdroidActivity.imaLiKonekcije(myContext)) {
			try {
				grupe = WebServis.DajGrupe(dajIpAdresuServera(), program);
				for (int i = 0; i < grupe.size(); i++) {
					ContentValues initialValues = new ContentValues();
					initialValues.put("sifra", grupe.get(i).dajIdGrupe());
					initialValues.put("skracenica", grupe.get(i)
							.dajSkracenicu());
					initialValues.put("dugme", grupe.get(i).dajDugme());
					initialValues.put("id_program", grupe.get(i)
							.dajIdPrograma());
					getWritableDatabase().insert("Grupe", null, initialValues);
				}
				close();
			} catch (Exception e) {
				Toast.makeText(myContext,
						"Greška: Nije moguće uspostaviti vezu sa serverom!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(myContext, "Nema konekcije na server!",
					Toast.LENGTH_LONG).show();
		}
	}

	public void izbrisiProizvode(String program) {
		// openDataBase();
		getWritableDatabase().delete("Proizvodi", "id_program=?",
				new String[] { program });
		close();
	}

	public void izbrisiGrupe(String program) {
		// openDataBase();
		getWritableDatabase().delete("Grupe", "id_program=?",
				new String[] { program });
		close();
	}

	public void izbrisiSveKupce() {
		// openDataBase();
		getWritableDatabase().delete("Kupci", "1=1", null);
		close();
	}

	public void izbrisiSveProizvode() {
		// openDataBase();
		getWritableDatabase().delete("Proizvodi", "1=1", null);
		close();
	}

	public void izbrisiSveGrupe() {
		// openDataBase();
		getWritableDatabase().delete("Grupe", "1=1", null);
		close();
	}

	public void izbrisiSveLokacije() {
		// openDataBase();
		getWritableDatabase().delete("Lokacije", "1=1", null);
		close();
	}

	public void izbrisiSveRokovePlacanja() {
		// openDataBase();
		getWritableDatabase().delete("Rok_Placanja", "1=1", null);
		close();
	}
	
	// metoda koja preko web servisa cita sve grupe artikala iz glavne baze i
	// upisuje ih u bazu
	// podataka na uredjaju
	public void sakrijPrograme(ArrayList<Program> izbrisani) {
		// openDataBase();
		for (int i = 0; i < izbrisani.size(); i++) {
			ContentValues cv = new ContentValues();
			cv.put("aktivan", "0");
			getWritableDatabase().update("Programi", cv, "sifra=?",
					new String[] { izbrisani.get(i).dajSifru() });
			// getWritableDatabase().delete("Kupci", "id_program=?", new
			// String[] {izbrisani.get(i).dajSifru()});
			// getWritableDatabase().delete("Lokacije", "id_program=?", new
			// String[] {izbrisani.get(i).dajSifru()});
			// getWritableDatabase().delete("Proizvodi", "id_program=?", new
			// String[] {izbrisani.get(i).dajSifru()});
			// getWritableDatabase().delete("Grupe", "id_program=?", new
			// String[] {izbrisani.get(i).dajSifru()});
		}
		close();
	}

	public void prikaziPrograme(ArrayList<Program> dodati) {
		for (int i = 0; i < dodati.size(); i++) {
			getWritableDatabase().delete("Programi", "sifra=?",
					new String[] { dodati.get(i).dajSifru() });
			ContentValues initialValues = new ContentValues();
			initialValues.put("sifra", dodati.get(i).dajSifru());
			initialValues.put("naziv", dodati.get(i).dajNaziv());
			initialValues.put("aktivan", "1");
			getWritableDatabase().insert("Programi", null, initialValues);
			// ContentValues cv=new ContentValues();
			// cv.put("aktivan", "1");
			// getWritableDatabase().update("Programi", cv, "sifra=?", new
			// String[] { dodati.get(i).dajSifru() });

		}
		close();
	}

	public boolean provjeriKorisnika(String username, String password) {
		Korisnik k = null;
		boolean b = false;
		int broj = -1;
		Cursor cursor = getReadableDatabase().query("Korisnik",
				new String[] { "count(*)" }, "korisnicko_ime=? AND lozinka=?",
				new String[] { username, password }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				broj = cursor.getInt(0);
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		if (broj == 1)
			return true;
		else {
			if (OrdroidActivity.imaLiKonekcije(myContext)) {
				try {
					k = new Korisnik(WebServis.ProvjeriKorisnika(
							dajIpAdresuServera(), username, password));
					if (!k.dajLozinku().equals("0")) {
						getWritableDatabase().delete("Korisnik", "1=1", null);
						ContentValues initialValues = new ContentValues();
						initialValues.put("sifra", k.dajID());
						initialValues.put("ime_prezime", k.dajImeIPrezime());
						initialValues.put("korisnicko_ime",
								k.dajKorisnickoIme());
						initialValues.put("lozinka", k.dajLozinku());
						initialValues.put("lokacija", k.dajLokaciju());
						getWritableDatabase().insert("Korisnik", null,
								initialValues);
						close();
						b = true;
					} else
						b = false;
				} catch (Exception e) {
					Toast.makeText(
							myContext,
							"Greška: Nije moguće uspostaviti vezu sa serverom!",
							Toast.LENGTH_LONG).show();
				}
				return b;
			} else {
				Toast.makeText(myContext, "Nema konekcije na server!",
						Toast.LENGTH_LONG).show();
				return false;
			}
		}
	}

	public ArrayList<String> dajNazivePrograma() {
		ArrayList<String> programi = new ArrayList<String>();
		Cursor cursor = getReadableDatabase().query("Programi",
				new String[] { "naziv" }, "aktivan=?", new String[] { "1" },
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				programi.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return programi;
	}

	public String[] dajNaziveProgramaZaMeni(String sifra) {
		Cursor cursor = getReadableDatabase()
				.rawQuery(
						"SELECT p.naziv, k.osnovni_popust FROM Kupci k, Programi p WHERE k.id_program=p.sifra AND k.sifra=? AND p.aktivan=1",
						new String[] { sifra });

		String[] programi = new String[cursor.getCount()];
		int i = 0;
		if (cursor.moveToFirst()) {
			do {
				programi[i] = cursor.getString(0) + ">> "
						+ pretvoriuDecimalni(cursor.getString(1), true) + " %";
				i++;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return programi;
	}

	public ArrayList<String> dajSifrePrograma() {
		// openDataBase();
		ArrayList<String> programi = new ArrayList<String>();
		Cursor cursor = getWritableDatabase().query("Programi",
				new String[] { "sifra" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				programi.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return programi;
	}

	public ArrayList<Program> dajPrograme(String aktivan) {
		// openDataBase();
		ArrayList<Program> programi = new ArrayList<Program>();
		Cursor cursor = getWritableDatabase().query("Programi",
				new String[] { "sifra, naziv" }, "aktivan=?",
				new String[] { aktivan }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Program pp = new Program(cursor.getString(0),
						cursor.getString(1));
				pp.izaberi(false);
				programi.add(pp);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return programi;
	}

	public ArrayList<Rok_placanja> dajRokovePlacanja() {
		ArrayList<Rok_placanja> rokoviPlacanja = new ArrayList<Rok_placanja>();
		Cursor cursor = getWritableDatabase().query("Rok_placanja",
				new String[] { "nr_brjtin, ba_kupdob, nr_rabkup, nr_rokpla" },null ,null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Rok_placanja rp = new Rok_placanja(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),cursor.getString(3));
				rokoviPlacanja.add(rp);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return rokoviPlacanja;
	}
	
	public Program dajProgram(String naziv) {
		// openDataBase();
		Program program = null;
		Cursor cursor = getWritableDatabase().query("Programi",
				new String[] { "sifra, naziv" }, "naziv=?",
				new String[] { naziv }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				program = new Program(cursor.getString(0), cursor.getString(1));
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return program;
	}

	public Program dajProgramSifra(String sifra) {
		// openDataBase();
		Program program = null;
		Cursor cursor = getWritableDatabase().query("Programi",
				new String[] { "sifra, naziv" }, "sifra=?",
				new String[] { sifra }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				program = new Program(cursor.getString(0), cursor.getString(1));
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return program;
	}

	public String dajSifruPrograma(String naziv) {
		// openDataBase();
		String vrati = "";
		Cursor cursor = getWritableDatabase().query("Programi",
				new String[] { "sifra" }, "naziv=?", new String[] { naziv },
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				vrati = cursor.getString(0);
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return vrati;
	}

	public String dajSifruGrupe(String naziv) {
		// openDataBase();
		String vrati = "";
		Cursor cursor = getWritableDatabase().query("Grupe",
				new String[] { "sifra" }, "skracenica=?",
				new String[] { naziv }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				vrati = cursor.getString(0);
				break;
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return vrati;
	}

	// metoda koja vraca sifru logovanog korisnika
	public String DajSifruLogovanogKorisnika(String username) {
		String sifra = "";
		Cursor cursor = getReadableDatabase().query("Korisnik",
				new String[] { "sifra" }, "korisnicko_ime=?",
				new String[] { username }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				sifra = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		close();
		return sifra;
	}

	// metoda koja se poziva kada se zatvara baza podataka na uredjaju
	@Override
	public synchronized void close() {
		super.close();
	}

	public String pretvoriuDecimalni(String broj, boolean rabat) {
		String vrati = "";
		DecimalFormat df = new DecimalFormat("0.000");
		if (rabat)
			vrati = String.format("%.2f", Double.parseDouble(broj) * 100);
		else
			vrati = String.format("%.2f", Double.parseDouble(broj));
		return vrati;
	}
}
