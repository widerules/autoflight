package com.adelya.autoFlight;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.adelya.autoFlight.util.AdelyaUtil;

public class SleepAlarmBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {

		// on récupére l'heure actuelle stockée (ou pas)
		SharedPreferences sp = context.getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		// et voici l'heure... En timmeInMillis of course
		String hour = sp.getString(AdelyaUtil.PREF_HOURIN, "");
		String hourOut = sp.getString(AdelyaUtil.PREF_HOUROUT, "");
		// si l'heure n'est pas trouvé, on se fait pas chié --> pas d'alarme
		if (!"".equals(hour)) {

			Long hourInMillis = Long.valueOf(hour);
			
			// on regarde si l'heure est avant maintenant ou pas dans quel cas on 
			// pousse d'un jour
			if (hourInMillis < Calendar.getInstance().getTimeInMillis()) {
				Calendar alarmHour = Calendar.getInstance();
				alarmHour.setTimeInMillis(hourInMillis);
				alarmHour.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1);
				alarmHour.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
				
				hourInMillis = alarmHour.getTimeInMillis();
			}
			setAlarm(context, hourInMillis, SleepAlarmReceiver.class);
			
			if (!"".equals(AdelyaUtil.PREF_HOUROUT)) {

				Long hourOutInMillis = Long.valueOf(hourOut);
				
				// on regarde si l'heure est avant maintenant ou pas dans quel cas on 
				// pousse d'un jour
				if (hourOutInMillis < Calendar.getInstance().getTimeInMillis()) {
					Calendar alarmHour = Calendar.getInstance();
					alarmHour.setTimeInMillis(hourOutInMillis);
					alarmHour.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1);
					alarmHour.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
					
					hourOutInMillis = alarmHour.getTimeInMillis();
				}
				setAlarm(context, hourOutInMillis, UnsleepAlarmReceiver.class);
			}
		}
	}
	
	/**
	 * Sette une alarme et la classe associé
	 * @param time Temps en millis
	 * @param classToLaunch La classe à lancer lors de réveil
	 */
	private void setAlarm(Context context, Long time, Class<?> classToLaunch) {

		// Récupération de l'instance du service AlarmManager.
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		// On instancie l'Intent qui va être appelé au moment du reveil.
		Intent intent = new Intent(context, classToLaunch);

		// On créer le pending Intent qui identifie l'Intent de reveil avec un
		// ID et un/des flag(s)
		PendingIntent pendingintent = PendingIntent.getBroadcast(context, 0,
				intent, 0);

		// On annule l'alarm pour replanifier si besoin
		am.cancel(pendingintent);

		// ok maintenant, on va faire la différence entre l'heure actuelle et
		// celle choisie
		// long when = hourInMillis - cal.getTimeInMillis();

		// On ajoute le reveil au service de l'AlarmManager
		am.setRepeating(AlarmManager.RTC_WAKEUP, time,
				AlarmManager.INTERVAL_DAY, pendingintent);
	}
}
