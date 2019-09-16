package com.mwcorp.costs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import com.mwcorp.costs.var.FolderInfo;
import com.mwcorp.costs.var.RecordCurrency;

//класс информации о записях	
public class rec
{
	// имена полей для listview
	public static String NAME = "name";
	public static String DATE_EDIT = "date_edit";
	public static String PATH = "path";
	public static String PATHINI = "pathini";
	public static String PATHTXT = "pathtxt";
	public static String DIR = "dir";
	public static String CURRENCY = "currency";
	public static String DATE_CALCULATE = "date_calculate";

	public static String RECORD = "/record";
	public static String SUFINI = ".ini";
	public static String SUFTXT = ".txt";
	public static String BACK_FOLDER = "..";
	
	public static String currentFolder = "";
	static rec inst;
	
    public rec()
    {
       	inst = this;
       	setRootFolder();
    }
	public static boolean setFolder(String path)
	{
		File ff = new File(path);
		ff.mkdirs();
		currentFolder = path;
		return true;
	}
	public static String getCurrentFolder()
	{
		if (currentFolder.length() == 0)
			setRootFolder();
		return currentFolder;
	}
	public static String setRootFolder()
	{
		currentFolder = st.getPathAppFolder()+RECORD;
		setFolder(currentFolder);
		return currentFolder;
	}
	public static boolean isRootFolder()
	{
		if (currentFolder.length() == 0){
			currentFolder = setRootFolder();
		}
		if (currentFolder.compareToIgnoreCase(getRootFolder()) == 0){
				return true;
		}
		return false;
	}
	public static String getRootFolder()
	{
		return st.getPathAppFolder()+RECORD;
	}
	public static void createParamFile(String name, boolean dir, String txtfile)
	{
		FileWriter wr;
		try {
			String sss = "";
			sss = getRndNameEmpty(SUFINI);
			var.create_inifile = sss;
			wr = new FileWriter(getCurrentFolder()+"/"+sss, false);
			if (dir){
				wr.write(DIR+"=1\n");
				wr.write(NAME+"="+name.trim()+"\n");
				wr.write(PATHINI+"="+var.create_inifile+"\n");
			}else {
				wr.write(DIR+"=0\n");
				wr.write(NAME+"="+st.getHeaderText(name)+"\n");
				wr.write(PATHINI+"="+var.create_inifile+"\n");
				wr.write(PATHTXT+"="+txtfile+"\n");
				wr.write(DATE_EDIT+"="+String.valueOf(new Date().getTime())+"\n");
				if (var.recordCur!=null&&var.recordCur.size()>0) {
					String out = "";
					for (RecordCurrency rc:var.recordCur) {
						out+=rc.symb+"="+String.valueOf(rc.value)+",";
					}
					wr.write(CURRENCY+"="+out+"\n");
					wr.write(DATE_CALCULATE+"="+new Date().getTime()+"\n");
				}
			}
//			wr.write(PATH+"="+rec.getCurrentFolder()+"\n");
			wr.close();
		} catch (IOException e) {
			st.toast("Not create .ini file.");
		}
	}
	public static void modifyParamFile(String inifile, String name)
	{
		FolderInfo fi = new FolderInfo();
		fi = rec.loadinifile(inifile);
		FileWriter wr;
		try {
			wr = new FileWriter(getCurrentFolder()+"/"+inifile, false);
			wr.write(DIR+"="+fi.dir+"\n");
			if (name.length() == 0)
				name = st.c().getString(R.string.no_title);
			wr.write(NAME+"="+st.getHeaderText(name));
			wr.write(PATHINI+"="+fi.inifile+"\n");
			if (fi.pathtxt.length()>0)
				wr.write(PATHTXT+"="+fi.pathtxt+"\n");
//			wr.write(PATH+"="+getCurrentFolder()+"\n");
			wr.write(DATE_EDIT+"="+String.valueOf(new Date().getTime())+"\n");
			if (var.recordCur!=null&&var.recordCur.size()>0) {
				String out = "";
				for (RecordCurrency rc:var.recordCur) {
					out+=rc.symb+"="+String.valueOf(rc.value)+",";
				}
				wr.write(CURRENCY+"="+out+"\n");
				wr.write(DATE_CALCULATE+"="+new Date().getTime()+"\n");
			}
			wr.close();
		} catch (IOException e) 
		{
			st.logEx(e);
			st.toast("Error.\nNot modifity .ini file.");
		}
	}
// возвращает случайное 6 значное число имени ini, 
// которого гарантированного нет в текущей папке	
	public static String getRndNameEmpty(String suf)
	{
		boolean fl = true;
		String sss ="123456";
		while (fl){
			sss = st.Rnd()+suf;;
			File ff = new File(rec.getCurrentFolder()+sss);
			if (!ff.exists())
				fl = false;
		}
		return sss;
	}
	public static ArrayList<FolderInfo> getArrayThisFolder()
	{
		var.folderinfo.clear();
		FolderInfo fi = new FolderInfo();
		if (!isRootFolder()) {
			String[] tmp = getCurrentFolder().split("/");
			fi.path = "";
			fi.dir = 1;
			for (int ii =0;ii<tmp.length-1;ii++) {
				fi.path+=tmp[ii]+"/";
			}
//			fi.path += tmp[tmp.length-1];
			fi.path=fi.path.substring(0, fi.path.length()-1);
			fi.name = BACK_FOLDER;
			var.folderinfo.add(fi);
		}
		File ff = new File(getCurrentFolder());
		String[] ar = ff.list();
		if (ar!=null){
			for (int i=0;i<ar.length;i++) 
			{
				if (!ar[i].endsWith(SUFINI))
					continue;
				fi = null;
				fi = loadinifile(ar[i]);
				
				if (fi != null)
					var.folderinfo.add(fi);
			}
		}
		sortedFiles();
		return var.folderinfo;
	}
//возвращает отсортированный массив из текущей папки с папками вверху   
    public static void sortedFiles()
    {
        ArrayList<FolderInfo> ret = new ArrayList<FolderInfo>();
        ArrayList<String> ar = new ArrayList<String>();
        String[] tmp = null;
     // добавляем директории        
        for (FolderInfo fi:var.folderinfo)
        {
        	if (fi.dir == 1){
        		ar.add(fi.name);
        	}
        }
        tmp = null;
        tmp = ar.toArray(new String[ar.size()]);
//        tmp = convertArrayListToArray(ar);
        Arrays.sort(tmp);
        for (int i=0;i<tmp.length;i++)
        {
            for (FolderInfo fi:var.folderinfo)
            {
            	if (tmp[i].compareToIgnoreCase(fi.name) == 0&&fi.dir==1){
            		ret.add(fi);
            	}
            }
        }
// добавляем файлы
        if (ar.size() > 0)
        	ar.clear();
        for (FolderInfo fi:var.folderinfo)
        {
        	if (fi.dir == 0){
        		ar.add(fi.name);
        	}
        }
// сортируем файлы
        tmp = null;
        tmp = ar.toArray(new String[ar.size()]); 
//        tmp = convertArrayListToArray(ar);
        Arrays.sort(tmp);
        for (int i=0;i<tmp.length;i++)
        {
            for (FolderInfo fi:var.folderinfo)
            {
            	if (tmp[i].compareToIgnoreCase(fi.name) == 0&&fi.dir==0){
            		ret.add(fi);
            	}
            }
        }
        if (var.folderinfo.size()>0)
        	var.folderinfo.clear();
        var.folderinfo = ret;        
    }
	public static String[] convertArrayListToArray(ArrayList ar)
    {
		if (ar == null&&ar.size()<0)
			return null;
		String[] ret = new String[ar.size()]; 
		for (int i = 0;i<ar.size()-1;i++) {
			ret[i]=(String) ar.get(i);
		}
		return ret;
   	}
	public static FolderInfo loadinifile(String path)
	{
		FolderInfo ret = new FolderInfo();
		FileReader fr;
		String par = "";
		String param = "";
		String param_value = "";
		try {
			if (!path.contains(getCurrentFolder()))
				path=getCurrentFolder()+"/"+path;
			fr = new FileReader(path);
			Scanner sc = new Scanner(fr);
			sc.useLocale(Locale.US);
			while (sc.hasNext()) {
				if (sc.hasNextLine()) {
					par = sc.nextLine();
				} 
				if (par.length()>0) {
					if (par.indexOf("=") == 0)
						continue;
					param = par.substring(0, par.indexOf("="));
					param_value = par.substring(par.indexOf("=")+1);
					if (param.compareToIgnoreCase(DIR) == 0) {
						ret.dir = Integer.valueOf(param_value);
					}
					else if (param.compareToIgnoreCase(DATE_EDIT) == 0) {
						ret.dateedit = Long.valueOf(param_value);
					}
					else if (param.compareToIgnoreCase(PATH) == 0) {
//						ret.path = param_value;
					}
					else if (param.compareToIgnoreCase(PATHTXT) == 0) {
						ret.pathtxt = param_value;
					}
					else if (param.compareToIgnoreCase(PATHINI) == 0) {
						ret.inifile = param_value;
					}
					else if (param.compareToIgnoreCase(NAME) == 0) {
						ret.name = param_value;
					}
					else if (param.compareToIgnoreCase(CURRENCY) == 0) {
						String[] in;
						try {
							in = param_value.split(",");
							if (in!=null&&in.length > 0) {
								for (int i=0;i<in.length;i++){
									RecordCurrency rc = new RecordCurrency();
									rc.symb="";
									rc.value=0;
									rc.symb = in[i].substring(0, in[i].indexOf("="));
									rc.value = Float.valueOf(in[i].substring(in[i].indexOf("=")+1));
									ret.cur.add(rc);
								}
							}
						} catch(Throwable e) 
						{
							st.logEx(e);
						}
					}
					else if (param.compareToIgnoreCase(DATE_CALCULATE) == 0) {
						ret.date_calculate = Long.valueOf(param_value);
					}
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			st.logEx(e);
		}
		return ret;
	}
	public static String getBackFolder()
    {
		String ret ="";
		String[] ar = getCurrentFolder().split("/");
		for (int ii = 0;ii<ar.length-1;ii++) {
			ret+=ar[ii]+"/";
		}
		ret = ret.substring(0, ret.length()-1);
		return ret;
   	}
    public static void delini(String path) 
    {
    	File ff = new File(path);
    	if (ff!=null&&ff.exists()){
  			ff.delete();
  		}
    }
    public static boolean deleteDir(File dir)
    {
        if(!dir.isDirectory())
            return false;
        String[] children = dir.list();
        for (String p:children) 
        {
           File temp =  new File(dir, p);
           if(temp.isDirectory())
           {
               if(!deleteDir(temp))
                   return false;
           }
           else
           {
               if(!temp.delete())
                   return false;
           }
        }
        dir.delete();
        return true;
    }
}