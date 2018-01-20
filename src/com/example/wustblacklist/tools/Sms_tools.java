package com.example.wustblacklist.tools;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Sms_tools {

    // 查询最近短信收件箱联系人的电话
    public static String SeekLastSms(Context context) {
	
	Cursor cursor = context.getContentResolver().query(
		Uri.parse("content://sms/inbox"), null, null, null, "date desc");
	if (cursor.moveToFirst()&&cursor!=null&&cursor.getCount()!=0) {
	    do {
		String smsPhone = cursor
			.getString(cursor.getColumnIndex("address"));
		if(smsPhone.contains("+")){
		    smsPhone=smsPhone.substring(3);
		}
		if(!Contacts_tools.IsBlackListByPhone(context, smsPhone)){
		    return smsPhone;
		}
	    } while (cursor.moveToNext());
	} 
	return null;
    }
}
