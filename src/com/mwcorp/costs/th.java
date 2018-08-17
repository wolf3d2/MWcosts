package com.mwcorp.costs;

import java.util.ArrayList;

import com.mwcorp.tools.Pref;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;

// управление скинами
public class th 
{
	// цвета элементов0
	public static final int ALERT_WINDOW_BACK = 60;
	public static final int ALERT_WINDOW_TEXT_LIST_BUTTON = 61;
	public static final int ALERT_WINDOW_BUTTON_TEXT = 62;
	public static final int ALERT_EDITTEXT = 64;

	public static final int MAIN_TOP_PANEL_BACK = 0;
	public static final int MAIN_BUTTON_TEXT_TOP_PANEL = 1;
	public static final int MAIN_LISTVIEW_NOTSELECT = 2;
	public static final int MAIN_LISTVIEW_SELECT = 3;

	public static final int EDITCURRENCY_BACK = 20;
	public static final int EDITCURRENCY_EDITTEXT_BACK = 21;
	public static final int EDITCURRENCY_EDITTEXT_TEXTVIEW = 22;
	public static final int EDITCURRENCY_EDITTEXT_TEXT = 23;

	public static final int NEWREC_TOPPANEL = 30;
	public static final int NEWREC_TOPPANEL_SUM_TEXT = 31;
	public static final int NEWREC_TOOLBAR_BACK = 32;
	public static final int NEWREC_EDITTEXT_BACK = 33;
	public static final int NEWREC_EDITTEXT_TEXT = 34;
	public static final int NEWREC_TOOLBAR_BUTTON_TEXT = 35;
	public static final int NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON = 36;

	public static final int PREF_ACTIVITY_BACK = 40;
	public static final int PREF_ACTIVITY_MAIN_TEXT = 41;
	public static final int PREF_ACTIVITY_DESC_TEXT = 42;

	public static final int ABOUT_BACK = 50;
	public static final int ABOUT_TEXT_BACK = 51;
	public static final int ABOUT_TEXT = 52;
	public static final int ABOUT_TEXT_DESC = 53;
	public static final int ABOUT_VERSION = 54;
	public static final int ABOUT_BUTTON_TEXT = 55;
	
	protected static ArrayList<Theme> getSkinLight() 
	{
		ArrayList<Theme> ar = new ArrayList<Theme>();
		ar.add(new Theme(MAIN_TOP_PANEL_BACK,Color.TRANSPARENT));
		ar.add(new Theme(th.MAIN_BUTTON_TEXT_TOP_PANEL,Color.BLACK));
		ar.add(new Theme(th.MAIN_LISTVIEW_NOTSELECT,Color.WHITE));
		ar.add(new Theme(th.MAIN_LISTVIEW_SELECT,Color.GRAY));
		ar.add(new Theme(th.ALERT_WINDOW_BACK, 0xff141414));
		ar.add(new Theme(th.ALERT_WINDOW_BUTTON_TEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_WINDOW_TEXT_LIST_BUTTON, Color.WHITE));
		ar.add(new Theme(th.ALERT_EDITTEXT, Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_BACK,Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_BACK,Color.MAGENTA));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXT,Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXTVIEW,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOPPANEL,0xff4B0082));
		ar.add(new Theme(th.NEWREC_TOPPANEL_SUM_TEXT,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK,0xff4B0082));
		ar.add(new Theme(th.NEWREC_EDITTEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.NEWREC_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BUTTON_TEXT,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON, R.drawable.round_button_blue));
		ar.add(new Theme(th.PREF_ACTIVITY_BACK,Color.WHITE));
		ar.add(new Theme(th.PREF_ACTIVITY_MAIN_TEXT,Color.BLACK));
		ar.add(new Theme(th.PREF_ACTIVITY_DESC_TEXT,Color.GRAY));
		ar.add(new Theme(th.ABOUT_BACK,Color.WHITE));
		ar.add(new Theme(th.ABOUT_TEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.ABOUT_TEXT,Color.BLACK));
		ar.add(new Theme(th.ABOUT_BACK,Color.BLUE));
		ar.add(new Theme(th.ABOUT_TEXT_DESC,Color.BLUE));
		ar.add(new Theme(th.ABOUT_VERSION,Color.RED));
		ar.add(new Theme(th.ABOUT_BUTTON_TEXT,Color.BLACK));
		return ar;
	}
	protected static ArrayList<Theme> getSkinGray() 
	{
		ArrayList<Theme> ar = new ArrayList<Theme>();
		ar.add(new Theme(MAIN_TOP_PANEL_BACK,Color.GRAY));
		ar.add(new Theme(th.MAIN_BUTTON_TEXT_TOP_PANEL,Color.BLACK));
		ar.add(new Theme(th.MAIN_LISTVIEW_NOTSELECT,Color.GRAY));
		ar.add(new Theme(th.MAIN_LISTVIEW_SELECT,Color.DKGRAY));
		ar.add(new Theme(th.ALERT_WINDOW_BACK, 0xff141414));
		ar.add(new Theme(th.ALERT_WINDOW_BUTTON_TEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_EDITTEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_WINDOW_TEXT_LIST_BUTTON, Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_BACK,Color.GRAY));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXTVIEW,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOPPANEL,Color.LTGRAY));
		ar.add(new Theme(th.NEWREC_TOPPANEL_SUM_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK,Color.DKGRAY));
		ar.add(new Theme(th.NEWREC_EDITTEXT_BACK,Color.GRAY));
		ar.add(new Theme(th.NEWREC_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BUTTON_TEXT,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON, R.drawable.round_button_darkgray));
		ar.add(new Theme(th.PREF_ACTIVITY_BACK,Color.GRAY));
		ar.add(new Theme(th.PREF_ACTIVITY_MAIN_TEXT,Color.BLACK));
		ar.add(new Theme(th.PREF_ACTIVITY_DESC_TEXT,Color.LTGRAY));
		ar.add(new Theme(th.ABOUT_BACK,Color.GRAY));
		ar.add(new Theme(th.ABOUT_TEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.ABOUT_TEXT,Color.BLACK));
		ar.add(new Theme(th.ABOUT_BACK,Color.BLUE));
		ar.add(new Theme(th.ABOUT_TEXT_DESC,Color.BLUE));
		ar.add(new Theme(th.ABOUT_VERSION,Color.RED));
		ar.add(new Theme(th.ABOUT_BUTTON_TEXT,Color.BLACK));
		return ar;
	}
	protected static ArrayList<Theme> getSkinBlack() 
	{
		ArrayList<Theme> ar = new ArrayList<Theme>();
		ar.add(new Theme(MAIN_TOP_PANEL_BACK,Color.BLACK));
		ar.add(new Theme(th.MAIN_BUTTON_TEXT_TOP_PANEL,Color.WHITE));
		ar.add(new Theme(th.MAIN_LISTVIEW_NOTSELECT,Color.BLACK));
		ar.add(new Theme(th.MAIN_LISTVIEW_SELECT,Color.DKGRAY));
		ar.add(new Theme(th.ALERT_WINDOW_BACK, 0xff141414));
		ar.add(new Theme(th.ALERT_WINDOW_BUTTON_TEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_EDITTEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_WINDOW_TEXT_LIST_BUTTON, Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_BACK,Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXTVIEW,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOPPANEL,Color.BLUE));
		ar.add(new Theme(th.NEWREC_TOPPANEL_SUM_TEXT,Color.GREEN));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK,Color.BLACK));
		ar.add(new Theme(th.NEWREC_EDITTEXT_BACK,Color.BLACK));
		ar.add(new Theme(th.NEWREC_EDITTEXT_TEXT,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BUTTON_TEXT,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON, R.drawable.round_button_darkgray));
		ar.add(new Theme(th.PREF_ACTIVITY_BACK,Color.BLACK));
		ar.add(new Theme(th.PREF_ACTIVITY_MAIN_TEXT,Color.WHITE));
		ar.add(new Theme(th.PREF_ACTIVITY_DESC_TEXT,Color.GRAY));
		ar.add(new Theme(th.ABOUT_BACK,Color.BLACK));
		ar.add(new Theme(th.ABOUT_TEXT_BACK,Color.BLACK));
		ar.add(new Theme(th.ABOUT_TEXT,Color.WHITE));
		ar.add(new Theme(th.ABOUT_BACK,Color.BLUE));
		ar.add(new Theme(th.ABOUT_TEXT_DESC,Color.BLUE));
		ar.add(new Theme(th.ABOUT_VERSION,Color.RED));
		ar.add(new Theme(th.ABOUT_BUTTON_TEXT,Color.WHITE));
		return ar;
	}
	protected static ArrayList<Theme> getSkinOrange() 
	{
		ArrayList<Theme> ar = new ArrayList<Theme>();
		ar.add(new Theme(MAIN_TOP_PANEL_BACK,0xffFF4500));//0xff
		ar.add(new Theme(th.MAIN_BUTTON_TEXT_TOP_PANEL,Color.BLACK));
		ar.add(new Theme(th.MAIN_LISTVIEW_NOTSELECT,0xffFFA07A));
		ar.add(new Theme(th.MAIN_LISTVIEW_SELECT,Color.LTGRAY));
		ar.add(new Theme(th.ALERT_WINDOW_BACK, 0xffFF4500));
		ar.add(new Theme(th.ALERT_WINDOW_TEXT_LIST_BUTTON, Color.BLACK));
		ar.add(new Theme(th.ALERT_EDITTEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_WINDOW_BUTTON_TEXT, Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_BACK,0xffFFA07A));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXTVIEW,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOPPANEL,0xffFF7F50));
		ar.add(new Theme(th.NEWREC_TOPPANEL_SUM_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK,0xffFF7F50));
		ar.add(new Theme(th.NEWREC_EDITTEXT_BACK,0xffFFA07A));
		ar.add(new Theme(th.NEWREC_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BUTTON_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON, R.drawable.round_button_red));
		ar.add(new Theme(th.PREF_ACTIVITY_BACK,0xffFFA07A));
		ar.add(new Theme(th.PREF_ACTIVITY_MAIN_TEXT,Color.WHITE));
		ar.add(new Theme(th.PREF_ACTIVITY_DESC_TEXT,Color.GRAY));
		ar.add(new Theme(th.ABOUT_BACK,0xffFF7F50));
		ar.add(new Theme(th.ABOUT_TEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.ABOUT_TEXT,Color.BLUE));
		ar.add(new Theme(th.ABOUT_TEXT_DESC,Color.RED));
		ar.add(new Theme(th.ABOUT_VERSION,Color.BLACK));
		ar.add(new Theme(th.ABOUT_BUTTON_TEXT,Color.WHITE));
		return ar;
	}
	protected static ArrayList<Theme> getSkinWhiteBlue() 
	{
		ArrayList<Theme> ar = new ArrayList<Theme>();
		ar.add(new Theme(MAIN_TOP_PANEL_BACK,Color.BLUE));
		ar.add(new Theme(th.MAIN_BUTTON_TEXT_TOP_PANEL,Color.WHITE));
		ar.add(new Theme(th.MAIN_LISTVIEW_NOTSELECT,Color.WHITE));
		ar.add(new Theme(th.MAIN_LISTVIEW_SELECT,Color.GRAY));
		ar.add(new Theme(th.ALERT_WINDOW_BACK, Color.BLUE));
		ar.add(new Theme(th.ALERT_WINDOW_BUTTON_TEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_EDITTEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_WINDOW_TEXT_LIST_BUTTON, Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_BACK,Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_BACK,Color.BLUE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXT,Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXTVIEW,Color.BLUE));
		ar.add(new Theme(th.NEWREC_TOPPANEL,Color.BLUE));
		ar.add(new Theme(th.NEWREC_TOPPANEL_SUM_TEXT,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK,Color.BLUE));
		ar.add(new Theme(th.NEWREC_EDITTEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.NEWREC_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BUTTON_TEXT,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON, R.drawable.round_button_blue));
		ar.add(new Theme(th.PREF_ACTIVITY_BACK,Color.BLACK));
		ar.add(new Theme(th.PREF_ACTIVITY_MAIN_TEXT,Color.WHITE));
		ar.add(new Theme(th.PREF_ACTIVITY_DESC_TEXT,Color.GRAY));
		ar.add(new Theme(th.ABOUT_BACK,Color.BLUE));
		ar.add(new Theme(th.ABOUT_TEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.ABOUT_TEXT,Color.BLACK));
		ar.add(new Theme(th.ABOUT_BACK,Color.BLUE));
		ar.add(new Theme(th.ABOUT_TEXT_DESC,Color.BLUE));
		ar.add(new Theme(th.ABOUT_VERSION,Color.RED));
		ar.add(new Theme(th.ABOUT_BUTTON_TEXT,Color.WHITE));
		return ar;
	}
	protected static ArrayList<Theme> getSkinLightGreen() 
	{
		ArrayList<Theme> ar = new ArrayList<Theme>();
		ar.add(new Theme(MAIN_TOP_PANEL_BACK,Color.GREEN));
		ar.add(new Theme(th.MAIN_BUTTON_TEXT_TOP_PANEL,Color.BLACK));
		ar.add(new Theme(th.MAIN_LISTVIEW_NOTSELECT,Color.WHITE));
		ar.add(new Theme(th.MAIN_LISTVIEW_SELECT,Color.GRAY));
		ar.add(new Theme(th.ALERT_WINDOW_BACK, Color.GREEN));
		ar.add(new Theme(th.ALERT_WINDOW_BUTTON_TEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_EDITTEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_WINDOW_TEXT_LIST_BUTTON, Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_BACK,Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_BACK,Color.GREEN));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXTVIEW,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOPPANEL,Color.GREEN));
		ar.add(new Theme(th.NEWREC_TOPPANEL_SUM_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK,Color.GREEN));
		ar.add(new Theme(th.NEWREC_EDITTEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.NEWREC_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BUTTON_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON, R.drawable.round_button_green));
		ar.add(new Theme(th.PREF_ACTIVITY_BACK,Color.BLACK));
		ar.add(new Theme(th.PREF_ACTIVITY_MAIN_TEXT,Color.WHITE));
		ar.add(new Theme(th.PREF_ACTIVITY_DESC_TEXT,Color.GRAY));
		ar.add(new Theme(th.ABOUT_BACK,Color.GREEN));
		ar.add(new Theme(th.ABOUT_TEXT_BACK,Color.WHITE));
		ar.add(new Theme(th.ABOUT_TEXT,Color.BLACK));
		ar.add(new Theme(th.ABOUT_BACK,Color.GREEN));
		ar.add(new Theme(th.ABOUT_TEXT_DESC,Color.GREEN));
		ar.add(new Theme(th.ABOUT_VERSION,Color.RED));
		ar.add(new Theme(th.ABOUT_BUTTON_TEXT,Color.BLACK));
		return ar;
	}
	protected static ArrayList<Theme> getSkinDarkGreen() 
	{
		ArrayList<Theme> ar = new ArrayList<Theme>();
		ar.add(new Theme(MAIN_TOP_PANEL_BACK,0xff006400));
		ar.add(new Theme(th.MAIN_BUTTON_TEXT_TOP_PANEL,Color.WHITE));
		ar.add(new Theme(th.MAIN_LISTVIEW_NOTSELECT,0xff228B22));
		ar.add(new Theme(th.MAIN_LISTVIEW_SELECT,0xff006400));
		ar.add(new Theme(th.ALERT_WINDOW_BACK, 0xff006400));
		ar.add(new Theme(th.ALERT_WINDOW_BUTTON_TEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_EDITTEXT, Color.BLACK));
		ar.add(new Theme(th.ALERT_WINDOW_TEXT_LIST_BUTTON, Color.WHITE));
		ar.add(new Theme(th.EDITCURRENCY_BACK,0xff228B22));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_BACK,Color.GREEN));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.EDITCURRENCY_EDITTEXT_TEXTVIEW,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOPPANEL,0xff006400));
		ar.add(new Theme(th.NEWREC_TOPPANEL_SUM_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK,0xff006400));
		ar.add(new Theme(th.NEWREC_EDITTEXT_BACK,0xff228B22));
		ar.add(new Theme(th.NEWREC_EDITTEXT_TEXT,Color.BLACK));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BUTTON_TEXT,Color.WHITE));
		ar.add(new Theme(th.NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON, R.drawable.round_button_green));
		ar.add(new Theme(th.PREF_ACTIVITY_BACK,Color.BLACK));
		ar.add(new Theme(th.PREF_ACTIVITY_MAIN_TEXT,Color.WHITE));
		ar.add(new Theme(th.PREF_ACTIVITY_DESC_TEXT,Color.GRAY));
		ar.add(new Theme(th.ABOUT_BACK,0xff228B22));
		ar.add(new Theme(th.ABOUT_TEXT_BACK,0xff228B22));
		ar.add(new Theme(th.ABOUT_TEXT,Color.BLACK));
		ar.add(new Theme(th.ABOUT_BACK,Color.GREEN));
		ar.add(new Theme(th.ABOUT_TEXT_DESC,0xff228B22));
		ar.add(new Theme(th.ABOUT_VERSION,Color.RED));
		ar.add(new Theme(th.ABOUT_BUTTON_TEXT,Color.BLACK));
		return ar;
	}
	public static class Theme
	{		
		int key = -1;
		long val = -1;
		
		public Theme(int keys,int vals)
		{
			key = keys;
			val = vals;
		}
//		// пока не сделал, просто пример как делается градиент
//		static Drawable getGradientDrawable(int key)
//		{
//			 GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, 
//				      new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA });
//				  drawable.setShape(GradientDrawable.RECTANGLE);
//				  drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//				  drawable.setCornerRadius(40);
//				  drawable.setStroke(10, Color.BLACK, 20, 5);
//				  //imageView.setImageDrawable(drawable);
//			
//			return drawable;
//		}
	}
	public static int getValue(int key){
		ArrayList<Theme> th = null;
		switch (var.skin_app)
		{
		case 0:th =  getSkinLight();break;
		case 1:th =  getSkinBlack();break;
		case 2:th =  getSkinGray();break;
		case 3:th =  getSkinOrange();break;
		case 4:th =  getSkinWhiteBlue();break;
		case 5:th =  getSkinLightGreen();break;
		case 6:th =  getSkinDarkGreen();break;
		default:th =  getSkinLight();break;
		}
		Theme tth = null;
		for (int i=0;i<th.size();i++)
		{
			tth = th.get(i);
			if (key == tth.key)
				return (int)tth.val;
		}
		return Color.BLACK; // default
	}
    // установка темы для активности
    public static void setTheme(Activity act)
    {
    	// вставлять все изменения темы в активностях до super.onCreate
    	int theme = R.style.AppBaseTheme;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
	        switch (var.skin_app)
	        {
	        case 1:
	    		theme = android.R.style.Theme_Holo;
	    		break;
	        case 2:
	    		theme = R.style.ThemeGray;
	    		break;
	        case 3:
	    		theme = R.style.ThemeOrange;
	    		break;
	        case 6:
	    		theme = R.style.ThemeDarkGreen;
	    		break;
//	        case 5:
//	    		theme = R.style.ThemeOrange;
//	    		break;
	        }
		}
		act.setTheme(theme);
    }

}
