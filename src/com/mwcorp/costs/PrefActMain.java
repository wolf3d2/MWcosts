package com.mwcorp.costs;

import com.mwcorp.costs.R;
import com.mwcorp.costs.R.array;
import com.mwcorp.costs.R.layout;
import com.mwcorp.costs.R.string;
import com.mwcorp.costs.R.xml;
import com.mwcorp.tools.Pref;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

@SuppressLint("NewApi")
public class PrefActMain extends PreferenceActivity implements OnSharedPreferenceChangeListener 
{
	String prefkey = "";
	static PrefActMain inst= null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
		inst = this;
		th.setTheme(inst);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_view);
//		inst = this;
        
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences pref = Pref.get();
        pref.registerOnSharedPreferenceChangeListener(this);
        
        setValue(Pref.PR_NEWREC_EDIT_SIZE_TEXT, R.string.cat_newrec_size_text_desc, Pref.getString(Pref.PR_NEWREC_EDIT_SIZE_TEXT,"16"));
        setSummary(Pref.PR_SKIN_APP, R.string.skin_name_desc, strVal(getResources().getStringArray(R.array.skin)[var.skin_app]));
//        setSummary(prefkey, R.string.skin_name_desc, strVal(getResources().getStringArray(R.array.skin)[var.skin_app]));
    	Ads.show(this, 6);
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
	@Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) 
    {
        prefkey = preference.getKey();
        Context c = this;
        
        if ("newrec_set_tb".equals(prefkey)){
        	st.runAct(PrefActSetToolbar.class, this);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
//    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(Pref.PR_NEWREC_EDIT_SIZE_TEXT.equals(prefkey))
        {
        	setValue(prefkey,R.string.cat_newrec_size_text_desc, "16");
        }
        else if(Pref.PR_SKIN_APP.equals(prefkey))
        {
            int index = Integer.decode(sharedPreferences.getString(prefkey, "0"));
            setSummary(prefkey, R.string.skin_name_desc, strVal(getResources().getStringArray(R.array.skin)[index]));

        	setValue(prefkey,R.string.skin_name_desc, "0");
        	inst.recreate();
        }
        Pref.readPreference();
    }
 // вывод текущего значения параметра НЕ МАССИВА, в виде [value]\ntext
    void setValue(String key, int id, String defValue)
    {
        try{
            setSummary(key, id,strVal(Pref.get().getString(key, defValue)));
        }
        catch (Throwable e) {
        }
    }
    void setSummary(String prefName,int summaryStr,String value)
    {
		Preference p = getPreferenceScreen().findPreference(prefName);
        if(p!=null)
        {
            String summary;
            if(summaryStr==0)
            {
                summary = value;
            }
            else
            {
            	if (value!=null&&value.length() > 0){
            		summary = value+"\n"+getString(summaryStr);
            	} else {
            		summary = value+getString(summaryStr);
            	}
            } 
            p.setSummary(summary);
        }
    }
    final String strVal(String src)
    {
    	if (src.length() < 1)
    		return src;
        return "[ "+src+" ]";
    }
	@Override
    public void onBackPressed()
    {
		Pref.init(this);
    	Pref.readPreference();
    	finish();
		super.onBackPressed();
    	if (Newrecord.inst !=null)
    		Newrecord.inst.recreate();
    	if (MainActivity.inst !=null)
    		MainActivity.inst.recreate();
    }
}