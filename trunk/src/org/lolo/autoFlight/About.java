package org.lolo.autoFlight;

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
		
		// ajout du onClick sur l'image adelya
		ImageView iv = (ImageView) findViewById(R.id.imgAbout);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://www.adelya.com"));
				startActivity(i);
			}
		});
	}
}
