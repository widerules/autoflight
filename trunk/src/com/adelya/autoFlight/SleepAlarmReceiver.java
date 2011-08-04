package com.adelya.autoFlight;

import java.util.Calendar;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;

import com.adelya.autoFlight.util.AdelyaUtil;

/**
 * Intent permettant d'afficher le fait que le téléphone va passer en mode avion
 * 
 * @author lolo
 * 
 */
public class SleepAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {

		if (Boolean.parseBoolean(AdelyaUtil.getPreferences(context,
				AdelyaUtil.PREF_ACTIVATE, "false"))) {
			// maintenant on va vérifier que le jour est voulu
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			// on fait un -2 car la semaine commence à 1 avec dimanche
			Integer today = cal.get(Calendar.DAY_OF_WEEK) - 2;
			// donc là dimanche = -1
			if (today < 0) {
				today += 7;
			}
			if (AdelyaUtil
					.getPreferences(context, AdelyaUtil.PREF_CHOOSEN_DAYS)
					.contains(today.toString())) {
				Intent notificationIntent = new Intent(context, Chooser.class);
				// parametre de la nouvelle intent
				notificationIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				// doit-on mettre le son?
				if ("true".equals(AdelyaUtil.getPreferences(context,
						AdelyaUtil.PREF_VIBRATE, "False"))) {
					// on fait vibrer un coup
					Vibrator vib = (Vibrator) context
							.getSystemService(Context.VIBRATOR_SERVICE);
					Integer vibrateTime = Integer.parseInt(AdelyaUtil
							.getPreferences(context,
									AdelyaUtil.PREF_VIBRATE_TIME, "1"));
					vib.vibrate(vibrateTime * 1000);
				}
				// doit-on mettre le son?
				if ("true".equals(AdelyaUtil.getPreferences(context,
						AdelyaUtil.PREF_SOUND, "False"))) {
					// on jour le son
					Ringtone ringtone = RingtoneManager
							.getRingtone(
									context,
									RingtoneManager
											.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
					ringtone.play();
				}

				// et on envoit la sauce à l'autre intent
				context.startActivity(notificationIntent);
			}
		}
	}
}
