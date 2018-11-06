package com.mwcorp.costs;

import java.util.ArrayList;
import java.util.Date;


/** Класс содержит глобальные переменные */
public class var 
{
	
	/** тема приложения из настроек */
	public static int skin_app = 0;
	/** размер текста в list на главном экране */
	public static int size_text_listadapter = 15;
	public static String filename = "";
	/** массив значений активных кнопок для панели инструментов в настройках */;
	public static ArrayList<ArrayToolbar> ar_tb = new ArrayList<ArrayToolbar>();
	public static ArrayList<RecordCurrency> recordCur = new ArrayList<RecordCurrency>();
	public static ArrayList<myCur> currency = new ArrayList<myCur>();
	public static ArrayList<FolderInfo> folderinfo = new ArrayList<FolderInfo>();
	/** режим просмотра */
	public static boolean fl_view_mode = false;
	public static int newrec_size_text = 16;
	public static boolean fl_newrec_fullscreen = false;
	public static boolean auto_calculate = false;
	public static boolean fl_newrec_show_toolbar = true;
	public static boolean fl_newrec_big_size_sum = false;
	public static boolean fl_newrec_big_toolbar = false;
	public static int id_cur = -1;
	// как заполнять кнопки панели инструментов
	// 1 - слева-направо по рядам
	public static int tb_layout_type = 1;
	public static int tb_line_port = 1;
	public static int tb_line_land = 1;
	public static String file_desc = "";
	public static String create_inifile = "";
	// константы
	public static String STR_NULL = "";
	public static String STR_SPACE = " ";
	public static String STR_CR = "\n";

// класс для хранения валют
	public static class myCur
    {
		int id = 0;
		// обозначение валюты
		String cur_symb = "";
		// наименование валюты
		String name = "";
		long kurs_of_date = 0;
		float kurs_value = 0;
		boolean base_cur = false;
		// разделитель строки валюты
		public static String DELIMITER = "------";
// имена полей для listview
		public static String LCHECKBOX = "check_box";
		public static String LSYMB = "symb";
		public static String LNAME = "name";
		public static String LKURSDATE = "kursdate";
		public static String LKURDVALUE = "kursvalue";
		public static String LBASECUR = "basecur";
		public static String LID = "id";
        public myCur(int idd,String cs, String n, long kurs_date, float kurs_val, boolean base)
        {
        	id = idd;
    		cur_symb = cs;
        	name = n;
    		kurs_of_date = kurs_date;
    		kurs_value = kurs_val;
    		base_cur = base;
        }
        public myCur()
        {
        	id = 0;
    		cur_symb = "";
        	name = "";
    		kurs_of_date = new Date().getTime();
    		kurs_value = 1;
    		base_cur = false;
        }
    }
	// подсчитанные значения валют для записи
	public static class RecordCurrency
    {
		String symb = "";
		float value = 0;
		long datecounted = -1;

		public RecordCurrency()
        {
        	symb = "";
        	value = 0;
        	datecounted = new Date().getTime();

        }
    }
// класс значений панели инструментов
	public static class ArrayToolbar
    {
		public int id =-1;
		public String text =var.STR_NULL;

		public ArrayToolbar()
        {
			id = -1;
			text = var.STR_NULL;
        }
    }
// класс хранит информацию о текущей папке
	public static class FolderInfo
    {
		ArrayList<RecordCurrency> cur = new ArrayList<RecordCurrency>();
		long dateedit = 0;
		long date_calculate = 0;
		String path = "";
		String pathtxt = "";
		String inifile = "";
		String name = "";
		int dir = 0;
		boolean exist = false;
		
        public FolderInfo()
        {
        	name = "";
        	inifile = "";
        	path="";
        	pathtxt="";
        	dir = 0;
    		dateedit = 0;
    		date_calculate = 0;
    		if (cur!=null&&cur.size()>=0)
    			cur.clear();
        	exist = false;
        }
    }
}
