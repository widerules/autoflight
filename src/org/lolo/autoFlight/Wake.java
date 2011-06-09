package org.lolo.autoFlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Classe qui va juste enlever le mode avion
 * 
 * @author laurent
 *
 */
public class Wake extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Settings.System.putInt(Wake.this.getApplicationContext()
				.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", 0);
		Wake.this.getApplicationContext().sendBroadcast(intent);

		Wake.this.finish();
	}
}
