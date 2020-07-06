package org.comp.adapteri;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.comp.aktivnosti.NaruceniProizvodiAktivnost;
import org.comp.aktivnosti.NarucivanjeAktivnost;
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

public class AdapterNaruceniArtikli extends ArrayAdapter<Proizvod> {
	private Narudzba narudzba=null;
	private Context context = null;
	private int selektovanaStavka;	

	public AdapterNaruceniArtikli(Context context, int textViewResourceId, ArrayList<Proizvod> stavke) {
		super(context, textViewResourceId, stavke);
		this.context = context;
		this.narudzba=new Narudzba(stavke, "Pregled");
		this.selektovanaStavka=-1;		
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.stavka, null);
		}		
		final DecimalFormat df=new DecimalFormat("0.000");
		final DecimalFormat dk=new DecimalFormat("0.00");
		//uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Proizvod artikl = narudzba.dajArtikleNarudzbe().get(position);			
		TextView tvNaziv=(TextView) v.findViewById(R.id.tvNaziv111);
		TextView tvKolicina =(TextView) v.findViewById(R.id.tvKolicina111);
		TextView tvCijena=(TextView) v.findViewById(R.id.tvCijena111);
		TextView tvIznos =(TextView) v.findViewById(R.id.tvIznos111);
		ImageButton btnIzbrisi=(ImageButton) v.findViewById(R.id.btnIzbrisi111);
		tvNaziv.setText(artikl.dajNaziv());
		tvKolicina.setText(String.valueOf(dk.format(artikl.dajNarucenuKolicinu())));
		if(NarucivanjeAktivnost.narudzba.dajTipDokumenta().equals("Maloprodaja")){
				double dd=Double.valueOf(artikl.dajMLPcijenu());
				//tvCijena.setText(String.format("%.2f", dd));
				tvCijena.setText(String.format("%.2f", Double.valueOf(artikl.dajOsnovicuMLP())));
				tvIznos.setText(String.format("%.2f", artikl.dajIznosMPL()));
			}
			else{
				double dd=Double.valueOf(artikl.dajCijenu());
				//tvCijena.setText(String.format("%.2f", dd));
				tvCijena.setText(String.format("%.2f", Double.valueOf(artikl.dajOsnovicuVLP())));
				tvIznos.setText(String.format("%.2f", artikl.dajIznosVeleprodaja()));
			}
		btnIzbrisi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				narudzba.izbrisiArtikl(artikl.dajSifru());
				notifyDataSetChanged();
				NarucivanjeAktivnost.txtUkupanIznos.setText(String.format("%.2f", NarucivanjeAktivnost.narudzba.dajUkupanIznosNarudzbe()));
				//NarucivanjeAktivnost.adapterArtikli.notifyDataSetChanged();
				NaruceniProizvodiAktivnost.tvUkupnaVrijednost.setText(NarucivanjeAktivnost.narudzba.dajUkupnuVrijednostNarudzbe());
		        NaruceniProizvodiAktivnost.tvPopust.setText(String.format("%.2f", NarucivanjeAktivnost.narudzba.dajUkupanPopustNarudzbe()));
		        NaruceniProizvodiAktivnost.tvPDV.setText(NarucivanjeAktivnost.narudzba.dajPDV());
		        NaruceniProizvodiAktivnost.tvZaPlatiti.setText(String.format("%.2f", NarucivanjeAktivnost.narudzba.dajUkupanIznosNarudzbe()));
			}
		});			
		return v;
	}	

}
