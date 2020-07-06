package org.comp.aktivnosti;

import org.comp.adapteri.AdapterKupac;
import org.comp.adapteri.AdapterLokacija;
import org.comp.klase.BazaPodataka;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class PregledKupacaAktivnost extends Activity {
	private BazaPodataka baza;
	private Button btnNazad;
	private Spinner sProgrami;
	private ListView lvKupci;
	public static ListView lvLokacije;
	private AdapterKupac adapterKupac;
	private AdapterLokacija adapterLokacija;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregledkupaca);
        setTitle("Pregled kupaca");
        try {
			baza=new BazaPodataka(PregledKupacaAktivnost.this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnNazad=(Button) findViewById(R.id.btnNazadKupci);
		sProgrami=(Spinner) findViewById(R.id.sProdajniProgrami);
		ArrayAdapter <String> adapter=new ArrayAdapter<String>(PregledKupacaAktivnost.this, android.R.layout.simple_dropdown_item_1line, baza.dajNazivePrograma()); 
		sProgrami.setAdapter(adapter);
		String pocetnaSifraPrograma=baza.dajSifruPrograma(sProgrami.getSelectedItem().toString());
		adapterKupac=new AdapterKupac(PregledKupacaAktivnost.this, R.layout.kupacred, baza.dajKupceZaProgram(pocetnaSifraPrograma));
		lvKupci=(ListView) findViewById(R.id.lvKupci);
		lvLokacije=(ListView) findViewById(R.id.lvMjestaIsporuke);
		btnNazad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});	
		
		lvKupci.setAdapter(adapterKupac);
		int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
    	lvKupci.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
    	lvKupci.setDividerHeight(2);
    	lvLokacije.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
    	lvLokacije.setDividerHeight(2);
		sProgrami.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
				String sifra=baza.dajSifruPrograma(sProgrami.getSelectedItem().toString());
				adapterKupac=new AdapterKupac(PregledKupacaAktivnost.this, R.layout.kupacred, baza.dajKupceZaProgram(sifra));
				lvKupci.setAdapter(adapterKupac);
			} 
   
			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});	
        
    }    
}