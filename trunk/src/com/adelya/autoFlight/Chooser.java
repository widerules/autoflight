package com.adelya.autoFlight;

import android.app.Activity;
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

		// on récupére l'heure à laquelle on devait sonner
		// SharedPreferences sp = getSharedPreferences("autoFlight",
		// Activity.MODE_WORLD_WRITEABLE);
		// String hour = sp.getString("hour", "");
		// Long hourInMillis = Long.valueOf(hour);

		// récupération du bouton oui
		Button btnYes = (Button) findViewById(R.id.btnYes);
		// récupération du bouton non
		Button btnNo = (Button) findViewById(R.id.btnNo);
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
