package org.comp.aktivnosti;

import org.comp.klase.BazaPodataka;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.Dialog;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PostavkeAktivnost extends Activity {
	private BazaPodataka baza;
	private Button btnPostavke;
	private Button btnOdjava;
	private ProgressDialog progressDialog;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocetna);
        try {
			baza=new BazaPodataka(PostavkeAktivnost.this);
			Log.e("baza je", baza.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnOdjava=(Button) findViewById(R.id.btnOdjava);
		btnOdjava.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PostavkeAktivnost.this);
			    builder.setMessage("Da li ste sigurni da se želite odjaviti?")
			           .setCancelable(false)
			           .setTitle("Odjava")
			           .setIcon(android.R.drawable.ic_dialog_alert)
			           .setPositiveButton("Da", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
			                   //ako se potvrdi da se zeli odjaviti zavrsava se aktivnost i vracamo se
			            	   //na login formu
			            	   finish();
			            	   Intent ii=new Intent(PostavkeAktivnost.this, OrdroidActivity.class);
			            	   startActivity(ii);
			               }
			           })
			           .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {				                   
			                   //ako se ipak ne zeli odjaviti sa sistema, samo se sakriva dijalog
			            	   //i nastavlja se sa radom
			            	   dialog.cancel();
			               }
			           });
			    AlertDialog alert = builder.create();
			    alert.show();
				
			}
		});
		
        btnPostavke=(Button) findViewById(R.id.btnPostavke);
        btnPostavke.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final Dialog dialogPostavke = new Dialog(PostavkeAktivnost.this);
				   dialogPostavke.setContentView(R.layout.postavke);
				   dialogPostavke.setTitle("Unesite IP adresu servera:");
				   
				   Button btnIzaberi = (Button) dialogPostavke.findViewById(R.id.btnIzaberi);
				   Button btnOdustani=(Button) dialogPostavke.findViewById(R.id.btnOdustani);
				   btnOdustani.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialogPostavke.dismiss();
					}
				});
				   final EditText ipAdresa=(EditText) dialogPostavke.findViewById(R.id.txtIpAdresa);
				   EditText staraIpAdresa=(EditText) dialogPostavke.findViewById(R.id.txtStaraIpAdresa);
				   staraIpAdresa.setText(baza.dajIpAdresuServera());
				   btnIzaberi.setOnClickListener(new View.OnClickListener() {			
						@Override
						public void onClick(View v) {
							if(ipAdresa.getText().toString().equals(""))
								Toast.makeText(getApplicationContext(), "Morate unijeti IP adresu!", Toast.LENGTH_LONG).show();
							else {
								baza.postaviIpAdresuServisa(ipAdresa.getText().toString());
								Toast.makeText(getApplicationContext(), "Uspješno ste unijeli IP adresu servera!", Toast.LENGTH_LONG).show();
								dialogPostavke.dismiss();
							}
						}
			        });				   
				   dialogPostavke.show();	
			}
		});
    }
}