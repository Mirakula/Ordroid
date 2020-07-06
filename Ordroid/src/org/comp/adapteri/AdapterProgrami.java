package org.comp.adapteri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.comp.aktivnosti.R;
import org.comp.klase.Program;

import android.app.Dialog;
import android.content.Context;
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

public class AdapterProgrami extends ArrayAdapter<Program>{
	private Context context = null;
	private int selektovanaStavka;	
	private ArrayList<Program> programi=null;

	public AdapterProgrami(Context context, int textViewResourceId, ArrayList<Program> prog) {
		super(context, textViewResourceId, prog);
		this.programi=prog;
		this.context = context;
		this.selektovanaStavka=-1;		
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.programired, null);
		}		
		
		//uzimamo stavku i ispisujemo njene atribute u odgovarajuca polja
		final Program program = getItem(position);			
		TextView tvNazivPrograma=(TextView) v.findViewById(R.id.tvNazivPrograma22);
		final CheckBox cbIzaberi=(CheckBox) v.findViewById(R.id.cbIzaberiProgram);		
		tvNazivPrograma.setText(program.dajNaziv());
		cbIzaberi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				CheckBox c=(CheckBox) buttonView;
				programi.get(position).izaberi(isChecked);
				notifyDataSetChanged();
			}
		});
		cbIzaberi.setChecked(program.DajJeLiIzabran());
		return v;
	}	
	
	public ArrayList<String> dajIzabranePrograme(){
		ArrayList<String> lista=new ArrayList<String>();
		for(int i=0; i<programi.size(); i++){
			if(programi.get(i).DajJeLiIzabran())
				lista.add(programi.get(i).dajSifru());
		}
		return lista;
	}
	
	public Program dajProgram(String sifra){
		Program p=null;
		for(int i=0; i<programi.size(); i++){
			if(programi.get(i).dajSifru().equals(sifra)){
				p=new Program(programi.get(i).dajSifru(), programi.get(i).dajNaziv());
				break;
			}
		}
		return p;
	}
	public void dodajProgram(Program p){
		p.izaberi(false);
		programi.add(p);
		notifyDataSetChanged();
	}
	
	public boolean imaLiPrograma(String sifra){
		boolean b=false;
		for(int i=0; i<programi.size(); i++){
			if(programi.get(i).dajSifru().equals(sifra)){
				b=true;
				break;
			}
		}
		return b;
	}
	
	public void izbrisiProgram(String sifra){
		for(int i=0; i<programi.size(); i++) {
			if(programi.get(i).dajSifru().equals(sifra)){
				programi.remove(i);
				notifyDataSetChanged();
				break;
			}
		}
		notifyDataSetChanged();
	}
}
