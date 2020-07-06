package org.comp.aktivnosti;

import java.util.ArrayList;

import org.comp.adapteri.AdapterProgrami;
import org.comp.klase.BazaPodataka;
import org.comp.klase.OsvjeziAkcijskePopuste;
import org.comp.klase.Program;
import org.comp.klase.SlanjeNarudzbe;
import org.comp.klase.WebServis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UcitavanjePodatakaAktivnost extends Activity implements Runnable {
	private BazaPodataka baza;
	private ProgressDialog progressDialog;
	private ListView lvProgramiSaServera;
	private ListView lvIzabraniProgrami;
	private Button btnNazad;
	private Button btnNastavi;
	private Button btnOdaberi;
	private Button btnUkini;
	private Button btnUcitajPrograme;
	private RadioButton rBtnSve;
	private RadioButton rBtnAsortiman;
	private RadioButton rBtnKupci;
	private RadioButton rBtnFinansije;
	private RadioGroup radioGrupa;
	private CheckBox cbUcitajPodatke;
	private AdapterProgrami adapter1;
	private AdapterProgrami adapter2;
	private ArrayList<Program> dodati;
	private ArrayList<Program> izbrisani;
	private boolean ponovo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ucitavanjepodataka);
		setTitle("Učitavanje podataka");
		try {
			baza = new BazaPodataka(UcitavanjePodatakaAktivnost.this);
		} catch (Exception e) {
			Log.e("Greška baza", e.getMessage());
		}
		lvIzabraniProgrami = (ListView) findViewById(R.id.lvIzabraniProgrami);
		lvProgramiSaServera = (ListView) findViewById(R.id.lvProgramiSaServera);
		
		System.out.println(lvProgramiSaServera);
		
		int[] colors = { 0, 0xFFFF0000, 0 };
		lvIzabraniProgrami.setDivider(new GradientDrawable(
				Orientation.RIGHT_LEFT, colors));
		lvIzabraniProgrami.setDividerHeight(2);

		lvProgramiSaServera.setDivider(new GradientDrawable(
				Orientation.RIGHT_LEFT, colors));
		lvProgramiSaServera.setDividerHeight(2);

		adapter1 = new AdapterProgrami(UcitavanjePodatakaAktivnost.this,
				R.layout.programired, baza.dajPrograme("0"));
		lvProgramiSaServera.setAdapter(adapter1);
		adapter2 = new AdapterProgrami(UcitavanjePodatakaAktivnost.this,
				R.layout.programired, baza.dajPrograme("1"));
		lvIzabraniProgrami.setAdapter(adapter2);
		btnOdaberi = (Button) findViewById(R.id.btnOdaberi);
		btnUkini = (Button) findViewById(R.id.btnUkini);
		btnNazad = (Button) findViewById(R.id.btnNazad78);
		btnUcitajPrograme = (Button) findViewById(R.id.btnUcitajProgrameSaServera);

		rBtnSve = (RadioButton) findViewById(R.id.rBsve);
		rBtnAsortiman = (RadioButton) findViewById(R.id.rBasortiman);
		rBtnKupci = (RadioButton) findViewById(R.id.rBkupci);
		rBtnFinansije = (RadioButton) findViewById(R.id.rBfinansije);

		radioGrupa = (RadioGroup) findViewById(R.id.radGroup);

		cbUcitajPodatke = (CheckBox) findViewById(R.id.cBOsvjezi);

		ponovo = false;
		ponovo = getIntent().getExtras().getBoolean("ponovo");
		if (baza.dajPrograme("1").size() == 0
				&& baza.dajPrograme("0").size() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					UcitavanjePodatakaAktivnost.this);
			builder.setMessage(
					"Lokalna baza je prazna. Želite li povući podatke sa servera?")
					.setCancelable(false)
					.setTitle("Upozorenje")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("Da",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// ako se potvrdi da se zeli odjaviti
									// zavrsava se aktivnost i vracamo se
									// na login formu
									dialog.dismiss();
									rBtnAsortiman.setEnabled(false);
									rBtnFinansije.setEnabled(false);
									rBtnKupci.setEnabled(false);
									rBtnSve.setEnabled(false);
									rBtnSve.setChecked(true);
									cbUcitajPodatke.setChecked(true);
									cbUcitajPodatke.setEnabled(false);
								}
							})
					.setNegativeButton("Ne",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// ako se ipak ne zeli odjaviti sa sistema,
									// samo se sakriva dijalog
									// i nastavlja se sa radom
									dialog.cancel();
									Toast.makeText(
											UcitavanjePodatakaAktivnost.this,
											"Lokalna baza je prazna. Kraj rada!",
											Toast.LENGTH_LONG).show();
									finish();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

		radioGrupa
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {

						for (int i = 0; i < arg0.getChildCount(); i++) {
							if (rBtnSve.getId() == arg1) {

								return;
							} else if (rBtnAsortiman.getId() == arg1) {
								return;
							} else if (rBtnKupci.getId() == arg1) {
								return;
							} else if (rBtnFinansije.getId() == arg1) {
								return;
							}
						}
					}
				});

		cbUcitajPodatke
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (!isChecked)
							radioGrupa.setEnabled(false);
						else
							radioGrupa.setEnabled(true);
					}
				});

		btnNazad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btnNastavi = (Button) findViewById(R.id.btnNastavi);
		btnNastavi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					if (OrdroidActivity
							.imaLiKonekcije(UcitavanjePodatakaAktivnost.this)) {
						progressDialog = ProgressDialog.show(
								UcitavanjePodatakaAktivnost.this,
								"Dobavljanje podataka", "Molimo sačekajte...");

						Thread thread = new Thread(
								UcitavanjePodatakaAktivnost.this);
						thread.start();
					}				
					else if (!OrdroidActivity
						.imaLiKonekcije(UcitavanjePodatakaAktivnost.this)
						&& cbUcitajPodatke.isChecked()) {
					Toast.makeText(UcitavanjePodatakaAktivnost.this,
							"Nema konekcije na server!", Toast.LENGTH_LONG)
							.show();
					if (getIntent().getExtras().getBoolean("ponovo"))
						finish();
					else {
						Intent i = new Intent(UcitavanjePodatakaAktivnost.this,
								PocetnaAktivnost.class);
						startActivity(i);
						finish();
					}
				} else {
					if (getIntent().getExtras().getBoolean("ponovo"))
						finish();
					else {
						Intent i = new Intent(UcitavanjePodatakaAktivnost.this,
								PocetnaAktivnost.class);
						startActivity(i);
						finish();
					}
				}
			}
		});

		btnOdaberi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dodati = new ArrayList<Program>();
				ArrayList<String> izabrani = new ArrayList<String>();
				izabrani = adapter1.dajIzabranePrograme();
				for (int i = 0; i < izabrani.size(); i++) {
					if (adapter2.imaLiPrograma(izabrani.get(i)))
						continue;
					else {
						Program p = new Program(adapter1.dajProgram(izabrani
								.get(i)));
						p.izaberi(false);
						adapter2.dodajProgram(p);
						adapter1.izbrisiProgram(izabrani.get(i));
						dodati.add(p);
					}
				}
				baza.prikaziPrograme(dodati);
			}
		});

		btnUkini.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				izbrisani = new ArrayList<Program>();
				ArrayList<String> izabrani = new ArrayList<String>();
				izabrani = adapter2.dajIzabranePrograme();
				for (int i = 0; i < izabrani.size(); i++) {
					Program p = new Program(
							adapter2.dajProgram(izabrani.get(i)));
					p.izaberi(false);
					adapter2.izbrisiProgram(izabrani.get(i));
					adapter1.dodajProgram(p);
					izbrisani.add(p);
					// dodati.remove(p);
				}
				baza.sakrijPrograme(izbrisani);
			}
		});

		btnUcitajPrograme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (OrdroidActivity
						.imaLiKonekcije(UcitavanjePodatakaAktivnost.this)) {
					try {
						adapter1 = new AdapterProgrami(
								UcitavanjePodatakaAktivnost.this,
								R.layout.programired, WebServis.DajPrograme(
										baza.dajIpAdresuServera(),
										baza.dajKorisnickoIme()));
						lvProgramiSaServera.setAdapter(adapter1);
					} catch (Exception e) {
						Toast.makeText(
								UcitavanjePodatakaAktivnost.this,
								"Greška: Nije moguće uspostaviti vezu sa serverom!",
								Toast.LENGTH_LONG).show();
					}
				} else
					Toast.makeText(UcitavanjePodatakaAktivnost.this,
							"Nema konekcije na server!", Toast.LENGTH_LONG)
							.show();
			}
		});
	}

	// ova metoda se poziva kada se prikaze progress bar i ona se izvrsava u
	// pozadini
	// kada se ove metoda izvrsi progress bar se zatvori
	@Override
	public void run() {
		try {
			if (cbUcitajPodatke.isChecked() && rBtnSve.isChecked()) {
				ArrayList<String> ppp = new ArrayList<String>();
				ppp = baza.dajSifrePrograma();
				baza.izbrisiSveKupce();
				baza.izbrisiSveLokacije();
				baza.izbrisiSveGrupe();
				baza.izbrisiSveProizvode();
				baza.izbrisiAkcijskePopuste();
				
				for (int i = 0; i < ppp.size(); i++) {
					baza.ucitajKupce(ppp.get(i));
					baza.ucitajLokacije(ppp.get(i));
					baza.ucitajProizvode(ppp.get(i));
					baza.ucitajGrupe(ppp.get(i));
					
				}
				baza.ucitajAkcijskePopuste();
			} else if (cbUcitajPodatke.isChecked() && rBtnKupci.isChecked()) {
				ArrayList<String> ppp = new ArrayList<String>();
				ppp = baza.dajSifrePrograma();
				baza.izbrisiSveKupce();
				baza.izbrisiSveLokacije();
				for (int i = 0; i < ppp.size(); i++) {
					baza.ucitajKupce(ppp.get(i));
					baza.ucitajLokacije(ppp.get(i));
				}
			} else if (cbUcitajPodatke.isChecked() && rBtnAsortiman.isChecked()) {
				ArrayList<String> ppp = new ArrayList<String>();
				ppp = baza.dajSifrePrograma();
				baza.izbrisiSveGrupe();
				baza.izbrisiSveProizvode();
				for (int i = 0; i < ppp.size(); i++) {
					baza.ucitajProizvode(ppp.get(i));
					baza.ucitajGrupe(ppp.get(i));
				}
			} else if (cbUcitajPodatke.isChecked() && rBtnFinansije.isChecked()) {
				ArrayList<String> ppp = new ArrayList<String>();
				ppp = baza.dajSifrePrograma();
				baza.izbrisiSveKupce();
				for (int i = 0; i < ppp.size(); i++) {
					baza.ucitajKupce(ppp.get(i));
				}
			}
			baza.izbrisiSveRokovePlacanja();
			ArrayList<String> ppp = new ArrayList<String>();
			ppp = baza.dajSifrePrograma();
			for (int i = 0; i < ppp.size(); i++) {
				baza.ucitajRokovePlacanja(ppp.get(i));
			}
			
		} catch (SQLException ex) {
			Log.e("GREŠKA", "ucitavanje firmi " + ex.getMessage());
		}
		handler.sendEmptyMessage(0);
	}

	// ova metoda se izvrsava kada se zatvori progress bar
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();

			if (ponovo)
				finish();
			else {
				Intent i = new Intent(UcitavanjePodatakaAktivnost.this,
						PocetnaAktivnost.class);
				startActivity(i);
				finish();
			}
		}
	};

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (!getIntent().getExtras().getBoolean("ponovo"))
			menu.getItem(0).setVisible(false);
		return super.onPrepareOptionsMenu(menu);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.meni_akcijski_popust, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.meniAkcija:
			if (getIntent().getExtras().getBoolean("ponovo")) {
				new OsvjeziAkcijskePopuste(this).execute();
				finish();
			}
			break;
		}
		return true;
	}
}
