package com.mwcorp.tools;

import java.util.ArrayList;

import com.mwcorp.costs.R;
import com.mwcorp.costs.st;
import com.mwcorp.costs.th;
import com.mwcorp.perm.Perm;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
// ни в коем случае не использовать в конструкторе st.c()!!!
// Вместо неё, юзать inst.

//если в set(int) указать вместо параметров R.id.* ноли вместо кнопок, 
//то эти кнопки не выводятся

//если используется edittext,
//то его значение возвращается в кнопке ok, в переменной ret_edittext_text
//Если list, то в переменной position

//для закрытия окна диалога вставить в onBackPressed вызывающей
//активности проверку:
//
//if (GlobDialog.gbshow){
//	GlobDialog.inst.finish();
//	return;
//}

public class GlobDialog  extends Activity 
{
    public static GlobDialog inst;
	public static boolean gbshow = false;
	private Paint p_color	= new Paint(Paint.ANTI_ALIAS_FLAG);
	public static String ret_edittext_text = "";
	public static int position = -1;
	public static int[] list;
	public static ArrayList<String> slist;
    String m_text;
    int m_text_gravity = Gravity.CENTER;
    String m_ok;
    String m_no;
    String m_cancel;
    Context m_c;
    public static EditText et;
    static View m_view;
    st.UniObserver m_obs;
    public static final int NO_FINISH = 1;
    static WindowManager wm = null;
    public GlobDialog(Context c)
    {
        m_c = c;
        inst = this;
    }
    View.OnClickListener m_clkListenerGlobal = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(m_obs!=null&&m_obs.OnObserver(Integer.valueOf(v.getId()), this)!=NO_FINISH)
                finish();
        }
    };
    View.OnClickListener m_clkListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(m_obs!=null&&m_obs.OnObserver(Integer.valueOf(v.getId()), this)!=NO_FINISH)
                finish();
        }
    };
    View.OnClickListener m_clkListenerList = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
        	position = v.getId();
            if(m_obs!=null&&m_obs.OnObserver(Integer.valueOf(v.getId()), this)!=NO_FINISH)
            	finish();
        }
    };
    View.OnClickListener m_clkListenerEdit = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
        	ret_edittext_text = null;
        	if (et!=null)
        		ret_edittext_text = et.getText().toString();
            if(m_obs!=null&&m_obs.OnObserver(Integer.valueOf(v.getId()), this)!=NO_FINISH)
                finish();
        }
    };
    View.OnKeyListener m_keyListener = new View.OnKeyListener()
    {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
    	    if(keyCode == KeyEvent.KEYCODE_BACK)  
    	    {
    	    	finish();
        	    return true;  
    	    }  
            return false;
        }
    };
    public View createView()
    {
        LinearLayout ll = new LinearLayout(m_c);
//        ll.setBackgroundResource(android.R.drawable.dialog_frame);
        ll.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
        ll.setOnKeyListener(m_keyListener);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);
        ll.setPadding(20, 20, 20, 20);
        if(m_text!=null)
        {
            TextView tv = new TextView(m_c);
            tv.setPadding(20, 20, 20, 20);
            tv.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
            tv.setTextColor(th.getValue(th.ALERT_WINDOW_TEXT_LIST_BUTTON));
            tv.setText(m_text);
            tv.setMinWidth(200);
            if (m_text_gravity == 0)
            	tv.setGravity(Gravity.CENTER);
            else
            	tv.setGravity(m_text_gravity);
            m_text_gravity = 0;
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setMaxLines(15);
            ll.addView(tv);
        }
        LinearLayout.LayoutParams lp1 
        = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(20, 20, 20, 20);
        LinearLayout butLayout = new LinearLayout(m_c);
        butLayout.setLayoutParams(lp1);
        butLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        butLayout.setOrientation(LinearLayout.HORIZONTAL);
        if(m_ok!=null)
        {
            butLayout.addView(makeButton(m_ok, AlertDialog.BUTTON_POSITIVE,1));
        }
        if(m_no!=null)
        {
            butLayout.addView(makeButton(m_no, AlertDialog.BUTTON_NEGATIVE,1));
        }
        if(m_cancel!=null)
        {
            butLayout.addView(makeButton(m_cancel,AlertDialog.BUTTON_NEUTRAL,1));
        }
        ll.addView(getHideEditText());
        ll.addView(butLayout);
        return ll;
    }
    public View createViewEdit(String txt)
    {
        LinearLayout ll = new LinearLayout(m_c);
//      ll.setBackgroundResource(android.R.drawable.dialog_frame);
        ll.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
        ll.setOnKeyListener(m_keyListener);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);
        ll.setPadding(20, 20, 20, 20);
        if(m_text!=null)
        {
            TextView tv = new TextView(m_c);
            tv.setTextColor(Color.WHITE);
            tv.setText(m_text);
            tv.setMinWidth(200);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
            ll.addView(tv);
        }
        LinearLayout.LayoutParams lpet = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        lpet.gravity = Gravity.LEFT;
        lpet.leftMargin = 10;
        lpet.rightMargin = 10;
        et = new EditText(m_c);
        et.setBackgroundColor(Color.LTGRAY);
        et.setMinLines(1);
        et.setMaxLines(2);
        et.setPadding(5, 0, 5, 0);
        et.setId(AlertDialog.BUTTON_POSITIVE);
        et.setOnKeyListener(et_onKeyListener);
        et.setLayoutParams(lpet);
        et.setTextColor(th.getValue(th.ALERT_EDITTEXT));
        st.showKbd(et);
        if (txt!=null&&txt.length()>0) {
        	et.setText(txt);
        	et.setSelection(et.getText().toString().length());
        }
        
        ll.addView(et);

        LinearLayout.LayoutParams lp1 
        = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(20, 20, 20, 20);
        LinearLayout butLayout = new LinearLayout(m_c);
        butLayout.setLayoutParams(lp1);
        butLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        butLayout.setOrientation(LinearLayout.HORIZONTAL);
        if(m_ok!=null)
        {
            butLayout.addView(makeButton(m_ok, AlertDialog.BUTTON_POSITIVE,2));
        }
        if(m_no!=null)
        {
            butLayout.addView(makeButton(m_no, AlertDialog.BUTTON_NEGATIVE,2));
        }
        if(m_cancel!=null)
        {
            butLayout.addView(makeButton(m_cancel,AlertDialog.BUTTON_NEUTRAL,2));
        }
        ll.addView(butLayout);
        return ll;
    }
    public View createViewList()
    {
        LinearLayout ll = new LinearLayout(m_c);
//      ll.setBackgroundResource(android.R.drawable.dialog_frame);
        ll.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
        ll.setOnKeyListener(m_keyListener);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);
        ll.setPadding(5, 5, 5, 5);
        
        if(m_text!=null)
        {
            TextView tv = new TextView(m_c);
            tv.setTextColor(Color.WHITE);
            tv.setText(m_text);
            tv.setMinWidth(200);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0, 20, 0, 0);
            tv.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
            ll.addView(tv);
        }
        ScrollView sv = new ScrollView(m_c);
        sv.setPadding(20, 5, 15, 0);
        sv.setBackgroundColor(android.R.drawable.dialog_frame);
        // задаёт размер или по стандарту, или в пикселях
        switch (st.getOrientation(m_c))
        {
        case Configuration.ORIENTATION_PORTRAIT:
            if (list.length>6)
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 450));
            else
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            break;
        default:
            if (list.length>4)
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 450));
            else
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            break;
        }        
//        sv.setPadding(20, 150, 20, 150);
        
        if (list.length >0){
            LinearLayout ll1 = new LinearLayout(m_c);
//            ll1.setBackgroundResource(android.R.drawable.dialog_frame);
            ll1.setOnKeyListener(m_keyListener);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp1.gravity = Gravity.CENTER_HORIZONTAL;
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setLayoutParams(lp);
//            ll1.setPadding(20, 20, 20, 20);
        	
        	for (int i = 0;i<list.length;i++){
        		Button btn = new Button(m_c);
        		btn.setId(i);
        		btn.setTextSize(14);
        		btn.setText(list[i]);
        		btn.setTextColor(th.getValue(th.ALERT_WINDOW_TEXT_LIST_BUTTON));
        		btn.setOnClickListener(m_clkListenerList);
        		ll1.addView(btn);
        	}
        	sv.addView(ll1);
        }
        LinearLayout.LayoutParams lp1 
        = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(20, 20, 20, 20);
        LinearLayout butLayout = new LinearLayout(m_c);
        butLayout.setLayoutParams(lp1);
        butLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        butLayout.setOrientation(LinearLayout.HORIZONTAL);
        if(m_ok!=null)
        {
            butLayout.addView(makeButton(m_ok, AlertDialog.BUTTON_POSITIVE,3));
        }
        if(m_no!=null)
        {
            butLayout.addView(makeButton(m_no, AlertDialog.BUTTON_NEGATIVE,3));
        }
        if(m_cancel!=null)
        {
            butLayout.addView(makeButton(m_cancel,AlertDialog.BUTTON_NEUTRAL,3));
        }
        ll.addView(getHideEditText());
      	ll.addView(sv);
        ll.addView(butLayout);
        return ll;
    }
	@SuppressLint("ResourceAsColor")
	public View createViewListArray()
    {
        LinearLayout ll = new LinearLayout(m_c);
//      ll.setBackgroundResource(android.R.drawable.dialog_frame);
        ll.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
        ll.setOnKeyListener(m_keyListener);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);
        ll.setPadding(5, 5, 5, 5);
        
        if(m_text!=null)
        {
            TextView tv = new TextView(m_c);
            tv.setTextColor(Color.WHITE);
            tv.setText(m_text);
            tv.setMinWidth(200);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0, 15, 0, 0);
            tv.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
            ll.addView(tv);
        }
        ScrollView sv = new ScrollView(m_c);
        sv.setPadding(20, 5, 15, 0);
        sv.setBackgroundColor(android.R.drawable.dialog_frame);
        // задаёт размер или по стандарту, или в пикселях
        switch (st.getOrientation(m_c))
        {
        case Configuration.ORIENTATION_PORTRAIT:
            if (slist.size()>6)
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 450));
            else
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            break;
        default:
            if (slist.size()>4)
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 450));
            else
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            break;
        }        
        
        if (st.getOrientation(m_c) == Configuration.ORIENTATION_PORTRAIT){
            if (slist.size()>6)
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 450));
            else
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
        if (st.getOrientation(m_c) == Configuration.ORIENTATION_LANDSCAPE){
            if (slist.size()>4)
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 450));
            else
            	sv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
        
        if (slist.size() >0){
            LinearLayout ll1 = new LinearLayout(m_c);
//            ll1.setBackgroundResource(android.R.drawable.dialog_frame);
            ll1.setOnKeyListener(m_keyListener);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp1.gravity = Gravity.CENTER_HORIZONTAL;
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setLayoutParams(lp);
//            ll1.setPadding(20, 20, 20, 20);
        	
        	for (int i = 0;i<slist.size();i++){
        		Button btn = new Button(m_c);
        		btn.setId(i);
        		btn.setTextSize(14);
        		btn.setText(slist.get(i));
        		btn.setTextColor(th.getValue(th.ALERT_WINDOW_TEXT_LIST_BUTTON));
        		btn.setOnClickListener(m_clkListenerList);
        		ll1.addView(btn);
        	}
        	sv.addView(ll1);
        }
        

        LinearLayout.LayoutParams lp1 
        = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(20, 5, 20, 20);
        LinearLayout butLayout = new LinearLayout(m_c);
        butLayout.setLayoutParams(lp1);
        butLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        butLayout.setOrientation(LinearLayout.HORIZONTAL);
        if(m_ok!=null)
        {
            butLayout.addView(makeButton(m_ok, AlertDialog.BUTTON_POSITIVE,3));
        }
        if(m_no!=null)
        {
            butLayout.addView(makeButton(m_no, AlertDialog.BUTTON_NEGATIVE,3));
        }
        if(m_cancel!=null)
        {
            butLayout.addView(makeButton(m_cancel,AlertDialog.BUTTON_NEUTRAL,3));
        }
        // невидимый et, чтоб закрывалось окно списка
        EditText et= new EditText(m_c);
        et.setHeight(1);
        et.setWidth(1);
        et.setOnKeyListener(m_keyListener);
        et.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
        et.setTextColor(th.getValue(th.ALERT_WINDOW_BACK));
        ll.addView(et);
      	ll.addView(sv);
        ll.addView(butLayout);
        return ll;
    }
    // невидимый et, чтоб срабатывала обработка нажатий клавиш (кнопка Назад)
	// и закрывалось окно диалога
	EditText getHideEditText()
	{
        EditText et= new EditText(m_c);
        et.setHeight(1);
        et.setWidth(1);
        et.setOnKeyListener(m_hideETkeyListener);
        et.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
        et.setTextColor(th.getValue(th.ALERT_WINDOW_BACK));
		return et;
	}
    View.OnKeyListener m_hideETkeyListener = new View.OnKeyListener()
    {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
    	    if(keyCode == KeyEvent.KEYCODE_BACK)  
    	    {
    	    	finish();
        	    return true;  
    	    }  
            return false;
        }
    };

    Button makeButton(String text,int id, int type)
    {
        Button b = new Button(m_c);
        b.setMinWidth(100);
//        b.setBackgroundColor(Color.WHITE);
        b.setText(text);
        b.setId(id);
        b.setBackgroundResource(drawable.btn_default);
        b.setTextColor(th.getValue(th.ALERT_WINDOW_BUTTON_TEXT));
// 1- диалог, 2 - edittext, 3 - list
        switch (type)
        {
        case 1: b.setOnClickListener(m_clkListener);break;
        case 2: b.setOnClickListener(m_clkListenerEdit);break;
        case 3: b.setOnClickListener(m_clkListenerEdit);break;
        default: b.setOnClickListener(m_clkListener);break;
        }
//        b.setOnClickListener(m_clkListener);
        return b;
    }
    public void Layout(WindowManager.LayoutParams lp)
    {}
    public void setGravityText(int gravity)
    {
    	m_text_gravity = gravity;
    }
    public void showAlert()
    {
      	if (gbshow){
    		finish();
    		return;
    	}
//      	if (wm==null)
      		wm = (WindowManager)m_c.getSystemService(Service.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
		} else {
			lp.type = WindowManager.LayoutParams.TYPE_PHONE;
		}
//        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.format = PixelFormat.TRANSLUCENT;
//        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    			|WindowManager.LayoutParams.FLAG_FULLSCREEN
                |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                |WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                |WindowManager.LayoutParams.FLAG_DIM_BEHIND
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND
        ;
        lp.dimAmount = (float) 0.2;
        Layout(lp);
        m_view = createView();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
    		if (!Perm.checkPermission(m_c)) {
    			st.toastLong(R.string.perm_not_all_perm);
    			return;
    		}
        }
        gbshow = true;
//        m_view.setOnClickListener(m_clkListener);
//        m_view.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
        wm.addView(m_view, lp);
    }
    public void showEdit(String txt)
    {
      	if (wm!=null)
      		finish();
   		wm = (WindowManager)m_c.getSystemService(Service.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
		} else {
			lp.type = WindowManager.LayoutParams.TYPE_PHONE;
		}
//        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|
        WindowManager.LayoutParams.FLAG_DIM_BEHIND
        ;
        lp.dimAmount = (float) 0.2;
        Layout(lp);
        m_view = createViewEdit(txt);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
    		if (!Perm.checkPermission(m_c)) {
    			st.toastLong(R.string.perm_not_all_perm);
    			return;
    		}
        }
      	wm.addView(m_view, lp);
        gbshow = true;
    }
    public void showList()
    {
//      	if (wm==null)
      		wm = (WindowManager)m_c.getSystemService(Service.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
		} else {
			lp.type = WindowManager.LayoutParams.TYPE_PHONE;
		}
//        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|
        WindowManager.LayoutParams.FLAG_DIM_BEHIND
        ;
        lp.dimAmount = (float) 0.2;
        Layout(lp);
        if (list!=null&&list.length>-1)
        	m_view = createViewList();
        else if (slist!=null&&slist.size()>-1)
        	m_view = createViewListArray();
        m_view.setOnKeyListener(win_onKeyListener);
//        m_view.setBackgroundColor(th.getValue(th.ALERT_WINDOW_BACK));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
    		if (!Perm.checkPermission(m_c)) {
    			st.toastLong(R.string.perm_not_all_perm);
    			return;
    		}
        }
        wm.addView(m_view, lp);
        gbshow = true;
    }
    public void finish()
    {
//      	if (wm==null)
//      		wm = (WindowManager)m_c.getSystemService(Service.WINDOW_SERVICE);
        if (wm!=null&&m_view!=null&&gbshow)
        	wm.removeView(m_view);
        gbshow = false;
        //inst = null;
    }
    public void set(String title,String ok,String cancel)
    {
        m_text = title;
        m_ok = ok;
        m_cancel = cancel;
    }
    public void set(int title,int ok, int no, int cancel)
    {
        set(m_c.getString(title), ok==0?null:m_c.getString(ok),no==0?null:m_c.getString(no), cancel==0?null:m_c.getString(cancel));
    }
    public void setList(int title,int ok, int no, int cancel, int ... val)
    {
//    	list = new int[val.length];
    	list = val;
        set(m_c.getString(title), ok==0?null:m_c.getString(ok),no==0?null:m_c.getString(no), cancel==0?null:m_c.getString(cancel));
    }
    public void setListArray(int title,int ok, int no, int cancel, ArrayList<String> ar)
    {
//    	list = new int[val.length];
    	slist = ar;
        set(m_c.getString(title), ok==0?null:m_c.getString(ok),no==0?null:m_c.getString(no), cancel==0?null:m_c.getString(cancel));
    }
    public void set(String title,String ok,String no, String cancel)
    {
        m_text = title;
        m_ok = ok;
        m_no = no;
        m_cancel = cancel;
    }
    public void set(int title,int ok,int cancel)
    {
        set(m_c.getString(title), ok==0?null:m_c.getString(ok), cancel==0?null:m_c.getString(cancel));
    }
    public void set(String title,int ok,int cancel)
    {
        set(title, ok==0?null:m_c.getString(ok), cancel==0?null:m_c.getString(cancel));
    }
    public void setObserver(st.UniObserver obs)
    {
        m_obs = obs;
    }
 // нажатие ентера для edittext
    View.OnKeyListener et_onKeyListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) 
		{
    	    if(keyCode == KeyEvent.KEYCODE_BACK)  
    	    {
    	    	
            	et = null;
    	    	finish();
        	    return true;  
    	    }  
    	    else if(event.getAction() == KeyEvent.ACTION_DOWN && 
    	    	(keyCode == KeyEvent.KEYCODE_ENTER))
       		{
            	ret_edittext_text = et.getText().toString();
            	et = null;
                if(m_obs!=null&&m_obs.OnObserver(Integer.valueOf(v.getId()), this)!=NO_FINISH){
                	finish();
                }
   				return true;
   			}
       		return false;
		}
	};
// нажатие BACK для всего окна
	View.OnKeyListener win_onKeyListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) 
		{
    	    if(keyCode == KeyEvent.KEYCODE_BACK)  
    	    {
    	    	finish();
        	    return true;  
    	    }  
       		return false;
		}
	};
	@Override
    public void onBackPressed() 
    {
    	finish();
    }
}