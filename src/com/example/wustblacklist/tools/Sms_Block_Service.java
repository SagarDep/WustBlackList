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
	filter.setPriority(1000);// 设置优先级
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
		// 不再往下传播；
		this.abortBroadcast();
		StringBuffer sb = new StringBuffer();
		String sender = null;
		String content = null;
		String sendtime = null;
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
		    // 通过pdus获得接收到的所有短信消息，获取短信内容；
		    Object[] pdus = (Object[]) bundle.get("pdus");
		    // 构建短信对象数组；
		    SmsMessage[] mges = new SmsMessage[pdus.length];
		    for (int i = 0; i < pdus.length; i++) {
			// 获取单条短信内容，以pdu格式存,并生成短信对象；
			mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
		    }
		    for (SmsMessage mge : mges) {
			sb.append(mge.getMessageBody());

			sender = mge.getDisplayOriginatingAddress();// 获取短信的发送者
			content = mge.getMessageBody();// 获取短信的内容
			Date date = new Date(mge.getTimestampMillis());
			SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
			sendtime = format.format(date);// 获取短信发送时间；
			// SmsManager manager = SmsManager.getDefault();
			// manager.sendTextMessage("5556",
			// null,"发送人:"+sender+"-----发送时间:"+sendtime+"----内容:"+content,
			// null, null);//把拦截到的短信发送到指定的手机，此处为5556;
			if (sender.contains("+")) {
			    sender = sender.substring(3);
			}
			if (Contacts_tools.IsBlackListByPhone(context, sender)) {
			    // 屏蔽手机号为5556的短信，这里还可以时行一些处理，如把该信息发送到第三人的手机等等。
			    operationBlackList = new OperationBlackList(context);
			    ContentValues contentValues = new ContentValues();
			    if (Contacts_tools.SeekNameByPhone(context, sender) != null) {
				contentValues.put("name", Contacts_tools
					.SeekNameByPhone(context, sender));
			    } else {
				contentValues.put("name", "陌生人");
			    }
			    contentValues.put("number", sender);
			    String time = TimeTool.milliTime2LongStr(System
				    .currentTimeMillis());
			    contentValues.put("time", time);
			    contentValues.put("sms", sb.toString());
			    operationBlackList.addBlackList("smsblock", contentValues);
			    Toast.makeText(context, "已成功拦截短信并添加到记录中！", Toast.LENGTH_SHORT).show();
			    abortBroadcast();
			}
		    }
		}
	    }

	}
    }
}
