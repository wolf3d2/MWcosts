package com.mwcorp.costs;

import com.mwcorp.tools.Pref;

import android.app.Application;
import android.content.res.Configuration;

public class App extends Application {

	@Override
	public void onCreate() {
        try{
            Pref.init(this);
            Pref.readPreference();
        } catch (Throwable e) {}
		
		Configuration config = new Configuration();
		getBaseContext().getResources().updateConfiguration(config, null);
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
		Configuration config = new Configuration();
		getBaseContext().getResources().updateConfiguration(config, null);     
    }	
}