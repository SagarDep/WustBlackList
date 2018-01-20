package com.example.wustblacklist.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.wustblacklist.db.OperationBlackList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class Sms_Block_Service extends Service {
    private SMSBroadcastReceiver sBroadcastReceiver;
    private OperationBlackList operationBlackList;

    public Sms_Block_Service() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	sBroadcastReceiver = new SMSBroadcastReceiver();

	IntentFilter filter = new IntentFilter(
		"android.provider.Telephony.SMS_RECEIVED");
	filter.setPriority(1000);// �������ȼ�
	registerReceiver(sBroadcastReceiver, filter);

    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(sBroadcastReceiver);
    }
    public class SMSBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
	    if (intent.getAction().equals(
		    "android.provider.Telephony.SMS_RECEIVED")) {
		// �������´�����
		this.abortBroadcast();
		StringBuffer sb = new StringBuffer();
		String sender = null;
		String content = null;
		String sendtime = null;
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
		    // ͨ��pdus��ý��յ������ж�����Ϣ����ȡ�������ݣ�
		    Object[] pdus = (Object[]) bundle.get("pdus");
		    // �������Ŷ������飻
		    SmsMessage[] mges = new SmsMessage[pdus.length];
		    for (int i = 0; i < pdus.length; i++) {
			// ��ȡ�����������ݣ���pdu��ʽ��,�����ɶ��Ŷ���
			mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
		    }
		    for (SmsMessage mge : mges) {
			sb.append(mge.getMessageBody());

			sender = mge.getDisplayOriginatingAddress();// ��ȡ���ŵķ�����
			content = mge.getMessageBody();// ��ȡ���ŵ�����
			Date date = new Date(mge.getTimestampMillis());
			SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
			sendtime = format.format(date);// ��ȡ���ŷ���ʱ�䣻
			// SmsManager manager = SmsManager.getDefault();
			// manager.sendTextMessage("5556",
			// null,"������:"+sender+"-----����ʱ��:"+sendtime+"----����:"+content,
			// null, null);//�����ص��Ķ��ŷ��͵�ָ�����ֻ����˴�Ϊ5556;
			if (sender.contains("+")) {
			    sender = sender.substring(3);
			}
			if (Contacts_tools.IsBlackListByPhone(context, sender)) {
			    // �����ֻ���Ϊ5556�Ķ��ţ����ﻹ����ʱ��һЩ������Ѹ���Ϣ���͵������˵��ֻ��ȵȡ�
			    operationBlackList = new OperationBlackList(context);
			    ContentValues contentValues = new ContentValues();
			    if (Contacts_tools.SeekNameByPhone(context, sender) != null) {
				contentValues.put("name", Contacts_tools
					.SeekNameByPhone(context, sender));
			    } else {
				contentValues.put("name", "İ����");
			    }
			    contentValues.put("number", sender);
			    String time = TimeTool.milliTime2LongStr(System
				    .currentTimeMillis());
			    contentValues.put("time", time);
			    contentValues.put("sms", sb.toString());
			    operationBlackList.addBlackList("smsblock", contentValues);
			    Toast.makeText(context, "�ѳɹ����ض��Ų���ӵ���¼�У�", Toast.LENGTH_SHORT).show();
			    abortBroadcast();
			}
		    }
		}
	    }

	}
    }
}
