package com.mwcorp.costs;

import java.util.ArrayList;

import com.mwcorp.costs.var;
import com.mwcorp.costs.var.FolderInfo;
import com.mwcorp.costs.var.RecordCurrency;
import com.mwcorp.costs.var.myCur;


// анализ и подсчёт текста в записи
public class an 
{
	public static ArrayList<FolderInfo> cur_rec = new ArrayList<FolderInfo>();
	public static String basecursymb = "";
	public static String NOT_COUNTED = "#";

	public static void analizeText(String txt)
    {
		if (txt.startsWith(NOT_COUNTED))
			return;
		baseCur();
		var.recordCur.clear();
		String paragrapf = "";
		int start = 0;
		int end = 1;
		if (txt.length()== 0)
			txt = " ";
		String t1 = "";
		while (end != -1)
		{
			end = txt.indexOf("\n", start);
			if (end != -1) {
				t1= txt.substring(start, end);
				analizeParagrapf(t1);
				start=end+1;
				continue;
			}
		}
		String endtxt = txt.substring(start);
		if (endtxt.length() >0){
			analizeParagrapf(endtxt);
		}
		baseCurrentCount();
   	}
	public static void analizeParagrapf(String in)
    {
		in = in.trim();
		if (in.length() == 0)
			return;
		if(in.endsWith(NOT_COUNTED))
			return;
		int max = 0;
		if (var.currency.size()==0)
	    	st.CurLoad();

		for (myCur c:var.currency) {
	    	if (max < c.cur_symb.length()) {
	    		max = c.cur_symb.length();
		    }
	    }
		boolean cifr = false;
		String outcifr = "";
		String symb = "";
		String cursymb ="";
		String out = "";
		String thisCurSymb = "";
		float kurs = 0;
		
		for (int i=in.length();i>1;i--){
			symb = in.substring(i-1,i);
			if (symb.length()==0)
				continue;
			char cc = symb.charAt(0);
			if (cc >= '0'&&cc<='9'){
				cifr = true;
				if (out.length()>0){
					for (myCur mc:var.currency){
						if (mc.cur_symb.compareToIgnoreCase(out) == 0){
							kurs = mc.kurs_value;
							thisCurSymb = mc.cur_symb;
							break;
						}
					}
					if (kurs == 0)
						break;
				}
				outcifr=symb+outcifr;
			} else {
				if (in.length()-i == 0&&in.length()-i>=max&&cifr==false)
					break;
				if (symb.compareToIgnoreCase("-") == 0&&cifr){
					outcifr= symb+outcifr;
					break;
				}
				else if (symb.compareToIgnoreCase(".") == 0&&cifr){
					outcifr= symb+outcifr;
					continue;
				}
				else if (symb.compareToIgnoreCase(" ") == 0&&cifr){
//					continue;
				}
				if (cifr)
					break;
				if (max >= out.length()&&in.length()-i<=max){
					out = symb + out;
//					continue;
//				} else {
//					break;
				}
				if (cifr)
					out = symb + out;
//				break;
			}
		}
		if (out.length()>0&&kurs == 0){
			for (myCur c:var.currency){
				if (c.cur_symb.compareToIgnoreCase(out) == 0){
					kurs = c.kurs_value;
					thisCurSymb = c.cur_symb;
					break;
				}
			}
		}
		if (kurs == 0) {
			kurs = 1;
			thisCurSymb = basecursymb;
		}
		RecordCurrency cc = new RecordCurrency();
		cc.symb = thisCurSymb;
//		cc.value = kurs*st.str2float(outcifr,in);
		cc.value = st.str2float(outcifr,in);
		
		addCurrentCurrency(cc);
		
   	}
	public static void addCurrentCurrency(RecordCurrency cc1)
    {
		boolean fl = false;
		for (RecordCurrency cc:var.recordCur){
			if (cc.symb.compareToIgnoreCase(cc1.symb)==0){
				cc.value +=cc1.value;
				fl=true;
			}
		}
		if(fl== false){
			var.recordCur.add(cc1);
		}
   	}
	public static void baseCurrentCount()
    {
		float count = 0;
		int basecur = -1;
		float kurs = 0;
		RecordCurrency  mc = null;
		for (int i=0;i<var.recordCur.size();i++){
			
			if (var.recordCur.get(i).symb.compareToIgnoreCase(basecursymb) !=0){
				mc = var.recordCur.get(i);
				for (myCur cc:var.currency){
					if (cc.cur_symb.compareToIgnoreCase(mc.symb) == 0){
						kurs = cc.kurs_value;
					}
				}
				count += kurs * var.recordCur.get(i).value;
			}
			if (var.recordCur.get(i).symb.compareToIgnoreCase(basecursymb) ==0){
				basecur = i;
			}
		}
		RecordCurrency cc;
		if (basecur > -1){
			cc = var.recordCur.get(basecur);
			cc.value += count;
			var.recordCur.remove(basecur);
			var.recordCur.add(cc);
		}
		if (basecur == -1){
			cc = new RecordCurrency();
			cc.symb = basecursymb;
			cc.value = count;
			var.recordCur.add(cc);
		}
   	}
	public static boolean checkFormat(String symb)
    {
		boolean ret = false;
		char cha = symb.charAt(0);
        switch (cha)
        {
        case '0': ret = true;break;
        case '1': ret = true;break;
        case '2': ret = true;break;
        case '3': ret = true;break;
        case '4': ret = true;break;
        case '5': ret = true;break;
        case '6': ret = true;break;
        case '7': ret = true;break;
        case '8': ret = true;break;
        case '9': ret = true;break;
        case '.': ret = true;break;
        case '-': ret = true;break;
        }

		return ret;
   	}
	public static void baseCur()
    {
		for (myCur c:var.currency){
			if (c.base_cur){
				basecursymb = c.cur_symb.toUpperCase(); 
			}
		}
   	}
// нигде не используется
	public static boolean wordseparator(String in)
    {
       		char c= in.charAt(0);
            switch (c)
            {
            case '\n': return true;
            case '\t': return true;
            case '.': return true;
            case ',': return true;
            case ';': return true;
            case ':': return true;
            case '!': return true;
            case '(': return true;
            case ')': return true;
            case ' ': return true;
            }

		return false;
   	}
}
