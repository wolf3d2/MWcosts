package com.mwcorp.tools;

import android.view.KeyEvent;
import android.widget.EditText;

public class UtilText 
{
	// возвращает массивом позиции начала и конца 
	// слова для выделения
	// в массиве 2 значения - start и end позиции
	// слова
	public static int[] getWordPos(EditText et){
		if (et.isSelected())
			return null;
		// позиция курсора в et
		int index = et.getSelectionStart();
		int[] ret = new int[2];
		ret[0]=0;
		ret[1]=0;
		int symb = 0;
		String txt = et.getText().toString();
		// текст пуст
		if (txt.isEmpty())
			return ret;
		if (txt.length()<=index)
			index = txt.length()-1; 
		if (index < 0)
			index = 0;
		if (txt.length()>0&&txt.charAt(index) == '\n')
			index--;
		// определяем начало слова
		if (index == 0)
			ret[0]=0;
		else {
			symb = 0;
			for (int i=index;i>=0;i--){
				symb = txt.charAt(i);
				if (!isWordSeparator(symb)){
					if (i==index)
						continue;
					if(i>0)
						ret[0]=i+1;
					else
						ret[0]=0;
					break;
				}
					
			}
		}
		// определяем конец слова
		if (txt.length() == 0)
			ret[1]=0;
		else {
			symb = 0;
			for (int i=index;i<txt.length();i++){
				symb = txt.charAt(i);
				if (!isWordSeparator(symb)){
					if(i>0)
						ret[1]=i;
					else
						ret[1]=0;
					break;
				}
					
			}
			if (ret[1]==0&&txt.length()>0)
				ret[1] = txt.length();
		}		
		return ret;
	}
	// возвращает массивом позиции начала и конца 
	// абзаца для выделения
	// в массиве 2 значения - start и end позиции
	// абзаца
	public static int[] getParagraphPos(EditText et){
		if (et.isSelected())
			return null;
		// позиция курсора в et
		int index = et.getSelectionStart();
		int[] ret = new int[2];
		ret[0]=0;
		ret[1]=0;
		String txt = et.getText().toString();
		char symb = 0;
		// текст пуст
		if (txt.isEmpty())
			return ret;
		if (txt.length()<=index)
			index = txt.length()-1; 
		if (index < 0)
			index = 0;
		if (txt.length()>0&&txt.charAt(index) == '\n')
			index--;
		// определяем начало абзаца
		if (index == 0)
			ret[0]=0;
		else {
			symb = 0;
			for (int i=index;i>=0;i--){
				if (i>=txt.length())
					continue;
				symb = txt.charAt(i);
				if (symb=='\n'||i==0){
					if(i>0)
						ret[0]=i+1;
					else
						ret[0]=0;
					break;
				}
					
			}
		}
		// определяем конец абзаца
		if (txt.length() == 0)
			ret[1]=0;
		else {
			symb = 0;
			for (int i=index;i<txt.length();i++){
				symb = txt.charAt(i);
				if (symb=='\n'){
					if(i>0)
						ret[1]=i;
					else
						ret[1]=0;
					break;
				}
			}
			if (ret[1]==0&&txt.length()>0)
				ret[1] = txt.length();
		}
				
		return ret;
	}
	// возвращает массивом позиции начала и конца 
	// предложения для выделения
	// в массиве 2 значения - start и end позиции
	// предложения
	public static int[] getSentencePos(EditText et){
		if (et.isSelected())
			return null;
		// позиция курсора в et
		int index = et.getSelectionStart();
		int[] ret = new int[2];
		ret[0]=0;
		ret[1]=0;
		String txt = et.getText().toString();
		char symb = 0;
		// текст пуст
		if (txt.isEmpty())
			return ret;
		if (txt.length()<=index)
			index = txt.length()-1; 
		if (index < 0)
			index = 0;
		if (txt.length()>0&&txt.charAt(index) == '\n')
			index--;
		// определяем начало предложения
		if (index == 0)
			ret[0]=0;
		else {
			symb = 0;
			for (int i=index;i>=0;i--){
				if (i>=txt.length())
					continue;
				symb = txt.charAt(i);
				if (isSymbolSentenceEnd(symb)||i==0){
					if(i>0)
						ret[0]=i+1;
					else
						ret[0]=0;
					break;
				}
					
			}
		}
		// определяем конец предложения
		if (txt.length() == 0)
			ret[1]=0;
		else {
			symb = 0;
			for (int i=index;i<txt.length();i++){
				symb = txt.charAt(i);
				if (isSymbolSentenceEnd(symb)){
					if(i>0)
						ret[1]=i+1;
					else
						ret[1]=0;
					break;
				}
			}
			if (ret[1]==0&&txt.length()>0)
				ret[1] = txt.length();
		}
				
		return ret;
	}
	// возвращает массивом позиции начала и конца 
	// строки для выделения
	// в массиве 2 значения - start и end позиции
	// строки
	public static int[] getLinePos(EditText et){
		if (et.isSelected())
			return null;
		// позиция курсора в et
		int index = et.getSelectionStart();
		int[] ret = new int[2];
		ret[0]=0;
		ret[1]=0;
		// определяем начало строки
    	et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE,
        		KeyEvent.KEYCODE_MOVE_HOME));
    	ret[0] = et.getSelectionStart();
		// определяем конец строки
    	et.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_MULTIPLE,
        		KeyEvent.KEYCODE_MOVE_END));
    	ret[1] = et.getSelectionStart();
    	et.setSelection(index);
    	
		return ret;
	}
	// true - символ число или буква
    public static boolean isWordSeparator(int code)
    {
        return Character.isLetterOrDigit(code);
    }
    // true - это символ конца предложения
    public static boolean isSymbolSentenceEnd(char ch)
    {
    	switch (ch)
    	{
    	case '!':return true;
    	case '?':return true;
    	case '.':return true;
    	}
        return false;
    }

}
