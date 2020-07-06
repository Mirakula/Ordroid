package org.comp.aktivnosti;

import java.util.ArrayList;

import org.comp.adapteri.AdapterPoslateNarudzbe;
import org.comp.klase.BazaPodataka;
import org.comp.klase.Narudzba;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class PregledPoslatihAktivnost extends Activity {
	private BazaPodataka baza;
	private ArrayList<Narudzba> narudzbe;
	private ListView lvPoslate;
	private Button btnNazad;
	private AdapterPoslateNarudzbe adapter;
	private EditText txtUkupanIznos;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregledposlatih);
        setTitle("Poslate narudžbe");
        narudzbe=new ArrayList<Narudzba>();
        try {
			baza=new BazaPodataka(PregledPoslatihAktivnost.this);
			narudzbe=baza.dajNarudzbe("poslana");
		} catch (Exception e) {				
			Log.e("Greška baza", e.getMessage());
		}  
		txtUkupanIznos=(EditText)findViewById(R.id.txtUkupanIznosPoslate);
        if(narudzbe==null){
        	Toast.makeText(PregledPoslatihAktivnost.this, "Nema poslatih narudžbi!", Toast.LENGTH_LONG).show();
        	txtUkupanIznos.setText("0.00");
        }
        else {
        	lvPoslate=(ListView) findViewById(R.id.lvPoslane);
        	int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
        	lvPoslate.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        	lvPoslate.setDividerHeight(2);
        	adapter=new AdapterPoslateNarudzbe(PregledPoslatihAktivnost.this, R.layout.zakljucenanarudzbared, narudzbe);
        	lvPoslate.setAdapter(adapter);
        	txtUkupanIznos.setText(adapter.dajSumuNarudzbi());
        }
        
        btnNazad=(Button) findViewById(R.id.btnNazad77);
        btnNazad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent i=new Intent(PregledPoslatihAktivnost.this, NarudzbaAktivnost.class);
		        startActivity(i);
			}
		});
        
    }  
}