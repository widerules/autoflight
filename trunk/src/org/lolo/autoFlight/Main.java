package org.lolo.autoFlight;

import java.util.ArrayList;
import java.util.Calendar;

import org.lolo.autoFlight.bean.Conf;
import org.lolo.autoFlight.listview.ConfAdapter;
import org.lolo.autoFlight.util.AdelyaUtil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * Main permettant de setter l'heure à laquelle on va demander si le téléphone
 * doit passer en mode avion.
 * 
 * @author lolo
 * 
 */
public class Main extends Activity {

	/** The start calendar */
	private Calendar startCal;
	/** The ending calendar */
	private Calendar endCal;
	/** THE list */
	private ListView lv;
	/** The adapter that manipulate the listView that contains hours  */
	private ConfAdapter adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		lv = (ListView) findViewById(R.id.listViewConf);
		adapter = new ConfAdapter(this, R.layout.item_conf,
				new ArrayList<Conf>());

		final CheckBox chkActivate = (CheckBox) findViewById(R.id.chkActivate);
		chkActivate.setChecked(Boolean.parseBoolean(AdelyaUtil.getPreferences(
				Main.this, "chkActivate", "false")));
		chkActivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AdelyaUtil.setPreferences(Main.this, "chkActivate",
						Boolean.toString(((CheckBox) v).isChecked()));

				toggleOptions(((CheckBox) v).isChecked());
			}
		});

		initTimePicker();
		
		initAdapter();

		toggleOptions(Boolean.parseBoolean(AdelyaUtil.getPreferences(Main.this, "chkActivate", "true")));
		
		initAskerInTime();

		initAds();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.about:
			intent = new Intent(Main.this, About.class);
			startActivity(intent);
			return true;
		case R.id.config:
			intent = new Intent(Main.this, Config.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void initAdapter() {

		adapter.add(new Conf(getResources().getString(R.string.txtBeginDate),
				startCal, R.drawable.ic_dialog_time) {
			@Override
			public void launchAction() {

				final Dialog dialog = new Dialog(Main.this);
				dialog.setContentView(R.layout.dialog_time);
				dialog.setTitle(this.getLabel());

				// ouverture de la dialog permettant de choisir l'heure de début
				final TimePicker tp = (TimePicker) dialog
						.findViewById(R.id.tpTime);
				tp.setIs24HourView(Boolean.TRUE);
				tp.setCurrentHour(startCal.get(Calendar.HOUR_OF_DAY));
				tp.setCurrentMinute(startCal.get(Calendar.MINUTE));
				// set du bouton ok
				Button ok = (Button) dialog.findViewById(R.id.btnConfOk);
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Integer hour = tp.getCurrentHour();
						Integer min = tp.getCurrentMinute();
						Calendar cal = Calendar.getInstance();

						if (hour < cal.get(Calendar.HOUR_OF_DAY)
								|| (hour == cal.get(Calendar.HOUR_OF_DAY) && min < cal
										.get(Calendar.MINUTE))) {
							cal.add(Calendar.DAY_OF_MONTH, 1);
						}
						cal.set(Calendar.HOUR_OF_DAY, hour.intValue());
						cal.set(Calendar.MINUTE, min.intValue());
						cal.set(Calendar.SECOND, 0);

						setValue(cal);

						AdelyaUtil.setPreferences(Main.this, "hour",
								"" + cal.getTimeInMillis());

						initAskerInTime();
						dialog.dismiss();

						adapter.notifyDataSetChanged();
					}
				});
				Button cancel = (Button) dialog
						.findViewById(R.id.btnConfCancel);
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		adapter.add(new Conf(getResources().getString(R.string.txtEndDate),
				endCal, R.drawable.ic_dialog_time) {
			@Override
			public void launchAction() {

				final Dialog dialog = new Dialog(Main.this);
				dialog.setContentView(R.layout.dialog_time);
				dialog.setTitle(this.getLabel());

				// ouverture de la dialog permettant de choisir l'heure de début
				final TimePicker tp = (TimePicker) dialog
						.findViewById(R.id.tpTime);
				tp.setIs24HourView(Boolean.TRUE);
				tp.setCurrentHour(endCal.get(Calendar.HOUR_OF_DAY));
				tp.setCurrentMinute(endCal.get(Calendar.MINUTE));
				// set du bouton ok
				Button ok = (Button) dialog.findViewById(R.id.btnConfOk);
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Integer hour = tp.getCurrentHour();
						Integer min = tp.getCurrentMinute();
						Calendar cal = Calendar.getInstance();

						if (hour < cal.get(Calendar.HOUR_OF_DAY)
								|| (hour == cal.get(Calendar.HOUR_OF_DAY) && min < cal
										.get(Calendar.MINUTE))) {
							cal.add(Calendar.DAY_OF_MONTH, 1);
						}
						cal.set(Calendar.HOUR_OF_DAY, hour.intValue());
						cal.set(Calendar.MINUTE, min.intValue());
						cal.set(Calendar.SECOND, 0);

						setValue(cal);

						AdelyaUtil.setPreferences(Main.this, "hourOut", ""
								+ cal.getTimeInMillis());
						
						initAskerInTime();
						dialog.dismiss();

						adapter.notifyDataSetChanged();
					}
				});
				Button cancel = (Button) dialog
						.findViewById(R.id.btnConfCancel);
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Conf c = adapter.getItem(position);
				c.launchAction();
			}
		});
	}

	/**
	 * Création de la zone de pub
	 */
	private void initAds() {

		// mise en place de l'AdMod
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adlayout);
		// Create the adView
		// Please replace MY_BANNER_UNIT_ID with your AdMob Publisher ID
		final AdView adView = new AdView(this, AdSize.BANNER, "a14e24400b86401");

		// Add the adView to it
		adLayout.addView(adView);

		// Initiate a generic request to load it with an ad
		final AdRequest request = new AdRequest();

		adView.loadAd(request);

	}

	/**
	 * Initialisation du timepicker avec l'heure qui a déjà été saisie... ou pas
	 */
	private void initTimePicker() {
		// et voici l'heure... En timmeInMillis of course
		String hour = AdelyaUtil.getPreferences(Main.this, "hour", "");
		String hourOut = AdelyaUtil.getPreferences(Main.this, "hourOut", "");

		startCal = Calendar.getInstance();
		endCal = Calendar.getInstance();

		if ("".equals(hour)) {
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			startCal.set(Calendar.HOUR_OF_DAY, 23);
			startCal.set(Calendar.MINUTE, 0);
		} else {
			Long hourInMillis = Long.valueOf(hour);
			Calendar savedTime = Calendar.getInstance();
			savedTime.setTimeInMillis(hourInMillis);

			startCal.set(Calendar.HOUR_OF_DAY,
					savedTime.get(Calendar.HOUR_OF_DAY));
			startCal.set(Calendar.MINUTE, savedTime.get(Calendar.MINUTE));

			while (startCal.getTimeInMillis() < Calendar.getInstance()
					.getTimeInMillis()) {
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		if ("".equals(hourOut)) {
			endCal.add(Calendar.DAY_OF_MONTH, 1);
			endCal.set(Calendar.HOUR_OF_DAY, 6);
			endCal.set(Calendar.MINUTE, 0);
		} else {
			Long hourInMillis = Long.valueOf(hourOut);
			Calendar savedTime = Calendar.getInstance();
			savedTime.setTimeInMillis(hourInMillis);

			endCal.set(Calendar.HOUR_OF_DAY,
					savedTime.get(Calendar.HOUR_OF_DAY));
			endCal.set(Calendar.MINUTE, savedTime.get(Calendar.MINUTE));

			while (endCal.getTimeInMillis() < Calendar.getInstance()
					.getTimeInMillis()) {
				endCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
	}

	/**
	 * Initialisation de l'alarme du mode avion
	 */
	private void initAskerInTime() {
		// et voici l'heure... En timmeInMillis of course
		String hour = AdelyaUtil.getPreferences(Main.this, "hour", "");
		String hourOut = AdelyaUtil.getPreferences(Main.this, "hourOut", "");

		// a-t-on déjà configuré l'heure du mode avion
		if (!"".equals(hour)) {
			Long hourInMillis = Long.valueOf(hour);
			// on regarde si l'heure est avant maintenant ou pas dans quel cas
			// on
			// pousse d'un jour
			if (hourInMillis < Calendar.getInstance().getTimeInMillis()) {
				Calendar alarmHour = Calendar.getInstance();
				alarmHour.setTimeInMillis(hourInMillis);
				alarmHour.set(Calendar.DAY_OF_MONTH, Calendar.getInstance()
						.get(Calendar.DAY_OF_MONTH) + 1);
				alarmHour.set(Calendar.MONTH,
						Calendar.getInstance().get(Calendar.MONTH));

				hourInMillis = alarmHour.getTimeInMillis();
			}
			setAlarm(hourInMillis, SleepAlarmReceiver.class);
		}

		// s'il n'y a pas d'heure de out flight, on ne sette rien
		if (!"".equals(hourOut)) {

			Long hourOutInMillis = Long.valueOf(hourOut);
			// on regarde si l'heure est avant maintenant ou pas dans quel cas
			// on pousse d'un jour
			if (hourOutInMillis < Calendar.getInstance().getTimeInMillis()) {
				Calendar alarmHour = Calendar.getInstance();
				alarmHour.setTimeInMillis(hourOutInMillis);
				alarmHour.set(Calendar.DAY_OF_MONTH, Calendar.getInstance()
						.get(Calendar.DAY_OF_MONTH) + 1);
				alarmHour.set(Calendar.MONTH,
						Calendar.getInstance().get(Calendar.MONTH));

				hourOutInMillis = alarmHour.getTimeInMillis();
			}
			setAlarm(hourOutInMillis, UnsleepAlarmReceiver.class);
		}
	}

	/**
	 * Sette une alarme et la classe associé
	 * 
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
		removeAlarm(classToLaunch);

		// ok maintenant, on va faire la différence entre l'heure actuelle et
		// celle choisie
		// long when = hourInMillis - cal.getTimeInMillis();

		// On ajoute le reveil au service de l'AlarmManager
		am.setRepeating(AlarmManager.RTC_WAKEUP, time,
				AlarmManager.INTERVAL_DAY, pendingintent);
	}

	/**
	 * Désactive une alarme déjà setté
	 */
	private void removeAlarm(Class<?> classToLaunch) {

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
	}
	
	/**
	 * Deactivate / activate options
	 */
	private void toggleOptions(Boolean isChecked) {

		adapter.setEnabled(isChecked);
		adapter.notifyDataSetChanged();

		if (!isChecked) {

			// On annule l'alarm pour replanifier si besoin
			removeAlarm(SleepAlarmReceiver.class);
			removeAlarm(UnsleepAlarmReceiver.class);
		} else {
			initAskerInTime();
		}
	}
}