package org.lolo.autoFlight.listview;

import java.util.Calendar;
import java.util.List;

import org.lolo.autoFlight.R;
import org.lolo.autoFlight.bean.Conf;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter de la liste des conf
 * 
 * @author laurent
 * 
 */
public class ConfAdapter extends ArrayAdapter<Conf> {

	private Context context;
	private List<Conf> items;
	private Boolean enabled = Boolean.TRUE;

	public ConfAdapter(Context context, int textViewResourceId, List<Conf> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return Boolean.FALSE;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public View getView(int position, final View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_conf, null);
		}
		final Conf conf = items.get(position);
		if (conf != null) {
			ImageView iv = (ImageView) v.findViewById(R.id.confImage);
			if (conf.getIconResource() != -1) {
				iv.setImageResource(conf.getIconResource());
			}
			TextView tt = (TextView) v.findViewById(R.id.txtConf);
			if (tt != null) {
				tt.setText(conf.getLabel());
				if (!enabled) {
					tt.setPaintFlags(tt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				} else {
					tt.setPaintFlags(Paint.DEV_KERN_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
				}
			}
			TextView ttv = (TextView) v.findViewById(R.id.txtConfValue);
			if (ttv != null) {
				String hour = conf.getValue().get(Calendar.HOUR_OF_DAY)
						+ ":"
						+ (conf.getValue().get(Calendar.MINUTE) < 10 ? "0" : "")
						+ conf.getValue().get(Calendar.MINUTE);
				ttv.setText(hour);
				if (!enabled) {
					ttv.setPaintFlags(ttv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				} else {
					ttv.setPaintFlags(Paint.DEV_KERN_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
				}
			}
		}
		return v;
	}
}
