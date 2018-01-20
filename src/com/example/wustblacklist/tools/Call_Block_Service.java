package com.example.wustblacklist.tools;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.wustblacklist.db.OperationBlackList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class Call_Block_Service extends Service {
    private static final String TAG = "tag";
    private IncomingCallReceiver mReceiver;
    private ITelephony iTelephony;
    private AudioManager mAudioManager;
    private OperationBlackList operationBlackList;

    @Override
    public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	unregisterReceiver(mReceiver);
    }

    @Override
    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	Log.i("TAG", "����������");
	mReceiver = new IncomingCallReceiver();
	mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

	IntentFilter filter = new IntentFilter(
		"android.intent.action.PHONE_STATE");
	registerReceiver(mReceiver, filter);// ע��BroadcastReceiver
	// ���÷����ȡ���ص�endcall����

	TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	try {
	    Method getITelephonyMethod = TelephonyManager.class
		    .getDeclaredMethod("getITelephony", (Class[]) null);
	    getITelephonyMethod.setAccessible(true);
	    iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyMgr,
		    (Object[]) null);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private class IncomingCallReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
	    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
	    Log.i(TAG, "State: " + state);

	    String number = intent
		    .getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
	    Log.d(TAG, "Incomng Number: " + number);

	    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {// �绰��������
		if (Contacts_tools.IsBlackListByPhone(context, number)) {// ����ָ���ĵ绰����
		    // �Ⱦ�������
		    mAudioManager
			    .setRingerMode(AudioManager.RINGER_MODE_SILENT);
		    Log.d(TAG, "Turn ringtone silent");

		    try {
			operationBlackList = new OperationBlackList(context);
			ContentValues contentValues = new ContentValues();
			if (Contacts_tools.SeekNameByPhone(context, number) != null) {
			    contentValues.put("name", Contacts_tools
				    .SeekNameByPhone(context, number));
			} else {
			    contentValues.put("name", "İ����");
			}
			contentValues.put("number", number);
			String time = TimeTool.milliTime2LongStr(System
				.currentTimeMillis());
			contentValues.put("time", time);
			operationBlackList.addBlackList("callblock",
				contentValues);
			// �Ҷϵ绰
			Toast.makeText(context, "�ѳɹ����ص绰����ӵ���¼�У�",
				Toast.LENGTH_SHORT).show();
			iTelephony.endCall();
		    } catch (RemoteException e) {
			e.printStackTrace();
		    }

		    // �ٻָ���������
		    mAudioManager
			    .setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
	    }
	}
    }

}
