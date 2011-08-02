package com.adelya.autoFlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.adelya.autoFlight.util.AdelyaUtil;

/**
 * Classe qui va permettre de configurer l'application avec des conf par défaut
 * 
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
		final TextView tv = (TextView) findViewById(R.id.tvSeekBarToEdit);
		final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBarVibrate);

		cbSound.setChecked(AdelyaUtil.getPreferences(Config.this,
				AdelyaUtil.PREF_SOUND, "true").equals("true"));
		cbVibrate.setChecked(AdelyaUtil.getPreferences(Config.this,
				AdelyaUtil.PREF_VIBRATE, "true").equals("true"));
		cbVibrate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				seekBar.setEnabled(cbVibrate.isChecked());
			}
		});

		Integer vibrateTime = Integer.parseInt(AdelyaUtil.getPreferences(
				Config.this, AdelyaUtil.PREF_VIBRATE_TIME, "1"));
		tv.setText(" (" + vibrateTime + " s.)");
		seekBar.setProgress(vibrateTime);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tv.setText(" (" + progress + " s.)");
			}
		});

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
				AdelyaUtil.setPreferences(Config.this, AdelyaUtil.PREF_SOUND,
						Boolean.toString(cbSound.isChecked()));
				AdelyaUtil.setPreferences(Config.this, AdelyaUtil.PREF_VIBRATE,
						Boolean.toString(cbVibrate.isChecked()));
				AdelyaUtil.setPreferences(Config.this, AdelyaUtil.PREF_VIBRATE_TIME,
						Integer.toString(seekBar.getProgress()));
				Config.this.finish();
			}
		});
	}
}
