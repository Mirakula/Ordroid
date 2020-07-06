package org.comp.adapteri;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import org.comp.aktivnosti.PregledKupacaAktivnost;
import org.comp.aktivnosti.R;
import org.comp.klase.BazaPodataka;
import org.comp.klase.Kupac;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class AdapterKupac extends ArrayAdapter<Kupac> {
	private ArrayList<Kupac> kupci = null;
	private Context context = null;
	private int selektovanaStavka;
	

	public AdapterKupac(Context context, int textViewResourceId, ArrayList<Kupac> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.kupci = items;
		this.selektovanaStavka=0;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.kupacred, null);
		}
		
		
		
		//uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Kupac kupac = getItem(position);			
		final TextView tvNazivKupca =(TextView) v.findViewById(R.id.tvNazivKupcaRed);
		tvNazivKupca.setText(kupac.dajNaziv());
		RadioButton rBtnIzaberi=(RadioButton) v.findViewById(R.id.rbIzaberiKupcaRed);
		rBtnIzaberi.setChecked(kupac.dajJeLiIzabran());
		
		rBtnIzaberi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0; i< kupci.size(); i++)
	        		kupci.get(i).izaberiKupca(false);
				kupci.get(position).izaberiKupca(true);	
	            selektovanaStavka=position;
	            notifyDataSetChanged();	
	            
	            final Dialog dialogKupci = new Dialog(getContext());
       		    dialogKupci.setContentView(R.layout.podacikupca);
       		   
       		    android.view.WindowManager.LayoutParams params = dialogKupci.getWindow().getAttributes();
       		    params.width = LayoutParams.FILL_PARENT;
       		    dialogKupci.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
         
       		    
       		    dialogKupci.setTitle("Podaci o kupcu");
       		    TextView tvNazivKupca1=(TextView) dialogKupci.findViewById(R.id.tvNazivKupca);
       		    TextView tvAdresaKupca=(TextView) dialogKupci.findViewById(R.id.tvAdresaKupca);
       		    TextView tvMjestoKupca=(TextView) dialogKupci.findViewById(R.id.tvMjestoKupca);
       		    TextView tvIDbroj=(TextView) dialogKupci.findViewById(R.id.tvIDbroj);
       		    TextView tvPDVid=(TextView) dialogKupci.findViewById(R.id.tvPDVid);
       		    TextView tvRjesenje=(TextView) dialogKupci.findViewById(R.id.tvRjesenje);
       		    TextView tvUkPotrazivanje=(TextView) dialogKupci.findViewById(R.id.tvUkPotrazivanje);
       		    TextView tvDospjelo=(TextView) dialogKupci.findViewById(R.id.tvDospjelo);
       		    TextView tvDaniKasnjenja=(TextView) dialogKupci.findViewById(R.id.tvDaniKasnjenja);
       		    TextView tvOsnovniRabat=(TextView) dialogKupci.findViewById(R.id.tvOsnovniRabat);
       		    Button btnZatvori=(Button) dialogKupci.findViewById(R.id.btnZatvoriKupca);
       		    
       		    tvNazivKupca1.setText(kupac.dajNaziv());
       		    tvAdresaKupca.setText(kupac.dajAdresu());
       		    tvMjestoKupca.setText(kupac.dajMjesto());
       		    tvIDbroj.setText(kupac.dajKupacIdbroj());
       		    tvPDVid.setText(kupac.dajKupacVATbroj());
       		    tvRjesenje.setText(kupac.dajKupacSudBroj());
       		    tvUkPotrazivanje.setText(pretvoriuDecimalni(kupac.dajUkupniKredit(), false) + " KM");
       		    tvDospjelo.setText(pretvoriuDecimalni(kupac.dajDospjeliKredit(), false) + " KM");
       		    tvDaniKasnjenja.setText(kupac.dajPosljednjuUplatu());
       		    tvOsnovniRabat.setText(pretvoriuDecimalni(kupac.dajOsnovniPopust(), true)+"  %");
       		    
       		    btnZatvori.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialogKupci.dismiss();
					}
				});
       		    
       		   dialogKupci.show();	  
			}
		});
		
		//metoda koja se poziva kada se klikne na neku stavku, sve ostale stavke
		//se deselektuju a samo se ta stavka selektuje i postavlja se indeks
		//selektovane stavke na redni broj te stavke
		v.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	for(int i=0; i< kupci.size(); i++)
	        		kupci.get(i).izaberiKupca(false);
				kupci.get(position).izaberiKupca(true);
	        	notifyDataSetChanged();
	        	BazaPodataka baza=new BazaPodataka(context);
	        	AdapterLokacija adapterLokacija=new AdapterLokacija(context, R.layout.lokacijared, baza.dajLokacijeZaKupca(kupci.get(position).dajSifru()));	
	        	adapterLokacija.nazivKupca=kupci.get(position).dajNaziv();
	        	PregledKupacaAktivnost.lvLokacije.setAdapter(adapterLokacija);
	        	
	        }
		});		
		
		return v;
	}	
	
	//metoda koja vraca selektovanu stavku
	public Kupac DajSelektovanuStavku(){
		return kupci.get(selektovanaStavka);
	}
	
	public Kupac DajPrvogKupca(){
		return kupci.get(0);
	}
	
	public String pretvoriuDecimalni(String broj, boolean rabat){
		String vrati="";
		DecimalFormat df = new DecimalFormat("0.000");
		if(rabat)
			vrati=String.format("%.2f", Double.parseDouble(broj)*100);
		else
			vrati=String.format("%.2f", Double.parseDouble(broj));
    	return vrati;
	}
}
