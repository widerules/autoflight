package org.lolo.autoFlight;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SleepAlarmBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {

		// on récupére l'heure actuelle stockée (ou pas)
		SharedPreferences sp = context.getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		// et voici l'heure... En timmeInMillis of course
		String hour = sp.getString("hour", "");
		// si l'heure n'est pas trouvé, on se fait pas chié --> pas d'alarme
		if (!"".equals(hour)) {

			Calendar tomorrow = Calendar.getInstance();
			tomorrow.roll(Calendar.DAY_OF_MONTH, 1);
			tomorrow.set(Calendar.HOUR_OF_DAY, 0);
			tomorrow.set(Calendar.MINUTE, 0);
			
			hour = "" + tomorrow.getTimeInMillis();
			
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

			// Récupération de l'instance du service AlarmManager.
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			// On instancie l'Intent qui va étre appelé au moment du reveil.
			Intent intent = new Intent(context, SleepAlarmReceiver.class);

			// On créer le pending Intent qui identifie l'Intent de reveil avec
			// un
			// ID et un/des flag(s)
			PendingIntent pendingintent = PendingIntent.getBroadcast(context,
					0, intent, 0);

			// On annule l'alarm pour replanifier si besoin
			am.cancel(pendingintent);

			// ok maintenant, on va faire la différence entre l'heure actuelle
			// et
			// celle choisie
			// long when = hourInMillis - cal.getTimeInMillis();

			// On ajoute le reveil au service de l'AlarmManager
			am.setRepeating(AlarmManager.RTC_WAKEUP, hourInMillis,
					AlarmManager.INTERVAL_DAY, pendingintent);
		}
	}
}