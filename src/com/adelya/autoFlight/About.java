package com.adelya.autoFlight;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);
		setTitle(getResources().getString(R.string.about));

		// ajout du onClick sur l'image adelya
		ImageView iv = (ImageView) findViewById(R.id.imgAdelya);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://www.adelya.com"));
				startActivity(i);
			}
		});
		// ajout du onClick sur l'image adelya
		ImageView ivLA = (ImageView) findViewById(R.id.imgLA);
		ivLA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://asp2.adelya.com/la/"));
				startActivity(i);
			}
		});
		// TextView dans le quel on ajout le numéro de version
		ComponentName comp = new ComponentName(this, About.class);
		try {
			PackageInfo pinfo = getPackageManager().getPackageInfo(
					comp.getPackageName(), 0);
			TextView tvCopyright = (TextView) findViewById(R.id.copyright);
			tvCopyright.setText(tvCopyright.getText() + " - " + pinfo.versionName);
			tvCopyright.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(About.this, Log.class);
					startActivity(intent);
				}
			});
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		return;
	}
}
