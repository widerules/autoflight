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

		final TimePicker tpSleep = (TimePicker) findViewById(R.id.tpFlightTime);
		tpSleep.setIs24HourView(Boolean.TRUE); 
		final TimePicker tpWake = (TimePicker) findViewById(R.id.tpFlightOutTime);
		tpWake.setIs24HourView(Boolean.TRUE);
		
		// on récupère l'heure actuelle stockée (ou pas)
		final SharedPreferences sp = getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		
		initTimePickers();
		
        // 
        Button btnSave = (Button) findViewById(R.id.btnSavePref);
        btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Integer hour = tpSleep.getCurrentHour();
				Integer min = tpSleep.getCurrentMinute();
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

	/**
	 * Initialisation du timepicker avec l'heure qui a déjà été saisie... ou pas
	 */
	private void initTimePickers() {
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
			Long hourInMillis = Long.valueOf(hour);
			Calendar savedTime = Calendar.getInstance();
			savedTime.setTimeInMillis(hourInMillis);
			
			cal.set(Calendar.HOUR_OF_DAY, savedTime.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, savedTime.get(Calendar.MINUTE));
			
			if (cal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
				cal.roll(Calendar.DAY_OF_MONTH, 1);
			}
		}

		final TimePicker tpSleep = (TimePicker) findViewById(R.id.tpFlightTime);
		final TimePicker tpWake = (TimePicker) findViewById(R.id.tpFlightOutTime);
		tpSleep.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		tpSleep.setCurrentMinute(cal.get(Calendar.MINUTE));
		tpWake.setCurrentMinute(cal.get(Calendar.MINUTE));
	}

	/**
	 * Initialisation de l'alarme du mode avion
	 */
	private void initAskerInTime() {
		// on récupère l'heure actuelle stockée (ou pas)
		SharedPreferences sp = getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		// et voici l'heure... En timmeInMillis of course
		String hour = sp.getString("hour", "");

		// a-t-on déjà configuré l'heure du mode avion
		if ("".equals(hour)) {
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.roll(Calendar.DAY_OF_MONTH, 1);
			tomorrow.set(Calendar.HOUR_OF_DAY, 0);
			tomorrow.set(Calendar.MINUTE, 0);
			
			hour = "" + tomorrow.getTimeInMillis();
		}

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
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// On instancie l'Intent qui va être appelé au moment du reveil.
		Intent intent = new Intent(this, SleepAlarmReceiver.class);

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