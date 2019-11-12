package com.mwcorp.costs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.mwcorp.costs.var.ArrayToolbar;
import com.mwcorp.costs.var.FolderInfo;
import com.mwcorp.costs.var.RecordCurrency;
import com.mwcorp.dialog.Dlg;
import com.mwcorp.tools.GlobDialog;
import com.mwcorp.tools.Pref;
import com.mwcorp.tools.SameThreadTimer;
import com.mwcorp.tools.UtilText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
// поиск оставить таким как есть, 
// если сделать поиск следующего искомого по 
// нажатию ентера, то тогда не видно курсора в тексте

@SuppressLint("NewApi")
public class Newrecord extends Activity {
	// под команды зарезервировано 300 кодов
	final int ACTION_HOME = 501;
	final int ACTION_END = 502;
	final int ACTION_SEARCH = 503;
	final int ACTION_PGDN = 504;
	final int ACTION_PGUP = 505;
	final int ACTION_CLOSE = 506;
	final int ACTION_SAVE_EXIT = 507;
	final int ACTION_RUN_SETTING = 508;
	final int ACTION_SAVED = 509;
	final int ACTION_RECALC = 510;
	final int ACTION_TOSTART = 511;
	final int ACTION_TOEND = 512;
	final int ACTION_ARROW_LEFT = 513;
	final int ACTION_ARROW_RIGHT = 514;
	final int ACTION_ARROW_UP = 515;
	final int ACTION_ARROW_DOWN = 516;
	final int ACTION_DEL = 517;
	final int ACTION_SEL_FUNCTION = 518;
	final int ACTION_RUN_SETTING_TOOLBAR = 519;
	final int ACTION_COPY = 520;
	final int ACTION_PASTE = 521;
	final int ACTION_SHOW_KBD = 522;
	final int ACTION_HIDE_KBD = 523;
	final int ACTION_EXIT_APP = 524;
	
	/** константа для обнуления (0/0) tv в панели поиска при получении этого значения */
	public static int MAX_SEARCH_COUNT = -10000000;
	public static String START_FILENAME_DESCRIPTOR = "URI_TEXT_FILE://" + st.STR_lF;
	static Newrecord inst;

	public String settext = st.STR_NULL;
	boolean fl_exit_app = false;
	boolean fl_select = false;
	static boolean fl_temp_edit = true;
	int selStartPos = -1;
	int selEndPos = -1;
	ScrollView sv;
	HorizontalScrollView hsv;
	ArrayList<Integer> arpos = new ArrayList<Integer>();
	int pos_search = 0;

	int tb_btn_height = -1;
	int tb_row = -1;
	String selection = var.STR_NULL;
	ProgressBar load_progress = null;
	RelativeLayout toppanel;
	RelativeLayout searchpanel;
	TableLayout toolbar;
	RelativeLayout etpanel;
	View v;
	EditText et = null;
	EditText et_search = null;
	TextView tv_search = null;
	TextView tv_changeg = null;
	TextView tv_readonly = null;
	TextView tv_suminbase = null;
	ImageView im_edit = null;

	SameThreadTimer m_tm;

	boolean fl_editfile = false;
	boolean fl_changed = false;
	boolean fl_recalc = false;
	boolean fl_savedialog = false;
	boolean fl_changedofsearch = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		inst = this;
		th.setTheme(inst);
		// android.R.style.theme
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newrecord);
		tb_btn_height = -1;
		var.recordCur.clear();
		tv_search = null;
		et = null;
		et_search = null;
		searchpanel = null;
		toppanel = null;
		sv = null;
		v = getLayoutInflater().inflate(R.layout.newrecord, null);
        v.setBackgroundColor(th.getValue(th.NEWREC_EDITTEXT_BACK));
		toppanel = (RelativeLayout) v.findViewById(R.id.newrec_toppanel);
		toppanel.setBackgroundColor(th.getValue(th.NEWREC_TOOLBAR_BACK));
		tv_changeg = (TextView) toppanel.findViewById(R.id.newrec_changed);
		tv_readonly = (TextView) toppanel.findViewById(R.id.newrec_readonly);
		searchpanel = (RelativeLayout) v.findViewById(R.id.newrec_llsearch_panel);
		etpanel = (RelativeLayout) v.findViewById(R.id.newrec_llet);
		im_edit = (ImageView) v.findViewById(R.id.newrec_tb2_image_edit);
		im_edit.setBackgroundResource(th.getValue(th.NEWREC_TOOLBAR_BACK_RES_ROUND_BUTTON));
		im_edit.setVisibility(View.GONE);
        load_progress = (ProgressBar) v.findViewById(R.id.newrec_load_progress);
        load_progress.setBackgroundColor(th.getValue(th.NEWREC_EDITTEXT_BACK));
		//load_progress.setTextColor(th.getValue(th.NEWREC_EDITTEXT_TEXT));
        load_progress.setVisibility(View.GONE);
		tv_suminbase = (TextView) v.findViewById(R.id.newrec_suminbase);
		tv_suminbase.setTextColor(th.getValue(th.NEWREC_TOPPANEL_SUM_TEXT));
		tv_suminbase.setTextSize(12);
		if (var.fl_newrec_big_size_sum) {
			tv_suminbase.setTextSize(18);
		}
		TextView tv = (TextView) v.findViewById(R.id.newrec_changed);
		tv.setTextColor(th.getValue(th.NEWREC_TOPPANEL_SUM_TEXT));
		tv = (TextView) v.findViewById(R.id.newrec_readonly);
		tv.setTextColor(th.getValue(th.NEWREC_TOPPANEL_SUM_TEXT));
		et = (EditText) v.findViewById(R.id.newrec_edit);
		et.setBackgroundColor(th.getValue(th.NEWREC_EDITTEXT_BACK));
		et.setTextColor(th.getValue(th.NEWREC_EDITTEXT_TEXT));
		et.setTextSize(var.newrec_size_text);
		et.addTextChangedListener(tw);
		fl_exit_app = false;
		et.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				int act = event.getAction();
				switch (act)
				{
				case MotionEvent.ACTION_DOWN:
					if (var.fl_view_mode 
							&& fl_temp_edit 
							&& var.filename.length()>0
							) {
						startTimerToolbar2();
						if (im_edit != null) {
							if (im_edit.getVisibility() == View.VISIBLE)
								im_edit.setVisibility(View.GONE);
							im_edit.setVisibility(View.VISIBLE);
						}
					}
					break;
				}
				return false;
			}
		});

		et.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (searchpanel != null && searchpanel.getVisibility() == View.VISIBLE && toolbar != null
						&& toolbar.getVisibility() == View.VISIBLE) {
					showToolbarPanel(false);
				}
			}
		});
		et.setOnKeyListener(et_onKeyListener);
		sv = (ScrollView) v.findViewById(R.id.newrec_scroll);

		if (var.fl_newrec_fullscreen)
			this.getActionBar().hide();
		else
			this.getActionBar().show();
		// if (sv==null){
		// et.setMinLines(50);
		// } else {
		// sv.measure(0, 0);
		// et.setMinimumHeight(sv.getMeasuredHeight());
		// }
		fl_editfile = false;
		fl_changed = false;
		fl_recalc = false;
		// ******************
		// если блок закоментить, то режим просмотра пашет правильно,
		// но только со второго открытия файла!
		fl_temp_edit = false;
		if ((var.filename.length()>0)
			&&var.file_desc.isEmpty()
			&&var.fl_view_mode)
		{
				fl_temp_edit = true;
		}
		// ******************
		settext = st.STR_NULL;
		// открываем внешний файл
		if (var.filename.length() > 0) {
			setEditable();
			File ff = new File(var.filename);
			if (!ff.exists()|!ff.isFile()) {
				st.toast(R.string.toast_not_open_file);
				return;
			}
			if (var.newrec_open_size_text!=0&&ff.length() >= var.newrec_open_size_text*st.KB) {
				openFileExternal(ff);
			} else {
				settext = getFileText(var.filename,0);
				setTextInBackground(settext);
				fl_changed = false;
				var.auto_calculate = false;
				fl_editfile = true;
			}
		// открываем запись
		} else if (var.file_desc.length() > 0) {
			// this.setTitle(getString(R.string.edit_record));
			FolderInfo fi = rec.loadinifile(var.file_desc);
			settext = st.getFileString(fi.pathtxt);
			setTextInBackground(settext);

		} else {
			var.recordCur.clear();
		}
		// время видимости круглой кнопки edit
		hsv = (HorizontalScrollView) v.findViewById(R.id.newrec_hscroll);
		toolbar = (TableLayout) v.findViewById(R.id.newrec_bottompanel);
		if (var.fl_newrec_show_toolbar) {
			if (toolbar != null) {
				toolbar.setVisibility(View.VISIBLE);
				toolbar.setBackgroundColor(th.getValue(th.NEWREC_TOOLBAR_BACK));
			}
			if (hsv != null)
				hsv.setBackgroundColor(th.getValue(th.NEWREC_TOOLBAR_BACK));
			hsv.setHorizontalScrollBarEnabled(false);
			createToolbar();
		} else
			toolbar.setVisibility(View.GONE);
		setMinLinesEditText(et);
		// tv_changeg.setText("");
		if (var.auto_calculate)
			recalc();
		searchPanelHide();
		viewTopPanel();
		setContentView(v);
		fl_savedialog = false;
		fl_select = false;
		fl_changed = false;
//		fl_temp_edit = false;
//		if ((var.filename.length()>0)
//			&&var.file_desc.isEmpty()
//			&&var.fl_view_mode)
//				fl_temp_edit = true;
		startTimerToolbar2();
		setChangegText();
	}
	// конец onCreate

	/** запрос на чтение внешнего файла, если он большого размера */
	public void openFileExternal(final File ff)
	{
		String[] ar = new String[3];
		ar[0] = inst.getString(R.string.newrec_openfile_first)
				+st.STR_SPACE+var.newrec_open_size_text
				+"kb ("+inst.getString(R.string.only_view)+")";
		ar[1] = inst.getString(R.string.newrec_openfile_first)
				+st.STR_SPACE+(var.newrec_open_size_text*2)
				+"kb ("+inst.getString(R.string.only_view)+")";
		ar[2] = inst.getString(R.string.newrec_openfile_full);
		
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.dlg_list,
				ar);

		Dlg.CustomMenu(this, adapt, getString(R.string.newrec_openfile_title), 
				new st.UniObserver() {
			@Override
			public int OnObserver(Object param1, Object param2) {
				int size = (int) ff.length();
				switch (((Integer) param1).intValue()) 
				{
				case 0:
					if (size >= st.newrec_open_size_text*st.KB)
						size =  st.newrec_open_size_text*st.KB;
					settext = getFileText(ff.getAbsolutePath(),size);
					setTextInBackground(settext);
					fl_temp_edit = false;
					st.fl_view_mode = true;
					searchPanelHide();
					viewTopPanel();
					setContentView(v);
					fl_savedialog = false;
					fl_select = false;
					fl_changed = false;
					startTimerToolbar2();
					setChangegText();
					break;
				case 1:
					if (size >= st.newrec_open_size_text*st.KB*2)
						size =  st.newrec_open_size_text*st.KB*2;
					settext = getFileText(ff.getAbsolutePath(),size);
					setTextInBackground(settext);
					fl_temp_edit = false;
					st.fl_view_mode = true;
					searchPanelHide();
					viewTopPanel();
					setContentView(v);
					fl_savedialog = false;
					fl_select = false;
					fl_changed = false;
					startTimerToolbar2();
					setChangegText();
					
					break;
				default:
					settext = getFileText(ff.getAbsolutePath(),0);
					if (settext!=null)
						setTextInBackground(settext);
					break;
				}
				return 0;
			}
		});
		
	}
	/** записываем текст в et в фоне */
	public void setTextInBackground(final String txt)
	{
		if (txt.length() < 20000) {
			et.setText(txt);
			return;
		}
    	if (load_progress!=null)
  			load_progress.setVisibility(View.VISIBLE);
	    new Handler().postDelayed(new Runnable() {
	    	public void run() {
	    		et.setText(txt);
	        	if (load_progress!=null) {
	      			load_progress.setVisibility(View.GONE);
	      			fl_changed = false;
	      			setChangegText();
	        	}
	        }
	    // если времени указать мало, то load_progress не крутится
	    }, 1000);
		
	}
	TextWatcher tw = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			fl_changed = true;
			setChangegText();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (var.auto_calculate)
				recalc();
		}
	};

	@Override
	protected void onDestroy() {
		// inst = null;
		super.onDestroy();
	}

	public void saved() {
		save(var.file_desc);
		fl_changed = false;
		setChangegText();
	}

	public void saveEndExit() {
		saved();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_newrecord, menu);
		if (var.auto_calculate)
			menu.findItem(R.id.menunrec_auto_counted)
					.setTitle(this.getString(R.string.menunrec_auto_calculate) + " " + this.getString(R.string.on));
		else
			menu.findItem(R.id.menunrec_auto_counted)
					.setTitle(this.getString(R.string.menunrec_auto_calculate) + " " + this.getString(R.string.off));
		return true;
	}

	// обработка пунктов меню
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (item.getItemId()) {
		case R.id.menunrec_save:
			if (var.filename.length() == 0)
				saved();
			else if (var.filename != null && var.filename.length() > 0) {
				savefile();
			}
			return true;
		case R.id.menurec_settings:
			if (var.filename.length() > 0) {
				st.toast(R.string.toast_msg8);
				return true;
			}
			runAction(ACTION_RUN_SETTING);
			return true;
		case R.id.menunrec_counted:
			runAction(ACTION_RECALC);
			return true;
		case R.id.menunrec_close:
			runAction(ACTION_CLOSE);
			return true;
		case R.id.menunrec_auto_counted:
			if (var.auto_calculate) {
				item.setTitle(getString(R.string.menunrec_auto_calculate) + " " + getString(R.string.off));
				var.auto_calculate = false;
				Pref.get().edit().putBoolean(Pref.PR_AUTOCALC, false).commit();
			} else {
				item.setTitle(getString(R.string.menunrec_auto_calculate) + " " + getString(R.string.on));
				var.auto_calculate = true;
				Pref.get().edit().putBoolean(Pref.PR_AUTOCALC, true).commit();
				recalc();
			}
			return true;
		case R.id.menunrec_search:
			runAction(this.ACTION_SEARCH);
			return true;
		case R.id.menunrec_help:
			st.help(R.string.menunrec_help_desc, inst);
//			GlobDialog gd = new GlobDialog(inst);
//			gd.setGravityText(Gravity.LEFT | Gravity.TOP);
//			gd.set(R.string.menunrec_help_desc, R.string.ok, 0);
//			gd.setObserver(new st.UniObserver() {
//				@Override
//				public int OnObserver(Object param1, Object param2) {
//					if (((Integer) param1).intValue() == AlertDialog.BUTTON_POSITIVE) {
//						// finish();
//					}
//					return 0;
//				}
//			});
//			gd.showAlert();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void close() {
		fl_changed = false;
		if (var.filename.length() > 0)
			exitApp(true);
		else
			finish();
	}

	public void save(String path) {
		FileWriter wr;
		try {
			String sss = rec.getRndNameEmpty(rec.SUFTXT);
			if (path.length() == 0) {
				wr = new FileWriter(rec.getCurrentFolder() + "/" + sss, false);
			} else {
				wr = new FileWriter(rec.getCurrentFolder() + "/" + rec.loadinifile(path).pathtxt, false);
			}
			String frt = et.getText().toString();

			wr.write(et.getText().toString());
			wr.close();
			if (path.length() == 0) {
				rec.createParamFile(frt, false, sss);
				var.file_desc = var.create_inifile;
				var.create_inifile = "";
			} else
				rec.modifyParamFile(path, frt);

			// if (var.file_desc.length() > 0)
			// var.file_desc = "";
			MainActivity.inst.view();
		} catch (IOException e) {
		}

		// st.toast("сохранено");
	}
/** сохраняем только ini файл записи */
	public boolean saveIni(String fname) {
		if (fname.length() == 0) 
			return false;
		String frt = et.getText().toString();
		rec.modifyParamFile(fname, frt);
		return true;
	}

	public void savefile() {
		if (var.fl_view_mode)
			return;
		if (var.filename.length() == 0)
			return;
		if (var.filename.startsWith(START_FILENAME_DESCRIPTOR)) {
			st.toast(R.string.toast_error_saved);
			return;
		}
		FileWriter wr;
		try {
			wr = new FileWriter(var.filename, false);
			wr.write(et.getText().toString());
			wr.close();
			fl_changed = false;
			setChangegText();
		} catch (IOException e) {
		}

		// st.toast("сохранено");
	}

	@Override
	public void onBackPressed() {
		// if (GlobDialog.gbshow){
		// GlobDialog.inst.finish();
		// return;
		// }

		if (searchpanel != null && searchpanel.getVisibility() == View.VISIBLE) {
			searchPanelHide();
			return;
		}
		if (fl_recalc&&!fl_changed) {
			if (var.filename.length() == 0) {
				if (saveIni(var.file_desc)) {
					finish();
					MainActivity.inst.view();
//					return;
				}
			} 
		}
		if (fl_changed) {
			if (var.filename.length()>0&&var.fl_view_mode) {
				exitApp(true);
				return;
			}
			if (fl_savedialog)
				return;
			// if (GlobDialog.gbshow)
			// return;
			if (inst == null)
				finish();
			GlobDialog gd = new GlobDialog(inst);
			gd.set(R.string.toast_msg3, R.string.yes, R.string.no, R.string.cancel);
			gd.setObserver(new st.UniObserver() {
				@Override
				public int OnObserver(Object param1, Object param2) {
					fl_savedialog = true;
					// boolean bfl = var.filename.length()==0;
					int clc = ((Integer) param1).intValue();
					switch (clc) {
					case AlertDialog.BUTTON_POSITIVE:
						boolean fl = false;
						if (var.filename.length() == 0) {
							save(var.file_desc);
							if (fl_exit_app)
								exitApp(true);
						} else {
							savefile();
							var.filename = var.STR_NULL;
							fl = true;
						}
						if (fl) {
							exitApp(true);
						} else
							exitApp(false);
						break;
					case AlertDialog.BUTTON_NEGATIVE:
						if (var.filename.length() > 0) {
							exitApp(true);
						} else {
							if (fl_exit_app)
								exitApp(true);
							else
								exitApp(false);
						}
						break;
					case AlertDialog.BUTTON_NEUTRAL:
						fl_savedialog = false;
						break;
					}
					return 0;
				}
			});
			gd.showAlert();
		} else {
			if (var.filename.length() > 0)
				exitApp(true);
			else if (fl_exit_app)
				exitApp(true);
			else
				exitApp(false);
			fl_temp_edit = false;
			// super.onBackPressed();
		}
		// var.filename = var.STR_NULL;
	}

	public void viewTopPanel() {
    	//TextView tv_suminbase = (TextView)toppanel.findViewById(R.id.newrec_suminbase);
		String txt = "";
		if (var.filename.length() == 0) {
			for (RecordCurrency cc : var.recordCur) {
				txt += cc.symb + ": " + String.valueOf(cc.value) + st.STR_lF;
			}
			if (txt.length() > 0)
				txt = txt.substring(0, txt.length() - 1);
			tv_suminbase.setText(txt);
			// if (fl_changed)
			// tv_changeg.setText("*");
			// else
			// tv_changeg.setText("");
		} else {
			if (var.filename.startsWith(START_FILENAME_DESCRIPTOR))
				tv_suminbase.setText("UNTITLED");
			else
				tv_suminbase.setText(getString(R.string.edit_file) + var.STR_lF + var.filename);
		}
		setChangegText();
	}

	public void recalc() {
		if (var.filename.length() > 0) {
			return;
		}

		an.analizeText(et.getText().toString());
		fl_recalc = true;
		setChangegText();
		// if (tv_changeg!=null)
		// tv_changeg.setText("*");
		viewTopPanel();
	}

	public void search() {
		fl_changedofsearch = fl_changed;
		if (et.isSelected())
			st.toast("selected");
		String txts = et_search.getText().toString().toLowerCase().trim();
		String ettxt = et.getText().toString().toLowerCase();
		String subtxt = ettxt;
		arpos.clear();
		;
		if (txts.length() == 0) {
			et.setText(ettxt);
			searchViewPos(MAX_SEARCH_COUNT);
			return;
		}
		int pos = -1;
		int pos1 = 0;
		boolean fl = true;
		while (fl) {
			pos = subtxt.indexOf(txts);
			if (pos != -1) {
				pos = pos + pos1;
				arpos.add(pos);
				if (pos <= ettxt.length()) {
					subtxt = ettxt.substring(pos + txts.length());
				} else {
					break;
				}
				if (pos1 == 0)
					pos1 = pos;
				else
					pos1 = pos + txts.length();
				continue;
			}
			fl = false;
		}
		// !!!
		if (arpos.size() > 1) {
			arpos.remove(1);
		}
		if (arpos.size() > 0) {
			Spannable text = new SpannableString(ettxt);
			for (int i = 0; i < arpos.size(); i++) {
				text.setSpan(new BackgroundColorSpan(0x88ff8c00), arpos.get(i), arpos.get(i) + txts.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			// подчёркивание
			// text.setSpan(new UnderlineSpan(), 8, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// цвет текста
			// text.setSpan(new ForegroundColorSpan(Color.GREEN), 2, 3,
			// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// Set spannable text in TextView.
			// размер текста
			// spanText.setSpan(new RelativeSizeSpan(1.5f), String.valueOf(firstString)
			// .length(), totalString.length(), 0);

			et.setText(text);
			//setTextInBackground(getFileText(text.toString()));

			et.requestFocus();
			et.setSelection(arpos.get(0).intValue());
			pos_search = 0;
		} else {
			et.requestFocus();
			pos_search = -1;
		}
		et.setCursorVisible(true);
		searchViewPos(0);
		if (!fl_changedofsearch)
			fl_changed = false;
	}

	public void showToolbarPanel(boolean visible) {
		if (var.fl_newrec_show_toolbar) {
			if (toolbar == null)
				return;
			if (visible)
				toolbar.setVisibility(View.VISIBLE);
			else
				toolbar.setVisibility(View.GONE);
		}
	}

	public void searchPanelShow() {
		if (searchpanel == null)
			return;
		showToolbarPanel(false);
		searchpanel.setVisibility(View.VISIBLE);
		tv_search = (TextView) searchpanel.findViewById(R.id.newrec_search_result);
		Button btn = (Button) searchpanel.findViewById(R.id.newrec_search_start);
		et_search = (EditText) searchpanel.findViewById(R.id.newrec_search_edit);
		et_search.setTextColor(Color.BLACK);
		et_search.setOnKeyListener(etsearch_onKeyListener);
		et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					et_search.setBackgroundResource(R.drawable.edittext_back_focus_style);
					showToolbarPanel(false);
				} else {
					et_search.setBackgroundResource(R.drawable.edittext_back_notfocus_style);
					showToolbarPanel(true);
				}
			}
		});
		st.showKbd(et_search,true);
		if (et_search.getText().toString().length() > 0) {
			search();
		}
	}

	View.OnKeyListener etsearch_onKeyListener = new View.OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
				search();
				st.hideKbd(inst);
				showToolbarPanel(true);
				return true;
			}
			return false;
		}
	};
	View.OnKeyListener et_onKeyListener = new View.OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_UP && var.auto_calculate) {
				recalc();
			}
			// включено выделение (пока не работает)
			// if (fl_select){
			// switch (keyCode)
			// {
			// case KeyEvent.KEYCODE_DPAD_UP:
			// case KeyEvent.KEYCODE_DPAD_DOWN:
			// case KeyEvent.KEYCODE_DPAD_LEFT:
			// case KeyEvent.KEYCODE_DPAD_RIGHT:
			// int start = et.getSelectionStart();
			// int end = et.getSelectionEnd();
			// selEndPos = et.getSelectionEnd();
			// et.setSelection(Math.min(selStartPos,selEndPos),
			// Math.max(selStartPos,selEndPos));
			// st.toast("start "+et.getSelectionStart()
			// +"\nend "+et.getSelectionEnd()
			// );
			// return false;
			// }
			// }
			return false;
		}
	};

	public void searchPanelHide() {
		showToolbarPanel(true);
		fl_changedofsearch = fl_changed;
		int pos = et.getSelectionStart();
		et.setText(et.getText().toString());
		et.setSelection(pos);
		if (searchpanel == null)
			return;
		searchpanel.setVisibility(View.GONE);
		et_search = null;
		setEditable();

		if (fl_changedofsearch == false)
			fl_changed = false;
		setChangegText();
	}

	// устанавливает режим readonly
	public void setEditable() {
		if (var.fl_view_mode 
				&&!var.filename.trim().isEmpty()
				&&var.file_desc.isEmpty()
				&&fl_temp_edit) {
			et.setTextIsSelectable(true);
			et.setCursorVisible(false);
			startTimerToolbar2();
			im_edit.setVisibility(View.VISIBLE);
		}
	}
	/** показывать или нет "звёздочку" */
	public void setChangegText() {
		if (tv_changeg == null)
			return;
		if (fl_changed)
			tv_changeg.setText("*");
		else
			tv_changeg.setText(var.STR_NULL);
		if (tv_readonly == null)
			return;
		if (var.fl_view_mode && !var.filename.trim().isEmpty() && !fl_temp_edit)
			tv_readonly.setVisibility(View.VISIBLE);
		else
			tv_readonly.setVisibility(View.GONE);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.newrec_suminbase:
			if (tv_suminbase==null)
				return;
			if (!var.filename.isEmpty()&&!var.fl_view_mode)
				return;
			String out = tv_suminbase.getText().toString();
			out = out.replaceAll(st.STR_lF, ", ");
			out +=".";
			insertStr(out);
			return;
		case R.id.newrec_tb2_image_edit:
			//recreate();
			fl_temp_edit = true;
			setEditable();
			fl_changed = false;
			return;
		case R.id.newrec_search_close:
			searchPanelHide();
			return;
		case R.id.newrec_search_start:
			searchViewPos(0);
			return;
		case R.id.newrec_search_down:
			searchViewPos(1);
			return;
		case R.id.newrec_search_up:
			searchViewPos(-1);
			return;
		}
	}

	public void searchViewPos(int pos) {
		if (tv_search == null)
			return;
		if (arpos.size() == 0) {
			tv_search.setText("[0/0]");
			return;
		}
		if (pos == MAX_SEARCH_COUNT) {
			tv_search.setText("[0/0]");
			return;
		}
		else if (arpos.size() == 0) {
			tv_search.setText("[0/0]");
			return;
		}
		else if (pos == 0) {
			pos_search = 0;
		} 
		else if (pos == 1) {
			pos_search++;
			if (pos_search >= arpos.size())
				pos_search = 0;
			// pos_search = arpos.size()-1;
		} 
		else if (pos == -1) {
			pos_search--;
			if (pos_search < 0)
				pos_search = arpos.size() - 1;
			// pos_search = 0;
		}
		tv_search.setText("[" + (pos_search + 1) + "/" + arpos.size() + "]");
		et.requestFocus();
		et.setSelection(arpos.get(pos_search).intValue());
	}

	View.OnClickListener action_clkListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			runAction(view.getId());
		}
	};

	public void createToolbar() {
		if (var.ar_tb.size() == 0)
			return;
		if (toolbar == null)
			return;
		if (toolbar.getChildCount() != 0)
			toolbar.removeAllViews();
		if (tb_row == -1) {
			if (st.getOrientation(this) == Configuration.ORIENTATION_PORTRAIT)
				tb_row = var.tb_line_port;
			else
				tb_row = var.tb_line_land;
		}
		TableRow tr = null;
		// кнопок в ряду
		int btnrow = 1;
		// сколько ещё кнопок нужно добавить
		int ost = 0;
		// ********************
		// вывод слева направо
		if (var.tb_layout_type == 1) {
			btnrow = var.ar_tb.size() / tb_row;
			ost = var.ar_tb.size() - (tb_row * btnrow);
			tr = new TableRow(this);
			int cnt = 0;
			// счётчик рядов
			int tr_cnt = 1;
			int cnt_additional = ost;
			do {
				tr.addView(createbtn(this.getActionButtonStr(var.ar_tb.get(cnt)), var.ar_tb.get(cnt).id));
				cnt++;
				if (cnt == (tr_cnt * btnrow)) {
					if (ost >= tr_cnt) {
						tr.addView(createbtn(this.getActionButtonStr(var.ar_tb.get(cnt)), var.ar_tb.get(cnt).id));
						cnt++;
						cnt_additional--;
					}
					toolbar.addView(tr);
					if (tr_cnt < tb_row) {
						tr = new TableRow(this);
						tr_cnt++;
					}
				}
			} while (cnt + cnt_additional < var.ar_tb.size());
			// старый алгоритм формирования панели
			// boolean bbb = !st.isEven((float)(var.ar_tb.size()/tb_row));
			// if (!st.isEven((float)(var.ar_tb.size()/tb_row)))
			// btnrow = (int)Math.ceil(var.ar_tb.size()/tb_row)+1;
			// else
			// btnrow = (int)Math.ceil(var.ar_tb.size()/tb_row);
			// tr = new TableRow(this);
			// for (int i = 0;i<var.ar_tb.size();i++){
			// tr.addView(createbtn(this.getActionButtonStr(var.ar_tb.get(i)),var.ar_tb.get(i).id));
			//
			// if (i+1==(btnrow*row)){
			// row++;
			// llbottompanel.addView(tr);
			// tr = new TableRow(this);
			//
			// }
			// }
			// if (tr!=null){
			// llbottompanel.addView(tr);
			// }
		}
	}

	public Button createbtn(String txt, int id) {
		// выясняем высоту кнопки если её нет.
		if (tb_btn_height == -1) {
			// первоначальная высота кнопки
			// TableRow.LayoutParams lptmp = new TableRow.LayoutParams(
			// TableRow.LayoutParams.WRAP_CONTENT,
			// TableRow.LayoutParams.WRAP_CONTENT
			// );
			TableRow.LayoutParams lptmp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
					TableRow.LayoutParams.WRAP_CONTENT);
			Button bttmp = new Button(this);
			bttmp.setText("abc");
			bttmp.setLayoutParams(lptmp);
			bttmp.setMinLines(3);
			bttmp.setMaxLines(3);
			toolbar.addView(bttmp);
			bttmp.measure(0, 0);
			tb_btn_height = bttmp.getMeasuredHeight();
			toolbar.removeAllViews();
		}
		Button btn = new Button(this);
		btn.setTextSize(12);
		// если строка для кнопки длинная, то разбиваем её на 3 слова,
		// один под одним
		if (txt.length() > 12) {
			String[] ar = txt.split(var.STR_SPACE);
			txt = var.STR_NULL;
			for (int i = 0; i < ar.length; i++) {
				txt += ar[i] + var.STR_lF;
			}
			btn.setTextSize(7);
		} else if (txt.length() > 8)
			btn.setTextSize(8);

		btn.setText(txt);
		btn.setTextColor(th.getValue(th.NEWREC_TOOLBAR_BUTTON_TEXT));
		btn.setMinLines(3);
		btn.setMaxLines(3);
		btn.setId(id);

		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.MATCH_PARENT);
		btn.setLayoutParams(lp);
		btn.setPadding(1, 1, 1, 1);
		btn.measure(0, 0);
		int sizebtn = btn.getMeasuredHeight();
		if (var.fl_newrec_big_toolbar) {
			btn.setHeight((int) (sizebtn * 1.3));
			btn.setTextSize(16);
			if (txt.length() > 8)
				btn.setTextSize(8);
			btn.setTextColor(th.getValue(th.NEWREC_TOOLBAR_BUTTON_TEXT));
		}
		btn.setOnClickListener(action_clkListener);
		// lp.setMargins(1, 1, 0, 1);

		return btn;
	}

	public void runSetting() {
		st.runAct(PrefActMain.class, this);
	}

	// какой текст выводить на кнопках в панели инструментов
	// по умолчанию или менять на этот
	public String getActionButtonStr(ArrayToolbar ar) {
		int id = ar.id - 500;
		if (id == 19) {
			String ss = inst.getResources().getStringArray(R.array.rec_tb)[id];
			ar.text = ss.substring(ss.indexOf(".") + 1);
		}
		switch (id) {
		case 1:
			return "Home";
		case 2:
			return "End";
		case 4:
			return "PgDn";
		case 5:
			return "PgUp";
		case 11:
			return "toStart";
		case 12:
			return "toEnd";
		case 13:
			return "←";
		case 14:
			return "→";
		case 15:
			return "↑";
		case 16:
			return "↓";
		case 17:
			return "Delete";
		case 20:
			if (ar.text.compareToIgnoreCase("копировать") == 0)
				return "Копир.";
			return ar.text;
		default:
			return ar.text;
		}
	}

	public void exitApp(boolean bexit) {
		if (bexit) {
			if (MainActivity.inst != null)
				MainActivity.inst.finish();
			var.filename = var.STR_NULL;
		}
		fl_temp_edit = false;
		inst = null;
		finish();

	}

	public void runAction(int act) {
		switch (act) {
		case ACTION_COPY:
			String txt = getSelection();
			if (txt.isEmpty()) {
				runCopyCommand();
			} else if (txt.length() > 0) {
				copyText(txt);
			}
			break;
		case ACTION_PASTE:
			if (!var.filename.isEmpty() && var.fl_view_mode)
				break;
			ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			CharSequence str = cm.getText();
			if (str == null || str.length() == 0)
				break;
			if (getSelection().length() > 0) {
				et.getText().delete(Math.min(et.getSelectionStart(), et.getSelectionEnd()),
						Math.max(et.getSelectionStart(), et.getSelectionEnd()));
				et.getText().insert(et.getSelectionStart(), str);
			} else {
				et.getText().insert(et.getSelectionStart(), str);
			}
			break;
		case ACTION_SHOW_KBD:
			if (!var.filename.isEmpty() && var.fl_view_mode)
				break;
			st.showKbd(et,false);
			break;
		case ACTION_HIDE_KBD:
			st.hideKbd(inst);
			break;
		case ACTION_HOME:
			if (!var.filename.isEmpty() && var.fl_view_mode)
				break;
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_MOVE_HOME));
			break;
		case ACTION_END:
			if (!var.filename.isEmpty() && var.fl_view_mode)
				break;
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_MOVE_END));
			break;
		case ACTION_SEARCH:
			searchPanelShow();
			break;
		case ACTION_PGDN:
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_PAGE_DOWN));
			break;
		case ACTION_PGUP:
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_PAGE_UP));
			break;
		case ACTION_CLOSE:
			close();
			break;
		case ACTION_EXIT_APP:
			fl_exit_app = true;
			onBackPressed();
			break;
		case ACTION_SAVE_EXIT:
			if (!var.filename.isEmpty() && var.fl_view_mode)
				break;
			if (var.filename.length() > 0) {
				savefile();
				exitApp(true);
			} else
				saveEndExit();
			break;
		case ACTION_RUN_SETTING:
			runSetting();
			break;
		case ACTION_SAVED:
			if (!var.filename.isEmpty() && var.fl_view_mode)
				break;
			if (var.filename.length() > 0)
				savefile();
			else
				saved();
			break;
		case ACTION_RECALC:
			recalc();
			break;
		case ACTION_TOSTART:
			et.setSelection(0);
			break;
		case ACTION_TOEND:
			et.setSelection(et.getText().toString().length());
			break;
		case ACTION_ARROW_LEFT:
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_DPAD_LEFT));
			break;
		case ACTION_ARROW_RIGHT:
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_DPAD_RIGHT));
			break;
		case ACTION_ARROW_UP:
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_DPAD_UP));
			break;
		case ACTION_ARROW_DOWN:
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_DPAD_DOWN));
			break;
		case ACTION_DEL:
			if (!var.filename.isEmpty() && var.fl_view_mode)
				break;
			et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_DEL));
			break;
		case ACTION_SEL_FUNCTION:
			runSelectCommand();
			break;
		case ACTION_RUN_SETTING_TOOLBAR:
			st.runAct(PrefActSetToolbar.class, this);

			break;
		}
	}
	/** читаем текст из файла path, указанного размера size <br>
	 * Если size == 0, то читаем полностью */
	public String getFileText(String path, long size) {
		if (path == null)
			return var.STR_NULL;
		if (path.startsWith(Newrecord.START_FILENAME_DESCRIPTOR))
			return path.substring(START_FILENAME_DESCRIPTOR.length());
		File f = new File(path);

		try {
			FileInputStream fi = new FileInputStream(f);
			// st.toast(st.getKBString(f.length()));
			if (size == 0)
				size = f.length();
			byte buf[] = new byte[(int) size];
			fi.read(buf);
			int start = 0;
			if (buf.length > 3 && buf[0] == 0xef && buf[1] == 0xbb && buf[2] == 0xbf) {
				start = 3;
			}
			fi.close();
			return new String(buf, start, buf.length - start);
		} catch (Throwable e) {
			st.toast(R.string.toast_not_open_file);
		}
		return null;
	}

	public String getSelection() {
		if (et == null)
			return var.STR_NULL;
		String ret = et.getText().toString().substring(Math.min(et.getSelectionStart(), et.getSelectionEnd()),
				Math.max(et.getSelectionStart(), et.getSelectionEnd()));
		return ret;
	}

	public void copyText(String txt) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(
					CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("label", txt);
			clipboard.setPrimaryClip(clip);
		} else {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(
					CLIPBOARD_SERVICE);
			clipboard.setText(txt);
		}
		st.toast(getString(R.string.toast_msg6));

	}

	public void runCopyCommand() {
		if (et == null)
			return;
		ArrayAdapter adapt = new ArrayAdapter<String>(this, R.layout.dlg_list,
				getResources().getStringArray(R.array.copy_menu));

		Dlg.CustomMenu(this, adapt, getString(R.string.selection), new st.UniObserver() {
			@Override
			public int OnObserver(Object param1, Object param2) {
				int[] sel = new int[2];
				sel[0] = 0;
				sel[1] = 0;
				String txt = var.STR_NULL;
				switch (((Integer) param1).intValue()) {
				// copy all
				case 0:
					txt = et.getText().toString();
					break;
				// copy paragraph
				case 1:
					sel = UtilText.getParagraphPos(et);
					if (sel != null && sel[0] != sel[1] && sel[0] < sel[1]) {
						txt = et.getText().toString().substring(sel[0], sel[1]);
					}
					break;
				// copy line
				case 2:
					sel = UtilText.getLinePos(et);
					if (sel != null && sel[0] != sel[1]) {
						txt = et.getText().toString().substring(sel[0], sel[1]);
					}
					break;
				// copy предложение
				case 3:
					sel = UtilText.getSentencePos(et);
					if (sel != null && sel[0] != sel[1] && sel[0] < sel[1]) {
						txt = et.getText().toString().substring(sel[0], sel[1]);
					}
					break;
				// copy word
				case 4:
					sel = UtilText.getWordPos(et);
					if (sel != null && sel[0] != sel[1] && sel[0] < sel[1]) {
						txt = et.getText().toString().substring(sel[0], sel[1]);
					}
					break;
				}
				if (!txt.isEmpty())
					copyText(txt);
				return 0;
			}
		});

		// int[] sel = TextUtil.getWordPos(et);
		// if (sel !=null){
		// et.setSelection(sel[0], sel[1]);
		// st.sleep(1000);
		// }
		// st.toast("bbb");

	}

	public void runSelectCommand() {
		if (et == null)
			return;
		ArrayAdapter adapt = new ArrayAdapter<String>(this, R.layout.dlg_list,
				getResources().getStringArray(R.array.sel_menu));

		Dlg.CustomMenu(this, adapt, getString(R.string.selection), new st.UniObserver() {
			@Override
			public int OnObserver(Object param1, Object param2) {
				int[] sel = new int[2];
				sel[0] = 0;
				sel[1] = 0;
				String txt = var.STR_NULL;
				switch (((Integer) param1).intValue()) {
				// sel all
				case 0:
					sel[0] = 0;
					sel[1] = et.getText().toString().length();
					break;
				// sel paragraph
				case 1:
					sel = UtilText.getParagraphPos(et);
					if (sel != null && sel[0] != sel[1] && sel[0] < sel[1]) {
						txt = et.getText().toString().substring(sel[0], sel[1]);
					}
					break;
				// sel line
				case 2:
					sel = UtilText.getLinePos(et);
					if (sel != null && sel[0] != sel[1]) {
						txt = et.getText().toString().substring(sel[0], sel[1]);
					}
					break;
				// sel предложение
				case 3:
					sel = UtilText.getSentencePos(et);
					if (sel != null && sel[0] != sel[1] && sel[0] < sel[1]) {
						txt = et.getText().toString().substring(sel[0], sel[1]);
					}
					break;
				// sel word
				case 4:
					sel = UtilText.getWordPos(et);
					if (sel != null && sel[0] != sel[1] && sel[0] < sel[1]) {
						txt = et.getText().toString().substring(sel[0], sel[1]);
					}
					break;
				// удалить выделенное
				case 5:
					if (var.fl_view_mode)
						return 0;
					if (et.hasSelection())
						et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_DEL));
					return 0;
				}
				if (et != null && sel[0] != sel[1])
					et.setSelection(Math.min(sel[0], sel[1]), Math.max(sel[0], sel[1]));

				return 0;
			}
		});
	}
	// уже не используется, вместо неё юзается st.hideKbd(inst) (inst = Activity)
	// public void khideKbd()
	// {
	// InputMethodManager imm = (InputMethodManager)
	// getSystemService(INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
	// InputMethodManager.HIDE_NOT_ALWAYS);
	// }

	public void setMinLinesEditText(EditText ett) {
		ett.setMinLines(80);

		// int hd = st.getDisplayHeight(inst);
		// st.toastLong(""+hd);
		// int line = 10;
		// if (hd<240) line = 10;
		// else if (hd>=240&&hd<320) line = 10;
		// else if (hd>=320&&hd<480) line = 20;
		// else if (hd>=480&&hd<640) line = 25;
		// else if (hd>=640&&hd<800) line = 25;
		// else if (hd>=800&&hd<1000) line = 30;
		// else if (hd>=1000&&hd<1280) line = 35;
		// else if (hd>=1280&&hd<1500) line = 40;
		// else if (hd>=1500&&hd<1800) line = 45;
		// else if (hd>=1800&&hd<2000) line = 50;
		// else if (hd>=2000&&hd<2300) line = 55;
		// else if (hd>=2300&&hd<2600) line = 60;
		// else if (hd>=2600&&hd<3000) line = 65;
		// else if (hd>3000) line = 80;
		// ett.setMinLines(line);
	}
	/** устанавливаем  время показа кнпки включения редактирования*/
	public void startTimerToolbar2() {
		if (m_tm != null)
			m_tm.cancel();
		m_tm = new SameThreadTimer(2000, 0) {
			@Override
			public void onTimer(SameThreadTimer timer) {
				if (im_edit != null)
					im_edit.setVisibility(View.GONE);
				m_tm.cancel();
			}
		};
		m_tm.start();
	}

	public void insertStr(String insertStr) 
	{
		String oriContent = et.getText().toString();
		int index = et.getSelectionStart() >= 0 ? et.getSelectionStart() : 0;
		StringBuilder sBuilder = new StringBuilder(oriContent);
		sBuilder.insert(index, insertStr);
		et.setText(sBuilder.toString());
		et.setSelection(index + insertStr.length());
	}
}
