package com.adelya.autoFlight;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Chooser extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooser);

		// récupération du bouton oui
		Button btnYes = (Button) findViewById(R.id.btnYes);
		// récupération du bouton non
		Button btnNo = (Button) findViewById(R.id.btnNo);
		// le bouton de report de 5 minutes
		Button btnReport = (Button) findViewById(R.id.btnReport);
		// récupération du chrono
		final TextView chrono = (TextView) findViewById(R.id.textViewCountDown);
		final CountDownTimer counter = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				chrono.setText("" + ((int) (millisUntilFinished / 1000)));
			}

			@Override
			public void onFinish() {
				launchFlightMode();
			}
		}.start();

		btnYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				counter.cancel();
				launchFlightMode();
			}
		});
		
		btnReport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				counter.cancel();
				Chooser.this.finish();
				// Récupération de l'instance du service AlarmManager.
				AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

				// On instancie l'Intent qui va être appelé au moment du reveil.
				Intent intent = new Intent(Chooser.this, SleepAlarmReceiver.class);

				// On créer le pending Intent qui identifie l'Intent de reveil avec un
				// ID et un/des flag(s)
				PendingIntent pendingintent = PendingIntent.getBroadcast(Chooser.this, 0,
						intent, 0);

				// On ajoute le reveil au service de l'AlarmManager
				long actualTime = Calendar.getInstance().getTimeInMillis();
				am.set(AlarmManager.RTC_WAKEUP, actualTime + (5 * 60 * 1000), pendingintent);

			}
		});

		btnNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				counter.cancel();
				Chooser.this.finish();
			}
		});
	}

	private void launchFlightMode() {
		Settings.System.putInt(Chooser.this.getApplicationContext()
				.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", 1);
		Chooser.this.getApplicationContext().sendBroadcast(intent);

		Chooser.this.finish();
	}
}
