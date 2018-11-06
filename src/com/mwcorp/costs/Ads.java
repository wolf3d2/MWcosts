package com.mwcorp.costs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
// класс управления рекламой
final class Ads    
{
	// всего на текущий момент переменных адмоба = 
	// 8
	
	/** id девайса эмулятора */
	public static String debug_id_device = "";
	public static AdView m_ads= null;
	public static int count_failed_load = 0;
	public static LinearLayout llad = null;

	public static void createAds(Context c){
		m_ads = new AdView(c);
		m_ads.setAdSize(AdSize.BANNER);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
				);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		m_ads.setLayoutParams(lp);
		m_ads.setAdUnitId("ca-app-pub-6058046135647426/6800658997");
		m_ads.setAdListener(new AdListener(){
			@Override 
			public void onAdLoaded() 
			{ 
				count_failed_load = 0;
				if (llad!=null)
					llad.setVisibility(View.VISIBLE);
				m_ads.setVisibility(View.VISIBLE);
//		        st.toast("load ads");
			} 
			@Override 
			public void onAdClosed() 
			{
				m_ads.resume();
			}
			@Override 
			public void onAdLeftApplication () 
			{
		        m_ads.pause();
			}
            @Override
            public void onAdFailedToLoad(int errorCode) 
            {
            	
//          	st.toast("failed ads ("+count_failed_load+")");
            	if (count_failed_load>5)
            		return;
		        loadAds(m_ads);
		        count_failed_load++;
            }       
        });

		m_ads.setVisibility(View.GONE);
       	loadAds(m_ads);
        }
	public static void show(Context c, final int idAct){
		if (llad!=null){
			llad.removeView(m_ads);
			llad=null;
//			llad.removeAllViews();
		}
		debug_id_device ="";
		if (isDebugEmulator()) {
			String android_id = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
			debug_id_device = getmd5(android_id).toUpperCase(); 
		}
		llad = (LinearLayout)getLinearLayout(c,idAct);
		if (m_ads == null){
			createAds(c);
			count_failed_load = 0;
		}
		if (llad!=null)
		llad.addView(m_ads);
//		llad.setVisibility(View.GONE);

	}
	public static void loadAds(AdView ads){
//        if (Build.VERSION.SDK_INT>19
//            	&&st.isDebugEmulator()
//            	)
//            	return;
		AdRequest adRequest;
		if (!debug_id_device.isEmpty()) {
			adRequest = new AdRequest.Builder()
					.addTestDevice(debug_id_device)
	                .build();
		} else {
			adRequest = new AdRequest.Builder()
	                .build();
		}
		try {
	        ads.loadAd(adRequest);
		} catch (Throwable e) {
		}
	}
	public static LinearLayout getLinearLayout(Context c, int numAds){
		LinearLayout ll= null;
		switch (numAds)
		{
		case 1:
			ll = (LinearLayout) ((Activity) c).findViewById(R.id.llad_about);
			break;
		case 2:
			ll = (LinearLayout) ((Activity) c).findViewById(R.id.llad_main);
			break;
		case 3:
			ll = (LinearLayout) ((Activity) c).findViewById(R.id.llad_ce);
			break;
		case 4:
			ll = (LinearLayout) ((Activity) c).findViewById(R.id.llad_c);
			break;
		case 5:
			ll = (LinearLayout) ((Activity) c).findViewById(R.id.llad_pref_toolbar);
			break;
		case 6:
			ll = (LinearLayout) ((Activity) c).findViewById(R.id.llad_pref);
			break;
		}
		return ll; 
	}
	public static void destroy(){
//        if (m_ads != null)
//        	m_ads.destroy();
//        if (llad!=null)
//        	llad.removeAllViews();
	}
	public static void pause(){
//        if (m_ads != null)
//        	m_ads.pause();

//        	loadAds(m_ads);
	}
	public static void resume(){
        if (m_ads != null) {
        	m_ads.resume();
//        	m_ads.setVisibility(View.GONE);
        	loadAds(m_ads);
        }
	}
	public static final String getmd5(final String s) {
		try {
			// Create MD5 Hash 
			MessageDigest digest = java.security.MessageDigest .getInstance("MD5"); 
			digest.update(s.getBytes()); 
			byte messageDigest[] = digest.digest(); // Create Hex String 
			StringBuffer hexString = new StringBuffer(); 
			for (int i = 0; i < messageDigest.length; i++) 
			{ 
				String h = Integer.toHexString(0xFF & messageDigest[i]); 
				while (h.length() < 2) 
					h = "0" + h; 
				hexString.append(h); 
				} 
			return hexString.toString(); 
			} catch (NoSuchAlgorithmException e) {
			}
		return ""; 
		}
    public static boolean isDebugEmulator()
    {
    	// запущено на genymotion
        if (Build.MANUFACTURER.toLowerCase().contains("genymo"))
           	return true;
    	return false;
    }
}