package org.comp.aktivnosti;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PrikaziAlarmAktivnost extends Activity {
	private Button btnZavrsi;
	private TextView txtNaslov;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.prikazialarm);
		dialog.setCancelable(true);

		btnZavrsi = (Button) dialog.findViewById(R.id.btnZaustavi);
		txtNaslov = (TextView) dialog.findViewById(R.id.txtNaziv);

		btnZavrsi.setOnClickListener(btnClick);


			try {
				dialog.setTitle("OrderCom-slanje narudžbi");
				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				manager.cancel(0);
				String status = getIntent().getExtras().getString("tekst");
				txtNaslov.setText(status);

			} catch (Exception e) {
				dialog.setTitle("Greška");
				txtNaslov.setText("Greška!");
			}
			dialog.show();		

	}

	private OnClickListener btnClick = new OnClickListener() {

		public void onClick(View v) {
			dialog.dismiss();
			finish();
		}
	};

}
