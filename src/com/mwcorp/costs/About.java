package com.mwcorp.costs;

import com.mwcorp.dialog.Dlg;
import com.mwcorp.tools.GlobDialog;
import com.mwcorp.tools.Mail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class About extends Activity 
{
	static About inst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inst = this;
        View v = getLayoutInflater().inflate(R.layout.about, null);
		RelativeLayout rl = (RelativeLayout)v.findViewById(R.id.rl_about);
		rl.setBackgroundColor(th.getValue(th.ABOUT_BACK));
		TextView tv =null;
        try{
            String vers = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            vers = getString(R.string.version)+" "+vers;
            tv =((TextView)v.findViewById(R.id.Ver));
           	tv.setText(vers);
           	tv.setTextColor(th.getValue(th.ABOUT_VERSION));
            
        }
        catch (Throwable e) {}
        tv = (TextView)v.findViewById(R.id.about_autor);
        tv.setBackgroundColor(th.getValue(th.ABOUT_TEXT_BACK));
        tv.setTextColor(th.getValue(th.ABOUT_TEXT));
        setContentView(v);
        Button bt = (Button)v.findViewById(R.id.about_other_app);
        bt.setTextColor(th.getValue(th.ABOUT_BUTTON_TEXT));
        
        bt = (Button)v.findViewById(R.id.about_diary);
        bt.setTextColor(th.getValue(th.ABOUT_BUTTON_TEXT));
        
        bt = (Button)v.findViewById(R.id.about_feedback);
        bt.setTextColor(th.getValue(th.ABOUT_BUTTON_TEXT));
        
        // показ рекламы
        Ads.show(this,1);
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
    public void onClick1(View view) 
    {
    	inst=null;
    	finish();
    }
    public void onClickOtherApp(View view) 
    {
    	st.runOtherApp();
    }
    public void onClickDiary(View view) 
    {
		Dlg.helpDialog(inst, st.getAssetsFile(inst, "_diary_MWcosts.txt"));
    	//st.help(st.getAssetsFile(inst, "_diary_MWcosts.txt"), inst);
    }
    public void onClickFeedback(View view) 
    {
    	Mail.sendFeedback(this);
    }
    @Override
    public void onBackPressed()
    {
    	// не обрабатывает при открытом диалоге листа. Исправить
    	if (GlobDialog.gbshow){
      		GlobDialog.inst.finish();
      		return;
    	}
    		
    	super.onBackPressed();
    }

}