package org.lolo.autoFlight;

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

		// on récupère l'heure actuelle stockée (ou pas)
		SharedPreferences sp = context.getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		// et voici l'heure... En timmeInMillis of course
		String hour = sp.getString("hour", "");
		// si l'heure n'est pas trouvé, on se fait pas chié --> pas d'alarme
		if (!"".equals(hour)) {

			Long hourInMillis = Long.valueOf(hour);

			// Récupération de l'instance du service AlarmManager.
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			// On instancie l'Intent qui va être appelé au moment du reveil.
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
