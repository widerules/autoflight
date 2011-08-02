package com.adelya.autoFlight;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
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
	}
}
