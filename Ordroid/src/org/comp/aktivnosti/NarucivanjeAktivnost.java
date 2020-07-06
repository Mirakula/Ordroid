package org.comp.aktivnosti;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.comp.adapteri.AdapterArtikli;
import org.comp.adapteri.AdapterGrupe;
import org.comp.klase.BazaPodataka;
import org.comp.klase.Grupa;
import org.comp.klase.Kupac;
import org.comp.klase.Lokacija;
import org.comp.klase.MeniDugme;
import org.comp.klase.MenuFragment;
import org.comp.klase.Narudzba;
import org.comp.klase.Program;

import paket.comp.slideout.SlideoutActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NarucivanjeAktivnost extends Activity {
	private ListView lvArtikli;
	// private Spinner sGrupe;
	private Button btnNazad;
	private Button btnZakljuci;
	private Button btnStavke;
	private Button btnPrihvati;
	public static EditText txtUkupanIznos;
	private TextView txtFirma;
	private static BazaPodataka baza;
	public static Narudzba narudzba;
	public static AdapterArtikli adapterArtikli;
	private AdapterGrupe adapterGrupe;
	public static int idNarudzbe = -1;
	private static String sifraIzabranogPrograma;
	public static ArrayAdapter adapterKupci;
	public static ArrayAdapter adapterLokacije;
	private static Button dugme1;
	private static Button dugme2;
	private static Button dugme3;
	private static Button dugme4;
	private Button dugmeNazad;
	private static boolean prviNivo;
	private String pritisnutoDugme;
	public MeniDugme btnMeniProgram;
	private DecimalFormat df = new DecimalFormat("0.000");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novanarudzba);
		setTitle("Nova narudžba");
		try {
			baza = new BazaPodataka(NarucivanjeAktivnost.this);
		} catch (Exception e) {
			Log.e("Greška baza 123", e.getMessage());
		}
		
		//final String korIme=baza.dajKorisnickoIme();
		prviNivo = true;
		pritisnutoDugme="0";
		if (idNarudzbe != -1)
			narudzba = new Narudzba(baza.dajNarudzbu(idNarudzbe));
		MenuFragment.stavke=baza.dajNaziveProgramaZaMeni(narudzba.dajKupca().dajSifru());
		txtFirma = (TextView) findViewById(R.id.tvNazivIzabraneFirme);
		txtFirma.setEnabled(false);
		lvArtikli = (ListView) findViewById(R.id.lvArtikli111);
		int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
		lvArtikli.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		lvArtikli.setDividerHeight(2);
		btnNazad = (Button) findViewById(R.id.btnNazad15);
		btnZakljuci = (Button) findViewById(R.id.btnZakljuci);
		btnStavke = (Button) findViewById(R.id.btnPregledNarucenihStavki);
		btnPrihvati = (Button) findViewById(R.id.btnPrihvatiStavke);
		txtUkupanIznos = (EditText) findViewById(R.id.txtUkupnanIznos);
		btnMeniProgram=(MeniDugme) findViewById(R.id.btnMeniProgram1);
		// sGrupe=(Spinner) findViewById(R.id.sGrupe);
		txtUkupanIznos.setEnabled(false);
		txtUkupanIznos.setText(String.format("%.2f", narudzba.dajUkupanIznosNarudzbe()));
		sifraIzabranogPrograma = baza.dajSifruPrograma(narudzba.dajProgram()
				.dajNaziv());
		dugme1 = (Button) findViewById(R.id.button1);
		dugme2 = (Button) findViewById(R.id.button2);
		dugme3 = (Button) findViewById(R.id.button3);
		dugme4 = (Button) findViewById(R.id.button4);
		dugmeNazad = (Button) findViewById(R.id.button5);

		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		grupe = baza.dajDugmad(sifraIzabranogPrograma, "0", false);
		for(int i=0; i<grupe.size(); i++){
			if(grupe.get(i).dajDugme().equals("1"))
				dugme1.setText(grupe.get(i).dajSkracenicu());
			else if(grupe.get(i).dajDugme().equals("2"))
				dugme2.setText(grupe.get(i).dajSkracenicu());
			else if(grupe.get(i).dajDugme().equals("3"))
				dugme3.setText(grupe.get(i).dajSkracenicu());
			else if(grupe.get(i).dajDugme().equals("4"))
				dugme4.setText(grupe.get(i).dajSkracenicu());
		}
		if(dugme1.getText().toString().equals("Button"))
			dugme1.setVisibility(View.INVISIBLE);
		if(dugme2.getText().toString().equals("Button"))
			dugme2.setVisibility(View.INVISIBLE);
		if(dugme3.getText().toString().equals("Button"))
			dugme3.setVisibility(View.INVISIBLE);
		if(dugme4.getText().toString().equals("Button"))
			dugme4.setVisibility(View.INVISIBLE);
				
		dugme1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();

				if (prviNivo) {
					grupe1 = baza.dajDugmad(sifraIzabranogPrograma, "1", false);
					pritisnutoDugme = "1";
					if (grupe1.size() > 0) {
						dugme1.setVisibility(View.INVISIBLE);
						dugme2.setVisibility(View.INVISIBLE);
						dugme3.setVisibility(View.INVISIBLE);
						dugme4.setVisibility(View.INVISIBLE);
						

						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "1")) {
									dugme1.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme1.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme1.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "2")) {
									dugme2.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme2.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme2.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "3")) {
									dugme3.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme3.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme3.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "4")) {
									dugme4.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme4.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme4.setVisibility(View.INVISIBLE);
						}
						prviNivo = false;
					} else {
						grupe1 = baza.dajDugmad(sifraIzabranogPrograma, pritisnutoDugme, true);
						//for (int i = 0; i < grupe1.size(); i++) {
							//if (grupe1.get(i).dajDugme().equals("1")){
								adapterArtikli = new AdapterArtikli(
										NarucivanjeAktivnost.this,
										R.layout.rednarudzbe, baza
												.dajProizvodeZaGrupu(grupe1, sifraIzabranogPrograma));
								lvArtikli.setAdapter(adapterArtikli);
								//break;
							//}						
								
						//}
						
					}
				} else {
					grupe1 = baza.dajDugmad(sifraIzabranogPrograma, pritisnutoDugme + "1", false);
					adapterArtikli = new AdapterArtikli(
							NarucivanjeAktivnost.this,
							R.layout.rednarudzbe, baza
									.dajProizvodeZaGrupu(grupe1, sifraIzabranogPrograma));
					lvArtikli.setAdapter(adapterArtikli);
				}
			}
		});
		dugme2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();

				if (prviNivo) {
					grupe1 = baza.dajDugmad(sifraIzabranogPrograma, "2", false);
					pritisnutoDugme = "2";
					if (grupe1.size() > 0) {
						dugme1.setVisibility(View.INVISIBLE);
						dugme2.setVisibility(View.INVISIBLE);
						dugme3.setVisibility(View.INVISIBLE);
						dugme4.setVisibility(View.INVISIBLE);
						

						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "1")) {
									dugme1.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme1.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme1.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "2")) {
									dugme2.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme2.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme2.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "3")) {
									dugme3.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme3.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme3.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "4")) {
									dugme4.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme4.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme4.setVisibility(View.INVISIBLE);
						}
						prviNivo = false;
					} else {
						grupe1 = baza.dajDugmad(sifraIzabranogPrograma, pritisnutoDugme, true);
						//for (int i = 0; i < grupe1.size(); i++) {
							//if (grupe1.get(i).dajDugme().equals("2")){
								adapterArtikli = new AdapterArtikli(
										NarucivanjeAktivnost.this,
										R.layout.rednarudzbe, baza
												.dajProizvodeZaGrupu(grupe1, sifraIzabranogPrograma));
								lvArtikli.setAdapter(adapterArtikli);
								//break;
							//}						
								
						//}
					}
				} else {
					grupe1 = baza.dajDugmad(sifraIzabranogPrograma, pritisnutoDugme + "2", false);
					adapterArtikli = new AdapterArtikli(
							NarucivanjeAktivnost.this,
							R.layout.rednarudzbe, baza
									.dajProizvodeZaGrupu(grupe1, sifraIzabranogPrograma));
					lvArtikli.setAdapter(adapterArtikli);
				}

			}
		});

		dugme3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();
				if (prviNivo) {
					grupe1 = baza.dajDugmad(sifraIzabranogPrograma, "3", false);
					pritisnutoDugme = "3";
					if (grupe1.size() > 0) {
						dugme1.setVisibility(View.INVISIBLE);
						dugme2.setVisibility(View.INVISIBLE);
						dugme3.setVisibility(View.INVISIBLE);
						dugme4.setVisibility(View.INVISIBLE);
						

						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "1")) {
									dugme1.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme1.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme1.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "2")) {
									dugme2.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme2.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme2.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "3")) {
									dugme3.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme3.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme3.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "4")) {
									dugme4.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme4.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme4.setVisibility(View.INVISIBLE);
						}
						prviNivo = false;
					} else {
						grupe1 = baza.dajDugmad(sifraIzabranogPrograma, pritisnutoDugme, true);
						//for (int i = 0; i < grupe1.size(); i++) {
							//if (grupe1.get(i).dajDugme().equals("3")){
								adapterArtikli = new AdapterArtikli(
										NarucivanjeAktivnost.this,
										R.layout.rednarudzbe, baza
												.dajProizvodeZaGrupu(grupe1, sifraIzabranogPrograma));
								lvArtikli.setAdapter(adapterArtikli);
								//break;
							//}						
								
						//}
					}
				} else {
					grupe1 = baza.dajDugmad(sifraIzabranogPrograma, pritisnutoDugme + "3", false);
					adapterArtikli = new AdapterArtikli(
							NarucivanjeAktivnost.this,
							R.layout.rednarudzbe, baza
									.dajProizvodeZaGrupu(grupe1, sifraIzabranogPrograma));
					lvArtikli.setAdapter(adapterArtikli);
				}
			}
		});

		dugme4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();
				if (prviNivo) {
					grupe1 = baza.dajDugmad(sifraIzabranogPrograma, "4", false);
					pritisnutoDugme = "4";
					if (grupe1.size() > 0) {
						dugme1.setVisibility(View.INVISIBLE);
						dugme2.setVisibility(View.INVISIBLE);
						dugme3.setVisibility(View.INVISIBLE);
						dugme4.setVisibility(View.INVISIBLE);
						

						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "1")) {
									dugme1.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme1.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme1.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "2")) {
									dugme2.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme2.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme2.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "3")) {
									dugme3.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme3.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme3.setVisibility(View.INVISIBLE);
						}
						try {
							for (int i = 0; i < grupe1.size(); i++) {
								if (grupe1.get(i).dajDugme()
										.equals(pritisnutoDugme + "4")) {
									dugme4.setText(grupe1.get(i)
											.dajSkracenicu());
									dugme4.setVisibility(View.VISIBLE);
									break;
								}
							}
						} catch (Exception ee) {
							dugme4.setVisibility(View.INVISIBLE);
						}
						prviNivo = false;
					} else {
						grupe1 = baza.dajDugmad(sifraIzabranogPrograma, pritisnutoDugme, true);
						//for (int i = 0; i < grupe1.size(); i++) {
							//if (grupe1.get(i).dajDugme().equals("4")){
								adapterArtikli = new AdapterArtikli(
										NarucivanjeAktivnost.this,
										R.layout.proizvodred, baza
												.dajProizvodeZaGrupu(grupe1,sifraIzabranogPrograma));
								lvArtikli.setAdapter(adapterArtikli);
								//break;
							//}						
								
						//}
					}
				} else {
					grupe1 = baza.dajDugmad(sifraIzabranogPrograma, pritisnutoDugme + "4", false);
					adapterArtikli = new AdapterArtikli(
							NarucivanjeAktivnost.this,
							R.layout.rednarudzbe, baza
									.dajProizvodeZaGrupu(grupe1,sifraIzabranogPrograma));
					lvArtikli.setAdapter(adapterArtikli);
				}
			}
		});

		dugmeNazad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dugme1.setText("Button");
				dugme2.setText("Button");
				dugme3.setText("Button");
				dugme4.setText("Button");
				ArrayList<Grupa> grupe2 = new ArrayList<Grupa>();
				grupe2 = baza.dajDugmad(sifraIzabranogPrograma, "0", false);
				for(int i=0; i<grupe2.size(); i++){
					if(grupe2.get(i).dajDugme().equals("1"))
						dugme1.setText(grupe2.get(i).dajSkracenicu());
					else if(grupe2.get(i).dajDugme().equals("2"))
						dugme2.setText(grupe2.get(i).dajSkracenicu());
					else if(grupe2.get(i).dajDugme().equals("3"))
						dugme3.setText(grupe2.get(i).dajSkracenicu());
					else if(grupe2.get(i).dajDugme().equals("4"))
						dugme4.setText(grupe2.get(i).dajSkracenicu());
				}
				dugme1.setVisibility(View.VISIBLE);
				dugme2.setVisibility(View.VISIBLE);
				dugme3.setVisibility(View.VISIBLE);
				dugme4.setVisibility(View.VISIBLE);
				if(dugme1.getText().toString().equals("Button"))
					dugme1.setVisibility(View.INVISIBLE);
				if(dugme2.getText().toString().equals("Button"))
					dugme2.setVisibility(View.INVISIBLE);
				if(dugme3.getText().toString().equals("Button"))
					dugme3.setVisibility(View.INVISIBLE);
				if(dugme4.getText().toString().equals("Button"))
					dugme4.setVisibility(View.INVISIBLE);
				prviNivo=true;
				pritisnutoDugme="0";
				lvArtikli.setAdapter(null);
			}
		});
		Log.e("Broj grupa", String.valueOf(grupe.size()));
		if (idNarudzbe == -1)
			adapterGrupe = new AdapterGrupe(NarucivanjeAktivnost.this,
					android.R.layout.simple_dropdown_item_1line,
					baza.dajGrupeZaProgram(sifraIzabranogPrograma));
		else
			adapterGrupe = new AdapterGrupe(NarucivanjeAktivnost.this,
					android.R.layout.simple_dropdown_item_1line,
					baza.dajGrupeZaProgram(narudzba.dajProgram().dajSifru()));

		txtFirma.setText(narudzba.dajKupca().dajNaziv());

		btnNazad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					idNarudzbe = -1;
					finish();
			}
		});

		btnStavke.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (narudzba.dajUkupanIznosNarudzbe() != 0) {
					Intent i = new Intent(NarucivanjeAktivnost.this,
							NaruceniProizvodiAktivnost.class);
					startActivity(i);
				} else
					Toast.makeText(NarucivanjeAktivnost.this,
							"Narudžba nema stavki!", Toast.LENGTH_LONG).show();

			}
		});

		btnZakljuci.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (narudzba.dajUkupanIznosNarudzbe() != 0) {
					if (idNarudzbe != -1)
						baza.deleteNarudzba(idNarudzbe);
					try {
						
						baza.insertNarudzba(narudzba,
								"zakljucena");
						boolean mlp = false;
						if (narudzba.dajTipDokumenta().equals("Maloprodaja"))
							mlp = true;
						baza.insertStavkiNarudzbe(
								narudzba.dajArtikleNarudzbe(), mlp);
						Toast.makeText(getApplicationContext(),
								"Uspješno ste zaključili narudžbu.",
								Toast.LENGTH_LONG).show();
						idNarudzbe = -1;

					} catch (SQLException e2) {
						Toast.makeText(
								getApplicationContext(),
								"Došlo je do greške. Narudžba nije zaključena.",
								Toast.LENGTH_LONG).show();
						Log.e("GRESKa", "s " + e2.getMessage());
					}
					finish();
					Intent in = new Intent(NarucivanjeAktivnost.this,
							NarudzbaAktivnost.class);
					startActivity(in);
				} else
					Toast.makeText(NarucivanjeAktivnost.this,
							"Narudžba nema stavki!", Toast.LENGTH_LONG).show();
			}
		});

		btnPrihvati = (Button) findViewById(R.id.btnPrihvatiStavke);
		btnPrihvati.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (narudzba.dajUkupanIznosNarudzbe() != 0) {
					if (idNarudzbe != -1)
						baza.deleteNarudzba(idNarudzbe);
					try {
						baza.insertNarudzba(narudzba,
								"otvorena");
						boolean mlp = false;
						if (narudzba.dajTipDokumenta().equals("Maloprodaja"))
							mlp = true;
						baza.insertStavkiNarudzbe(
								narudzba.dajArtikleNarudzbe(), mlp);
						Toast.makeText(getApplicationContext(),
								"Uspješno ste spasili narudžbu.",
								Toast.LENGTH_LONG).show();
						idNarudzbe = -1;

					} catch (SQLException e2) {
						Toast.makeText(getApplicationContext(),
								"Došlo je do greške. Narudžba nije spašena.",
								Toast.LENGTH_LONG).show();
						Log.e("GRESKa", "s " + e2.getMessage());
					}
					finish();
					Intent in = new Intent(NarucivanjeAktivnost.this,
							NarudzbaAktivnost.class);
					startActivity(in);
				} else
					Toast.makeText(NarucivanjeAktivnost.this,
							"Narudžba nema stavki!", Toast.LENGTH_LONG).show();
			}
		});
		
		btnMeniProgram.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lvArtikli.setAdapter(null);
				
				int width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
								.getDisplayMetrics());
				SlideoutActivity.prepare(NarucivanjeAktivnost.this,
						R.id.inner_content, width);
				MenuActivity.aa = NarucivanjeAktivnost.this;
				startActivity(new Intent(NarucivanjeAktivnost.this,
						MenuActivity.class));
				overridePendingTransition(0, 0);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.meniZaglavlje:
			Intent i1 = new Intent(NarucivanjeAktivnost.this,
					NovaNarudzbaAktivnost.class);
			i1.putExtra("otvorena", true);
			NovaNarudzbaAktivnost.narudzba = narudzba;
			adapterKupci = new ArrayAdapter<String>(
					NarucivanjeAktivnost.this,
					android.R.layout.simple_spinner_item, baza
							.dajNaziveKupacaZaProgram(narudzba
									.dajProgram().dajSifru()));
			adapterLokacije = new ArrayAdapter<String>(
					NarucivanjeAktivnost.this,
					android.R.layout.simple_spinner_item, baza
							.dajNaziveLokacijaZaKupca(narudzba
									.dajKupca().dajNaziv()));

			startActivity(i1);
			break;		
		/*case R.id.meniProgrami:
			
			int width = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
							.getDisplayMetrics());
			SlideoutActivity.prepare(NarucivanjeAktivnost.this,
					R.id.inner_content, width);
			MenuActivity.aa = NarucivanjeAktivnost.this;
			startActivity(new Intent(NarucivanjeAktivnost.this,
					MenuActivity.class));
			overridePendingTransition(0, 0);
			break;*/
		}
		return true;
	}
	
	public static void PrikaziDugmad(String Program, String rabat){
		narudzba.dajKupca().postaviRabatZaProgram(String.valueOf((Double.valueOf(rabat)/100)));
		Log.e("RABATTTT", String.valueOf((Double.valueOf(rabat)/100)));
		dugme1.setText("Button");
		dugme2.setText("Button");
		dugme3.setText("Button");
		dugme4.setText("Button");
		dugme1.setVisibility(View.VISIBLE);
		dugme2.setVisibility(View.VISIBLE);
		dugme3.setVisibility(View.VISIBLE);
		dugme4.setVisibility(View.VISIBLE);
		String sifraPrograma="";
		sifraPrograma=baza.dajSifruPrograma(Program);
		sifraIzabranogPrograma=sifraPrograma;
		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		prviNivo=true;
		grupe = baza.dajDugmad(sifraPrograma, "0", false);
		for(int i=0; i<grupe.size(); i++){
			if(grupe.get(i).dajDugme().equals("1"))
				dugme1.setText(grupe.get(i).dajSkracenicu());
			else if(grupe.get(i).dajDugme().equals("2"))
				dugme2.setText(grupe.get(i).dajSkracenicu());
			else if(grupe.get(i).dajDugme().equals("3"))
				dugme3.setText(grupe.get(i).dajSkracenicu());
			else if(grupe.get(i).dajDugme().equals("4"))
				dugme4.setText(grupe.get(i).dajSkracenicu());
		}
		if(dugme1.getText().toString().equals("Button"))
			dugme1.setVisibility(View.INVISIBLE);
		if(dugme2.getText().toString().equals("Button"))
			dugme2.setVisibility(View.INVISIBLE);
		if(dugme3.getText().toString().equals("Button"))
			dugme3.setVisibility(View.INVISIBLE);
		if(dugme4.getText().toString().equals("Button"))
			dugme4.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(NarucivanjeAktivnost.this);
		builder.setMessage("Koristeći ovu akciju izgubiće se sve promjene na narudžbi koje nisu spašene. Da li želite nastaviti?")
           .setCancelable(false)
           .setTitle("Izlaz")
           .setIcon(android.R.drawable.ic_dialog_alert)
           .setPositiveButton("Da", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   finish();
               }
           })
           .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {				                   
            	   dialog.cancel();
               }
           });
    	AlertDialog alert = builder.create();
    	alert.show();	
		
		
		
	}
}
