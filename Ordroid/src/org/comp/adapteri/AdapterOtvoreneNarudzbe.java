package org.comp.adapteri;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.comp.aktivnosti.R;
import org.comp.klase.Narudzba;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterOtvoreneNarudzbe extends ArrayAdapter<Narudzba>{
	private Context context = null;
	private int selektovanaStavka;	
	private ArrayList<Narudzba> narudzbe=null;

	public AdapterOtvoreneNarudzbe(Context context, int textViewResourceId, ArrayList<Narudzba> narudzbe) {
		super(context, textViewResourceId, narudzbe);
		this.narudzbe=narudzbe;
		this.context = context;
		this.selektovanaStavka=-1;		
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.otvorenanarudzbared, null);
		}		
		final DecimalFormat df=new DecimalFormat("0.000");
		//uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Narudzba narudzba = getItem(position);			
		TextView tvNazivFirme =(TextView) v.findViewById(R.id.tvNazivFirme12);
		TextView tvNazivMisa =(TextView) v.findViewById(R.id.tvNazivMisa12);
		TextView tvVrijeme =(TextView) v.findViewById(R.id.tvVrijeme12);
		TextView tvIznos =(TextView) v.findViewById(R.id.tvIznosNarudzbe12);
		TextView tvTip=(TextView) v.findViewById(R.id.tvTipNarudzbe12);
		
		tvNazivFirme.setText(narudzba.dajKupca().dajNaziv());
		tvNazivMisa.setText(narudzba.dajLokaciju().dajNazivLokacije());
		Log.e("TAG", narudzba.dajKupca().dajOsnovniPopust());
		String vrijemeNarudzbe=narudzba.dajVrijeme();
		SimpleDateFormat formatter=new SimpleDateFormat("HH:mm"); ;
		long date=Date.parse(vrijemeNarudzbe);
		vrijemeNarudzbe = formatter.format(date);
	
		tvVrijeme.setText(vrijemeNarudzbe);
		double iznos11=narudzba.dajUkupanIznosNarudzbe();
		if(narudzba.dajTipDokumenta().equals("Maloprodaja"))
			tvTip.setText("MLP");
		else
			tvTip.setText("VLP");
		tvIznos.setText(String.format("%.2f", iznos11));
		
		return v;
	}	
	
	public boolean jeLiPrazan(){
		if(narudzbe==null)
			return true;
		else
			return false;
	}
	
	public void dodajPrvu(Narudzba nn){
		this.narudzbe=new ArrayList<Narudzba>();
		this.narudzbe.add(nn);
		notifyDataSetChanged();
	}
	
	public String dajSumuNarudzbi(){
		double suma=0;
		DecimalFormat df=new DecimalFormat("0.000");
		for(int i=0; i<narudzbe.size(); i++)
			suma+=narudzbe.get(i).dajUkupanIznosNarudzbe();
		return String.format("%.2f", suma); 
	}
}
