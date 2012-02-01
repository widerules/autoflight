package com.adelya.autoFlight;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.adelya.autoFlight.util.AdelyaUtil;

public class Log extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		
		TextView tvBegin = (TextView) findViewById(R.id.txtToFillBegin);
		TextView tvEnd = (TextView) findViewById(R.id.txtToFillEnd);
		TextView tvActivate = (TextView) findViewById(R.id.txtToFillActivate);
		TextView tvSound = (TextView) findViewById(R.id.txtToFillSound);
		TextView tvVibrate = (TextView) findViewById(R.id.txtToFillVibrate);
		TextView tvVibrateTime = (TextView) findViewById(R.id.txtToFillVibrateTime);
		TextView tvSettedDays = (TextView) findViewById(R.id.txtSettedDays);
		TextView tvSettedAlarmIn = (TextView) findViewById(R.id.txtSettedAlarmIn);
		TextView tvSettedAlarmOut = (TextView) findViewById(R.id.txtSettedAlarmOut);

		// et voici l'heure... En timmeInMillis of course
		String hourIn = AdelyaUtil.getPreferences(Log.this, AdelyaUtil.PREF_HOURIN, "");
		String hourOut = AdelyaUtil.getPreferences(Log.this, AdelyaUtil.PREF_HOUROUT, "");

		Calendar calIn = Calendar.getInstance();
		Calendar calOut = Calendar.getInstance();
		
		if (!"".equals(hourIn) && !"".equals(hourOut)) {
			calIn.setTimeInMillis(Long.valueOf(hourIn));
			calOut.setTimeInMillis(Long.valueOf(hourOut));
		}
		
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
		
		tvBegin.setText(("".equals(hourIn) ? getResources().getString(R.string.noConf) : f.format(calIn.getTime())));
		tvEnd.setText(("".equals(hourOut) ? getResources().getString(R.string.noConf) : f.format(calOut.getTime())));
		
		tvActivate.setText(AdelyaUtil.getPreferences(Log.this, AdelyaUtil.PREF_ACTIVATE, "False"));
		tvSound.setText(AdelyaUtil.getPreferences(Log.this, AdelyaUtil.PREF_SOUND, "False"));
		tvVibrate.setText(AdelyaUtil.getPreferences(Log.this, AdelyaUtil.PREF_VIBRATE, "False"));
		tvVibrateTime.setText(AdelyaUtil.getPreferences(Log.this, AdelyaUtil.PREF_VIBRATE_TIME, "False"));
		// jours sélectionnés
		String strDays = AdelyaUtil.getPreferences(Log.this, AdelyaUtil.PREF_CHOOSEN_DAYS, "");
		String [] days = strDays.split(",");
		for (String d : days) {
			if (!AdelyaUtil.isEmpty(d)) {
				Integer lI = Integer.parseInt(d);
				
				String short_day = getResources().getStringArray(R.array.short_days)[lI];
				
				tvSettedDays.setText(tvSettedDays.getText() + short_day + "/");
			}
		}
		// alarmes existante et enrgistré dans le AlarmManager ?
		// On instancie l'Intent qui va être appelé au moment du reveil.
		Intent intent = new Intent(this, SleepAlarmReceiver.class);

		// On créer le pending Intent qui identifie l'Intent de reveil avec un
		// ID et un/des flag(s)
		PendingIntent pendingintent = PendingIntent.getBroadcast(this, 0,
				intent, PendingIntent.FLAG_NO_CREATE);
		if (pendingintent != null) {
			tvSettedAlarmIn.setText("Oui");
		} else {
			tvSettedAlarmIn.setText("Non");
		}
		intent = new Intent(this, UnsleepAlarmReceiver.class);
		pendingintent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_NO_CREATE);
		if (pendingintent != null) {
			tvSettedAlarmOut.setText("Oui");
		} else {
			tvSettedAlarmOut.setText("Non");
		}
	}
}
