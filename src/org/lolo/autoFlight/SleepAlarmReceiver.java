package org.lolo.autoFlight;

import org.lolo.autoFlight.util.AdelyaUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;

/**
 * Intent permettant d'afficher le fait que le téléphone va passer en mode avion
 * @author lolo
 *
 */
public class SleepAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		
		Intent notificationIntent = new Intent(context, Chooser.class);

		// parametre de la nouvelle intent
		notificationIntent.addFlags(Intent.FLAG_FROM_BACKGROUND); 
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 

		// doit-on mettre le son?
		if ("true".equals(AdelyaUtil.getPreferences(context, "enableVibrate", "true"))) {
			// on fait vibrer un coup
			Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vib.vibrate(500);
		}
		// doit-on mettre le son?
		if ("true".equals(AdelyaUtil.getPreferences(context, "enableSound", "true"))) {
			// on jour le son
			Ringtone ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			ringtone.play();
		}
		
		// et on envoit la sauce à l'autre intent
		context.startActivity(notificationIntent);
	}
}
