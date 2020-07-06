package org.comp.adapteri;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.comp.klase.Grupa;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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

public class AdapterGrupe extends ArrayAdapter<Grupa> {
	private ArrayList<Grupa> grupe = null;
	private Context context = null;
	private int selektovanaStavka;	

	public AdapterGrupe(Context context, int textViewResourceId, ArrayList<Grupa> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.grupe = items;
		this.selektovanaStavka=-1;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(grupe.get(position).dajSkracenicu());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(grupe.get(position).dajSkracenicu());

        return label;
    }

	
	//metoda koja vraca selektovanu stavku
	public String DajSifru(int i){
		return grupe.get(i).dajIdGrupe();
	}
}
