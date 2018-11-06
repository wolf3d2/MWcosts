package com.mwcorp.tools;

import com.mwcorp.costs.st;
import com.mwcorp.costs.var;
import com.mwcorp.costs.var.ArrayToolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Pref 
{
	private static String tmp = var.STR_NULL;
	private static Pref INSTANCE;
	SharedPreferences mPref;
	public static String PR_VIEW_MODE = "view_mode";
// ключ, перечисление кнопок панели инструментов
	public static String PR_TOOLBAR_BUTTON = "toolbar_button";
	public static String PR_TOOLBAR_LINE_COUNT_PORT = "toolbar_linecnt_port";
	public static String PR_TOOLBAR_LINE_COUNT_LAND = "toolbar_linecnt_land";
	public static String PR_TOOLBAR_BUTTON_DEF = "519,Setting toolbar;501,Home;502,End;503,Search;504,PgDn;505,PgUp";
	public static String PR_NEWREC_BIG_TOOLBAR = "newrec_big_toolbar";
	public static String PR_NEWREC_SHOW_TOOLBAR = "newrec_show_toolbar";
	public static String PR_NEWREC_EDIT_SIZE_TEXT = "newrec_size_text";
	public static String PR_NEWREC_SUM_SIZE = "newrec_size_sum";
	public static String PR_AUTOCALC = "auto_calculate";
	public static String PR_NEWREC_FULLSCREEN = "newrec_fullscreen";
	public static String PR_SKIN_APP = "skin_app";

	public static void init(Context c)
	{
		INSTANCE = new Pref();
//		SharedPreferences sp = c.getSharedPreferences("settings", 0);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		INSTANCE.mPref = sp;
//		INSTANCE.mPref = c.getSharedPreferences("settings", 0);
		//INSTANCE.mVolumeKeysState = INSTANCE.mPrefs.getInt(VOLUME_KEYS_STATE, VOLUME_KEYS_NONE);
	}
	public static void removePreferences()
	{
		get().edit().remove("newrec_col_tb");
	}
// читаем настройки
	public static void readPreference()
	{
		st.sleep(500);
		removePreferences();
		var.fl_view_mode = getBoolean(PR_VIEW_MODE, false);
		var.auto_calculate = getBoolean(PR_AUTOCALC, false);
		var.fl_newrec_fullscreen = getBoolean(PR_NEWREC_FULLSCREEN, false);
		var.fl_newrec_big_toolbar = getBoolean(Pref.PR_NEWREC_BIG_TOOLBAR, false);
		var.fl_newrec_show_toolbar = getBoolean(PR_NEWREC_SHOW_TOOLBAR, true);
		var.fl_newrec_big_size_sum = getBoolean(PR_NEWREC_SUM_SIZE, false);
		tmp = getString(Pref.PR_NEWREC_EDIT_SIZE_TEXT, "16");
		var.newrec_size_text = st.str2hex(tmp, 10);
		tmp = getString(Pref.PR_TOOLBAR_BUTTON, Pref.PR_TOOLBAR_BUTTON_DEF);
		if (!tmp.startsWith("Empty")){
			String[] ar = tmp.split(";");
			String[] arra;
			if (var.ar_tb!=null)
				var.ar_tb.clear();
			ArrayToolbar artb = new ArrayToolbar();
			for (int i=0;i<ar.length;i++){
				arra = ar[i].split(",");
				artb = new ArrayToolbar();
				artb.id = Integer.valueOf(arra[0]);
				artb.text = arra[1];
				var.ar_tb.add(artb);
			}			
		}
		var.tb_line_port = getInt(PR_TOOLBAR_LINE_COUNT_PORT, 1);
		var.tb_line_land = getInt(PR_TOOLBAR_LINE_COUNT_LAND, 1);
		var.skin_app = st.str2int(getString(PR_SKIN_APP, "0"), 0);
	}
//	public static void setFullScreen(boolean fullscreen)
//	{
//		INSTANCE.mPrefs.edit().putBoolean(FULLSCREEN, fullscreen).commit();
//	}
//	public static final boolean isExitPanel()
//	{
//		return Prefs.getBoolean(Prefs.EXIT_PANEL,true);
//	}
//	public static final void setExitConfirm(boolean val)
//	{
//		Prefs.setBoolean(EXIT_CONFIRM, val);
//	}
	public static SharedPreferences get()
	{
		return INSTANCE.mPref;
	}
	public static void setInt(String key,int value)
	{
		INSTANCE.mPref.edit().putInt(key, value).commit();
		readPreference();
	}
	public static void setString(String key,String value)
	{
		INSTANCE.mPref.edit().putString(key, value).commit();
		readPreference();
	}
	public static void setBoolean(String key,boolean value)
	{
		INSTANCE.mPref.edit().putBoolean(key, value).commit();
		readPreference();
	}
	public static final int getInt(String key,int fallback)
	{
		return get().getInt(key, fallback);
	}
	public static final String getString(String key,String fallback)
	{
		return get().getString(key, fallback);
	}
	public static final boolean getBoolean(String key, boolean def) {
		return get().getBoolean(key, def);
	}

}