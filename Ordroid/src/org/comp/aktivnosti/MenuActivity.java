package org.comp.aktivnosti;
import org.comp.klase.MenuFragment;

import paket.comp.slideout.SlideoutHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

public class MenuActivity extends FragmentActivity{
	public static NarucivanjeAktivnost aa;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mSlideoutHelper = new SlideoutHelper(this);
	    mSlideoutHelper.activate();
	    MenuFragment mf = new MenuFragment();
	    mf.sa = aa;
	    getSupportFragmentManager().beginTransaction().add(paket.comp.slideout.R.id.slideout_placeholder, mf, "menu").commit();
	    mSlideoutHelper.open();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			mSlideoutHelper.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public SlideoutHelper getSlideoutHelper(){
		return mSlideoutHelper;
	}
	
	private SlideoutHelper mSlideoutHelper;

}
