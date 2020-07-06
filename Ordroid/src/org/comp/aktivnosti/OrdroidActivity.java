package org.comp.aktivnosti;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.comp.klase.BazaPodataka;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OrdroidActivity extends Activity implements Runnable {
	private Button btnIzlaz;
	private Button btnPrijava;
	private ProgressDialog progressDialog;
	private static BazaPodataka bP;
	private EditText txtKorisnickoIme;
	private EditText txtLozinka;
	public static boolean admin;
	public static String username;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			bP = new BazaPodataka(OrdroidActivity.this);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Log.e("Greška 123", e1.getMessage());
		}
		if (bP.jeLiLogovan()) {
			Intent i = new Intent(OrdroidActivity.this,
					UcitavanjePodatakaAktivnost.class);
			i.putExtra("ponovo", false);
			startActivity(i);
			username = bP.dajKorisnickoIme();
			finish();
		} else {
			setContentView(R.layout.main);
			admin = false;

			btnPrijava = (Button) findViewById(R.id.btnPrijava);
			btnIzlaz = (Button) findViewById(R.id.btnIzlaz);
			txtKorisnickoIme = (EditText) findViewById(R.id.txtKorisnickoIme);
			txtLozinka = (EditText) findViewById(R.id.txtLozinka);
			btnIzlaz.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});

			btnPrijava.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						if (txtKorisnickoIme.getText().toString().equals("")
								&& txtLozinka.getText().toString().equals(""))
							PrikaziPorukuOGresci("Morate unijeti korisničko ime i šifru!");
						else if (txtKorisnickoIme.getText().toString()
								.equals("admin")
								&& txtLozinka.getText().toString()
										.equals("postavke")) {
							Intent i = new Intent(OrdroidActivity.this,
									PostavkeAktivnost.class);
							startActivity(i);
							admin = true;
						} else {
							if (bP.dajIpAdresuServera().equals(""))
								PrikaziPorukuOGresci("Nije unešena IP adresa servera!");
							else {

								if (txtKorisnickoIme.getText().toString()
										.equals(""))
									PrikaziPorukuOGresci("Niste unijeli korisničko ime!");
								else if (txtLozinka.getText().toString()
										.equals(""))
									PrikaziPorukuOGresci("Niste unijeli šifru!");
								else if (bP.provjeriKorisnika(txtKorisnickoIme
										.getText().toString(), txtLozinka
										.getText().toString())) {
									progressDialog = ProgressDialog.show(
											OrdroidActivity.this,
											"Provjera podataka",
											"Molimo sačekajte...");

									Thread thread = new Thread(
											OrdroidActivity.this);
									thread.start();

								} else
									PrikaziPorukuOGresci("Pogrešni podaci!");
							}
						}
					} catch (Exception e) {
						Log.e("Greska12", e.getMessage());
						Toast.makeText(
								OrdroidActivity.this,
								"Greska: Nije moguce uspostaviti vezu sa serverom!",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	// metoda koja sluzi da se prikaze poruka o gresci
	public void PrikaziPorukuOGresci(String poruka) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				OrdroidActivity.this);
		builder.setMessage(poruka).setCancelable(false).setTitle("Greska")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// ova metoda se poziva kada se prikaze progress bar i ona se izvrsava u
	// pozadini
	// kada se ove metoda izvrsi progress bar se zatvori
	@Override
	public void run() {
		try {
			bP.Logovanje(txtKorisnickoIme.getText().toString().toUpperCase(), txtLozinka.getText().toString());
			Log.e("LOWER CASE", txtKorisnickoIme.getText().toString().toUpperCase());
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
			Intent i = new Intent(OrdroidActivity.this,
					UcitavanjePodatakaAktivnost.class);
			i.putExtra("ponovo", false);
			startActivity(i);
			username = txtKorisnickoIme.getText().toString().toUpperCase();
			finish();
		}
	};


	public static boolean imaLiKonekcije(Context context) {
		if (isNetworkAvailable(context)) {
			try {
				HttpURLConnection urlc = (HttpURLConnection) (new URL("http://"
						+ bP.dajIpAdresuServera() + "/WSOrderCom/Service1.asmx")
						.openConnection());
				Log.e("server", "http:// - - " + bP.dajIpAdresuServera() + " - - /WSOrderCom/Service1.asmx");
				urlc.setRequestProperty("User-Agent", "Test");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(3000);
				urlc.connect();
				return (urlc.getResponseCode() == 200);
			} catch (IOException e) {
				Log.e("Greska", "Error checking internet connection", e);
			}
		} else {
			Log.d("Greska", "No network available!");
		}
		return false;
	}

	private static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}