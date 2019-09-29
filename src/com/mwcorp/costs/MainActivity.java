package com.mwcorp.costs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import com.mwcorp.costs.var.FolderInfo;
import com.mwcorp.costs.var.RecordCurrency;
import com.mwcorp.dialog.Dlg;
import com.mwcorp.perm.Perm;
import com.mwcorp.tools.GlobDialog;
import com.mwcorp.tools.Mail;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	FolderInfo fiii = new FolderInfo();
	// параметры для par.ini
	// срок (2 недели) перед выскакиванием просьбы оценить
	long TIME_MONTH = 1000l * 3600l * 24l * 14l;
	//long TIME_MONTH = 5l * 60l * 1000l;
	// юзер нажал на Нет в просьбе оценить
	// (время до повторного срабатывания просьбы оценить
	long TIME_NEGATIVE_CLICK = 1000l * 3600l * 24l * 1l;
	//long TIME_NEGATIVE_CLICK = 1000l * 2l*60l;
	String START_TIME = "start_time";
	String RATE_APP = "rate_app";
	String VERSION_CODE = "version_code";
	public static final String VERS = "version";
	public static final String SLASH = "/";
	public static long curtime = 0;
	// время записанное в par.ini
	public static long partime = 0;
	// оценивалось приложение или нет
	String rate_app = "0";
	// первая установленная версия
	boolean new_vers = false;
	// путь и имя файла
	public static String path = var.STR_NULL;

	Button add;
	Button del;
	Button del_all;
	LinearLayout toppanel;
	ListView rlv;
	TextView tvpath;
	HorizontalScrollView hsv_path;
	LinearLayout ll_path;
	static MainActivity inst;
	// public static final String PID = "a14ef033de91702";
	public static File file_crash = null;
	boolean flmain_del = false;
	private ArrayList<HashMap<String, Object>> mainList;
	public static ArrayList<ArrayPath> ar_path = new ArrayList<ArrayPath>();

	private Thread.UncaughtExceptionHandler androidDefaultUEH;

	// класс для хранения пути для textview'ов в пути к текущей папке
	public static class ArrayPath {
		public int id = -1;
		public String path = var.STR_NULL;

		public ArrayPath() {
			id = -1;
			path = var.STR_NULL;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// настройки читаются в App.java
		// try{
		// Pref.init(this);
		// if (mainList != null&&mainList.size()>0)
		// mainList.clear();
		// Pref.readPreference();
		//// пример записи нового параметра в настройки
		//// Pref.get().edit().putString("sss", "ssss").commit();
		// } catch (Throwable e) {}
		inst = this;
		th.setTheme(inst);
		super.onCreate(savedInstanceState);
		// проверка
		checkIntent();
		curtime = new Date().getTime();
		setContentView(R.layout.activity_main);
		// this.getApplication().setTheme(R.style.Theme_IAPTheme);
		// inst = this;
		checkCrash();
		androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				st.saveCrash(e, inst);
				androidDefaultUEH.uncaughtException(thread, e);
			}
		});
		// вызываекм падение чтоб сработал отчёт об ошибке
		// int bbb = Integer.valueOf("huk");
		toppanel = (LinearLayout) findViewById(R.id.main_hll);
		toppanel.setBackgroundColor(th.getValue(th.MAIN_TOP_PANEL_BACK));
		st.setTextColorChildren(toppanel, th.getValue(th.MAIN_BUTTON_TEXT_TOP_PANEL));
		hsv_path = (HorizontalScrollView) findViewById(R.id.main_hscroll_path);
		hsv_path.setBackgroundColor(th.getValue(th.MAIN_TOP_PANEL_BACK));
		ll_path = (LinearLayout) findViewById(R.id.main_ll_path);
		ll_path.setBackgroundColor(th.getValue(th.MAIN_TOP_PANEL_BACK));
		del = (Button) toppanel.findViewById(R.id.main_del);
		del_all = (Button) toppanel.findViewById(R.id.main_del_all);
		add = (Button) toppanel.findViewById(R.id.main_add);
		
		// проверка разрешений
		if (!st.requestPermission(inst))
			return;


		// всякие подготовительные операции
		path = st.getPathAppFolder();
		File ff = new File(path);
		if (ff == null) {
			st.toast(R.string.toast_msg5);
			return;
		}
		path += "/par.setting";
		new_vers = false;
		String vers = "";
		try {
			vers = "" + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e1) {
		}
		ff = new File(path);
		if (!ff.exists()) {
			if (!st.setPathAppFolder()) {
				st.toast(R.string.toast_msg5);
				return;
			}
			FileWriter writer;
			try {
				writer = new FileWriter(path, false);
				String out1 = "rate_app=0\n";
				out1 += START_TIME + "=" + (long)(curtime+TIME_MONTH) + var.STR_lF;
				out1 += VERSION_CODE + "=" + vers + var.STR_lF;
				writer.write(out1);
				writer.close();
				new_vers = false;
			} catch (IOException e) {
			}
		} else {
			File file3 = new File(path);
			if (file3 != null && file3.exists() == true) {
				String par = "";
				String param = "";
				String param_value = "";
				FileReader fr;
				try {
					new_vers = false;
					fr = new FileReader(path);
					Scanner sc = new Scanner(fr);
					sc.useLocale(Locale.US);
					boolean fl = true;
					while (sc.hasNext()) {
						fl = false;
						if (sc.hasNextLine()) {
							par = sc.nextLine();
						}
						if (par.length() != 0) {
							param = par.substring(0, par.indexOf("="));
							param_value = par.substring(par.indexOf("=") + 1);
							if (param.contains(VERSION_CODE)) {
								fl = true;
								if (param_value.contains(vers) == false) {
									new_vers = true;
								}
							}
							if (param.contains(START_TIME)) {
								try {
									partime = Long.parseLong(param_value);
									fl = true;
								} catch (NumberFormatException e) {
									partime = 0;
								}
								fl = true;
							}
							if (param.contains(RATE_APP)) {
								rate_app = param_value;
								fl = true;
							}
							if (fl == false)
								saveIniParam(param, param_value, path);
						}
					}
					sc.close();

				} catch (Throwable e) {
				}
			}
		}
		if (partime == 0) {
			partime = curtime;
			// saveIniParam(START_TIME,var.STR_NULL+partime,path);
//			if (rate_app.compareToIgnoreCase("0") == 0)
//				rateApp();
		}
		String scurtime = "dd.MM.yyyy HH:mm:ss";
		Date dt = new Date();
		dt.setTime(curtime);
		SimpleDateFormat sdf = new SimpleDateFormat(scurtime);
		scurtime = sdf.format(dt);
		String spartime= "dd.MM.yyyy HH:mm:ss";
		dt = new Date();
		dt.setTime(partime);
		sdf = new SimpleDateFormat(spartime);
		scurtime = scurtime;
		spartime = sdf.format(dt);
		if (partime>curtime + TIME_MONTH) {
			partime = curtime;
			saveIniParam(START_TIME, var.STR_NULL + (long)(partime+TIME_MONTH), path);
		}

		if (curtime > partime + TIME_MONTH) {
			partime = curtime;
			saveIniParam(START_TIME, var.STR_NULL + (long)(partime+TIME_MONTH), path);
		}
		if (new_vers) {
			saveIniParam(VERSION_CODE, st.getAppVersionCode(this), path);
			new_vers = false;
			viewDiary();
		}
		else if (curtime > partime&& rate_app.compareToIgnoreCase("0") == 0)
			rateApp();

		st.CurLoad();
		rlv = (ListView) findViewById(R.id.main_listView);

		view();
		// показ рекламы
		Ads.count_failed_load = 0;
		Ads.show(this, 2);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		Ads.show(this, 2);
	}

	@Override
	public void onDestroy() {
		// Ads.destroy();
		inst = null;
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			st.runAct(About.class, inst);
			return true;
		case R.id.action_rate_app:
			rateGooglePlay();
			return true;
		case R.id.action_exit_app:
			finish();
			return true;
		case R.id.action_other_app:
			st.runOtherApp();
			return true;
		case R.id.action_settings:
			st.runAct(PrefActMain.class, inst);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void folderUp() {
		if (!rec.isRootFolder()) {
			rec.setFolder(rec.getBackFolder());
			view();
		}
	}

	public void onClick(View view) {
		// if (GlobDialog.gbshow){
		// return;
		// }
		if (view.getId()==R.id.main_exitapp)
			finish();
		if (!st.requestPermission(inst))
			return;
		switch (view.getId()) {
		case R.id.main_cur:
			st.runAct(Cur.class, this);
			return;
		case R.id.cur_del:
			GlobDialog gd = new GlobDialog(inst);
			gd.set(R.string.del_query, R.string.yes, R.string.no);
			gd.setObserver(new st.UniObserver() {
				@Override
				public int OnObserver(Object param1, Object param2) {
					if (((Integer) param1).intValue() == AlertDialog.BUTTON_POSITIVE) {

						rlv.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_NOTSELECT));
						String sss = "";
						int _id = 0;
						int idtv = 0;
						View v = null;
						TextView tv = null;
						;
						for (int i = 0; i < rlv.getChildCount(); i++) {

							sss = "";
							v = null;
							tv = null;
							_id = rlv.getChildAt(i).getId();
							v = rlv.getChildAt(i);
							if (_id == 1) {
								tv = (TextView) v.findViewById(R.id.rec_path);
								FolderInfo fi = rec.loadinifile(tv.getText().toString());
								File ff;
								if (fi.dir == 1) {

									rec.deleteDir(new File(rec.getCurrentFolder() + SLASH + fi.name));
									rec.delini(rec.getCurrentFolder() + SLASH + fi.inifile);
								} else {
									ff = new File(rec.getCurrentFolder() + SLASH + fi.pathtxt);
									if (ff != null && ff.exists()) {
										ff.delete();
										rec.delini(rec.getCurrentFolder() + SLASH + fi.inifile);
									}
								}
							} else {
								v.setId(0);
							}
						}
						flmain_del = false;
						visibility();
						view();
					}
					return 0;
				}
			});
			gd.showAlert();
			return;
		case R.id.main_del:
			GlobDialog gd2 = new GlobDialog(inst);
			gd2.set(R.string.del_query, R.string.yes, R.string.no);
			gd2.setObserver(new st.UniObserver() {
				@Override
				public int OnObserver(Object param1, Object param2) {
					if (((Integer) param1).intValue() == AlertDialog.BUTTON_POSITIVE) {

						rlv.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_NOTSELECT));
						String sss = "";
						int _id = 0;
						int idtv = 0;
						View v = null;
						TextView tv = null;
						;
						for (int i = 0; i < rlv.getChildCount(); i++) {

							sss = "";
							v = null;
							tv = null;
							_id = rlv.getChildAt(i).getId();
							v = rlv.getChildAt(i);
							if (_id == 1) {
								tv = (TextView) v.findViewById(R.id.rec_path);
								FolderInfo fi = rec.loadinifile(tv.getText().toString());
								File ff;
								if (fi.dir == 1) {

									rec.deleteDir(new File(rec.getCurrentFolder() + SLASH + fi.name));
									rec.delini(rec.getCurrentFolder() + SLASH + fi.inifile);
								} else {
									ff = new File(rec.getCurrentFolder() + SLASH + fi.pathtxt);
									if (ff != null && ff.exists()) {
										ff.delete();
										rec.delini(rec.getCurrentFolder() + SLASH + fi.inifile);
									}
								}
							} else {
								v.setId(0);
							}
						}
						flmain_del = false;
						visibility();
						view();
					}
					return 0;
				}
			});
			gd2.showAlert();
			return;
		case R.id.main_del_all:
			GlobDialog gd1 = new GlobDialog(inst);
			gd1.set(R.string.del_query, R.string.yes, R.string.no);
			gd1.setObserver(new st.UniObserver() {
				@Override
				public int OnObserver(Object param1, Object param2) {
					if (((Integer) param1).intValue() == AlertDialog.BUTTON_POSITIVE) {

						rlv.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_NOTSELECT));
						String sss = "";
						int _id = 0;
						int idtv = 0;
						View v = null;
						TextView tv = null;
						;
						for (int i = 0; i < rlv.getChildCount(); i++) {

							sss = "";
							v = null;
							tv = null;
							_id = rlv.getChildAt(i).getId();
							v = rlv.getChildAt(i);
							tv = (TextView) v.findViewById(R.id.rec_path);
							FolderInfo fi = rec.loadinifile(tv.getText().toString());
							File ff;
							if (fi.dir == 1) {

								rec.deleteDir(new File(rec.getCurrentFolder() + SLASH + fi.name));
								rec.delini(rec.getCurrentFolder() + SLASH + fi.inifile);
							} else {
								ff = new File(rec.getCurrentFolder() + SLASH + fi.pathtxt);
								if (ff != null && ff.exists()) {
									ff.delete();
									rec.delini(rec.getCurrentFolder() + SLASH + fi.inifile);
								}
							}
						}
						flmain_del = false;
						visibility();
						view();
					}
					return 0;
				}
			});
			gd1.showAlert();
			return;
		case R.id.main_add:
			var.file_desc = "";
			st.runAct(Newrecord.class, this);
			return;
		case R.id.main_folder_add:
			folderEdit(R.string.add_folder, "", "");
			return;
//		case R.id.main_updir:
//			folderUp();
//			return;
		case R.id.main_home:
			rec.setRootFolder();
			view();
			return;
		}
	}

	public void folderEdit(int title, final String txt, final String inifile) {
		final GlobDialog gd = new GlobDialog(inst);
		gd.set(title, R.string.ok, R.string.cancel);
		gd.setObserver(new st.UniObserver() {
			@Override
			public int OnObserver(Object param1, Object param2) {
				int ii = ((Integer) param1).intValue();
				if (((Integer) param1).intValue() == AlertDialog.BUTTON_POSITIVE) {
					if (gd.ret_edittext_text.length() > 0) {
						String sss = var.STR_NULL;
						char cc = 0;
						for (int i = 0; i < gd.ret_edittext_text.length(); i++) {
							cc = gd.ret_edittext_text.charAt(i);
							if (cc == '/')
								cc = '_';
							else if (cc == '\n')
								cc = ' ';
							sss += cc;
						}
						gd.ret_edittext_text = sss;
						File ff = new File(rec.getCurrentFolder() + SLASH + gd.ret_edittext_text);
						if (inifile.length() == 0) {
							ff.mkdir();
							rec.createParamFile(gd.ret_edittext_text.trim(), true, "");
						} else {
							FolderInfo fi = new FolderInfo();
							fi = rec.loadinifile(inifile);
							if (gd.ret_edittext_text.trim().length() == 0)
								return 0;
							String ewname = rec.getCurrentFolder() + gd.ret_edittext_text.trim();
							// File ren = new File(fi.path+SLASH+fi.name);

							File newname = new File(rec.getCurrentFolder() + SLASH + gd.ret_edittext_text.trim());
							File ren = new File(rec.getCurrentFolder() + SLASH + fi.name);
							if (ren != null) {
								ren.renameTo(newname);
								rec.modifyParamFile(inifile, gd.ret_edittext_text.trim());
							}
						}
						view();
					}
				}
				return 0;
			}
		});
		gd.showEdit(txt);

	}

	public void checkIntent() {
		String path = null;
		Intent it = getIntent();
		if (it != null && (Intent.ACTION_VIEW.equals(it.getAction()) || Intent.ACTION_EDIT.equals(it.getAction()))) {
			Uri data = it.getData();
			if (data != null) {
				String scheme = data.getScheme();
				if (ContentResolver.SCHEME_FILE.equals(scheme)) {
					path = Uri.decode(data.getEncodedPath());
				} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
					ContentResolver cr = getContentResolver();
					InputStream fi = null;
					int start = 0;
					byte buf[] = null;
					try {
						fi = cr.openInputStream(data);
						buf = new byte[fi.available()];
						fi.read(buf);
						if (buf.length > 3 && buf[0] == 0xef && buf[1] == 0xbb && buf[2] == 0xbf) {
							start = 3;
						}
						fi.close();
					} catch (IOException e) {
						st.log("error");
						st.logEx(e);
					}
					path = Newrecord.START_FILENAME_DESCRIPTOR+new String(buf, start, buf.length-start);
				} else {
					path = data.toString();
				}
			} else {
			}
		}

		//// path =data.toString();
		 if (path != null) {
			 var.filename = path;
			 st.runAct(Newrecord.class, this);
		 } else
			 var.filename = var.STR_NULL;

	}

	public boolean checkCrash() {
		// проверяем, что ведётся разработка на эмуляторе
		// и отчёт о краше выводить не надо
		if (st.isDebugEmulator())
			return false;
		try {
			String path = getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.dataDir;

			path += "/files" + st.SAVE_CRASH;
			file_crash = new File(path);
			if (!file_crash.exists())
				return false;
		} catch (Throwable e) {
			st.logEx(e);
		}
		new Dlg.RunOnYes(inst, inst.getString(R.string.error_msg)) {
			
			@Override
			public void run() {
				// File f = (File)userParam;
				if (file_crash != null) {
					Mail.sendFeedback(inst, file_crash);
					file_crash.delete();
					file_crash = null;
				}
				if (file_crash != null) {
					file_crash.delete();
					file_crash = null;
				}
			}
		};
		return true;
	}

	public void view() {
		if (flmain_del)
			rlv.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_SELECT));
		else {
			rlv.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_NOTSELECT));
		}
		String datetime = "";
		mainList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> hm;
		ArrayList<FolderInfo> ar = rec.getArrayThisFolder();
		SimpleDateFormat sdf;
		Date dt;
		FolderInfo fi;
		for (int i = 0; i < ar.size(); i++) {

			hm = new HashMap<String, Object>();
			// вывод ссылки на inifile
			hm.put("path", ar.get(i).inifile);
			String setcur = "";
			fi = ar.get(i);
			// выводим дату последнего редактирования
			datetime = "dd.MM.yyyy HH:mm:ss";
			if (fi.dateedit != 0) {
				dt = new Date();
				dt.setTime(fi.dateedit);
				sdf = new SimpleDateFormat(datetime);
				datetime = sdf.format(dt);
			} else
				datetime = "";
			hm.put(rec.DATE_EDIT, datetime);
			// выводим заголовок записи или имя папки
			if (ar.get(i).dir == 1) {
				// выводим иконку папки слева от надписи (не работает)
				// View vv = getLayoutInflater().inflate(R.layout.rec_list_item, null);
				// TextView tv = (TextView)vv.findViewById(R.id.rec_name);
				// if (tv!=null){
				// Drawable img = this.getResources().getDrawable( R.drawable.folder );
				// tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
				// }
				if (fi.name.compareToIgnoreCase(rec.BACK_FOLDER) == 0)
					// выход из папки (2 точки)
					hm.put(rec.NAME, "📂[" + ar.get(i).name.toUpperCase().trim() + "]");
				else
					// папка
					hm.put(rec.NAME, "📁[" + ar.get(i).name.toUpperCase().trim() + "]");
			} else {
				hm.put(rec.NAME, ar.get(i).name);
			}
			// выводим валюты если они есть
			if (fi.cur.size() > 0) {
				setcur = "";
				for (RecordCurrency cc : fi.cur) {
					if (cc.value != 0)
						setcur += cc.symb + ": " + String.valueOf(cc.value) + "\n";
				}
				if (setcur.trim().length() > 0)
					setcur = setcur.substring(0, setcur.length() - 1);
				hm.put(rec.CURRENCY, setcur);
			} else
				hm.put(rec.CURRENCY, "");
			// выводим дату пересчёта
			if (setcur.trim().length() > 0) {
				datetime = "dd.MM.yyyy HH:mm:ss";
				dt = new Date();
				dt.setTime(fi.date_calculate);
				sdf = new SimpleDateFormat(datetime);
				datetime = sdf.format(dt);
				hm.put(rec.DATE_CALCULATE, getString(R.string.newrec_calculate_time) + datetime);
			} else {
				hm.put(rec.DATE_CALCULATE, "");
			}

			mainList.add(hm);
		}
		int layout = getSimpleAdapter();
		if (mainList.size() > 0) {
			SimpleAdapter adapter = new SimpleAdapter(this, mainList, layout,
					new String[] { "path", rec.DATE_EDIT, rec.NAME, rec.DATE_CALCULATE, rec.CURRENCY },
					new int[] { R.id.rec_path, R.id.rec_create, R.id.rec_name, R.id.rec_recalc_date, R.id.rec_cur });
			rlv.setAdapter(adapter);
			for (int i = 0; i < rlv.getChildCount(); i++) {
				rlv.getChildAt(i).setId(0);
			}
			rlv.setOnItemClickListener(itemClickListener);
			rlv.setOnItemLongClickListener(itemLongClickListener);
			rlv.setOnKeyListener(m_keyListener);
		} else {
			rlv.setAdapter(null);
		}
		setPathTextView();
	}

	View.OnKeyListener m_keyListener = new View.OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (GlobDialog.gbshow) {
					GlobDialog.inst.finish();
					return true;
				}
				return false;
			}
			return false;
		}
	};

	AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			if (flmain_del == false) {
				TextView tv = (TextView) v.findViewById(R.id.rec_path);
				String txt = tv.getText().toString();
				if (txt.length() == 0) {
					txt = rec.getBackFolder();
				}
				if (txt.endsWith(rec.SUFINI) == false) {
					rec.setFolder(txt);
					view();
				} else {
					FolderInfo fi = rec.loadinifile(txt);
					if (fi.dir == 1) {
						rec.setFolder(rec.getCurrentFolder() + SLASH + (fi.name).trim());
					} else {
						runRecEdit(txt);
					}
				}
				view();
			} else {
				if (v.getId() > 0) {
					v.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_NOTSELECT));
					v.setId(0);
				} else {
					setItemIdAndColor(v);
				}
			}
		}
	};
	AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, final View v, int position, long id) {
			TextView tv = (TextView) v.findViewById(R.id.rec_path);
			String txt = tv.getText().toString();
			fiii = new FolderInfo();
			boolean flag = false;
			if (txt.endsWith(rec.SUFINI)) {
				fiii = rec.loadinifile(txt);
				flag = true;
			}
			if (!flmain_del) {
				if (flag && fiii.dir == 1) {
					final GlobDialog gd = new GlobDialog(inst);
					gd.setList(R.string.selection, 0, 0, R.string.cancel, R.string.folder0, R.string.folder1);
					gd.setObserver(new st.UniObserver() {
						@Override
						public int OnObserver(Object param1, Object param2) {
							switch (gd.position) {
							case 0:
								// выводим редактирование имени папки
								// при этом на экране уже есть один GlobDialog,
								// поэтому обязательно return 1,
								// чтобы закрылось предыдущее окно GlobDialog
								folderEdit(R.string.edit_folder, fiii.name, fiii.inifile);
								return 1;
							case 1:
								flmain_del = true;
								visibility();
								setItemIdAndColor(v);
								break;
							}
							// if (gd.position == 0){
							// folderEdit(R.string.edit_folder,fiii.name,fiii.inifile);
							// }
							// if (gd.position == 1){
							// flmain_del = true;
							// visibility();
							// setItemIdAndColor(v);
							// }
							gd.position = -1;
							return 0;
						}
					});
					gd.showList();
				} else {
					flmain_del = true;
					visibility();
					setItemIdAndColor(v);

				}
			} else {
				flmain_del = true;
			}
			return true;

		}
	};

	public void runRecEdit(String _path) {
		var.file_desc = _path;
		st.runAct(Newrecord.class, this);
	}

	public int getSimpleAdapter() {
		int lay = R.layout.rec_list_item_light;
		switch (var.skin_app) {
		case 1:
			lay = R.layout.rec_list_item_black;
			break;
		}
		return lay;
	}

	// запись параметров в par.ini
	// pth - путь и имя файла, par -имя параметра, par_value - значение параметра,
	public static void saveIniParam(String par, String par_value, String pth) {
		try {
			String out = "";
			String param = "";
			String param_value = "";
			String par_string = "";
			FileReader fr = new FileReader(pth);
			Scanner sc = new Scanner(fr);
			sc.useLocale(Locale.US);
			boolean fl = false;
			while (sc.hasNextLine()) {
				par_string = sc.nextLine();
				if (par_string.length() != 0) {
					param = par_string.substring(0, par_string.indexOf("="));
					param_value = par_string.substring(par_string.indexOf("=") + 1);
					if (param.contains(par)) {
						out += par + "=" + par_value + "\n";
						fl = true;
					} else {
						out += par_string + "\n";
					}
				}
			}
			sc.close();
			if (fl != true)
				out += par + "=" + par_value + "\n";
			FileWriter writer = new FileWriter(pth, false);
			writer.write(out);
			writer.close();
			st.sleep(10);
		} catch (IOException ex) {
		}
	}

	public void setItemIdAndColor(View v) {
		v.setBackgroundColor(th.getValue(th.MAIN_LISTVIEW_SELECT));
		v.setId(1);
	}

	@Override
	public void onBackPressed() {
		// не обрабатывает при открытом диалоге листа. Исправить
		// if (GlobDialog.gbshow){
		// GlobDialog.inst.finish();
		// return;
		// }
		if (flmain_del) {
			flmain_del = false;
			visibility();
			view();
			return;
		} else {
			if (rec.isRootFolder()) {
				finish();
			} else {
				folderUp();
			}
		}

		// super.onBackPressed();
	}

	public void visibility() {
		int vis = View.VISIBLE;
		if (flmain_del) {
			vis = View.GONE;
		} else {
			vis = View.VISIBLE;
		}
		Button btn;
		for (int i = 0; i < toppanel.getChildCount(); i++) {
			btn = (Button) toppanel.getChildAt(i);
			btn.setVisibility(vis);
		}
		if (flmain_del) {
			add.setVisibility(View.GONE);
			del.setVisibility(View.VISIBLE);
			del_all.setVisibility(View.VISIBLE);
		} else {
			add.setVisibility(View.VISIBLE);
			del.setVisibility(View.GONE);
			del_all.setVisibility(View.GONE);
		}

	}

	public void viewDiary() {
		Dlg.helpDialog(inst, st.getAssetsFile(inst, "_diary_MWcosts.txt"));
		//st.help(st.getAssetsFile(inst, "_diary_MWcosts.txt"), inst);
	}

	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	// просьба оценить приложение
	public void rateApp() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(getString(R.string.rate_title)); // Set title

		alertDialogBuilder
				// Set dialog message
				.setMessage(getString(R.string.rate_about)).setCancelable(false)
				.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						saveIniParam(START_TIME,var.STR_NULL+((long)(curtime+TIME_NEGATIVE_CLICK)),path);
						saveIniParam(RATE_APP, "0", path);
						//saveIniParam(START_TIME, var.STR_NULL + (0+TIME_NEGATIVE_CLICK), path);
						dialog.cancel();
					}
				}).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						rateGooglePlay();
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create(); // Create alert dialog
		alertDialog.show(); // Show alert dialog
	}
	public void rateGooglePlay() 
	{
		saveIniParam(RATE_APP, "1", path);
		saveIniParam(START_TIME, var.STR_NULL + (long)(curtime + (TIME_MONTH / 2)), path);
		startGooglePlayPage();
	}
	public void startGooglePlayPage() {
		Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()); 
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		if (goToMarket.resolveActivity(getPackageManager()) != null) {
			startActivity(goToMarket);
		}

	}

	// пока не работает
	public void kbdHide() {
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(button.getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@SuppressLint("NewApi")
	public void setPathTextView() {
		if (ll_path == null)
			return;
		String root_folder = rec.getRootFolder();
		String cur = (rec.getCurrentFolder()).substring(root_folder.length());
		String[] elem = cur.split(SLASH);
		ll_path.removeAllViews();
		if (ar_path.size() > 0)
			ar_path.clear();
		int ff = elem.length;

		for (int i = 0; i < elem.length; i++) {
			if (elem.length == 1 || elem[i].isEmpty())
				continue;
			ArrayPath ar = new ArrayPath();
			cur = root_folder;
			for (int ii = 0; ii <= i; ii++) {
				if (!elem[ii].isEmpty())
					cur += SLASH + elem[ii];
			}
			ar.path = cur;
			ar.id = i;
			ar_path.add(ar);
			ll_path.addView(createTextViewPath(SLASH + elem[i], i));
		}
		if (ll_path.getChildCount() == 0)
			ll_path.addView(createTextViewPath(inst.getString(R.string.home_folder), 1));

		ll_path.measure(0, 0);
		hsv_path.post(new Runnable() {

			@Override
			public void run() {
				hsv_path.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
			}
		});
	}

	public TextView createTextViewPath(String txt, int id) {
		TextView tv = new TextView(inst);
		tv.setText(txt);
		tv.setId(id);
		tv.setBackgroundColor(th.getValue(th.MAIN_TOP_PANEL_BACK));
		tv.setTextColor(th.getValue(th.MAIN_BUTTON_TEXT_TOP_PANEL));
		tv.setTextSize(18);
		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = v.getId();
				ArrayPath ar = inst.getArrayById(id);
				if (ar == null)
					return;
				String sss = ar.path;
				rec.setFolder(sss);
				view();
			}
		});
		return tv;
	}

	public ArrayPath getArrayById(int id) {

		for (int i = 0; i < ar_path.size(); i++) {
			ArrayPath ap = ar_path.get(i);
			if (id == ar_path.get(i).id)
				return ar_path.get(i);
		}
		return null;
	}
	@TargetApi(Build.VERSION_CODES.M)
	@Override
    public void onRequestPermissionsResult(int requestCode, String[] perm, int[] grantResults ) {
    	super.onRequestPermissionsResult(requestCode, perm, grantResults);
        //Log.d( TAG, "Permissions granted: " + permissions.toString() + " " + grantResults.toString() );
		ArrayList<String> al = new ArrayList<String>();
    	for (int i=0;i<grantResults.length;i++) {
    		if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
    			al.add(perm[i]);
    	}
    	if (al.size()>0) {
    		if (Perm.checkPermission(inst)) {
    			inst.recreate();
    			return;
    		}
        	String[] ss = new String[al.size()];
        	ss =al.toArray(ss);
        	Perm.postRequestPermission(inst,ss);
    	}
	}
}