package com.adelya.autoFlight.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adelya.autoFlight.R;
import com.adelya.autoFlight.util.AdelyaUtil;

public class SelectDayAdapter extends ArrayAdapter<String> {

	private Context context;
	
	public SelectDayAdapter(Context context) {
		super(context, R.layout.item_days);
		this.context = context;
		this.add("");
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_days, null);
			
			TextView tv = (TextView) v.findViewById(R.id.txtChoosenDays);
			tv.setText("");
			String strDays = AdelyaUtil.getPreferences(context, AdelyaUtil.PREF_CHOOSEN_DAYS);
			
			String [] days = strDays.split(",");
			
			for (String d : days) {
				if (!AdelyaUtil.isEmpty(d)) {
					Integer lI = Integer.parseInt(d);
					
					String short_day = context.getResources().getStringArray(R.array.short_days)[lI];
					
					tv.setText(tv.getText() + short_day + "/");
				}
			}
		}
		return v;
	}
	
}
