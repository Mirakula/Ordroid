package org.comp.adapteri;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.comp.aktivnosti.R;
import org.comp.klase.Lokacija;

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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AdapterLokacija extends ArrayAdapter<Lokacija> {
	private ArrayList<Lokacija> lokacije = null;
	public  String nazivKupca="";
	private Context context = null;
	private int selektovanaStavka;
	

	public AdapterLokacija(Context context, int textViewResourceId, ArrayList<Lokacija> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.lokacije = items;
		this.selektovanaStavka=-1;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.lokacijared, null);
		}
		
		
		
		//uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Lokacija lokacija = getItem(position);			
		TextView tvNaziv =(TextView) v.findViewById(R.id.tvNazivLokacijeRed);
		tvNaziv.setText(lokacija.dajNazivLokacije());

		
		
		//metoda koja se poziva kada se klikne na neku stavku, sve ostale stavke
		//se deselektuju a samo se ta stavka selektuje i postavlja se indeks
		//selektovane stavke na redni broj te stavke
		v.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	
	            selektovanaStavka=position;
	            
	            final Dialog dialogLokacija = new Dialog(getContext());
       		    dialogLokacija.setContentView(R.layout.podacilokacije);
       		   
       		    android.view.WindowManager.LayoutParams params = dialogLokacija.getWindow().getAttributes();
       		    params.width = LayoutParams.FILL_PARENT;
       		    dialogLokacija.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
         
       		    
       		    dialogLokacija.setTitle("Podaci o mjestu isporuke");
       		    TextView tvNazivKupca1=(TextView) dialogLokacija.findViewById(R.id.tvNazivKupcaLokacija);
       		    TextView tvNazivLokacije=(TextView) dialogLokacija.findViewById(R.id.tvAdresaIsporuke);
       		    TextView tvAdresaLokacije=(TextView) dialogLokacija.findViewById(R.id.tvAdresaLokacije);
       		    TextView tvMjestoLokacije=(TextView) dialogLokacija.findViewById(R.id.tvMjestoLokacije);
       		    TextView tvTip=(TextView) dialogLokacija.findViewById(R.id.tvTip);
       		    TextView tvProdajniProstor=(TextView) dialogLokacija.findViewById(R.id.tvProdajniProstor);
       		    TextView tvBrojKasa=(TextView) dialogLokacija.findViewById(R.id.tvBrojKasa);
       		    TextView tvImeKontakta=(TextView) dialogLokacija.findViewById(R.id.tvImeKontakta);
       		    TextView tvTelefonKontakta=(TextView) dialogLokacija.findViewById(R.id.tvTelefon);
       		    Button btnZatvori=(Button) dialogLokacija.findViewById(R.id.btnZatvoriLokaciju);
       		    
       		    tvNazivKupca1.setText(nazivKupca);
       		    tvNazivLokacije.setText(lokacija.dajNazivLokacije());
       		    tvAdresaLokacije.setText(lokacija.dajAdresuLokacije());
       		    tvMjestoLokacije.setText(lokacija.dajMjestoLokacije());
       		    tvTip.setText(lokacija.dajTipLokacije());
       		    tvProdajniProstor.setText("");
       		    tvBrojKasa.setText(lokacija.dajTrgovinaNaMaloBroj());
       		    tvImeKontakta.setText(lokacija.dajImeKontaktOsobe());
       		    tvTelefonKontakta.setText(lokacija.dajTelefonKontaktOsobe());
       		    
       		    btnZatvori.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialogLokacija.dismiss();
					}
				});
       		    
       		   dialogLokacija.show();           
	            	
        }
		});		
		
		return v;
	}	
	
	//metoda koja vraca selektovanu stavku
	public Lokacija DajStavku(int i){
		return lokacije.get(i);
	}
	
	public String pretvoriuDecimalni(String broj, boolean rabat){
		String vrati="";
		DecimalFormat df = new DecimalFormat("0.000");
		if(rabat)
			vrati=df.format(Double.parseDouble(broj)*100);
		else
			vrati=df.format(Double.parseDouble(broj));
    	return vrati;
	}
}
