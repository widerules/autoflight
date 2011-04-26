package org.lolo.autoFlight;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Main permettant de setter l'heure à laquelle on va demander si le téléphone
 * doit passer en mode avion.
 * 
 * @author lolo
 * 
 */
public class Main extends Activity {
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		final TimePicker tp = (TimePicker) findViewById(R.id.tpFlightTime);
		// on récupère l'heure actuelle stockée (ou pas)
		final SharedPreferences sp = getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		
		initTimePicker();
		
        // 
        Button btnSave = (Button) findViewById(R.id.btnSavePref);
        btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Integer hour = tp.getCurrentHour();
				Integer min = tp.getCurrentMinute();
				Calendar cal = Calendar.getInstance();
				
				if (hour < cal.get(Calendar.HOUR_OF_DAY) || (hour == cal.get(Calendar.HOUR_OF_DAY) && min < cal.get(Calendar.MINUTE))) {
					cal.roll(Calendar.DAY_OF_MONTH, 1);
				}
				cal.set(Calendar.HOUR_OF_DAY, hour.intValue());
				cal.set(Calendar.MINUTE, min.intValue());
				cal.set(Calendar.SECOND, 0);
				
				sp.edit().putString("hour", "" + cal.getTimeInMillis()).commit();
				
				Toast.makeText(Main.this, "Heure réglée sur " + hour + ":" + min, Toast.LENGTH_LONG).show();
				
		        initAskerInTime();
			}
		});
        
        initAskerInTime();
    }

	private void initTimePicker() {
		// on récupère l'heure actuelle stockée (ou pas)
		SharedPreferences sp = getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		// et voici l'heure... En timmeInMillis of course
		String hour = sp.getString("hour", "");

		Calendar cal = Calendar.getInstance();
		
		if ("".equals(hour)) {
			cal.roll(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			
		} else {
			cal.setTimeInMillis(Long.valueOf(hour));
		}

		final TimePicker tp = (TimePicker) findViewById(R.id.tpFlightTime);
		tp.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		tp.setCurrentMinute(cal.get(Calendar.MINUTE));
	}

	public void initAskerInTime() {
		// on récupère l'heure actuelle stockée (ou pas)
		SharedPreferences sp = getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		// et voici l'heure... En timmeInMillis of course
		String hour = sp.getString("hour", "");

		if ("".equals(hour)) {
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.roll(Calendar.DAY_OF_MONTH, 1);
			tomorrow.set(Calendar.HOUR_OF_DAY, 0);
			tomorrow.set(Calendar.MINUTE, 0);
			
			hour = "" + tomorrow.getTimeInMillis();
		}

		Long hourInMillis = Long.valueOf(hour);

		// Récupération de l'instance du service AlarmManager.
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// On instancie l'Intent qui va être appelé au moment du reveil.
		Intent intent = new Intent(this, AlarmReceiver.class);

		// On créer le pending Intent qui identifie l'Intent de reveil avec un
		// ID et un/des flag(s)
		PendingIntent pendingintent = PendingIntent.getBroadcast(this, 0,
				intent, 0);

		// On annule l'alarm pour replanifier si besoin
		am.cancel(pendingintent);

		// ok maintenant, on va faire la différence entre l'heure actuelle et
		// celle choisie
		// long when = hourInMillis - cal.getTimeInMillis();

		// On ajoute le reveil au service de l'AlarmManager
		am.setRepeating(AlarmManager.RTC_WAKEUP, hourInMillis,
				AlarmManager.INTERVAL_DAY, pendingintent);
	}
}