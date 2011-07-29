package com.adelya.autoFlight;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

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
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		return;
	}
}
