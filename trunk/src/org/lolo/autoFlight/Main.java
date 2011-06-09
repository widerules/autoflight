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
import android.widget.CheckBox;
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
		tp.setIs24HourView(Boolean.TRUE);
		final TimePicker tpFinalHour = (TimePicker) findViewById(R.id.tpFlightOutTime);
		tpFinalHour.setIs24HourView(Boolean.TRUE);
		final CheckBox chkEndTime = (CheckBox) findViewById(R.id.chkEndTime);

		// on récupère l'heure actuelle stockée (ou pas)
		final SharedPreferences sp = getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		
		initTimePicker();
		
        // 
        Button btnSave = (Button) findViewById(R.id.btnSavePref);
        btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String toastMsg = "";
				
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
				
				toastMsg += "Entrée Mode Avion: " + hour + ":" + min;
				
				if (chkEndTime.isChecked()) {
					Integer hourOut = tpFinalHour.getCurrentHour();
					Integer minOut = tpFinalHour.getCurrentMinute();
					Calendar calOut = Calendar.getInstance();
					
					if (hourOut < calOut.get(Calendar.HOUR_OF_DAY) || (hourOut == calOut.get(Calendar.HOUR_OF_DAY) && minOut < calOut.get(Calendar.MINUTE))) {
						calOut.roll(Calendar.DAY_OF_MONTH, 1);
					}
					calOut.set(Calendar.HOUR_OF_DAY, hourOut.intValue());
					calOut.set(Calendar.MINUTE, minOut.intValue());
					calOut.set(Calendar.SECOND, 0);
					
					sp.edit().putString("hourOut", "" + calOut.getTimeInMillis()).commit();
					
					toastMsg += "\nSortie Mode Avion: " + hourOut + ":" + minOut;
				}
				
				Toast.makeText(Main.this, toastMsg, Toast.LENGTH_LONG).show();
				
		        initAskerInTime();
			}
		});
        
        initAskerInTime();
    }

	/**
	 * Initialisation du timepicker avec l'heure qui a déjà été saisie... ou pas
	 */
	private void initTimePicker() {
		// on récupère l'heure actuelle stockée (ou pas)
		SharedPreferences sp = getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		// et voici l'heure... En timmeInMillis of course
		String hour = sp.getString("hour", "");
		String hourOut = sp.getString("hourOut", "");

		Calendar cal = Calendar.getInstance();
		Calendar calOut = Calendar.getInstance();

		final CheckBox chkEndTime = (CheckBox) findViewById(R.id.chkEndTime);
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
		if ("".equals(hourOut)) {
			calOut.roll(Calendar.DAY_OF_MONTH, 1);
			calOut.set(Calendar.HOUR_OF_DAY, 0);
			calOut.set(Calendar.MINUTE, 0);
			// et on décoche la checkbox
			chkEndTime.setChecked(Boolean.FALSE);
		} else {
			Long hourInMillis = Long.valueOf(hourOut);
			Calendar savedTime = Calendar.getInstance();
			savedTime.setTimeInMillis(hourInMillis);
			
			calOut.set(Calendar.HOUR_OF_DAY, savedTime.get(Calendar.HOUR_OF_DAY));
			calOut.set(Calendar.MINUTE, savedTime.get(Calendar.MINUTE));
			
			if (calOut.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
				calOut.roll(Calendar.DAY_OF_MONTH, 1);
			}
			chkEndTime.setChecked(Boolean.TRUE);
		}

		final TimePicker tp = (TimePicker) findViewById(R.id.tpFlightTime);
		tp.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		tp.setCurrentMinute(cal.get(Calendar.MINUTE));

		final TimePicker tpOut = (TimePicker) findViewById(R.id.tpFlightOutTime);
		tpOut.setCurrentHour(calOut.get(Calendar.HOUR_OF_DAY));
		tpOut.setCurrentMinute(calOut.get(Calendar.MINUTE));
	}

	/**
	 * Initialisation de l'alarme du mode avion
	 */
	private void initAskerInTime() {
		// on récupèe l'heure actuelle stockée (ou pas)
		SharedPreferences sp = getSharedPreferences("autoFlight",
				Activity.MODE_WORLD_WRITEABLE);
		// et voici l'heure... En timmeInMillis of course
		String hour = sp.getString("hour", "");
		String hourOut = sp.getString("hourOut", "");

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
		setAlarm(hourInMillis, SleepAlarmReceiver.class);
		
		// s'il n'y a pas d'heure de out flight, on ne sette rien
		if (!"".equals(hourOut)) {

			Long hourOutInMillis = Long.valueOf(hourOut);
			// on regarde si l'heure est avant maintenant ou pas dans quel cas on 
			// pousse d'un jour
			if (hourOutInMillis < Calendar.getInstance().getTimeInMillis()) {
				Calendar alarmHour = Calendar.getInstance();
				alarmHour.setTimeInMillis(hourInMillis);
				alarmHour.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1);
				alarmHour.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
				
				hourOutInMillis = alarmHour.getTimeInMillis();
			}
			setAlarm(hourOutInMillis, UnsleepAlarmReceiver.class);
		}
	}
	
	/**
	 * Sette une alarme et la classe associé
	 * @param time Temps en millis
	 * @param classToLaunch La classe à lancer lors de réveil
	 */
	private void setAlarm(Long time, Class<?> classToLaunch) {

		// Récupération de l'instance du service AlarmManager.
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// On instancie l'Intent qui va être appelé au moment du reveil.
		Intent intent = new Intent(this, classToLaunch);

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
		am.setRepeating(AlarmManager.RTC_WAKEUP, time,
				AlarmManager.INTERVAL_DAY, pendingintent);
	}
}