package org.comp.adapteri;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.comp.aktivnosti.R;
import org.comp.klase.Narudzba;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class AdapterZakljuceneNarudzbe extends ArrayAdapter<Narudzba>{
	private Context context = null;
	private int selektovanaStavka;	
	private ArrayList<Narudzba> narudzbe=null;

	public AdapterZakljuceneNarudzbe(Context context, int textViewResourceId, ArrayList<Narudzba> narudzbe) {
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
			v = vi.inflate(R.layout.zakljucenanarudzbared, null);
		}		
		final DecimalFormat df=new DecimalFormat("0.000");
		//uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Narudzba narudzba = getItem(position);			
		TextView tvNazivFirme=(TextView) v.findViewById(R.id.tvNazivFirme22);
		TextView tvNazivMisa=(TextView) v.findViewById(R.id.tvNazivMisa22);
		TextView tvVrijeme =(TextView) v.findViewById(R.id.tvVrijeme22);
		TextView tvIznos =(TextView) v.findViewById(R.id.tvIznosNarudzbe22);
		TextView tvTip=(TextView) v.findViewById(R.id.tvTipNarudzbe22);
		final CheckBox cbIzaberi=(CheckBox) v.findViewById(R.id.cbIzaberiNarudzbu);		
		
		tvNazivFirme.setText(narudzba.dajKupca().dajNaziv());
		tvNazivMisa.setText(narudzba.dajLokaciju().dajNazivLokacije());
		String vrijemeNarudzbe=narudzba.dajVrijeme();
		SimpleDateFormat formatter=new SimpleDateFormat("HH:mm"); ;
		long date=Date.parse(vrijemeNarudzbe);
		vrijemeNarudzbe = formatter.format(date);
	
		if(narudzba.dajTipDokumenta().equals("Maloprodaja"))
			tvTip.setText("MLP");
		else
			tvTip.setText("VLP");
		
		tvVrijeme.setText(vrijemeNarudzbe);
		double iznos11=narudzba.dajUkupanIznosNarudzbe();
		tvIznos.setText(String.format("%.2f", iznos11));
		cbIzaberi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				CheckBox c=(CheckBox) buttonView;
				narudzbe.get(position).izaberiNarudzbu(c.isChecked());
				notifyDataSetChanged();
				
			}
		});
		cbIzaberi.setChecked(narudzba.dajJeLiIzabrana());
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialogStavke = new Dialog(context);
       		    dialogStavke.setContentView(R.layout.narucenestavke);
       		    android.view.WindowManager.LayoutParams params = dialogStavke
						.getWindow().getAttributes();
				params.width = android.view.WindowManager.LayoutParams.FILL_PARENT;
				params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
				dialogStavke.getWindow().setAttributes(
						(android.view.WindowManager.LayoutParams) params);
       		    dialogStavke.setTitle("Pregled narudžbe");
       		    
       		    ListView lvNaruceno=(ListView) dialogStavke.findViewById(R.id.lvNaruceneStavke);
       		    int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
       		    lvNaruceno.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
       		    lvNaruceno.setDividerHeight(2);
       		    Button btnZatvoriDijalog = (Button) dialogStavke.findViewById(R.id.btnZatvoriPregledStavki);
       			AdapterStavke adapterNaruceno=new AdapterStavke(context, R.layout.stavkepregled, narudzba.dajArtikleNarudzbe());
       			adapterNaruceno.postaviJeLiMaloprodaja(narudzba.dajTipDokumenta().equals("Maloprodaja"));
       			lvNaruceno.setAdapter(adapterNaruceno);
       			
       			TextView tvKupac=(TextView) dialogStavke.findViewById(R.id.tvKupaczakljucene);
       			TextView tvMIS=(TextView) dialogStavke.findViewById(R.id.tvMISzakljucene);
       			tvKupac.setText(narudzba.dajKupca().dajNaziv());
       			tvMIS.setText(narudzba.dajLokaciju().dajNazivLokacije());
       			TextView tvUkupnaVrijednost=(TextView) dialogStavke.findViewById(R.id.tvUkupnaVrijednost1);
       	        TextView tvPopust=(TextView) dialogStavke.findViewById(R.id.tvPopust1);
       	        TextView tvPDV=(TextView) dialogStavke.findViewById(R.id.tvPDV1);
       	        TextView tvZaPlatiti=(TextView) dialogStavke.findViewById(R.id.tvZaPlatiti1);
       	        tvUkupnaVrijednost.setText(narudzba.dajUkupnuVrijednostNarudzbe());
       	        tvPopust.setText(String.format("%.2f", narudzba.dajUkupanPopustNarudzbe()));
       	        tvPDV.setText(narudzba.dajPDV());
       	        tvZaPlatiti.setText(String.format("%.2f", narudzba.dajUkupanIznosNarudzbe()));
       	        
       	        btnZatvoriDijalog.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialogStavke.dismiss();
					}
				});       			
       		   dialogStavke.show();   		
			}
		});
		
		return v;
	}	
	
	public ArrayList<Integer> dajIzabraneNarudzbe(){
		ArrayList<Integer> lista=new ArrayList<Integer>();
		for(int i=0; i<narudzbe.size(); i++){
			if(narudzbe.get(i).dajJeLiIzabrana())
				lista.add(narudzbe.get(i).dajId());
		}
		return lista;
	}
	
	public ArrayList<Narudzba> dajIzabraneNarudzbeZaSlanje() {
		ArrayList<Narudzba> lista=new ArrayList<Narudzba>();
		for(int i=0; i<narudzbe.size(); i++){
			if(narudzbe.get(i).dajJeLiIzabrana())
				lista.add(narudzbe.get(i));
		}
		return lista;
	}
	
	public String dajSumuNarudzbi(){
		double suma=0;
		DecimalFormat df=new DecimalFormat("0.000");
		for(int i=0; i<narudzbe.size(); i++)
			suma+=narudzbe.get(i).dajUkupanIznosNarudzbe();
		return String.format("%.2f", suma); 
	}

}
