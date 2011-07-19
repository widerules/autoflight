package org.lolo.autoFlight.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Classe utilitaire
 * 
 * @author laurent
 * 
 */
public class AdelyaUtil {

	/**
	 * Mise à jour d'une préférence
	 * 
	 * @param context Le contexte de l'application
	 * @param key La clé de la préférences à setter
	 * @param value La valeur de la préférences à setter
	 */
	public static void setPreferences(Context context, String key, String value) {
		// on récupère les SharedPreferences de l'appli
		SharedPreferences settings = context.getSharedPreferences("AutoFlight",
				Context.MODE_WORLD_WRITEABLE);
		Editor editor = settings.edit();
		editor.putString(key, value);
		// Commit the edits!
		editor.commit();
	}

	/**
	 * récupération d'une préférence
	 * 
	 * @param context Le contexte de l'application
	 * @param key La clé de la préférences à recupérer
	 * @return La valeur de la préférences
	 */
	public static String getPreferences(Context context, String key) {
		return getPreferences(context, key, "");
	}

	/**
	 * récupération d'une préférence
	 * 
	 * @param context Le contexte de l'application
	 * @param key La clé de la préférences à recupérer
	 * @return La valeur de la préférences
	 */
	public static String getPreferences(Context context, String key, String defaut) {
		// on récupère les SharedPreferences de l'appli
		SharedPreferences settings = context.getSharedPreferences("AutoFlight",
				Context.MODE_WORLD_WRITEABLE);
		return settings.getString(key, defaut);
	}
	
}
