package org.lolo.autoFlight;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Intent permettant d'afficher le fait que le téléphone va passer en mode avion
 * @author lolo
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		
		NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		CharSequence contentTitle = "My notification";  // expanded message title
		CharSequence contentText = "Hello World!";      // expanded message text

		Intent notificationIntent = new Intent(context, Chooser.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, 0);
		
		Notification notification = new Notification(android.R.drawable.stat_notify_more, "Passage en mode avion", 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_SOUND;
		
		notif.notify(12, notification);
		
		notificationIntent.addFlags(Intent.FLAG_FROM_BACKGROUND); 
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		
		context.startActivity(notificationIntent);
	}
	
}
