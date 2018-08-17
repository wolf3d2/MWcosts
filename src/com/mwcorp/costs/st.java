package com.mwcorp.costs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import com.mwcorp.costs.var.myCur;
import com.mwcorp.tools.GlobDialog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/** Класс содержит полезные статические переменные */
public class st extends var
{
	// килобайт
	public static int KB = 1024;
	public static final boolean DEBUG = true;
	public static final String TAG = "COSTS: ";
	private static final int MAX_STACK_STRING = 8192;
	public static final String SAVE_CRASH = "/save_crash.txt";
	private static final String CAUSED_BY = "caused by";

    public static abstract class UniObserver
    {
    /** Конструктор с двумя параметрами */
        public UniObserver(Object param1,Object param2)
        {
            m_param1 = param1;
            m_param2 = param2;
        }
    /** Пустой конструктор. Оба параметра - null*/
        public UniObserver()
        {
        }
    /** Вызов функции {@link #OnObserver(Object, Object)} с текущими параметрами*/
        public int Observ(){return OnObserver(m_param1, m_param2);}
    /** Основная функция обработчика */ 
        public abstract int OnObserver(Object param1,Object param2);
    /** Пользовательский параметр 1 */  
        Object m_param1;
    /** Пользовательский параметр 2 */  
        public Object m_param2;
    }

	public static void sleep(int sl)
    {
      	try {
       	Thread.sleep(sl); // спать sl милисекунд.
         } catch(Exception e){}    	        
   	}
	public static String getPathAppFolder()
    {
		return Environment.getExternalStorageDirectory().getAbsolutePath()+ "/MWcosts";
   	}
	public static boolean setPathAppFolder()
    {
		File f = new File(getPathAppFolder());
		f.mkdir();
		
		return true;
   	}
	// переводит строку в int	
    public static int str2int(String sss, int defValue)
    {
      	int i1=0;
       	try {
       		if (sss.length()!=0)
       			i1 = Integer.valueOf(sss);
       		else 
       			i1 = 0;
        	}catch (NumberFormatException e) {
        		i1 = defValue;
        	}  
        return i1;    	        
   	}
 // возвращает int с указанной системой счисления
    // radix - система счисления (2,8,10,16 и т.д.    
       public static int parseInt(String string,int radix) {
           int result = 0;
           int degree = 1;
           for(int i=string.length()-1;i>=0;i--)
           {
               int digit = Character.digit(string.charAt(i), radix);
               if (digit == -1) {
                   continue;
               }
               result+=degree*digit;
               degree*=radix;
           }
           return result;
       }

    // переводит строку из заданной системы счисления
    public static int str2hex(String sss, int radix)
    {
    	boolean bbb = true;
    	if (sss.length() == 0) {
//    		st.toast("Empty.");
    		return 0;
    	}
    	sss = sss.toUpperCase();
    	int i1=0;
    	try {
    		if (sss.startsWith("0x")) {
    			i1 = st.parseInt(sss.substring(2),radix);
    			bbb = false;
    		}
    		else if (sss.startsWith("#")) {
    			i1 = st.parseInt(sss.substring(1),radix);
    			bbb = false;
    		}
    		 if (bbb) {
    			i1 = st.parseInt(sss.trim(),radix);
    		}
//    		i1 = Integer.parseInt(sss.replace("#",st.STR_NULL),16);
    	}catch (NumberFormatException e) {
    		toast("Error numeric format");
    		i1 = 0;
    	}
        return i1;    	        
   	}
    
 // переводит строку в float	
    public static float str2float(String sss, String err)
    {
      	float i1=0;
       	try {
       		if (sss.length()!=0)
       			i1 = Float.valueOf(sss);
       		else 
       			i1 = 0;
        	}catch (NumberFormatException e) {
        		if (err != null){
//        			Context c = MainActivity.inst;
       				st.toast(st.c().getString(R.string.error_format)+"\n"+err);
        		} else
    				st.toast(R.string.error_format);
        	}  
        return i1;    	        
   	}
    static boolean runAct(Class<?>cls,Context c)
    {
        try{
            c.getApplicationContext().startActivity(
                    new Intent(Intent.ACTION_VIEW)
                        .setComponent(new ComponentName(c,cls))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
       }
        catch(Throwable e)
        {
            log("costs:");
            logEx(e);
        	return false;
        }
        return true;
    }
	public static void saveCrash(Throwable e, Activity mact)
	{
		Log.e("MWcosts Crash", st.getStackString(e));
		e.printStackTrace();
		st.strToFile(st.getStackString(e), new File(mact.getFilesDir(),SAVE_CRASH));
	}
    static void log(String txt)
    {
        if(DEBUG)
            Log.w(TAG, txt);
    }
    public static final void logEx(Throwable e)
    {
        if(DEBUG)
        {
        if(e.getMessage()!=null)
            Log.e(TAG, e.getMessage());
        Log.e(TAG, Log.getStackTraceString(e));
        }
    }
    /** Возвращает активный контекст. Если запущено {@link SetKbdActivity} - то возвращает его, иначе - {@link ServiceJbKbd}*/    
    public static Context c()
    {
        if(Newrecord.inst!=null)
           return Newrecord.inst;
        else if(Cur.inst!=null)
           return Cur.inst;
       else if(CurEdit.inst!=null)
            return CurEdit.inst;
        if(About.inst!=null)
            return About.inst;
        return MainActivity.inst;
    }
    // вывод сообщения длительностью 700мс
    public static void toast(String txt)
    {
        Context c = st.c();
        Toast.makeText(c, txt, 700).show();
   	}
    public static void toast(int res_str)
    {
        Context c = st.c();
        Toast.makeText(c, c.getString(res_str), 700).show();
   	}
    public static void toastLong(int res_str)
    {
        Context c = st.c();
        st.toast(c.getString(res_str));
   	}
    // вывод длительного (>3сек) сообщения 
    public static void toastLong(String txt)
    {
        Context c = st.c();
        Toast.makeText(c, txt, Toast.LENGTH_LONG).show();
   	}
    public static void CurLoad()
    {
    	if (var.currency!=null)
    		var.currency.clear();
    	String path = getPathAppFolder()+"/currensy.txt";
		File file = new File(path);
		myCur cur1 = new myCur();
		if (file!=null&&file.isFile()==false){
			CurDefault();
			return;
		}
		try {
			FileReader fr = new FileReader(path);
			Scanner sc = new Scanner(fr);
			sc.useLocale(Locale.US);
			String str = "";
			String[] par;
			int check = 0;
			cur1 = new myCur();
			try {
				while (sc.hasNext()) {
					if (sc.hasNextLine()) {
						par = sc.nextLine().toString().trim().split("=");
						if (par[0].compareToIgnoreCase("CurrencyId")==0){
							cur1.id = Integer.valueOf(par[1].trim());
						}
						else if (par[0].compareToIgnoreCase("SymbolCurrency")==0){
							cur1.cur_symb = par[1].trim(); 
						}
						else if (par[0].compareToIgnoreCase("Name")==0){
							cur1.name = par[1].trim();
						}
						else if (par[0].compareToIgnoreCase("KursOfDay")==0){
							cur1.kurs_of_date = Long.valueOf(par[1].trim()); 
						}
						else if (par[0].compareToIgnoreCase("KursValue")==0){
							cur1.kurs_value = Float.valueOf(par[1].trim()); 
						}
						else if (par[0].compareToIgnoreCase("BaseCurrency")==0){
							cur1.base_cur = Boolean.valueOf(par[1].trim());
							if (cur1.base_cur){
								for (myCur c:var.currency){
									c.base_cur = false;
								}
							}
						}
						if (par[0].compareToIgnoreCase(myCur.DELIMITER) == 0){
							var.currency.add(cur1);
							cur1 = new myCur();
						}
					}
				}
			} catch (Throwable e) {
				st.logEx(e);
			}
			sc.close();
			if (var.currency == null)
				CurDefault();
			if (var.currency.size() == 0)
				CurDefault();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			st.logEx(e);
		}


   	}
    public static void CurDefault()
    {
    	var.currency.clear();
		myCur cur1;
		cur1 = new myCur(CurRnd(), c().getString(R.string.curdefcs_rub), c().getString(R.string.curdefn_rub), new Date().getTime(), 1, true);
		var.currency.add(cur1);
		cur1 = new myCur(CurRnd(), c().getString(R.string.curdefcs_usd), c().getString(R.string.curdefn_usd), new Date().getTime(), 1, false);
		var.currency.add(cur1);
		cur1 = new myCur(CurRnd(), c().getString(R.string.curdefcs_eur), c().getString(R.string.curdefn_eur), new Date().getTime(), 1, false);
		var.currency.add(cur1);
		cur1 = new myCur(CurRnd(), c().getString(R.string.curdefcs_gbr), c().getString(R.string.curdefn_gbr), new Date().getTime(), 1, false);
		var.currency.add(cur1);
		cur1 = new myCur(CurRnd(), c().getString(R.string.curdefcs_brb), c().getString(R.string.curdefn_brb), new Date().getTime(), 1, false);
		var.currency.add(cur1);
		cur1 = new myCur(CurRnd(), c().getString(R.string.curdefcs_uah), c().getString(R.string.curdefn_uah), new Date().getTime(), 1, false);
		var.currency.add(cur1);
   	}
    public static boolean CurCheckRecord(myCur c)
    {
    	return true;
   	}
    public static int CurRnd()
    {
    	int ret = 0;
    	double rnd = 0;
    	int vs=0;
    	boolean bbb = true;
    	while (bbb)
    	{
       		rnd = Math.random();
       		String sss = String.valueOf(rnd);
       		vs = sss.indexOf(".");
   			sss=sss.substring(vs+1,vs+7);
   			ret = Integer.valueOf(sss);
   			for (myCur c:var.currency){
   				if (ret == c.id) {
   					continue;
   				}
   			}
   			bbb = false;
    	}
    	return ret;
   	}
    public static String Rnd()
    {
    	String ret = "";
    	double rnd = 0;
    	int vs=0;
   		rnd = Math.random();
   		ret = String.valueOf(rnd);
   		vs = ret.indexOf(".");
		ret=ret.substring(vs+1,vs+7);
    	return ret;
   	}
    public static boolean setBaseCurrency()
    {
    	boolean ret = false;
        for (myCur c:var.currency){
        	if (c.base_cur){
        		ret=true;
        		break;
        	}
        }
        if (ret == false){
        	myCur c = new myCur();
        	c = var.currency.get(0);
        	c.base_cur = true;
        	var.currency.set(0, c);
        }
    	return ret;
    }
    public static boolean CurSave()
    {
    	String path = st.getPathAppFolder()+"/currensy.txt";
		FileWriter wr;
		try {
			if (var.currency.size() == 0)
				CurDefault();
			setBaseCurrency();
			st.setPathAppFolder();
			wr = new FileWriter(path, false);
			for (myCur cur:var.currency) {
				wr.write("CurrencyId="+String.valueOf(cur.id)+"\n");
				wr.write("SymbolCurrency="+cur.cur_symb+"\n");
				wr.write("Name="+cur.name.trim()+"\n");
				wr.write("KursOfDay="+cur.kurs_of_date+"\n");
				wr.write("KursValue="+String.valueOf(cur.kurs_value)+"\n");
				wr.write("BaseCurrency="+String.valueOf(cur.base_cur)+"\n");
				wr.write(myCur.DELIMITER+"\n\n");
				}
			wr.close();
			Cur.inst.view();
			return true;
		} catch (IOException e) {
			st.logEx(e);
			st.toast("Error. Currency not save.");
			return false;
		}
    }
    public static String strFile(File f)
    {
    	String s= null;
		try{
			FileInputStream fin = new FileInputStream(f);
			byte buf[] = new byte[(int) f.length()];
			fin.read(buf);
			fin.close();
			s = new String(buf);
		}
		catch(Throwable e)
		{
		}
		return s;
    }
    public static boolean strToFile(String s,File f)
    {
    	try{
    		f.delete();
    		FileOutputStream fout = new FileOutputStream(f);
    		fout.write(s.getBytes());
    		fout.close();
    		return true;
    	}
    	catch(Throwable e){}
    	return false;
    }
	public static String getAppVersion(Context c)
	{
        PackageManager pm = c.getPackageManager();
        try{
         return pm.getPackageInfo(c.getPackageName(), 0).versionName;
        }
        catch (Throwable e) {
        }
        return "";
	}
	public static String getAppVersionCode(Context c)
	{
        PackageManager pm = c.getPackageManager();
        try{
         return ""+pm.getPackageInfo(c.getPackageName(), 0).versionCode;
        }
        catch (Throwable e) {
        }
        return "";
	}
	public static String getAppNameAndVersion(Context c)
	{
		return c.getString(R.string.app_name)+" "+getAppVersion(c);
	}
    public static final String getStackString(Throwable e)
    {
    	if(e==null)
    		e = new Exception();
    	StringBuffer msg = new StringBuffer(e.getClass().getName());
    		if(!TextUtils.isEmpty(e.getMessage()))
    			msg.append(' ').append(e.getMessage());
    	msg.append('\n');
    	StackTraceElement st[] = e.getStackTrace();
    	for(StackTraceElement s:st)
    		msg.append(s.toString()).append('\n');
    	Throwable cause = e.getCause();
    	if(cause!=null&&msg.length()<MAX_STACK_STRING)
    		msg.append('\n').append(CAUSED_BY).append('\n').append(getStackString(cause));
    	String ret = msg.toString();
    	return ret;
    }
// возвращает первые 2 строки входящей строки
    public static String getHeaderText(String txt)
    {
		String ret = "";
		String par = "";
		Scanner sc = new Scanner(txt);
		sc.useLocale(Locale.US);
		while (sc.hasNext()) {
			par = "";
			if (sc.hasNextLine()) {
				par = sc.nextLine();
			}
			if (par.length()>0){
				ret = par + "\n";
				break;
			}
		}
		sc.close();
		return ret;
   	}
    public static String getAssetsFile(Context c, String namefile)
    {
        byte[] buffer = null;
        InputStream is;
    	AssetManager am;
        try {
          	am = c.getAssets();
          	is = am.open(namefile);
            is = c.getAssets().open(namefile);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return new String(buffer);
   	}
    static String getFileString(String path)
    {
    	File f = new File(rec.getCurrentFolder()+"/"+path);
        try{
            FileInputStream fi = new FileInputStream(f);
            byte buf[] = new byte[(int)f.length()];
            fi.read(buf);
            int start = 0;
            if(buf.length>3&&buf[0]==0xef&&buf[1]==0xbb&&buf[2]==0xbf)
            {
                start = 3;
            }
            fi.close();
            return new String(buf, start, buf.length-start);
        }
        catch(Throwable e)
        {
            st.logEx(e);
        }
        return null;
    }
    public static void showKbd(final EditText et)
    {
       (new Handler()).postDelayed(new Runnable() {

			public void run() {

                et.requestFocus();
                et.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                et.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));                       
                if (et !=null)
                	et.setSelection(et.getText().toString().length());
            }
        }, 200);
    }
    public static void hideKbd(Activity con)
    {
			InputMethodManager imm = (InputMethodManager) con.getSystemService(con.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(con.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static void setTextColorChildren(LinearLayout ll,int color)
    {
    	if (ll==null)
    		return;
    	Button b = null;
    	for (int i=0;i<ll.getChildCount();i++){
    		b = (Button) ll.getChildAt(i);
    		b.setTextColor(color);
    	}
    }
    public static int getOrientation(Context c)
    {
    	/*int orient = getResources().getConfiguration().orientation;

		switch (orient) {
		case Configuration.ORIENTATION_PORTRAIT:
			size = (int) (measureHeight * port);

			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			size = (int) (measureHeight * land);
			break;
		}*/
    	return c.getResources().getConfiguration().orientation;
    }
    public static int getDisplayWidth(Context c)
    {
    	return c.getResources().getDisplayMetrics().widthPixels;
    }
    public static int getDisplayHeight(Context c)
    {
    	return c.getResources().getDisplayMetrics().heightPixels;
    }
    public static float screenDensity(Context c)
    {
        return c.getResources().getDisplayMetrics().density;
    }
    // возвращает отстут текста от квадрата чекбокса
    public static CheckBox getTextPadding(CheckBox cb)
    {
        if(android.os.Build.VERSION.SDK_INT <= 17){
        	cb.setPadding(60,0,0,0);
        }
    	return cb;
    }
    public static void runOtherApp()
    {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Михаил+Вязенкин"));
    	c().startActivity(intent);
    }
    // true - число чётное
    public static boolean isEven(float x)
    {
    	return (x%2) == 0;
    }
    // ИСПОЛЬЗУЕТСЯ В ДВУХ МЕСТАХ!
    // если запущено на эмуляторе (значит ведётся отладка 
    // и отчёт о падении выводить не надо)
    public static boolean isDebugEmulator()
    {
    	// запущено на genymotion
        if (Build.MANUFACTURER.toLowerCase().contains("genymo"))
           	return true;
    	return false;
    }
    public static void help(int id_txt, Context c) 
    {
    	help(c.getString(id_txt), c);
    }
    // показывает окно GlobDialog на экране с одной кнопкой Ок
    public static void help(String txt, Context c) 
    {
        GlobDialog gd = new GlobDialog(c);
        gd.setGravityText(Gravity.LEFT|Gravity.TOP);
        gd.set(txt, R.string.ok, 0);
        gd.setObserver(new st.UniObserver()
        {
            @Override
            public int OnObserver(Object param1, Object param2)
            {
                return 0;
            }
        });
        gd.showAlert();
    }
    public static String getKBString(long val) 
    {
    	String ret = var.STR_NULL;
    	double dval = 0;
    	dval = (double)val/st.KB;
    	if (dval> 1){
        	dval = (double)dval/st.KB;
        	if (dval > 1){
            	dval = (double)dval/st.KB;
            	if (dval > 1){
            		ret = "Gb";
            	} else 
            		ret = "Mb";
        		
        	} else 
        		ret = "kb";
    	} else
    		ret = "b";
    	dval*=st.KB;
    	double cel = Math.round(dval*100);
    	ret = ""+ cel/100+ret;
    	return ret;
    }

}