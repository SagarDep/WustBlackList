package com.example.wustblacklist.tools;

import java.util.HashMap;

import com.example.wustblacklist.MainActivity;
import com.example.wustblacklist.db.OperationBlackList;
import com.example.wustblacklist.fragement.BlackListFragement;

import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.widget.Toast;

public class Contacts_tools {
    // 查询最近来电的电话，包括未接，已接，挂断
    public static String SeekLastContacts(Context context) {
	Cursor cursor = context.getContentResolver().query(
		CallLog.Calls.CONTENT_URI, null, null, null, "date desc");
	if (cursor!=null&&cursor.moveToFirst()&&cursor.getCount()!=0) {
	    do {
		// 呼叫类型
		// 号码
		String number = cursor.getString(cursor
			.getColumnIndex(Calls.NUMBER));
		if (!IsBlackListByPhone(context, number)&&Integer.parseInt(cursor.getString(cursor.getColumnIndex(Calls.TYPE)))!=Calls.OUTGOING_TYPE) {
		    cursor.close();
		    return number;
		}
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return null;
    }

    // 根据电话来查询是否在联系人列表中，如有则返回姓名，如无则返回null
    public static String SeekNameByPhone(Context context, String number) {
	// 取得电话本中开始一项的光标
	Cursor cursor = context.getContentResolver().query(
		ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	if (cursor!=null&&cursor.moveToFirst()&&cursor.getCount()!=0) {
	    // 取得联系人ID
	    do {
		String contactId = cursor.getString(cursor
			.getColumnIndex(ContactsContract.Contacts._ID));
		String contactName = cursor
			.getString(cursor
				.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		int hasPhone = cursor
			.getInt(cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
		if (hasPhone > 0) {
		    Cursor phone = context.getContentResolver().query(
			    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			    null,
			    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
				    + " = ?", new String[] { contactId }, null);
		    // 取得电话号码(可能存在多个号码)
		    if (phone.moveToFirst()) {
			do {
			    String strPhoneNumber = phone
				    .getString(phone
					    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			    strPhoneNumber=strPhoneNumber.replaceAll(" ", "");
			    if (number.equals(strPhoneNumber)) {
				return contactName;
			    }
			} while (phone.moveToNext());
		    }
		    phone.close();
		}
	    } while (cursor.moveToNext());
	}
	cursor.close();
	return null;

    }

    // 根据要添加的号码来查询是否在黑名单中
    public static boolean IsBlackListByPhone(Context context, String number) {
	OperationBlackList operationBlackList = new OperationBlackList(context);
	if (operationBlackList.viewBlackList("blacklist","number=?",
		new String[] { number })) {
	    return true;
	}
	return false;
    }

    // 添加姓名和号码到黑名单中
    public static void addBlackListMessage(Context context, String name,
	    String number) {
	OperationBlackList operationBlackList = new OperationBlackList(context);
	ContentValues contentValues = new ContentValues();
	contentValues.put("name", name);
	contentValues.put("number", number);
	if (IsBlackListByPhone(context, number)) {
	    Toast.makeText(context, "黑名单中已有此号码", Toast.LENGTH_SHORT).show();
	} else {
	    Boolean flag = operationBlackList.addBlackList("blacklist",contentValues);
	    if (flag) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
		((Activity) context).finish();

		Toast.makeText(context, "已添加" + name + "到黑名单中",
			Toast.LENGTH_SHORT).show();
	    } else {
		Toast.makeText(context, "添加黑名单失败", Toast.LENGTH_SHORT).show();
	    }
	}
    }
}
