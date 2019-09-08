package com.mwcorp.tools;

import java.io.File;
import java.util.Locale;

import com.mwcorp.costs.R;
import com.mwcorp.costs.st;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class Mail 
{
	public static String EMAIL = "mwsoft@tut.by";
	public static void sendFeedback(Context c) {
		sendFeedback(c, null);
	}
	public static void sendFeedback(Context c,File crash) {
		StringBuilder info;
		info = new StringBuilder();
		if (crash!=null)
			info = new StringBuilder(String.format(Locale.ENGLISH,
				"\n\n%s", c.getString(R.string.app_info)));
		String delim = ": ";
		info.append(String.format(Locale.ENGLISH, "%s%s%s%s\n",
				c.getString(R.string.app_info_os), delim, "Android ",
				Build.VERSION.RELEASE));
		info.append(String.format(Locale.ENGLISH, "%s%s%s\n",
				c.getString(R.string.app_info_device), delim, Build.MODEL));
		info.append('\n');
		info.append("===\n");
		if(crash!=null)
		{
			info.append(st.strFile(crash));
			info.append("===\n");
		}
		final Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("text/message");
		emailIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] {EMAIL});

		String subj = null;
		if(crash==null)
			subj = c.getString(R.string.feedback_subject) + st.getAppNameAndVersion(c);
		else
			subj = "Crash report "+st.getAppNameAndVersion(c);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subj);
		emailIntent.putExtra(Intent.EXTRA_TEXT, info.toString());
		c.startActivity(Intent.createChooser(emailIntent, c.getString(R.string.act_feedback)));
	}
}
