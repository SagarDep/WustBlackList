package com.example.wustblacklist.sharedPreferences;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class BlackListSharedPreferences {
    public static String FILE_NAME = "WustBlackList";
    public static void saveSettingMessage(Context context,HashMap<String, String>hashMap) {
	SharedPreferences sharedPreferences = context.getSharedPreferences(
		FILE_NAME, Context.MODE_PRIVATE);
	Editor editor = sharedPreferences.edit();
	editor.putString("blacklist_block_way", hashMap.get("blacklist_block_way"));
	editor.putString("call_block_way", hashMap.get("call_block_way"));
	editor.putString("sms_block_way", hashMap.get("sms_block_way"));
	editor.putString("night_block_switch", hashMap.get("night_block_switch"));
	editor.putString("night_block_way", hashMap.get("night_block_way"));
	editor.putString("meeting_block_switch", hashMap.get("meeting_block_switch"));
	editor.putString("meeting_block_way", hashMap.get("meeting_block_way"));
	editor.putString("meeting_block_time", hashMap.get("meeting_block_time"));
	
	editor.commit();
    }

    /**
     * 提取之前保存的用户数据
     * 
     * @return
     */
    public static HashMap<String, String> getSettingMessage(Context context) {
	SharedPreferences sharedPreferences = context.getSharedPreferences(
		FILE_NAME, Context.MODE_PRIVATE);
	HashMap<String, String> hashMap = new HashMap<String, String>();
	hashMap.put("blacklist_block_way", sharedPreferences.getString("blacklist_block_way", "0"));
	hashMap.put("call_block_way", sharedPreferences.getString("call_block_way", "0"));
	hashMap.put("sms_block_way", sharedPreferences.getString("sms_block_way", "枪支，弹药，保险"));
	hashMap.put("night_block_switch", sharedPreferences.getString("night_block_switch", "0"));
	hashMap.put("night_block_way", sharedPreferences.getString("night_block_way", "22:00-07:00"));
	hashMap.put("meeting_block_switch", sharedPreferences.getString("meeting_block_switch", "0"));
	hashMap.put("meeting_block_way", sharedPreferences.getString("meeting_block_way", "不好意思，我正在开会，稍后联系你！"));
	hashMap.put("meeting_block_time",sharedPreferences.getString("meeting_block_time", "30:00"));
	return hashMap;
    }
}
