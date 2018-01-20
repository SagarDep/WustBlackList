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
    // ��ѯ�������ĵ绰������δ�ӣ��ѽӣ��Ҷ�
    public static String SeekLastContacts(Context context) {
	Cursor cursor = context.getContentResolver().query(
		CallLog.Calls.CONTENT_URI, null, null, null, "date desc");
	if (cursor!=null&&cursor.moveToFirst()&&cursor.getCount()!=0) {
	    do {
		// ��������
		// ����
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

    // ���ݵ绰����ѯ�Ƿ�����ϵ���б��У������򷵻������������򷵻�null
    public static String SeekNameByPhone(Context context, String number) {
	// ȡ�õ绰���п�ʼһ��Ĺ��
	Cursor cursor = context.getContentResolver().query(
		ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	if (cursor!=null&&cursor.moveToFirst()&&cursor.getCount()!=0) {
	    // ȡ����ϵ��ID
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
		    // ȡ�õ绰����(���ܴ��ڶ������)
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

    // ����Ҫ��ӵĺ�������ѯ�Ƿ��ں�������
    public static boolean IsBlackListByPhone(Context context, String number) {
	OperationBlackList operationBlackList = new OperationBlackList(context);
	if (operationBlackList.viewBlackList("blacklist","number=?",
		new String[] { number })) {
	    return true;
	}
	return false;
    }

    // ��������ͺ��뵽��������
    public static void addBlackListMessage(Context context, String name,
	    String number) {
	OperationBlackList operationBlackList = new OperationBlackList(context);
	ContentValues contentValues = new ContentValues();
	contentValues.put("name", name);
	contentValues.put("number", number);
	if (IsBlackListByPhone(context, number)) {
	    Toast.makeText(context, "�����������д˺���", Toast.LENGTH_SHORT).show();
	} else {
	    Boolean flag = operationBlackList.addBlackList("blacklist",contentValues);
	    if (flag) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
		((Activity) context).finish();

		Toast.makeText(context, "�����" + name + "����������",
			Toast.LENGTH_SHORT).show();
	    } else {
		Toast.makeText(context, "��Ӻ�����ʧ��", Toast.LENGTH_SHORT).show();
	    }
	}
    }
}
