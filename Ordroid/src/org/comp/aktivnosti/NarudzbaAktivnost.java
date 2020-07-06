package org.comp.aktivnosti;

import java.util.ArrayList;

import org.comp.adapteri.AdapterOtvoreneNarudzbe;
import org.comp.klase.BazaPodataka;
import org.comp.klase.Narudzba;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class NarudzbaAktivnost extends Activity{
	private BazaPodataka baza;
	private Button btnNovaNarudzba;
	private Button btnPregledZakljucenih;	
	private Button btnPregledPoslatih;
	public static ListView lvOtvoreneNarudzbe;
	public static AdapterOtvoreneNarudzbe adapter;
	private Button btnNazad;
	private static ArrayList<Narudzba> narudzbe;
	private EditText txtUkupanIznos;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.narudzbe);
        setTitle("Otvorene narudžbe");
        adapter=null;
        narudzbe=new ArrayList<Narudzba>();
        try {
			baza=new BazaPodataka(NarudzbaAktivnost.this);
			narudzbe=baza.dajNarudzbe("otvorena");
		} catch (Exception e) {				
			Log.e("Greška baza 111", e.getMessage());
		} 
		txtUkupanIznos=(EditText)findViewById(R.id.txtUkupanIznosOtvorene);
		txtUkupanIznos.setText("0.00");
		if(narudzbe!=null){
        	lvOtvoreneNarudzbe=(ListView) findViewById(R.id.lvOtvorene);
        	int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
        	lvOtvoreneNarudzbe.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        	lvOtvoreneNarudzbe.setDividerHeight(2);
        	adapter=new AdapterOtvoreneNarudzbe(NarudzbaAktivnost.this, R.layout.otvorenanarudzbared, narudzbe);
        	lvOtvoreneNarudzbe.setAdapter(adapter);
        	txtUkupanIznos.setText(adapter.dajSumuNarudzbi());
        	lvOtvoreneNarudzbe.setOnItemClickListener(new OnItemClickListener() {
        		public void onItemClick(AdapterView<?> parent, View view,
        		          int position, long id) {
							NarucivanjeAktivnost.idNarudzbe=adapter.getItem(position).dajId();
        		          Intent i=new Intent(NarudzbaAktivnost.this, NarucivanjeAktivnost.class);
        		          i.putExtra("otvorena", true);
        		          startActivity(i);
        		          finish();
        		      }
        		    }); 
        }
		
		btnNazad=(Button) findViewById(R.id.btnNazad12);
		btnNazad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		btnPregledPoslatih=(Button) findViewById(R.id.btnPregledPoslatih);
		btnPregledPoslatih.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent i =new Intent(NarudzbaAktivnost.this, PregledPoslatihAktivnost.class);
				startActivity(i);
			}
		});
        btnNovaNarudzba=(Button) findViewById(R.id.btnNovaNarudzba);
        btnNovaNarudzba.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				Intent i =new Intent(NarudzbaAktivnost.this, NovaNarudzbaAktivnost.class);
				i.putExtra("otvorena", false);
				startActivity(i);
			}
		});
        
        btnPregledZakljucenih=(Button) findViewById(R.id.btnPregledZakljucenih);        
        btnPregledZakljucenih.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent i =new Intent(NarudzbaAktivnost.this, PregledZakljucenihAktivnost.class);
				startActivity(i);
			}
		});        
    }  
    
    /*public static void osvjeziPrikazNarudzbi(AdapterOtvoreneNarudzbe a){
    		if(adapter.jeLiPrazan() && a!=null){
    			adapter.dodajPrvu(a.getItem(0));
    		}
    		else if(a!=null){
    			adapter=a;
    			adapter.notifyDataSetChanged();
    			lvOtvoreneNarudzbe.setAdapter(adapter);
    		} 
    		else{
    			adapter.clear();
    			lvOtvoreneNarudzbe.setAdapter(adapter);
    		
    		}
    	NarucivanjeAktivnost.idNarudzbe=-1;
    }*/
}