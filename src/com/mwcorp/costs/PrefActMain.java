package com.mwcorp.costs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.mwcorp.costs.R;
import com.mwcorp.dialog.Dlg;
import com.mwcorp.tools.Pref;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PrefActMain extends PreferenceActivity implements OnSharedPreferenceChangeListener 
{
	String prefkey = "";
	static PrefActMain inst= null;
	public static String FILENAME_SETTING = "settings_backup.xml";
	
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
        setValue(Pref.PR_READ_SIZE_TEXT_LEN, R.string.cat_newrec_size_big_file_desc, st.STR_NULL+Pref.getString(Pref.PR_READ_SIZE_TEXT_LEN,st.STR_NULL+var.DEF_OPEN_SIZE_TEXT));
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
        else if ("default_setting".equals(prefkey)) {
        	
        }
        else if ("default_setting".equals(prefkey)){
        	Dlg.yesNoDialog(inst, inst.getString(R.string.def_setting), new st.UniObserver() {
				
				@Override
				public int OnObserver(Object param1, Object param2) {
                    if(((Integer)param1).intValue()==AlertDialog.BUTTON_POSITIVE)
                    {
                    	Pref.get().edit().clear().commit();
                    	System.exit(0);
                    	st.toast(R.string.ok);
                    }
					
					return 0;
				}
			});
        }
        else if ("save_setting".equals(prefkey)){
        	backup(inst, true);
        }
        else if ("load_setting".equals(prefkey)){
        	backup(inst, false);
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
        else if(Pref.PR_READ_SIZE_TEXT_LEN.equals(prefkey))
        {
        	setValue(prefkey,R.string.cat_newrec_size_big_file_desc, st.STR_NULL+var.DEF_OPEN_SIZE_TEXT);
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
    public static void backup(final Context cont, final boolean bSave)
    {
        Dlg.yesNoDialog(cont, cont.getString(bSave?R.string.save_setting:R.string.load_setting)+" ?", new st.UniObserver()
        {
            @Override
            public int OnObserver(Object param1, Object param2)
            {
                if(((Integer)param1).intValue()==AlertDialog.BUTTON_POSITIVE)
                {
                    int ret = backupPref(cont, bSave);
                    try{
                    if(ret==0)
                        Toast.makeText(cont, st.STR_ERROR, 700).show();
                    else if(ret==1)
                    	if(!bSave)
                    	{
                    		st.exitApp();
                    		//Toast.makeText(getApplicationContext(), R.string.reboot, Toast.LENGTH_LONG).show();                        
                    	} else{
                          Toast.makeText(cont, R.string.ok, 700).show();
                    	}
                   }
                   catch(Throwable e)
                    {
                    	st.toast("error save/load setting");
                    }
                }
                return 0;
            }
        });
    }
    static int backupPref(Context cont, boolean bSave)
    {
        try{
            String appname = cont.getPackageName();
            String path = getBackupPathAndName();
            String prefDir = cont.getFilesDir().getParent()+"/shared_prefs/";
            File ar[] = st.getFilesByExt(new File(prefDir), "xml");
            if(ar==null||ar.length==0)
                return 0;
            File f = new File(path);
            FileInputStream in = null;
            FileOutputStream out = null;
            if(bSave)
            {
            	for (File ff:ar) {
            		if (ff.getName().contains(appname)) {
                        in = new FileInputStream(ff.getAbsolutePath());
            			break;
            		}
            	}
            	if (in == null)
            		return 0;
                f.delete();
                out = new FileOutputStream(f);
            }
            else
            {
                if(!f.exists())
                {
                    Toast.makeText(cont, "File not exist: "+path, 700).show();
                    return -1;
                }
                for (int i=0; i< ar.length;i++) {
                	if (ar[i].toString().indexOf(appname+"_preferences.xml")>=0) {
                        out = new FileOutputStream(ar[i]);
                	}
                }
                if (out==null)
                	return -1;
                in = new FileInputStream(f);
            }
            
            byte b[] = new byte[in.available()];
            in.read(b);
            out.write(b);
            out.flush();
            in.close();
            out.close();
            return 1;
        }
        catch (Throwable e) {
        }
        return 0;
    }
    final static String getBackupPathAndName()
    {
        return st.getPathAppFolder()+"/"+FILENAME_SETTING;
    }

}