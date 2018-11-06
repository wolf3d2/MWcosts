package com.mwcorp.costs;

import java.util.ArrayList;

import com.mwcorp.costs.R;
import com.mwcorp.costs.R.array;
import com.mwcorp.costs.R.id;
import com.mwcorp.costs.R.layout;
import com.mwcorp.costs.R.string;
import com.mwcorp.costs.var.ArrayToolbar;
import com.mwcorp.dialog.Dlg;
import com.mwcorp.tools.Pref;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PrefActSetToolbar extends Activity 
{
	static PrefActSetToolbar inst = null;
	public static ArrayAdapter<String> ar_adapt_tb = null;
	
	CheckBox show_cb;
	CheckBox bigtb_cb;
	LinearLayout lllist;
	boolean fl_changed = false;
	TextView tvtemp;
	
	TextWatcher tw = new TextWatcher()
	{
        @Override
        public void afterTextChanged(Editable s) 
        {
//        	fl_changed = true;
        }
         
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) 
        {
//        	fl_changed = true;
        }
     
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) 
        {
        	fl_changed = true;
        }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		inst = this;
		th.setTheme(inst);
		super.onCreate(savedInstanceState);
		Pref.init(this);
		setContentView(R.layout.prefsettoolbar);
		tvtemp = (TextView) findViewById(R.id.toolbar_linecnt_port_desc);
		tvtemp.setText("["+var.tb_line_port+"]");
		tvtemp = (TextView) findViewById(R.id.toolbar_linecnt_land_desc);
		tvtemp.setText("["+var.tb_line_land+"]");
		lllist = (LinearLayout) findViewById(R.id.toolbar_list1);
		bigtb_cb = (CheckBox) findViewById(R.id.toolbar_big_cb);
		bigtb_cb.setChecked(var.fl_newrec_big_toolbar);
		bigtb_cb = st.getTextPadding(bigtb_cb);
		show_cb = (CheckBox) findViewById(R.id.toolbar_visible_cb);
		show_cb = st.getTextPadding(show_cb);
		show_cb.setChecked(var.fl_newrec_show_toolbar);
		llupdate();
        // показ рекламы
        Ads.show(this,5);
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
	@SuppressLint("NewApi")
	@Override
    public void onBackPressed()
    {
		String out = var.STR_NULL;
		for (ArrayToolbar ar: var.ar_tb){
			out+=""+ar.id+","+ar.text+";";
		}
		Pref.get().edit().putBoolean(Pref.PR_NEWREC_SHOW_TOOLBAR, show_cb.isChecked()).commit();
		if (out.length()==0){
			out = "Empty";
			Pref.get().edit().putBoolean(Pref.PR_NEWREC_SHOW_TOOLBAR, false).commit();
			if (var.ar_tb.size()>0)
				var.ar_tb.clear();
			llupdate();
//			if (lllist.getChildCount()> 0)
//				lllist.removeAllViews();
		}
		Pref.init(this);

        Pref.get().edit().putString(Pref.PR_TOOLBAR_BUTTON, out).commit();
        Pref.get().edit().putBoolean(Pref.PR_NEWREC_BIG_TOOLBAR, bigtb_cb.isChecked()).commit();
        Pref.get().edit().putInt(Pref.PR_TOOLBAR_LINE_COUNT_PORT, var.tb_line_port).commit();
        Pref.get().edit().putInt(Pref.PR_TOOLBAR_LINE_COUNT_LAND, var.tb_line_land).commit();
    	Pref.readPreference();
    	finish();
		super.onBackPressed();
   		fl_changed = false;
   		if (Newrecord.inst!=null) {
   			Newrecord.inst.recreate();
   		}
   		inst = null;
    }
    public void adapter()
    {
        ar_adapt_tb = new ArrayAdapter<String>(this, 
        		R.layout.dlg_list,
                getResources().getStringArray(R.array.rec_tb)
                );
        
        Dlg.CustomMenu(this, ar_adapt_tb, getString( R.string.selection), new st.UniObserver()
        {
            @Override
            public int OnObserver(Object param1, Object param2)
            {
                int pos = ((Integer)param1).intValue();
                if(pos > 0)
                {
                	if (ar_adapt_tb == null)
                		return 0;
                    String txt = ar_adapt_tb.getItem(pos);
                    txt = txt.substring(txt.indexOf('.')+1,txt.length());
                    ArrayToolbar ar = new ArrayToolbar();
                    ar.id = 500+pos;
                    ar.text = txt;
                    var.ar_tb.add(ar);
                    llupdate();
                    if (var.ar_tb.size() > 0)
                    	show_cb.setChecked(true);
                }
                return 0;
            }
        });
    }
    public void onClick(View view) 
    {
        switch (view.getId())
        {
        case R.id.toolbar_add:
        	adapter();
            return;
        case R.id.toolbar_linecnt_port:
        case R.id.toolbar_linecnt_port_desc:
           	ArrayAdapter<String> ar = new ArrayAdapter<String>(this, 
            		R.layout.dlg_list,
                    new String[] {"1","2","3","4"}
                    );
            
            Dlg.CustomMenu(this, ar, getString( R.string.toolbar_linecnt_port), new st.UniObserver()
            {
                @Override
                public int OnObserver(Object param1, Object param2)
                {
                    int pos = (((Integer)param1).intValue())+1;
                	TextView tvp =(TextView)findViewById(R.id.toolbar_linecnt_port_desc);
                    tvp.setText("["+pos+"]");
                    var.tb_line_port = pos;
                    if (pos > 1)
                    	st.toastLong(inst.getString(R.string.toast_msg7)+var.STR_SPACE+pos);
                    return 0;
                }
            });
            return;
        case R.id.toolbar_linecnt_land:
        case R.id.toolbar_linecnt_land_desc:
           	ArrayAdapter<String> ar1 = new ArrayAdapter<String>(this, 
            		R.layout.dlg_list,
                    new String[] {"1","2"}
                    );
            
            Dlg.CustomMenu(this, ar1, getString( R.string.toolbar_linecnt_land), new st.UniObserver()
            {
                @Override
                public int OnObserver(Object param1, Object param2)
                {
                    int pos = (((Integer)param1).intValue())+1;
                    TextView tv =(TextView)findViewById(R.id.toolbar_linecnt_land_desc);
                    tv.setText("["+pos+"]");
                    var.tb_line_land = pos;
                    if (pos > 1)
                    	st.toastLong(inst.getString(R.string.toast_msg7)+var.STR_SPACE+pos);
                    return 0;
                }
            });
            return;
        }
    }
    public Button createBtn(int id, String str, 
    		boolean left, boolean right, int id_leftof
    		) 
    {
    	Button btn = new Button(this);
    	btn.setText(str);
    	btn.setOnClickListener(m_clkListener);
    	RelativeLayout.LayoutParams lp = null;
    	if (right) {
            lp = new RelativeLayout.LayoutParams(
            		RelativeLayout.LayoutParams.WRAP_CONTENT,
            		RelativeLayout.LayoutParams.WRAP_CONTENT)
            		;
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	}
    	else if (left){
            lp = new RelativeLayout.LayoutParams(
            		RelativeLayout.LayoutParams.WRAP_CONTENT,
            		RelativeLayout.LayoutParams.WRAP_CONTENT)
            		;
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.addRule(RelativeLayout.LEFT_OF, id_leftof);
    	}
    	else if (!right&&!left){
            lp = new RelativeLayout.LayoutParams(
            		RelativeLayout.LayoutParams.WRAP_CONTENT,
            		RelativeLayout.LayoutParams.WRAP_CONTENT)
            		;
            lp.addRule(RelativeLayout.LEFT_OF, id_leftof);
    	}
    		
    		
    	btn.setLayoutParams(lp);
    	btn.setId(id);
    	return btn;
    }
    public void llupdate() 
    {
    	if (lllist != null)
    		lllist.removeAllViews();
        
        int pos = 0;
    	for (int i =0;i<var.ar_tb.size();i++){
            RelativeLayout rl = new RelativeLayout(this);
// добавляем строку команды
// под команды зарезервировано 300 кодов            
            // создаём текст команды
    		if (i==0){
    			// первая строка
    			if (var.ar_tb.size()!=1){
    				rl.addView(createBtn(var.ar_tb.get(i).id+300,"∇", false, true,-1));
            		rl.addView(createBtn(var.ar_tb.get(i).id+900,"✖", false,false,var.ar_tb.get(i).id+300));
    			} else {
            		rl.addView(createBtn(var.ar_tb.get(i).id+900,"✖", false,true,var.ar_tb.get(i).id+300));
    				
    			}
        		// текст
        		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                		RelativeLayout.LayoutParams.WRAP_CONTENT,
                		RelativeLayout.LayoutParams.WRAP_CONTENT)
                		;
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.LEFT_OF, var.ar_tb.get(i).id+900);
        		
                TextView tv = new TextView(this);
        		tv.setText(var.ar_tb.get(i).text);
        		tv.setTextSize(17);
        		tv.setGravity(Gravity.LEFT|Gravity.CENTER_HORIZONTAL);
        		tv.setLayoutParams(lp);
        		rl.addView(tv);
    		}
    		else if (i>0&&pos > 0&&pos < var.ar_tb.size()-1){
    			// следующие строки
    			rl.addView(createBtn(var.ar_tb.get(i).id+300,"∇", false, true,-1));
    			// перед ней
    			rl.addView(createBtn(var.ar_tb.get(i).id+600,"∆", false, false,var.ar_tb.get(i).id+300));
    			// удалить
        		rl.addView(createBtn(var.ar_tb.get(i).id+900,"✖", false,false,var.ar_tb.get(i).id+600));
        		// текст
        		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                		RelativeLayout.LayoutParams.WRAP_CONTENT,
                		RelativeLayout.LayoutParams.WRAP_CONTENT)
                		;
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.LEFT_OF, var.ar_tb.get(i).id+900);
        		
                TextView tv = new TextView(this);
        		tv.setText(var.ar_tb.get(i).text);
        		tv.setLayoutParams(lp);
        		tv.setTextSize(17);
        		tv.setGravity(Gravity.LEFT|Gravity.CENTER_HORIZONTAL);
        		rl.addView(tv);
    		}
    		else if (i>0&&pos == var.ar_tb.size()-1){
    			// последняя строка
    			rl.addView(createBtn(var.ar_tb.get(i).id+600,"∆", false, true,-1));
    			// удалить
        		rl.addView(createBtn(var.ar_tb.get(i).id+900,"✖", false,false,var.ar_tb.get(i).id+600));
        		// текст
        		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                		RelativeLayout.LayoutParams.WRAP_CONTENT,
                		RelativeLayout.LayoutParams.WRAP_CONTENT)
                		;
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.LEFT_OF, var.ar_tb.get(i).id+900);
        		
                TextView tv = new TextView(this);
        		tv.setText(var.ar_tb.get(i).text);
        		tv.setTextSize(17);
        		tv.setGravity(Gravity.LEFT|Gravity.CENTER_HORIZONTAL);
        		tv.setLayoutParams(lp);
        		rl.addView(tv);
    		}

        	if (rl.getChildCount()> 0)
    			lllist.addView(rl);
    		pos++;
    	}
    }
    View.OnClickListener m_clkListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
        	if (var.ar_tb.size()==0)
        		return;
        	ArrayList<ArrayToolbar> ar = new ArrayList<ArrayToolbar>();
        	int id = v.getId();
        	// вниз
        	if (id-500>300&&id-500<600){
        		if (var.ar_tb.size()==1)
        			return;
        		for (int i=0;i<var.ar_tb.size();i++){
        			if (id != var.ar_tb.get(i).id+300)
        				ar.add(var.ar_tb.get(i));
        			else {
        				ar.add(var.ar_tb.get(i+1));
        				ar.add(var.ar_tb.get(i));
        				var.ar_tb.remove(i+1);
        			}
        		}
        	}
        	// вверх
        	else if (id-500>600&&id-500<900){
        		boolean fl = false;
        		for (int i=0;i<var.ar_tb.size();i++){
        			if (id != var.ar_tb.get(i).id+600)
        				ar.add(var.ar_tb.get(i));
        			else {
        				if (fl==false){
            				ArrayToolbar rec = var.ar_tb.get(i);
            				if (ar.size()>0)
            					ar.remove(ar.size()-1);
            				ar.add(rec);
            				ar.add(var.ar_tb.get(i-1));
            				fl=true;
        				}
        			}
        		}
        	}
        	// удалить
        	else if (id-500>900&&id-500<1200){
        		for (int i=0;i<var.ar_tb.size();i++){
        			if (id-900 == var.ar_tb.get(i).id)
        				continue;
        			ar.add(var.ar_tb.get(i));
        		}
        	}
        	var.ar_tb.clear();
        	for (int i=0;i<ar.size();i++){
        		var.ar_tb.add(ar.get(i));
        	}
        	llupdate();
        }
    };

}