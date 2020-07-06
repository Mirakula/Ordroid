package org.comp.klase;

import org.comp.adapteri.AdapterMeni;
import org.comp.aktivnosti.MenuActivity;
import org.comp.aktivnosti.NarucivanjeAktivnost;
import org.comp.aktivnosti.R;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class MenuFragment extends ListFragment {
	public static String[] stavke;
	public static int position =-1;
	public static NarucivanjeAktivnost sa;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	
		setListAdapter(new AdapterMeni(getActivity().getApplicationContext(),
				R.layout.redmenija, stavke));
		getListView().setCacheColorHint(0);
		int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example
		getListView().setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		getListView().setDividerHeight(2);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		this.position=position;
		(((MenuActivity) getActivity()).getSlideoutHelper())
				.close1(stavke[position]);	
		sa.PrikaziDugmad(stavke[position].split(">> ")[0], stavke[position].split(">> ")[1].split(" %")[0]);
	}

}