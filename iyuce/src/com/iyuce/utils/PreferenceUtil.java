package com.iyuce.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {
	public static SharedPreferences getSharePre(Context paramContext) {
		return PreferenceManager.getDefaultSharedPreferences(paramContext);
	}

	public static void save(Context paramContext, String paramString, int paramInt) {
		SharedPreferences.Editor localEditor = getSharePre(paramContext).edit();
		localEditor.putInt(paramString, paramInt);
		localEditor.commit();
	}

	public static void save(Context paramContext, String paramString, long paramLong) {
		SharedPreferences.Editor localEditor = getSharePre(paramContext).edit();
		localEditor.putLong(paramString, paramLong);
		localEditor.commit();
	}

	public static void save(Context paramContext, String paramString, Float paramFloat) {
		SharedPreferences.Editor localEditor = getSharePre(paramContext).edit();
		localEditor.putFloat(paramString, paramFloat.floatValue());
		localEditor.commit();
	}

	public static void save(Context paramContext, String paramString1, String paramString2) {
		SharedPreferences.Editor localEditor = getSharePre(paramContext).edit();
		localEditor.putString(paramString1, paramString2);
		localEditor.commit();
	}

	public static void save(Context paramContext, String paramString, boolean paramBoolean) {
		SharedPreferences.Editor localEditor = getSharePre(paramContext).edit();
		localEditor.putBoolean(paramString, paramBoolean);
		localEditor.commit();
	}

	public static void clear(Context paramContext) {
		SharedPreferences.Editor localEditor = getSharePre(paramContext).edit();
		localEditor.clear().commit();
	}

	public static void remove(Context paramContext) {
		SharedPreferences.Editor localEditor = getSharePre(paramContext).edit();
		localEditor.remove("username");
		localEditor.remove("password");
		localEditor.remove("mUserName");
		localEditor.commit();
	}
}
