package org.comp.adapteri;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.comp.aktivnosti.R;
import org.comp.klase.Narudzba;
import org.comp.klase.Proizvod;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class AdapterStavke extends ArrayAdapter<Proizvod> {
	private Narudzba narudzba=null;
	private Context context = null;
	private int selektovanaStavka;	
	private boolean maloprodaja;

	public AdapterStavke(Context context, int textViewResourceId, ArrayList<Proizvod> stavke) {
		super(context, textViewResourceId, stavke);
		this.context = context;
		this.narudzba=new Narudzba(stavke, "Pregled");
		this.selektovanaStavka=-1;
		this.maloprodaja=false;
		
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.stavkepregled, null);
		}		
		final DecimalFormat df=new DecimalFormat("0.000");
		final DecimalFormat dk=new DecimalFormat("0.00");
		//uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Proizvod artikl = narudzba.dajArtikleNarudzbe().get(position);			
		TextView tvNaziv=(TextView) v.findViewById(R.id.tvNaziv999);
		TextView tvKolicina =(TextView) v.findViewById(R.id.tvKolicina999);
		TextView tvCijena=(TextView) v.findViewById(R.id.tvCijena999);
		TextView tvIznos =(TextView) v.findViewById(R.id.tvIznos999);
		tvNaziv.setText(artikl.dajNaziv());
		tvKolicina.setText(String.valueOf(dk.format(artikl.dajNarucenuKolicinu())));
		if(maloprodaja){
				//double dd=Double.valueOf(artikl.dajMLPcijenu());
				double dd=Double.valueOf(artikl.dajOsnovicuMLP());
				tvCijena.setText(String.format("%.2f", dd));				
				tvIznos.setText(String.format("%.2f", artikl.dajIznosMPL()));
			}
			else{
				//double dd=Double.valueOf(artikl.dajCijenu());
				double dd=Double.valueOf(artikl.dajOsnovicuVLP());
				tvCijena.setText(String.format("%.2f", dd));				
				tvIznos.setText(String.format("%.2f", artikl.dajIznosVeleprodaja()));
			}			
		return v;
	}	
	
	public void postaviJeLiMaloprodaja(boolean b){
		maloprodaja=b;
	}

}
