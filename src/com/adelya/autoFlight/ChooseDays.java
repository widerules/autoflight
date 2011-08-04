package com.adelya.autoFlight;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.adelya.autoFlight.util.AdelyaUtil;

public class ChooseDays extends Dialog {

	private ListView lv;

	public ChooseDays(Context context) {
		super(context);
		setContentView(R.layout.dialog_choose_days);
		setTitle(getContext().getResources().getString(R.string.chooseDays));

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		getWindow().setAttributes(lp);

		lv = (ListView) findViewById(R.id.lvDays);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_list_item_multiple_choice, getContext()
						.getResources().getStringArray(R.array.days));
		// On attribue à notre listView l'adaptateur que l'on vient de créer
		// lv.setAdapter(mSchedule);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(adapter);
		
		// récupération des jours choisis
		String [] days = AdelyaUtil.getPreferences(context, AdelyaUtil.PREF_CHOOSEN_DAYS).split(",");
		
		for (String d : days) {
			if (!AdelyaUtil.isEmpty(d)) {
				Integer dI = Integer.parseInt(d);
				lv.setItemChecked(dI, Boolean.TRUE);
			}
		}

		Button btnOk = (Button) findViewById(R.id.btnConfOk);
		Button btnCancel = (Button) findViewById(R.id.btnConfCancel);

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ChooseDays.this.cancel();
			}
		});

		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String selectedDays = "";
				for (long l : lv.getCheckItemIds()) {
					selectedDays += Long.toString(l) + ",";
				}

				AdelyaUtil.setPreferences(ChooseDays.this.getContext(),
						AdelyaUtil.PREF_CHOOSEN_DAYS, selectedDays);

				ChooseDays.this.cancel();
			}
		});
	}
}
