package org.comp.aktivnosti;

import java.util.ArrayList;

import org.comp.adapteri.AdapterZakljuceneNarudzbe;
import org.comp.klase.BazaPodataka;
import org.comp.klase.Narudzba;
import org.comp.klase.SlanjeNarudzbe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Prediction;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class PregledZakljucenihAktivnost extends Activity {
	private BazaPodataka baza;
	private ArrayList<Narudzba> narudzbe;
	private ListView lvZakljucene;
	private Button btnNazad;
	private Button btnBrisi;
	private Button btnPosalji;
	private ProgressDialog progressDialog;
	private AdapterZakljuceneNarudzbe adapter;
	private EditText txtUkupanIznos;
	private int provjera;
	private Activity activity;
	boolean svePoslate = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zakljucenenarudzbe);
		provjera = 0;
		setTitle("Zaključene narudžbe");
		activity = this;
		narudzbe = new ArrayList<Narudzba>();
		try {
			baza = new BazaPodataka(PregledZakljucenihAktivnost.this);
			narudzbe = baza.dajNarudzbe("zakljucena");
		} catch (Exception e) {
			Log.e("Greška baza", e.getMessage());
		}
		txtUkupanIznos = (EditText) findViewById(R.id.txtUkupanIznosZakljucene);
		if (narudzbe == null) {
			Toast.makeText(PregledZakljucenihAktivnost.this,
					"Nema zaključenih narudžbi!", Toast.LENGTH_LONG).show();
			txtUkupanIznos.setText("0.00");
		} else {
			lvZakljucene = (ListView) findViewById(R.id.lvZakljucene);
			int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
        	lvZakljucene.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        	lvZakljucene.setDividerHeight(2);
			adapter = new AdapterZakljuceneNarudzbe(
					PregledZakljucenihAktivnost.this,
					R.layout.zakljucenanarudzbared, narudzbe);
			lvZakljucene.setAdapter(adapter);
			txtUkupanIznos.setText(adapter.dajSumuNarudzbi());
		}
		

		btnNazad = (Button) findViewById(R.id.btnNazad33);
		btnPosalji = (Button) findViewById(R.id.btnPosalji33);

		btnPosalji.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (narudzbe == null)
					Toast.makeText(PregledZakljucenihAktivnost.this,
							"Nema zaključenih narudžbi!", Toast.LENGTH_LONG)
							.show();
				else if (adapter.dajIzabraneNarudzbe().size() == 0)
					Toast.makeText(PregledZakljucenihAktivnost.this,
							"Niste izabrali niti jednu narudžbu za slanje!",
							Toast.LENGTH_LONG).show();
				else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							PregledZakljucenihAktivnost.this);
					builder.setMessage(
							"Da li ste sigurni da želite poslati izabrane narudžbe?")
							.setCancelable(false)
							.setTitle("Slanje narudžbi")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton("Da",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// progressDialog =
											// ProgressDialog.show(PregledZakljucenihAktivnost.this,
											// "Šaljem narudžbe na server!",
											// "Molimo sačekajte...");
											// Thread thread = new
											// Thread(PregledZakljucenihAktivnost.this);
											// thread.start();
											new SlanjeNarudzbe(
													activity.getApplicationContext(),
													adapter.dajIzabraneNarudzbeZaSlanje()).execute(
													baza.dajIpAdresuServera(),
													baza.dajKorisnickoIme());
											finish();
											Intent in = new Intent(
													PregledZakljucenihAktivnost.this,
													NarudzbaAktivnost.class);
											startActivity(in);
										}
									})
							.setNegativeButton("Ne",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
		btnNazad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent i = new Intent(PregledZakljucenihAktivnost.this,
						NarudzbaAktivnost.class);
				startActivity(i);
			}
		});

		btnBrisi = (Button) findViewById(R.id.btnBrisi33);
		btnBrisi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (narudzbe == null)
					Toast.makeText(PregledZakljucenihAktivnost.this,
							"Nema zaključenih narudžbi!", Toast.LENGTH_LONG)
							.show();
				else if (adapter.dajIzabraneNarudzbe().size() == 0)
					Toast.makeText(PregledZakljucenihAktivnost.this,
							"Niste izabrali niti jednu narudžbu za brisanje!",
							Toast.LENGTH_LONG).show();
				else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							PregledZakljucenihAktivnost.this);
					builder.setMessage(
							"Da li ste sigurni da želite izbrisati izabrane narudžbe?")
							.setCancelable(false)
							.setTitle("Brisanje")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton("Da",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											ArrayList<Integer> lista = adapter
													.dajIzabraneNarudzbe();
											for (int i = 0; i < lista.size(); i++)
												baza.deleteZakljuceneNarudzbe(lista
														.get(i));
											Toast.makeText(
													PregledZakljucenihAktivnost.this,
													"Uspješno ste izbrisali izabrane narudžbe!",
													Toast.LENGTH_LONG).show();
											finish();
											Intent in = new Intent(
													PregledZakljucenihAktivnost.this,
													NarudzbaAktivnost.class);
											startActivity(in);
										}
									})
							.setNegativeButton("Ne",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
	}

	// @Override
	// public void run() {
	// TODO Auto-generated method stub
	// ArrayList<Integer> provjeraNarudzbi;
	// ArrayList<Narudzba> lista=adapter.dajIzabraneNarudzbeZaSlanje();
	// ArrayList<Narudzba> poslate=new ArrayList<Narudzba>();
	// svePoslate=true;
	// if(OrdroidActivity.imaLiKonekcije(PregledZakljucenihAktivnost.this)){
	// try{
	// WebServis.cont=this;
	// provjeraNarudzbi=WebServis.posaljiNarudzbu(baza.dajIpAdresuServera(),
	// lista, baza.dajKorisnickoIme());
	// for(int i=0; i<provjeraNarudzbi.size(); i++){
	// if(provjeraNarudzbi.get(i)!=1){
	// svePoslate=false;
	// break;
	// }
	// }
	// //baza.promjeniStatusNarudzbe(poslate);
	// provjera=1;
	// }catch(Exception e){
	// provjera=2;
	// }
	// }else
	// provjera=3;
	// handler.sendEmptyMessage(0);
	// }

	// ova metoda se izvrsava kada se zatvori progress bar
	// private Handler handler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// progressDialog.dismiss();
	// if(provjera==1 && svePoslate)
	// Toast.makeText(PregledZakljucenihAktivnost.this,
	// "Izabrane narudžbe su uspješno poslate na server!",
	// Toast.LENGTH_LONG).show();
	// else if(provjera==1 && !svePoslate)
	// Toast.makeText(PregledZakljucenihAktivnost.this,
	// "Neke narudžbe nisu uspješno poslate na server. Pokušajte ih ponovno poslati!",
	// Toast.LENGTH_LONG).show();
	// else if(provjera==2)
	// Toast.makeText(PregledZakljucenihAktivnost.this,
	// "Greška: Nije moguće uspostaviti vezu sa serverom!",
	// Toast.LENGTH_LONG).show();
	// else if(provjera==3)
	// Toast.makeText(PregledZakljucenihAktivnost.this,
	// "Nema konekcije na server!", Toast.LENGTH_LONG).show();
	// finish();
	// Intent in =new Intent(PregledZakljucenihAktivnost.this,
	// NarudzbaAktivnost.class);
	// startActivity(in);
	// }
	// };
}