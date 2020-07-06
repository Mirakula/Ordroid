package org.comp.aktivnosti;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.comp.klase.BazaPodataka;
import org.comp.klase.Kupac;
import org.comp.klase.Lokacija;
import org.comp.klase.Narudzba;
import org.comp.klase.Program;
import org.comp.klase.Rok_placanja;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NovaNarudzbaAktivnost extends Activity {

	public static AutoCompleteTextView sKupci;
	public static Spinner sProdajniProgrami;
	public static Spinner sLokacije;
	private Spinner sTipDokumenta;
	private Spinner sVrstaPlacanja;
	private Button btnNazad;
	private Button btnZapocni;
	private BazaPodataka baza;
	private EditText txtSkladiste;
	private EditText txtOrgBroj;
	private TextView tvRabat;
	private TextView tvDug;
	public static TextView tvRokPlacanja;
	private Button btnPlus10;
	private Button btnMinus10;
	private Button btnPlus1;
	private Button btnMinus1;
	private Button btnPodaciOKupcu;
	private ImageButton btnSkener;
	private ArrayAdapter<String> adapterProgrami;
	public static ArrayAdapter<String> adapterKupci;
	public static ArrayAdapter<String> adapterLokacije;
	private int brojDana;
	public static Narudzba narudzba;
	public static String izabranaFirma;
	private int selektovaniKupac = -1;
	private int selektovaniProgram = -1;
	private int selektovanaLokacija = -1;
	private int selektovaniNacinPlacanja = -1;
	private int selektovanaVrstaDokumenta = -1;
	
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meninovanarudzba);
		setTitle("Zaglavlje dokumenta");
		try {
			baza = new BazaPodataka(NovaNarudzbaAktivnost.this);
		} catch (Exception e) {
			Log.e("Greška baza", e.getMessage());
		}
		final SharedPreferences prefs = this.getSharedPreferences(
				"org.comp.paket", Context.MODE_PRIVATE);
		final ArrayAdapter vrstePlacanjaAdapter = ArrayAdapter
				.createFromResource(NovaNarudzbaAktivnost.this,
						R.array.placanje,
						android.R.layout.simple_dropdown_item_1line);
		final ArrayAdapter tipDokumentaAdapter = ArrayAdapter
				.createFromResource(NovaNarudzbaAktivnost.this,
						R.array.dokument,
						android.R.layout.simple_dropdown_item_1line);
		sProdajniProgrami = (Spinner) findViewById(R.id.sProdajniProgramiNarudzba);
		sLokacije = (Spinner) findViewById(R.id.sLokacijaNarudzba);
		sVrstaPlacanja = (Spinner) findViewById(R.id.sNacinPlacanja);
		sTipDokumenta = (Spinner) findViewById(R.id.sTipDokumenta);
		sKupci = (AutoCompleteTextView) findViewById(R.id.acKupac);
		btnPlus10 = (Button) findViewById(R.id.btnPlus10RokPlacanja);
		btnPlus1 = (Button) findViewById(R.id.btnPlus1RokPlacanja);
		btnMinus10 = (Button) findViewById(R.id.btnMinus10RokPlacanja);
		btnMinus1 = (Button) findViewById(R.id.btnMinus1RokPlacanja);
		btnPodaciOKupcu = (Button) findViewById(R.id.btnPodaciKupca);
		tvRabat = (TextView) findViewById(R.id.tvRabatKupac);
		tvDug = (TextView) findViewById(R.id.tvDugKupca);
		tvRokPlacanja = (TextView) findViewById(R.id.tvRokPlacanja);
		txtSkladiste = (EditText) findViewById(R.id.txtSkladiste);
		txtOrgBroj = (EditText) findViewById(R.id.txtOrginalniBroj);
		
		btnSkener = (ImageButton) findViewById(R.id.btnSkener);
		
		adapterProgrami = new ArrayAdapter<String>(NovaNarudzbaAktivnost.this,
				android.R.layout.simple_spinner_item, baza.dajNazivePrograma());
		sProdajniProgrami.setAdapter(adapterProgrami);
		String pocetnaSifraPrograma = baza.dajSifruPrograma(sProdajniProgrami
				.getSelectedItem().toString());
		if (!getIntent().getExtras().getBoolean("otvorena")) {
			adapterKupci = new ArrayAdapter<String>(NovaNarudzbaAktivnost.this,
					android.R.layout.simple_spinner_item,
					baza.dajNaziveKupacaZaProgram(pocetnaSifraPrograma));
			sKupci.setAdapter(adapterKupci);
			adapterLokacije = new ArrayAdapter<String>(
					NovaNarudzbaAktivnost.this,
					android.R.layout.simple_spinner_item,
					baza.dajNaziveLokacijaZaKupca(sKupci.getText().toString()));
			// rok placanja
			 tvRokPlacanja.setText("1");
			
			selektovaniKupac = 0;
			selektovanaLokacija = 0;
			selektovanaVrstaDokumenta = 0;
			selektovaniNacinPlacanja = 0;
			selektovaniProgram = 0;
		} else {
			sKupci.setText(narudzba.dajKupca().dajNaziv());
			adapterLokacije = NarucivanjeAktivnost.adapterLokacije;
			tvRokPlacanja.setText(String.valueOf(narudzba.dajRokPlacanja()));
			selektovanaLokacija = adapterLokacije
					.getPosition(narudzba.dajLokaciju()
							.dajNazivLokacije());
			sLokacije.setSelection(selektovanaLokacija);
			if (!narudzba.dajSkladiste().equals(""))
				txtSkladiste.setText(narudzba.dajSkladiste());
			if (!narudzba.DajOrginalniBroj().equals(""))
				txtOrgBroj.setText(narudzba.DajOrginalniBroj());
		}
		sKupci.setThreshold(0);			
		
		if (getIntent().getExtras().getBoolean("otvorena")) {

			sKupci.setText(narudzba.dajKupca().dajNaziv());
			sProdajniProgrami.setEnabled(false);
			sKupci.setEnabled(false);
			sVrstaPlacanja.setEnabled(false);
			sTipDokumenta.setEnabled(false);
			tvRokPlacanja.setText("" + narudzba.dajRokPlacanja());
			selektovanaLokacija = adapterLokacije
					.getPosition(narudzba.dajLokaciju()
							.dajNazivLokacije());
			sLokacije.setSelection(selektovanaLokacija);
		}
		txtSkladiste.setText(prefs.getString("skladiste", ""));
		sLokacije.setAdapter(adapterLokacije);
		/*
		selektovanaLokacija = adapterLokacije
				.getPosition(narudzba.dajLokaciju()
						.dajNazivLokacije());
		sLokacije.setSelection(selektovanaLokacija);
		*/
		sTipDokumenta.setAdapter(tipDokumentaAdapter);
		sTipDokumenta.setSelection(1);
		sVrstaPlacanja.setAdapter(vrstePlacanjaAdapter);

		btnNazad = (Button) findViewById(R.id.btnOdustaniNarudzbu);
		btnNazad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getIntent().getExtras().getBoolean("otvorena")) {
					finish();
				} else {
					finish();
					Intent i = new Intent(NovaNarudzbaAktivnost.this,
							NarudzbaAktivnost.class);
					startActivity(i);
				}
			}
		});

		sKupci.setHintTextColor(Color.RED);
		sKupci.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				try {
					Kupac pocetniKupac;
					tvRabat.setText("");
					tvDug.setText("");
					adapterLokacije.clear();
					sLokacije.setAdapter(adapterLokacije);
					pocetniKupac = baza.dajKupca(sKupci.getText().toString(),
							sProdajniProgrami.getSelectedItem().toString());
					tvRabat.setText(pretvoriuDecimalni(
							pocetniKupac.dajOsnovniPopust(), true)
							+ " %");
					tvDug.setText(pretvoriuDecimalni(
							pocetniKupac.dajUkupniKredit(), false)
							+ " KM");

					adapterLokacije = new ArrayAdapter<String>(
							NovaNarudzbaAktivnost.this,
							android.R.layout.simple_spinner_item, baza
									.dajNaziveLokacijaZaKupca(sKupci.getText()
											.toString()));
					sLokacije.setAdapter(adapterLokacije);
					Log.d("pred racunanje"," roka placanja");
					String sifra_kupca = baza.dajKupca(sKupci.getText().toString(),sProdajniProgrami.getSelectedItem().toString()).dajSifru();
					Log.d("sifra kupca rp", sifra_kupca);
					String sifra_programa = baza.dajSifruPrograma(sProdajniProgrami
							.getSelectedItem().toString());
					Log.d("sifra programa", " " + sifra_programa);
					int rokPlacanja = baza.dajRokPlacanjaZaFirmu(sifra_kupca, sifra_programa);
					tvRokPlacanja.setText("" + rokPlacanja);
					Log.d("Ucitan rok placanja:","" + tvRokPlacanja.getText());			
					if (narudzba != null) {
						selektovanaLokacija = adapterLokacije
								.getPosition(narudzba.dajLokaciju()
										.dajNazivLokacije());
						sLokacije.setSelection(selektovanaLokacija);
					}
					if (getIntent().getExtras().getBoolean("otvorena")) {
						selektovanaLokacija = adapterLokacije
								.getPosition(narudzba.dajLokaciju()
										.dajNazivLokacije());
						sLokacije.setSelection(selektovanaLokacija);
					}
				} catch (Exception ew) {
				}
			}
		});
		
		sKupci.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvDug.setText("");
				tvRabat.setText("");
				adapterLokacije.clear();
				sLokacije.setAdapter(adapterLokacije);
				sKupci.showDropDown();
				
			}			
			
		});

		sTipDokumenta.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int i, long l) {
				if (getIntent().getExtras().getBoolean("otvorena")) {
					if (selektovanaVrstaDokumenta != i) {
						int id = tipDokumentaAdapter.getPosition(narudzba
								.dajTipDokumenta());
						sTipDokumenta.setSelection(id);
					} else
						selektovanaVrstaDokumenta = i;
				} else {
					if (i == 0) {
						sVrstaPlacanja.setEnabled(false);
						sVrstaPlacanja.setSelection(0);
						tvRokPlacanja.setText("0");
					} else {
						sVrstaPlacanja.setEnabled(true);
						sVrstaPlacanja.setSelection(1);
					}
				}
				//tvRokPlacanja.setText("1");
				btnMinus1.setEnabled(true);
				btnMinus10.setEnabled(true);
				btnPlus1.setEnabled(true);
				btnPlus10.setEnabled(true);
			}

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			}
		});

		sVrstaPlacanja.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int i, long l) {
				if (getIntent().getExtras().getBoolean("otvorena")) {
					if (i != selektovaniNacinPlacanja) {
						int id = vrstePlacanjaAdapter.getPosition(narudzba
								.dajVrstuPlacanja());
						sVrstaPlacanja.setSelection(id);
					} else
						selektovaniNacinPlacanja = i;
				}
				if (sVrstaPlacanja.getSelectedItem().toString()
						.equals("Gotovina")
						&& sTipDokumenta.getSelectedItem().toString()
								.equals("Veleprodaja")) {
					tvRokPlacanja.setText("0");
					btnMinus1.setEnabled(false);
					btnMinus10.setEnabled(false);
					btnPlus1.setEnabled(false);
					btnPlus10.setEnabled(false);
				} else {
					//tvRokPlacanja.setText("1");
					btnMinus1.setEnabled(true);
					btnMinus10.setEnabled(true);
					btnPlus1.setEnabled(true);
					btnPlus10.setEnabled(true);
				}
			}

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			}
		});

		sProdajniProgrami
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> adapterView,
							View view, int i, long l) {
						String pocetnaSifraPrograma;
						if (selektovaniProgram != i
								&& getIntent().getExtras().getBoolean(
										"otvorena")) {
							int id = adapterProgrami.getPosition(narudzba
									.dajProgram().dajNaziv());
							pocetnaSifraPrograma = narudzba.dajProgram()
									.dajSifru();
							sProdajniProgrami.setSelection(id);
						} else {
							selektovaniProgram = i;
							pocetnaSifraPrograma = baza
									.dajSifruPrograma(sProdajniProgrami
											.getSelectedItem().toString());
							sKupci.setText("");
						}
						adapterKupci = new ArrayAdapter<String>(
								NovaNarudzbaAktivnost.this,
								android.R.layout.simple_spinner_item,
								baza.dajNaziveKupacaZaProgram(pocetnaSifraPrograma));
						sKupci.setAdapter(adapterKupci);
						
						adapterLokacije.clear();
						sLokacije.setAdapter(adapterLokacije);
						if (!sKupci.getText().toString().equals("")) {
							Kupac pocetniKupac = baza.dajKupca(sKupci.getText()
									.toString(), sProdajniProgrami
									.getSelectedItem().toString());

							tvRabat.setText(pretvoriuDecimalni(
									pocetniKupac.dajOsnovniPopust(), true)
									+ "  %");
							tvDug.setText(pocetniKupac.dajUkupniKredit()
									+ " KM");

							adapterLokacije = new ArrayAdapter<String>(
									NovaNarudzbaAktivnost.this,
									android.R.layout.simple_spinner_item, baza
											.dajNaziveLokacijaZaKupca(sKupci
													.getText().toString()));
							sLokacije.setAdapter(adapterLokacije);
							selektovanaLokacija = adapterLokacije
									.getPosition(narudzba.dajLokaciju()
											.dajNazivLokacije());
							sLokacije.setSelection(selektovanaLokacija);
						}
					}

					public void onNothingSelected(AdapterView<?> adapterView) {
						return;
					}
				});

		btnZapocni = (Button) findViewById(R.id.btnZapocniNarudzbu);
		if (getIntent().getExtras().getBoolean("otvorena"))
			btnZapocni.setText("Spasi promjene");
		btnZapocni.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getIntent().getExtras().getBoolean("otvorena")) {
					if (txtSkladiste.getText().toString().length() != 6
							&& !txtSkladiste.getText().toString().equals(""))
						Toast.makeText(NovaNarudzbaAktivnost.this,
								"Šifra skladišta mora imati 6 cifara!",
								Toast.LENGTH_LONG).show();
					else if (Integer
							.valueOf(tvRokPlacanja.getText().toString()) < 0)
						Toast.makeText(NovaNarudzbaAktivnost.this,
								"Rok plaćanja ne može biti manji od 0!",
								Toast.LENGTH_LONG).show();
					else {
						Lokacija l = baza.dajLokaciju(sLokacije
								.getSelectedItem().toString(), narudzba
								.dajKupca().dajSifru());
						narudzba.postaviMIS(l);
						narudzba.postaviSkladiste(txtSkladiste.getText()
								.toString());
						narudzba.postaviRokPlacanja(Integer
								.valueOf(tvRokPlacanja.getText().toString()));
						narudzba.postaviOrginalniBroj(txtOrgBroj.getText()
								.toString());
						NarucivanjeAktivnost.narudzba = narudzba;

						prefs.edit()
								.putString("skladiste",
										txtSkladiste.getText().toString())
								.commit();

						Toast.makeText(NovaNarudzbaAktivnost.this,
								"Uspješno ste spasili promjene!",
								Toast.LENGTH_LONG).show();
						finish();
					}
				} else {
					if (txtSkladiste.getText().toString().length() != 6
							&& !txtSkladiste.getText().toString().equals(""))
						Toast.makeText(NovaNarudzbaAktivnost.this,
								"Šifra skladišta mora imati 6 cifara!",
								Toast.LENGTH_LONG).show();
					else if (Integer
							.valueOf(tvRokPlacanja.getText().toString()) < 0)
						Toast.makeText(NovaNarudzbaAktivnost.this,
								"Rok plaćanja ne može biti manji od 0!",
								Toast.LENGTH_LONG).show();
					else {

						AlertDialog.Builder builder = new AlertDialog.Builder(
								NovaNarudzbaAktivnost.this);
						builder.setMessage(
								"Da biste započeli narudžbu morate prvo spasiti zaglavlje dokumenta. Da li želite nastaviti?")
								.setCancelable(false)
								.setTitle("Zaglavlje dokumenta")
								.setIcon(android.R.drawable.ic_dialog_alert)
								.setPositiveButton("Da",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												try {
													Kupac k = baza
															.dajKupca(
																	sKupci.getText()
																			.toString(),
																	sProdajniProgrami
																			.getSelectedItem()
																			.toString());

													Lokacija l = baza
															.dajLokaciju(
																	sLokacije
																			.getSelectedItem()
																			.toString(),
																	k.dajSifru());
													Program p = baza
															.dajProgram(sProdajniProgrami
																	.getSelectedItem()
																	.toString());
													if (sTipDokumenta
															.getSelectedItem()
															.toString()
															.equals("Maloprodaja"))
														k.postaviRabat();
													narudzba = new Narudzba(
															k,
															l,
															txtSkladiste
																	.getText()
																	.toString(),
															sTipDokumenta
																	.getSelectedItem()
																	.toString(),
															sVrstaPlacanja
																	.getSelectedItem()
																	.toString(),
															p,
															"Nova",
															Integer.valueOf(tvRokPlacanja
																	.getText()
																	.toString()));
													narudzba.postaviOrginalniBroj(txtOrgBroj
															.getText()
															.toString());
													prefs.edit()
															.putString(
																	"skladiste",
																	txtSkladiste
																			.getText()
																			.toString())
															.commit();

													dialog.dismiss();
													Toast.makeText(
															NovaNarudzbaAktivnost.this,
															"Spasili ste zaglavlje dokumenta!",
															Toast.LENGTH_LONG)
															.show();
													Intent i = new Intent(
															NovaNarudzbaAktivnost.this,
															NarucivanjeAktivnost.class);
													i.putExtra("otvorena",
															false);
													startActivity(i);
													NarucivanjeAktivnost.narudzba = narudzba;
													finish();
												} catch (Exception ew) {
													Toast.makeText(
															getApplicationContext(),
															"Neispravan naziv kupca!!",
															Toast.LENGTH_LONG)
															.show();
												}
											}
										})
								.setNegativeButton("Ne",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}
				}
			}
		});

		btnPodaciOKupcu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sKupci.getText().toString().length() > 3) {
					Kupac kupac = baza.dajKupca(sKupci.getText().toString(),
							sProdajniProgrami.getSelectedItem().toString());
					Log.d("KUPAC", sKupci.getText().toString());
					if (kupac != null) {
						final Dialog dialogKupci = new Dialog(
								NovaNarudzbaAktivnost.this);
						dialogKupci.setContentView(R.layout.podacikupca);
						android.view.WindowManager.LayoutParams params = dialogKupci
								.getWindow().getAttributes();
						params.width = android.view.WindowManager.LayoutParams.FILL_PARENT;
						dialogKupci
								.getWindow()
								.setAttributes(
										(android.view.WindowManager.LayoutParams) params);
						dialogKupci.setTitle("Podaci o kupcu");
						TextView tvNazivKupca1 = (TextView) dialogKupci
								.findViewById(R.id.tvNazivKupca);
						TextView tvAdresaKupca = (TextView) dialogKupci
								.findViewById(R.id.tvAdresaKupca);
						TextView tvMjestoKupca = (TextView) dialogKupci
								.findViewById(R.id.tvMjestoKupca);
						TextView tvIDbroj = (TextView) dialogKupci
								.findViewById(R.id.tvIDbroj);
						TextView tvPDVid = (TextView) dialogKupci
								.findViewById(R.id.tvPDVid);
						TextView tvRjesenje = (TextView) dialogKupci
								.findViewById(R.id.tvRjesenje);
						TextView tvUkPotrazivanje = (TextView) dialogKupci
								.findViewById(R.id.tvUkPotrazivanje);
						TextView tvDospjelo = (TextView) dialogKupci
								.findViewById(R.id.tvDospjelo);
						TextView tvDaniKasnjenja = (TextView) dialogKupci
								.findViewById(R.id.tvDaniKasnjenja);
						TextView tvOsnovniRabat = (TextView) dialogKupci
								.findViewById(R.id.tvOsnovniRabat);
						Button btnZatvori = (Button) dialogKupci
								.findViewById(R.id.btnZatvoriKupca);

						tvNazivKupca1.setText(kupac.dajNaziv());
						tvAdresaKupca.setText(kupac.dajAdresu());
						tvMjestoKupca.setText(kupac.dajMjesto());
						tvIDbroj.setText(kupac.dajKupacIdbroj());
						tvPDVid.setText(kupac.dajKupacVATbroj());
						tvRjesenje.setText(kupac.dajKupacSudBroj());
						tvUkPotrazivanje.setText(pretvoriuDecimalni(
								kupac.dajUkupniKredit(), false)
								+ " KM");
						tvDospjelo.setText(pretvoriuDecimalni(
								kupac.dajDospjeliKredit(), false)
								+ " KM");
						tvDaniKasnjenja.setText(kupac.dajPosljednjuUplatu());
						tvOsnovniRabat.setText(pretvoriuDecimalni(
								kupac.dajOsnovniPopust(), true)
								+ "  %");

						btnZatvori.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialogKupci.dismiss();
							}
						});

						dialogKupci.show();
					}
				}
			}
		});

		btnPlus1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				brojDana = Integer.valueOf(tvRokPlacanja.getText().toString());
				brojDana += 1;
				tvRokPlacanja.setText(String.valueOf(brojDana));
			}
		});

		btnPlus10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				brojDana = Integer.valueOf(tvRokPlacanja.getText().toString());
				brojDana += 10;
				tvRokPlacanja.setText(String.valueOf(brojDana));
			}
		});

		btnMinus1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				brojDana = Integer.valueOf(tvRokPlacanja.getText().toString());
				brojDana -= 1;
				if(brojDana <= 0) brojDana = 0;
				tvRokPlacanja.setText(String.valueOf(brojDana));
			}
		});

		btnMinus10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				brojDana = Integer.valueOf(tvRokPlacanja.getText().toString());
				brojDana -= 10;
				if(brojDana <= 0) brojDana = 0;
				tvRokPlacanja.setText(String.valueOf(brojDana));
			}
		});

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
	
	public void scanBar(View v) {
		try {
			//start the scanning activity from the com.google.zxing.client.android.SCAN intent
		    Intent intent = new Intent(ACTION_SCAN);
		    intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
		    intent.putExtra("SCAN_FORMATS", "CODE_128");
		    startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
		            //on catch, show the download dialog
			showDialog(NovaNarudzbaAktivnost.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), "Instalirajte barcode skener.", Toast.LENGTH_SHORT).show();
		}
    }

	private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);
		downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				try {
					act.startActivity(intent);
				} catch (ActivityNotFoundException anfe) {
					
				}
			}

		});
		downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				
			}
		});
		return downloadDialog.show();
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			String contents = intent.getStringExtra("SCAN_RESULT");
			txtOrgBroj.setText(contents);
		}
	}
	
}
