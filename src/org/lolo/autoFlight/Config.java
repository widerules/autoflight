package org.lolo.autoFlight;

import org.lolo.autoFlight.util.AdelyaUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Classe qui va permettre de configurer l'application avec des conf par défaut
 * @author laurent
 *
 */
public class Config extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.configuration));
		setContentView(R.layout.config);
		
		final CheckBox cbSound = (CheckBox) findViewById(R.id.cbSound);
		final CheckBox cbVibrate = (CheckBox) findViewById(R.id.cbVibrate);

		cbSound.setChecked(AdelyaUtil.getPreferences(Config.this, "enableSound", "true").equals("true"));
		cbVibrate.setChecked(AdelyaUtil.getPreferences(Config.this, "enableVibrate", "true").equals("true"));

		Button btnOk = (Button) findViewById(R.id.btnConfOk);
		Button btnCancel = (Button) findViewById(R.id.btnConfCancel);
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Config.this.finish();
			}
		});
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AdelyaUtil.setPreferences(Config.this, "enableSound", Boolean.toString(cbSound.isChecked()));
				AdelyaUtil.setPreferences(Config.this, "enableVibrate", Boolean.toString(cbVibrate.isChecked()));
				Config.this.finish();
			}
		});
	}
}
