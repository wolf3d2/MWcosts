package com.mwcorp.costs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.mwcorp.costs.var.myCur;
import com.mwcorp.tools.GlobDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class Cur extends Activity 
{
	ListView lv;
	LinearLayout toppanel;
	Button del;
	Button add;
	static Cur inst;
	boolean fl_del = false;;
	private ArrayList<HashMap<String, Object>> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		inst = this;
		th.setTheme(inst);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cur);
		fl_del = false;
//        View v = getLayoutInflater().inflate(R.layout.cur, null);
        toppanel = (LinearLayout)findViewById(R.id.cur_toppanel);
        toppanel.setBackgroundColor(th.getValue(th.MAIN_TOP_PANEL_BACK));
        st.setTextColorChildren(toppanel,th.getValue(th.MAIN_BUTTON_TEXT_TOP_PANEL));
        del = (Button)toppanel.findViewById(R.id.cur_del);
        add = (Button)toppanel.findViewById(R.id.cur_add);
		lv = (ListView)findViewById(R.id.cur_listView1);
		view();

        // показ рекламы
        Ads.show(this,4);
	}
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        Ads.show(this,4);
    }

    @Override
    public void onDestroy() {
//    	inst = null;
        super.onDestroy();
    }
    public void view()
    {
    	st.CurLoad();
	    if (fl_del)
	    	lv.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_SELECT));
	    else {
	    	lv.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_NOTSELECT));
	    }
    	mList = new ArrayList<HashMap<String, Object>>();
	    HashMap<String, Object> hm;
	    for (myCur c:var.currency) {
		    hm = new HashMap<String, Object>();
		    hm.put(myCur.LNAME, c.name.trim());
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		    hm.put(myCur.LKURSDATE, sdf.format(c.kurs_of_date).trim());
		    hm.put(myCur.LNAME, c.name.trim());
		    hm.put(myCur.LSYMB, c.cur_symb.trim());
		    hm.put(myCur.LKURDVALUE, String.valueOf(c.kurs_value).trim());
		    if (c.base_cur){
			    hm.put(myCur.LBASECUR, inst.getString(R.string.cur_main_desc));
		    } else {
			    hm.put(myCur.LBASECUR, "");
		    }
		    hm.put(myCur.LID, c.id);
		    
		    mList.add(hm);
	    }
	    int layout = getSimpleAdapter();

	    SimpleAdapter adapter = new SimpleAdapter(this, mList, layout, 
	    			new String[]{myCur.LNAME,myCur.LKURSDATE,myCur.LSYMB,myCur.LKURDVALUE,myCur.LBASECUR,myCur.LID},
	    			new int[]{R.id.cur_name,R.id.cur_date,R.id.cur_symb,R.id.cur_value,R.id.cur_base,R.id.cur_id});
	    lv.setAdapter(adapter);
	    for (int i=0; i<lv.getChildCount();i++){
	    	lv.getChildAt(i).setId(0);
	    }
	    lv.setOnItemClickListener(itemClickListener);
	    lv.setOnItemLongClickListener(itemLongClickListener);
    }
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        	st.toast(""+view.getId());
        	if (fl_del == false){
            	TextView tv = (TextView)view.findViewById(R.id.cur_id);
            	String sss = tv.getText().toString();
            	int iii = Integer.valueOf(tv.getText().toString());
            	runCurEdit(iii);
        	} else {
        		if (view.getId() > 0){
        			view.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_NOTSELECT));
        			view.setId(0);
        		} else {
        			setItemIdAndColor(view);
        		}
        	}
        }
    };
    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() 
    {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
      {
    	  add.setVisibility(View.GONE);
    	  del.setVisibility(View.VISIBLE);
    	  fl_del = true;
    	  setItemIdAndColor(view);
    	  return true;
      }};
      public void setItemIdAndColor(View v) 
      {
    	  v.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_SELECT));
    	  v.setId(1);
      }
    public void onClick_cur_add(View view) 
    {
    	runCurEdit(-1);
    }
    public void onClick_cur_del(View view) 
    {
     	if (GlobDialog.gbshow){
    		return;
    	}

        GlobDialog gd =  new GlobDialog(inst);
        gd.set(R.string.del_query, R.string.yes, R.string.no);
        gd.setObserver(new st.UniObserver()
        {
            @Override
            public int OnObserver(Object param1, Object param2)
            {
                if(((Integer)param1).intValue()==AlertDialog.BUTTON_POSITIVE)
                {
        	    	lv.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_NOTSELECT));
                	String sss = "";
                	int _id = 0;
                	int idtv = 0;
                	View v = null;
                	TextView tv = null;;
                	for (int i=0; i<lv.getChildCount();i++){
                		sss = "";
                		v = null;
                		tv = null;
                		_id = lv.getChildAt(i).getId();
            			v = lv.getChildAt(i);
                		if (_id == 1){
                    		tv = (TextView) v.findViewById(R.id.cur_id);
                    		idtv = Integer.valueOf(tv.getText().toString());
                    		for (int ii = 0; ii<var.currency.size();ii++){
                    			if (idtv == var.currency.get(ii).id){
                    				if (var.currency.size()>0){
                    					var.currency.remove(ii);
                    					break;
                    				}
                    			}
                    		}
                		} else {
                			v.setId(0);
                		}
                	}
                	st.CurSave();
                } 
               	fl_del = false;
        		view();
               	add.setVisibility(View.VISIBLE);
              	del.setVisibility(View.GONE);
                return 0;
            }
        });
        gd.showAlert();
    	
    }
	public void runCurEdit(int pos) 
    {
    	var.id_cur = pos;
        st.runAct(CurEdit.class,this);
    }
    @Override
    public void onBackPressed()
    {
     	if (GlobDialog.gbshow){
    		GlobDialog.inst.finish();
    		return;
    	}
     	if (fl_del){
            add.setVisibility(View.VISIBLE);
            del.setVisibility(View.GONE);
            fl_del = false;
            view();
            return;
    	}
		super.onBackPressed();
    }
    public int getSimpleAdapter() 
    {
  	  int lay = R.layout.cur_list_item_light;
  	  switch (var.skin_app)
  	  {
  	  case 1:lay=R.layout.cur_list_item_black;break;
  	  }
        return  lay;
    }

}