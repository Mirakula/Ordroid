package org.comp.aktivnosti;

import java.text.DecimalFormat;

import org.comp.adapteri.AdapterNaruceniArtikli;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class NaruceniProizvodiAktivnost extends Activity {
	
	private AdapterNaruceniArtikli adapter;
	private Button btnNazad;
	public static TextView tvUkupnaVrijednost;
	public static TextView tvPopust;
	public static TextView tvPDV;
	public static TextView tvZaPlatiti;
	private ListView lvArtikli;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naruceniproizvodi);   
        setTitle("Stavke narudzbe");
        DecimalFormat df = new DecimalFormat("0.000");
        lvArtikli=(ListView) findViewById(R.id.listView1);
        btnNazad=(Button) findViewById(R.id.btnZatvori);
        tvUkupnaVrijednost=(TextView) findViewById(R.id.tvUkupnaVrijednost);
        tvPopust=(TextView) findViewById(R.id.tvPopust);
        tvPDV=(TextView) findViewById(R.id.tvPDV);
        tvZaPlatiti=(TextView) findViewById(R.id.tvZaPlatiti);
        adapter=new AdapterNaruceniArtikli(NaruceniProizvodiAktivnost.this, R.layout.stavka, NarucivanjeAktivnost.narudzba.dajArtikleNarudzbe());
        lvArtikli.setAdapter(adapter);
        int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
		lvArtikli.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		lvArtikli.setDividerHeight(2);
        tvUkupnaVrijednost.setText(NarucivanjeAktivnost.narudzba.dajUkupnuVrijednostNarudzbe());
        tvPopust.setText(String.format("%.2f", NarucivanjeAktivnost.narudzba.dajUkupanPopustNarudzbe()));
        tvPDV.setText(NarucivanjeAktivnost.narudzba.dajPDV());
        tvZaPlatiti.setText(String.format("%.2f", NarucivanjeAktivnost.narudzba.dajUkupanIznosNarudzbe()));
        btnNazad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});	 
 }

}
