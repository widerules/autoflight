package org.lolo.autoFlight;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Main permettant de setter l'heure à laquelle on va demander si le téléphone 
 * doit passer en mode avion.
 * @author lolo
 *
 */
public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initAskerInTime();
    }
    
    public void initAskerInTime() {
    	// on récupère l'heure actuelle stockée (ou pas)
    	SharedPreferences sp = getSharedPreferences("autoFlight", Activity.MODE_PRIVATE);
    	// et voici l'heure... En timmeInMillis of course
    	String hour = sp.getString("hour", "");
    	
    	///////////////
    	// HACK
    	///////////////
    	hour = "" + (Calendar.getInstance().getTimeInMillis() + 5000);
    	sp.edit().putString("hour", Long.valueOf(hour).toString());
    	///////////////
    	// \HACK
    	///////////////
    	
    	Long hourInMillis = Long.valueOf(hour);
    	
    	//Récupération de l'instance du service AlarmManager.
    	AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    	//On instancie l'Intent qui va être appelé au moment du reveil.
    	Intent intent = new Intent(this, AlarmReceiver.class);

    	//On créer le pending Intent qui identifie l'Intent de reveil avec un ID et un/des flag(s)
    	PendingIntent pendingintent = PendingIntent.getBroadcast(this, 0, intent, 0);

    	//On annule l'alarm pour replanifier si besoin
    	am.cancel(pendingintent);
    	
    	// l'heure actuelle
    	Calendar cal = Calendar.getInstance();
    	
    	// ok maintenant, on va faire la différence entre l'heure actuelle et 
    	// celle choisie
    	long when = hourInMillis - cal.getTimeInMillis();
    	
    	//On ajoute le reveil au service de l'AlarmManager
    	am.setRepeating(AlarmManager.RTC_WAKEUP, hourInMillis, AlarmManager.INTERVAL_DAY, pendingintent);
    }
}