package org.comp.adapteri;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.comp.aktivnosti.R;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class AdapterProizvod extends ArrayAdapter<Proizvod> {
	private ArrayList<Proizvod> proizvodi = null;
	private Context context = null;
	private int selektovanaStavka;
	

	public AdapterProizvod(Context context, int textViewResourceId, ArrayList<Proizvod> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.proizvodi = items;
		this.selektovanaStavka=0;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.proizvodred, null);
		}
		
		
		
		//uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Proizvod proizvod = getItem(position);			
		final TextView tvNazivProizvoda =(TextView) v.findViewById(R.id.tvNazivProizvodaRed);
		tvNazivProizvoda.setText(proizvod.dajNaziv());
		
		
		//metoda koja se poziva kada se klikne na neku stavku, sve ostale stavke
		//se deselektuju a samo se ta stavka selektuje i postavlja se indeks
		//selektovane stavke na redni broj te stavke
		v.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	final Dialog dialogProizvod = new Dialog(getContext());
       		    dialogProizvod.setContentView(R.layout.proizvod);
       		   
       		    dialogProizvod.setTitle("Podaci o proizvodu");   
       		    
       		 android.view.WindowManager.LayoutParams params = dialogProizvod.getWindow().getAttributes();
             params.width = LayoutParams.FILL_PARENT;
             dialogProizvod.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
         
	        	
       		    TextView tvNaziv=(TextView) dialogProizvod.findViewById(R.id.tvNazivProizvoda);
       		    TextView tvEAN=(TextView) dialogProizvod.findViewById(R.id.tvEANkod);
       		    TextView tvCijena=(TextView) dialogProizvod.findViewById(R.id.tvCijenaProizvoda);
       		    TextView tvMLPCijena=(TextView) dialogProizvod.findViewById(R.id.tvMaloprodajnaCijenaProizvoda);
       		    TextView tvStanje=(TextView) dialogProizvod.findViewById(R.id.tvStanjeProizvoda);
       		    Button btnZatvori=(Button) dialogProizvod.findViewById(R.id.btnNazadProizvodi1);
   		    
       		    btnZatvori.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialogProizvod.dismiss();
					}
				});
    		    
       		    dialogProizvod.show(); 
	        	tvNaziv.setText(proizvod.dajNaziv());
	        	tvEAN.setText(proizvod.dajEAN());
	        	tvCijena.setText(pretvoriuDecimalni(proizvod.dajCijenu())+" KM");
	        	tvMLPCijena.setText(pretvoriuDecimalni(proizvod.dajMLPcijenu())+" KM");
	        	tvStanje.setText(proizvod.dajKolicinu());
	        }
		});	
		
		return v;
	}	
	
	//metoda koja vraca selektovanu stavku
	public Proizvod DajSelektovanuStavku(){
		return proizvodi.get(selektovanaStavka);
	}
	
	
	public String pretvoriuDecimalni(String broj){
		String vrati="";
		DecimalFormat df = new DecimalFormat("0.000");
		vrati=String.format("%.2f", Double.parseDouble(broj));
    	return vrati;
	}
}
