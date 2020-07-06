package org.comp.aktivnosti;

import org.comp.klase.BazaPodataka;
import org.comp.klase.OsvjeziAkcijskePopuste;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PocetnaAktivnost extends Activity{
	private BazaPodataka baza;
	private Button btnNarudzbe;
	private Button btnKupci;	
	private Button btnProizvodi;
	private Button btnUcitavanjePodataka;
	
	private Button btnOdjava;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocetnimeni);
        try {
			baza=new BazaPodataka(PocetnaAktivnost.this);
		} catch (Exception e) {				
			Log.e("Gre≈°ka baza", e.getMessage());
		} 
		btnOdjava=(Button) findViewById(R.id.btnOdjava1);
		btnOdjava.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PocetnaAktivnost.this);
			    builder.setMessage("Da li ste sigurni da se ûelite odjaviti?")
			           .setCancelable(false)
			           .setTitle("Odjava")
			           .setIcon(android.R.drawable.ic_dialog_alert)
			           .setPositiveButton("Da", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
			                   //ako se potvrdi da se zeli odjaviti zavrsava se aktivnost i vracamo se
			            	   //na login formu
			            	   baza.Odjava();
			            	   finish();
			            	   Intent ii=new Intent(PocetnaAktivnost.this, OrdroidActivity.class);
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
		
        btnNarudzbe=(Button) findViewById(R.id.btnNarudzbe);
        btnNarudzbe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(baza.dajPrograme("1").size()==0)
						Toast.makeText(PocetnaAktivnost.this, "Nema prodajnih programa. Dodajte u lokalnu bazu prodajne programe!", Toast.LENGTH_LONG).show();
				else{
					if(baza.osvjeziAkcijskePopuste())
						new OsvjeziAkcijskePopuste(PocetnaAktivnost.this).execute();
					Intent i =new Intent(PocetnaAktivnost.this, NarudzbaAktivnost.class);
					startActivity(i);
				}
			}
		});
        
        btnUcitavanjePodataka=(Button) findViewById(R.id.btnUcitavanjePodataka);
        btnUcitavanjePodataka.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i =new Intent(PocetnaAktivnost.this, UcitavanjePodatakaAktivnost.class);
				i.putExtra("ponovo",true);
				startActivity(i);
			}
		});
        
        btnKupci=(Button) findViewById(R.id.btnKupci);
        btnKupci.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(baza.dajPrograme("1").size()==0)
					Toast.makeText(PocetnaAktivnost.this, "Nema prodajnih programa. Dodajte u lokalnu bazu prodajne programe!", Toast.LENGTH_LONG).show();
				else{
					Intent i =new Intent(PocetnaAktivnost.this, PregledKupacaAktivnost.class);
					startActivity(i);
				}
			}
		});
        
        btnProizvodi=(Button) findViewById(R.id.btnProizvodi);
        btnProizvodi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(baza.dajPrograme("1").size()==0)
					Toast.makeText(PocetnaAktivnost.this, "Nema prodajnih programa. Dodajte u lokalnu bazu prodajne programe!", Toast.LENGTH_LONG).show();
				else{
					Intent i =new Intent(PocetnaAktivnost.this, PregledProizvodaAktivnost.class);
					startActivity(i);
				}
			}
		});
    }  
}