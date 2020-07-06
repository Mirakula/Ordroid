package org.comp.klase;

import org.comp.aktivnosti.OrdroidActivity;
import org.comp.aktivnosti.PrikaziAlarmAktivnost;
import org.comp.aktivnosti.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.util.Log;

public class OsvjeziAkcijskePopuste extends AsyncTask<String, Void, String> {

	private Context context = null;
	private int provjera;
	BazaPodataka baza;

	public OsvjeziAkcijskePopuste(Context context) {
		this.context = context;
	}

	protected void onPreExecute() {

	}

	@Override
	protected String doInBackground(String... params) {
		String responseString = null;
		provjera = 0;
		baza = new BazaPodataka(context);
		if (OrdroidActivity.imaLiKonekcije(context)) {
			try {
				baza.ucitajAkcijskePopuste();
				provjera = 1;
			} catch (Exception e) {
				provjera = 0;
				baza.izbrisiAkcijskePopuste();
			}
		} else
			provjera = 2;
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(ns);

		int icon = R.drawable.ordroid;
		CharSequence tickerText = null;
		if (provjera == 1){
			tickerText = "Akcijski popusti su uspješno ažurirani!";
			baza.postaviDatumOsvjezavanjaPopusta();
		}
		else if (provjera == 0)
			tickerText = "Greška. Akcijski popusti nisu ažurirani! Pokušajte ponovo!";
		else if (provjera == 2)
			tickerText = "Nema konekcije na server!";

		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);

		CharSequence contentTitle = "OrderCom";

		Intent notificationIntent = new Intent(context,
				PrikaziAlarmAktivnost.class);
		notificationIntent.putExtra("tekst", tickerText);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.sound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, contentTitle, tickerText,
				contentIntent);
		mNotificationManager.notify(0, notification);
	}
}
