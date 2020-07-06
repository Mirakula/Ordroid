package org.comp.klase;



import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.comp.aktivnosti.OrdroidActivity;
import org.comp.aktivnosti.PrikaziAlarmAktivnost;
import org.comp.aktivnosti.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SlanjeNarudzbe extends AsyncTask<String, Void, String>{

	private StatusLine statusLine;
	private Context context=null;
	private ArrayList<Narudzba> narudzbe;
	private int provjera;
	private boolean svePoslate;
	
	public SlanjeNarudzbe(Context context, ArrayList<Narudzba> nar){
		this.context = context;
		narudzbe=new ArrayList<Narudzba>();
		narudzbe=nar;
		provjera=-1;
	}
	
	//private ProgressDialog Dialog = new ProgressDialog(context);

	  protected void onPreExecute() {
	                        // here you have place code which you want to show in UI thread like progressbar or dialog or any layout . here  i am displaying a progressDialog with test please wait while loading......

//	                        Dialog.setMessage(" �aljem narud�be na server............");
//	                        Dialog.show();
	  }
	
	@Override
	protected String doInBackground(String... params) {
		String ip=params[0];
		String korIme=params[1];
		String responseString = null;
		ArrayList<Integer> provjeraNarudzbi;		
		ArrayList<Narudzba> poslate=new ArrayList<Narudzba>();
		svePoslate=true;
     	   if(OrdroidActivity.imaLiKonekcije(context)){
     		   try{
     			   WebServis.cont=context;
     			   provjeraNarudzbi=WebServis.posaljiNarudzbu(ip, narudzbe, korIme);
     			   for(int i=0; i<provjeraNarudzbi.size(); i++){
     				   if(provjeraNarudzbi.get(i)!=1){
     					   svePoslate=false;
     					   break;
     				   }
     			   }
     			   provjera=1;
     		   }catch(Exception e){
     				   provjera=2;
     			}
     	   }else
     		   provjera=3;
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		 //Dialog.dismiss();  
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(ns);

		int icon = R.drawable.ordroid;
		CharSequence tickerText = null;
		if(provjera==1 && svePoslate)
        	tickerText="Izabrane narudžbe su uspješno poslate na server!";
        else if(provjera==1 && !svePoslate)
        	tickerText= "Neke narudžbe nisu uspješno poslate na server. Pokušajte ih ponovno poslati!";
        else if(provjera==2)
        	tickerText= "Greška: Nije moguće uspostaviti vezu sa serverom!";
        else if(provjera==3)
        	tickerText= "Nema konekcije na server!";


		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText,
				when);

		CharSequence contentTitle = "OrderCom";

		Intent notificationIntent = new Intent(context, PrikaziAlarmAktivnost.class);
		notificationIntent.putExtra("tekst", tickerText);
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				0, notificationIntent, 0);
		notification.sound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, contentTitle,
				tickerText, contentIntent);
		mNotificationManager.notify(0, notification);
	}
}
