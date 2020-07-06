package org.comp.aktivnosti;

import java.util.ArrayList;

import org.comp.adapteri.AdapterProizvod;
import org.comp.klase.BazaPodataka;
import org.comp.klase.Grupa;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class PregledProizvodaAktivnost extends Activity {
	private BazaPodataka baza;
	private Button btnNazad;
	private Spinner sProgrami;
	private ListView lvProizvodi;
	private AdapterProizvod adapterProizvod;
	private Button dugme1;
	private Button dugme2;
	private Button dugme3;
	private Button dugme4;
	private Button dugmeNazad;
	private boolean prviNivo;
	private String pritisnutoDugme;
	private String sifra;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pregledproizvoda);
		setTitle("Pregled proizvoda");
		try {
			baza = new BazaPodataka(PregledProizvodaAktivnost.this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sifra = "";
		prviNivo = true;
		pritisnutoDugme = "0";
		dugme1 = (Button) findViewById(R.id.button11);
		dugme2 = (Button) findViewById(R.id.button22);
		dugme3 = (Button) findViewById(R.id.button33);
		dugme4 = (Button) findViewById(R.id.button44);
		dugmeNazad = (Button) findViewById(R.id.button55);
		btnNazad = (Button) findViewById(R.id.btnNazadProizvodi);
		sProgrami = (Spinner) findViewById(R.id.sProdajniProgramiProizvodi);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				PregledProizvodaAktivnost.this,
				android.R.layout.simple_dropdown_item_1line,
				baza.dajNazivePrograma());
		sProgrami.setAdapter(adapter);
		sifra = baza.dajSifruPrograma(sProgrami.getSelectedItem().toString());

		ArrayList<Grupa> grupe = new ArrayList<Grupa>();
		grupe = baza.dajDugmad(sifra, "0", false);
		for (int i = 0; i < grupe.size(); i++) {
			if (grupe.get(i).dajDugme().equals("1"))
				dugme1.setText(grupe.get(i).dajSkracenicu());
			else if (grupe.get(i).dajDugme().equals("2"))
				dugme2.setText(grupe.get(i).dajSkracenicu());
			else if (grupe.get(i).dajDugme().equals("3"))
				dugme3.setText(grupe.get(i).dajSkracenicu());
			else if (grupe.get(i).dajDugme().equals("4"))
				dugme4.setText(grupe.get(i).dajSkracenicu());
		}
		if (dugme1.getText().toString().equals("Button"))
			dugme1.setVisibility(View.INVISIBLE);
		if (dugme2.getText().toString().equals("Button"))
			dugme2.setVisibility(View.INVISIBLE);
		if (dugme3.getText().toString().equals("Button"))
			dugme3.setVisibility(View.INVISIBLE);
		if (dugme4.getText().toString().equals("Button"))
			dugme4.setVisibility(View.INVISIBLE);

		dugme1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();

				if (prviNivo) {
					grupe1 = baza.dajDugmad(sifra, "1", false);
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
						grupe1 = baza.dajDugmad(sifra, pritisnutoDugme, true);
						//for (int i = 0; i < grupe1.size(); i++) {
							//if (grupe1.get(i).dajDugme().equals("1")){
								adapterProizvod = new AdapterProizvod(
										PregledProizvodaAktivnost.this,
										R.layout.proizvodred, baza
											.dajProizvodeZaGrupu(grupe1, sifra));
								lvProizvodi.setAdapter(adapterProizvod);
								//break;
							//}						
								
						//}
						
					}
				} else {
					grupe1 = baza.dajDugmad(sifra, pritisnutoDugme + "1", false);
					adapterProizvod = new AdapterProizvod(
							PregledProizvodaAktivnost.this,
							R.layout.proizvodred, baza
									.dajProizvodeZaGrupu(grupe1, sifra));
					lvProizvodi.setAdapter(adapterProizvod);
				}
			}
		});
		dugme2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();

				if (prviNivo) {
					grupe1 = baza.dajDugmad(sifra, "2", false);
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
						grupe1 = baza.dajDugmad(sifra, pritisnutoDugme, true);
						//for (int i = 0; i < grupe1.size(); i++) {
							//if (grupe1.get(i).dajDugme().equals("2")){
								adapterProizvod = new AdapterProizvod(
										PregledProizvodaAktivnost.this,
										R.layout.proizvodred, baza
										.dajProizvodeZaGrupu(grupe1, sifra));
								lvProizvodi.setAdapter(adapterProizvod);
								//break;
							//}						
								
						//}
					}
				} else {
					grupe1 = baza.dajDugmad(sifra, pritisnutoDugme + "2", false);
					adapterProizvod = new AdapterProizvod(
							PregledProizvodaAktivnost.this,
							R.layout.proizvodred, baza
									.dajProizvodeZaGrupu(grupe1, sifra));
					lvProizvodi.setAdapter(adapterProizvod);
				}

			}
		});

		dugme3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();
				if (prviNivo) {
					grupe1 = baza.dajDugmad(sifra, "3", false);
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
						grupe1 = baza.dajDugmad(sifra, pritisnutoDugme, true);
						//for (int i = 0; i < grupe1.size(); i++) {
							//if (grupe1.get(i).dajDugme().equals("3")){
								adapterProizvod = new AdapterProizvod(
										PregledProizvodaAktivnost.this,
										R.layout.proizvodred, baza
										.dajProizvodeZaGrupu(grupe1,sifra));
								lvProizvodi.setAdapter(adapterProizvod);
								//break;
							//}						
								
						//}
					}
				} else {
					grupe1 = baza.dajDugmad(sifra, pritisnutoDugme + "3", false);
					adapterProizvod = new AdapterProizvod(
							PregledProizvodaAktivnost.this,
							R.layout.proizvodred, baza
									.dajProizvodeZaGrupu(grupe1,sifra));
					lvProizvodi.setAdapter(adapterProizvod);
				}
			}
		});

		dugme4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();
				if (prviNivo) {
					grupe1 = baza.dajDugmad(sifra, "4", false);
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
						grupe1 = baza.dajDugmad(sifra, pritisnutoDugme, true);
						//for (int i = 0; i < grupe1.size(); i++) {
							//if (grupe1.get(i).dajDugme().equals("4")){
								adapterProizvod = new AdapterProizvod(
										PregledProizvodaAktivnost.this,
										R.layout.proizvodred, baza
												.dajProizvodeZaGrupu(grupe1,sifra));
								lvProizvodi.setAdapter(adapterProizvod);
								//break;
							//}						
								
						//}
					}
				} else {
					grupe1 = baza.dajDugmad(sifra, pritisnutoDugme + "4", false);
					adapterProizvod = new AdapterProizvod(
							PregledProizvodaAktivnost.this,
							R.layout.proizvodred, baza
									.dajProizvodeZaGrupu(grupe1, sifra));
					lvProizvodi.setAdapter(adapterProizvod);
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
				grupe2 = baza.dajDugmad(sifra, "0", false);
				for (int i = 0; i < grupe2.size(); i++) {
					if (grupe2.get(i).dajDugme().equals("1"))
						dugme1.setText(grupe2.get(i).dajSkracenicu());
					else if (grupe2.get(i).dajDugme().equals("2"))
						dugme2.setText(grupe2.get(i).dajSkracenicu());
					else if (grupe2.get(i).dajDugme().equals("3"))
						dugme3.setText(grupe2.get(i).dajSkracenicu());
					else if (grupe2.get(i).dajDugme().equals("4"))
						dugme4.setText(grupe2.get(i).dajSkracenicu());
				}
				dugme1.setVisibility(View.VISIBLE);
				dugme2.setVisibility(View.VISIBLE);
				dugme3.setVisibility(View.VISIBLE);
				dugme4.setVisibility(View.VISIBLE);
				if (dugme1.getText().toString().equals("Button"))
					dugme1.setVisibility(View.INVISIBLE);
				if (dugme2.getText().toString().equals("Button"))
					dugme2.setVisibility(View.INVISIBLE);
				if (dugme3.getText().toString().equals("Button"))
					dugme3.setVisibility(View.INVISIBLE);
				if (dugme4.getText().toString().equals("Button"))
					dugme4.setVisibility(View.INVISIBLE);
				prviNivo = true;
				pritisnutoDugme = "0";
				lvProizvodi.setAdapter(null);
			}
		});

		// adapterProizvod=new AdapterProizvod(PregledProizvodaAktivnost.this,
		// R.layout.proizvodred,
		// baza.dajProizvodeZaProgram(pocetnaSifraPrograma));
		lvProizvodi = (ListView) findViewById(R.id.lvProizvodi);
		int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
    	lvProizvodi.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
    	lvProizvodi.setDividerHeight(2);
		btnNazad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		lvProizvodi.setAdapter(adapterProizvod);
		sProgrami.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int k, long l) {
				sifra = baza.dajSifruPrograma(sProgrami.getSelectedItem()
						.toString());
				ArrayList<Grupa> grupe1 = new ArrayList<Grupa>();
				grupe1 = baza.dajDugmad(sifra, "0", false);
				dugme1.setVisibility(View.INVISIBLE);
				dugme2.setVisibility(View.INVISIBLE);
				dugme3.setVisibility(View.INVISIBLE);
				dugme4.setVisibility(View.INVISIBLE);
				prviNivo = true;
				pritisnutoDugme = "0";
				/*
				 * for(int k=0; k<grupe.size(); k++){
				 * if(grupe.get(k).dajDugme().equals("1"))
				 * dugme1.setText(grupe.get(k).dajSkracenicu()); else
				 * if(grupe.get(k).dajDugme().equals("2"))
				 * dugme2.setText(grupe.get(k).dajSkracenicu()); else
				 * if(grupe.get(k).dajDugme().equals("3"))
				 * dugme3.setText(grupe.get(k).dajSkracenicu()); else
				 * if(grupe.get(k).dajDugme().equals("4"))
				 * dugme4.setText(grupe.get(k).dajSkracenicu()); }
				 * if(dugme1.getText().toString().equals("Button"))
				 * dugme1.setVisibility(View.INVISIBLE);
				 * if(dugme2.getText().toString().equals("Button"))
				 * dugme2.setVisibility(View.INVISIBLE);
				 * if(dugme3.getText().toString().equals("Button"))
				 * dugme3.setVisibility(View.INVISIBLE);
				 * if(dugme4.getText().toString().equals("Button"))
				 * dugme4.setVisibility(View.INVISIBLE);
				 */

				try {
					for (int i = 0; i < grupe1.size(); i++) {
						if (grupe1.get(i).dajDugme().equals("1")) {
							dugme1.setText(grupe1.get(i).dajSkracenicu());
							dugme1.setVisibility(View.VISIBLE);
							break;
						}
					}
				} catch (Exception ee) {
					dugme1.setVisibility(View.INVISIBLE);
				}
				try {
					for (int i = 0; i < grupe1.size(); i++) {
						if (grupe1.get(i).dajDugme().equals("2")) {
							dugme2.setText(grupe1.get(i).dajSkracenicu());
							dugme2.setVisibility(View.VISIBLE);
							break;
						}
					}
				} catch (Exception ee) {
					dugme2.setVisibility(View.INVISIBLE);
				}
				try {
					for (int i = 0; i < grupe1.size(); i++) {
						if (grupe1.get(i).dajDugme().equals("3")) {
							dugme3.setText(grupe1.get(i).dajSkracenicu());
							dugme3.setVisibility(View.VISIBLE);
							break;
						}
					}
				} catch (Exception ee) {
					dugme3.setVisibility(View.INVISIBLE);
				}
				try {
					for (int i = 0; i < grupe1.size(); i++) {
						if (grupe1.get(i).dajDugme().equals("4")) {
							dugme4.setText(grupe1.get(i).dajSkracenicu());
							dugme4.setVisibility(View.VISIBLE);
							break;
						}
					}
				} catch (Exception ee) {
					dugme4.setVisibility(View.INVISIBLE);
				}
				// adapterProizvod=new
				// AdapterProizvod(PregledProizvodaAktivnost.this,
				// R.layout.kupacred, baza.dajProizvodeZaProgram(sifra));
				// lvProizvodi.setAdapter(adapterProizvod);
				lvProizvodi.setAdapter(null);
			}

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			}
		});
	}
}