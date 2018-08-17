package com.mwcorp.costs;

import java.util.Date;

import com.mwcorp.costs.var.myCur;
import com.mwcorp.dialog.Dlg;
import com.mwcorp.tools.GlobDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class CurEdit extends Activity 
{
	static CurEdit inst;
	String symb_begin = "";
	String name_begin = "";
	String kurs_begin = "";
	boolean base_begin = false;
	
	EditText symb;
	EditText name;
	EditText kurs;
	CheckBox base;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inst = this;
        View v = getLayoutInflater().inflate(R.layout.cur_edit, null);
        v.setBackgroundColor(th.getValue(th.EDITCURRENCY_BACK));
		TextView tv = null;
    	name = (EditText)v.findViewById(R.id.curedit_name);
    	name.setBackgroundColor(th.getValue(th.EDITCURRENCY_EDITTEXT_BACK));
    	name.setTextColor(th.getValue(th.EDITCURRENCY_EDITTEXT_TEXT));
    	name.setOnKeyListener(et_onKeyListener);
    	tv = (TextView)v.findViewById(R.id.curedit_symb_txt);
    	tv.setTextColor(th.getValue(th.EDITCURRENCY_EDITTEXT_TEXTVIEW));
    	symb = (EditText)v.findViewById(R.id.curedit_symb);
    	symb.setBackgroundColor(th.getValue(th.EDITCURRENCY_EDITTEXT_BACK));
    	symb.setTextColor(th.getValue(th.EDITCURRENCY_EDITTEXT_TEXT));
    	symb.setOnKeyListener(et_onKeyListener);
    	tv = (TextView)v.findViewById(R.id.curedit_name_txt);
    	tv.setTextColor(th.getValue(th.EDITCURRENCY_EDITTEXT_TEXTVIEW));
    	kurs = (EditText)v.findViewById(R.id.curedit_kurs);
    	kurs.setBackgroundColor(th.getValue(th.EDITCURRENCY_EDITTEXT_BACK));
    	kurs.setTextColor(th.getValue(th.EDITCURRENCY_EDITTEXT_TEXT));
    	kurs.setOnKeyListener(et_onKeyListener);
    	tv = (TextView)v.findViewById(R.id.curedit_kurs_txt);
    	tv.setTextColor(th.getValue(th.EDITCURRENCY_EDITTEXT_TEXTVIEW));
    	base = (CheckBox)v.findViewById(R.id.curedit_base);
    	base.setTextColor(th.getValue(th.EDITCURRENCY_EDITTEXT_TEXTVIEW));
        if(android.os.Build.VERSION.SDK_INT <= 17)
        	base.setPadding(60,0,0,0);
		name_begin = "";
		symb_begin = "";
		kurs_begin = "";
		base_begin = false;
        if (var.id_cur > -1){
        	for (myCur c:var.currency) {
        		if (var.id_cur == c.id) {
        			name.setText(c.name);
        			name.setSelection(name.getText().toString().length());
        			symb.setText(c.cur_symb);
        			symb.setSelection(symb.getText().toString().length());
        			kurs.setText(String.valueOf(c.kurs_value));
        			kurs.setSelection(kurs.getText().toString().length());
        			base.setChecked(c.base_cur);
        			name_begin = c.name;
        			symb_begin = c.cur_symb;
        			kurs_begin = String.valueOf(c.kurs_value);
        			base_begin = c.base_cur;
        			
        			break;
        		}
        	}
        }
		setContentView(v);
        // показ рекламы
        Ads.show(this,3);
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
//    	inst = null;
        super.onDestroy();
    }
    public void onClick_save(View view) 
    {
    	preSave();
   	}
    @Override
    public void onBackPressed()
    {
//      	if (GlobDialog.gbshow){
//    		GlobDialog.inst.finish();
//    		return;
//    	}
    	if (isChanged()) {
    		GlobDialog gd = new GlobDialog(inst);
    		gd.set(R.string.toast_msg3, R.string.yes, R.string.no, R.string.cancel);
            gd.setObserver(new st.UniObserver()
            {
                @Override
                public int OnObserver(Object param1, Object param2)
                {
                    if(((Integer)param1).intValue()==AlertDialog.BUTTON_POSITIVE)
                    {
                    	preSave();
                		st.CurLoad();
                    }
                    else if(((Integer)param1).intValue()==AlertDialog.BUTTON_NEGATIVE)
                    {
                    	finish();
                    }
                    else if(((Integer)param1).intValue()==AlertDialog.BUTTON_NEUTRAL)
                    {
                    }
                    return 0;
                }
            });
            gd.showAlert();
    	} else {
    		st.CurLoad();
    		super.onBackPressed();
    	}
    }
    public boolean isChanged()
    {
    	boolean ret = false;
        if (name.getText().toString().compareToIgnoreCase(name_begin) != 0)
        	ret =true;
        else if (symb.getText().toString().compareToIgnoreCase(symb_begin) != 0)
        	ret =true;
        else if (kurs.getText().toString().compareToIgnoreCase(kurs_begin) != 0)
        	ret =true;
        else if (base.isChecked() != base_begin)
        	ret =true;
    	return ret;
    }
    public void preSave()
    {
        try {	
			myCur c = new myCur(st.CurRnd(),"","",new Date().getTime(),0,false);
	    	c.cur_symb = symb.getText().toString();
	    	c.name = name.getText().toString().trim();
	    	c.kurs_value = Float.valueOf(kurs.getText().toString().trim());
	    	c.base_cur = base.isChecked();
	    	if (c.base_cur){
	    		for (myCur cc:var.currency){
	    			cc.base_cur = false;
	    		}
	    		c.kurs_value = 1;
	    	}
			if (var.id_cur > 0){
				for (int i=0;i<var.currency.size();i++){
					if (var.id_cur == var.currency.get(i).id){
						var.currency.set(i,c);
						break;
					}
				}
				var.id_cur = -1;
			} else
				var.currency.add(c);

			st.CurSave();
        	inst = null;
        	finish();
        	Cur.inst.view();
    	} catch (Throwable e) {
    		st.logEx(e);
    	}
    }
    View.OnKeyListener et_onKeyListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) 
		{
    	    if(event.getAction() == KeyEvent.ACTION_DOWN && 
    	    	(keyCode == KeyEvent.KEYCODE_ENTER))
       		{
    	    	switch (v.getId())
    	    	{
    	    	case R.id.curedit_symb:v.setNextFocusDownId(R.id.curedit_name);return true;
    	    	case R.id.curedit_name:v.setNextFocusDownId(R.id.curedit_kurs);return true;
    	    	case R.id.curedit_kurs:v.setNextFocusDownId(R.id.curedit_symb);return true;
    	    	}
       		}
       		return false;
		}
	};

}