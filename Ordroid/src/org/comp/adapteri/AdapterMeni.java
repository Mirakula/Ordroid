package org.comp.adapteri;

import org.comp.aktivnosti.R;
import org.comp.klase.BazaPodataka;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterMeni extends ArrayAdapter<String> {

	Bitmap bm;
	String[] izbori;
	Context con;
	BazaPodataka baza;
	public AdapterMeni(Context context, int textViewResourceId, String[] objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		izbori = objects;
		con=context;
		baza=new BazaPodataka(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater vi = (LayoutInflater) con
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = vi.inflate(R.layout.redmenija, parent, false);
		}

		TextView label = (TextView) row.findViewById(R.id.TekstMenija);
		TextView rabat = (TextView) row.findViewById(R.id.TekstMenijaRabat);
		label.setText(izbori[position].split(">> ")[0]);
		rabat.setText(izbori[position].split(">> ")[1]);
		
		return row;
	}
}