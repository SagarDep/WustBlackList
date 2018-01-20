package com.example.wustblacklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;
import com.example.wustblacklist.db.OperationBlackList;
import com.example.wustblacklist.fragement.BlackListFragement;
import com.example.wustblacklist.fragement.SmsFragement;
import com.example.wustblacklist.fragement.SettingsFragement;
import com.example.wustblacklist.fragement.TeleFragement;
import com.example.wustblacklist.sharedPreferences.BlackListSharedPreferences;
import com.example.wustblacklist.tools.Call_Block_Service;
import com.example.wustblacklist.tools.Contacts_tools;
import com.example.wustblacklist.tools.Sms_Block_Service;
import com.example.wustblacklist.tools.Sms_Block_Service.SMSBroadcastReceiver;
import com.example.wustblacklist.tools.Sms_tools;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private static final int REQUEST_TEXT = 100;
    private ViewPager viewPager;
    private List<Fragment> items;
    private List<String> titles;
    private myFragementAdapter adapter;
    private Button button, button2;
    private String[] item;
    private Context context;
    private String number1, number2, name1, name2;
    private OperationBlackList operationBlackList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	// viewpager的写法
	context = MainActivity.this;
	viewPager = (ViewPager) findViewById(R.id.viewpager);
	items = new ArrayList<Fragment>();
	BlackListFragement blackList = new BlackListFragement();
	SmsFragement ems = new SmsFragement();
	TeleFragement tele = new TeleFragement();
	SettingsFragement settings = new SettingsFragement();
	items.add(blackList);
	items.add(tele);
	items.add(ems);
	items.add(settings);
	titles = new ArrayList<String>();
	titles.add("黑名单");
	titles.add("电话拦截");
	titles.add("短信拦截");
	titles.add("设置");
	adapter = new myFragementAdapter(getSupportFragmentManager());
	viewPager.setAdapter(adapter);
	viewPager.setOffscreenPageLimit(4);
	HashMap<String, String> hashMap = BlackListSharedPreferences
		.getSettingMessage(context);
	int temp = Integer.parseInt(hashMap.get("blacklist_block_way"));
	Intent intent;
	switch (temp) {
	case 0:
	    intent = new Intent(MainActivity.this, Call_Block_Service.class);
	    startService(intent);
	    intent = new Intent(MainActivity.this, Sms_Block_Service.class);
	    startService(intent);
	    break;
	case 1:
	    intent = new Intent(MainActivity.this, Call_Block_Service.class);
	    startService(intent);
	    intent = new Intent(MainActivity.this, Sms_Block_Service.class);
	    stopService(intent);
	    break;
	case 2:
	    intent = new Intent(MainActivity.this, Call_Block_Service.class);
	    stopService(intent);
	    intent = new Intent(MainActivity.this, Sms_Block_Service.class);
	    startService(intent);
	    break;
	default:
	    intent = new Intent(MainActivity.this, Call_Block_Service.class);
	    stopService(intent);
	    intent = new Intent(MainActivity.this, Sms_Block_Service.class);
	    stopService(intent);
	    break;
	}
	// 控件的响应事件
	button = (Button) findViewById(R.id.add_blacklist);

	number1 = Contacts_tools.SeekLastContacts(context);
	if (number1 != null) {
	    name1 = Contacts_tools.SeekNameByPhone(context, number1);
	}
	number2 = Sms_tools.SeekLastSms(context);
	if (number2 != null) {
	    name2 = Contacts_tools.SeekNameByPhone(context, number2);
	}
	button.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("添加到黑名单");
		item = new String[] { "从联系人中添加", "添加最近来电号码：\n", "添加最近短信号码\n",
			"手动输入号码" };

		if (number1 != null) {
		    if (name1 != null) {
			item[1] += name1;
		    } else {
			item[1] += number1;
		    }
		} else {
		    item[1] += "无";
		}

		if (number2 != null) {
		    if (name2 != null) {
			item[2] += name2;
		    } else {
			item[2] += number2;
		    }
		} else {
		    item[2] += "无";
		}
		builder.setItems(item, new OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub

			switch (which) {
			case 0:
			    Intent intent = new Intent(Intent.ACTION_PICK,
				    Contacts.CONTENT_URI);
			    startActivityForResult(intent, REQUEST_TEXT);
			    break;
			case 1:
			    if (number1 != null) {
				if (name1 != null) {
				    Contacts_tools.addBlackListMessage(context,
					    name1, number1);
				} else {
				    Contacts_tools.addBlackListMessage(context,
					    "陌生人", number1);
				}
			    } else {
				Toast.makeText(context,
					"手机中无任何通讯记录或者已将所有联系人加入黑名单中",
					Toast.LENGTH_SHORT).show();
			    }
			    break;
			case 2:
			    if (number2 != null) {
				if (name2 != null) {
				    Contacts_tools.addBlackListMessage(context,
					    name2, number2);
				} else {
				    Contacts_tools.addBlackListMessage(context,
					    "陌生人", number2);
				}
			    } else {
				Toast.makeText(context,
					"手机中无任何短信联系人或者已将所有短信联系人加入黑名单中",
					Toast.LENGTH_SHORT).show();
			    }
			    break;
			case 3:
			    final View layout = getLayoutInflater().inflate(
				    R.layout.mydialog_addblacklist,
				    (ViewGroup) findViewById(R.id.mydialog));
			    AlertDialog.Builder builder1 = new AlertDialog.Builder(
				    context);
			    builder1.setTitle("手动添加黑名单")
				    .setView(layout)
				    .setPositiveButton("确定",
					    new OnClickListener() {
						@Override
						public void onClick(
							DialogInterface dialog,
							int which) {
						    // TODO Auto-generated
						    // method stub
						    EditText editText1 = (EditText) layout
							    .findViewById(R.id.editText1);
						    EditText editText2 = (EditText) layout
							    .findViewById(R.id.editText2);
						    String name = editText1
							    .getText()
							    .toString();
						    String number = editText2
							    .getText()
							    .toString();
						    if (name.equals("")
							    || number
								    .equals("")) {
							Toast.makeText(
								context,
								"请输入完整信息！",
								Toast.LENGTH_SHORT)
								.show();
						    } else {
							Contacts_tools
								.addBlackListMessage(
									context,
									name,
									number);
						    }

						}
					    }).setNegativeButton("取消", null);
			    AlertDialog alertDialog = builder1.create();
			    alertDialog.show();// 让对话框显示
			    break;
			default:
			    break;
			}
		    }
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	    }
	});
	button2 = (Button) findViewById(R.id.delete_blacklist);
	button2.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("删除全部联系人");
		builder.setMessage("你确定删除黑名单的全部联系人吗？");
		builder.setPositiveButton("确定", new OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated
			// method stub
			operationBlackList = new OperationBlackList(context);
			Boolean flag = operationBlackList.deleteBlackList(
				"blacklist", null, null);
			if (flag) {
			    Toast.makeText(context, "已删除所有黑名单",
				    Toast.LENGTH_SHORT).show();
			    Intent intent = new Intent(context,
				    MainActivity.class);
			    startActivity(intent);
			    finish();
			} else {
			    Toast.makeText(context, "请添加黑名单",
				    Toast.LENGTH_SHORT).show();
			}
		    }
		});
		builder.setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	    }
	});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	String phoneString = "";
	if (requestCode == REQUEST_TEXT && resultCode == Activity.RESULT_OK) {
	    Cursor cursor = getContentResolver().query(data.getData(), null,
		    null, null, null);
	    if (cursor.moveToFirst()) {
		String contactId = cursor.getString(cursor
			.getColumnIndex(Contacts._ID));
		String name = cursor.getString(cursor
			.getColumnIndex(Contacts.DISPLAY_NAME));
		Cursor phoneCursor = getContentResolver().query(
			CommonDataKinds.Phone.CONTENT_URI, null,
			CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
			null, null);
		while (phoneCursor.moveToNext()) {
		    phoneString = phoneCursor.getString(phoneCursor
			    .getColumnIndex(CommonDataKinds.Phone.NUMBER));
		    phoneString = phoneString.replaceAll(" ", "");
		    Contacts_tools.addBlackListMessage(context, name,
			    phoneString);
		}
	    }
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    public class myFragementAdapter extends FragmentPagerAdapter {

	public myFragementAdapter(FragmentManager fm) {
	    super(fm);
	    // TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
	    // TODO Auto-generated method stub
	    return items.get(arg0);
	}

	public CharSequence getPageTitle(int position) {
	    return titles.get(position);
	}

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return items.size();
	}

    }

}
