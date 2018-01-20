package com.example.wustblacklist.fragement;

import java.util.HashMap;

import com.example.wustblacklist.MainActivity;
import com.example.wustblacklist.R;
import com.example.wustblacklist.R.layout;
import com.example.wustblacklist.sharedPreferences.BlackListSharedPreferences;
import com.example.wustblacklist.tools.Call_Block_Service;
import com.example.wustblacklist.tools.Contacts_tools;
import com.example.wustblacklist.tools.Sms_Block_Service;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsFragement extends Fragment {
    private final static int OP_REGISTER_BlANK_PHONE = 100;
    private final static int OP_REGISTER_POWER_OFF = 200;
    private final static int OP_REGISTER_DOWNTIME = 300;
    private final static int OP_CANCEL = 400;
    private final String ENABLE_SERVICE_BlANK_PHONE = "tel:**67*13800000000%23";
    private final String ENABLE_SERVICE_POWER_OFF = "tel:**67*13810538911%23";
    private final String ENABLE_SERVICE_DOWNTIME = "tel:**67*13701110216%23";
    private final String DISABLE_SERVICE = "tel:%23%2367%23";
    private String time;
    private Context context;
    private Button button[];
    private TextView textView[];
    private Switch switches[];
    private View view;
    private String[] items_blacklist = new String[] { "全部拦截（默认）", "只拦截黑名单中的电话",
	    "只拦截黑名单中的短信", "都不拦截" };
    private String[] items_callblock = new String[] { "忙碌（默认）", "关机", "停机",
	    "空号" };
    private RelativeLayout relativeLayout[];
    private Handler mHandler = new Handler() {
	public void handleMessage(Message response) {
	    int what = response.what;
	    Intent intent;
	    switch (what) {
	    case OP_REGISTER_BlANK_PHONE:
		intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(ENABLE_SERVICE_BlANK_PHONE));
		startActivity(intent);
		break;
	    case OP_REGISTER_DOWNTIME:
		intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(ENABLE_SERVICE_DOWNTIME));
		startActivity(intent);
		break;
	    case OP_REGISTER_POWER_OFF:
		intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(ENABLE_SERVICE_POWER_OFF));
		startActivity(intent);
		break;
	    case OP_CANCEL:
		intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(DISABLE_SERVICE));
		startActivity(intent);
		break;
	    }
	}
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	view = inflater.inflate(R.layout.settings, null);
	context = getActivity();
	init();
	loadData();
	addListener();
	return view;
    }

    public void loadData() {
	// TODO Auto-generated method stub
	//
	HashMap<String, String> hashMap = BlackListSharedPreferences
		.getSettingMessage(context);
	Boolean flag = Integer.parseInt(hashMap.get("night_block_switch")) == 0 ? false
		: true;
	Boolean flag2 = Integer.parseInt(hashMap.get("meeting_block_switch")) == 0 ? false
		: true;
	textView[0].setText(items_blacklist[Integer.parseInt(hashMap
		.get("blacklist_block_way"))]);
	textView[1].setText(items_callblock[Integer.parseInt(hashMap
		.get("call_block_way"))]);
	textView[2].setText(hashMap.get("sms_block_way"));
	textView[3].setText(hashMap.get("night_block_way"));
	textView[4].setText(hashMap.get("meeting_block_time"));
	textView[5].setText(hashMap.get("meeting_block_way"));
	switches[0].setChecked(flag);
	relativeLayout[0]
		.setVisibility(flag == true ? View.VISIBLE : View.GONE);
	switches[1].setChecked(flag2);
	relativeLayout[1].setVisibility(flag2 == true ? View.VISIBLE
		: View.GONE);
    }

    public void addListener() {
	// TODO Auto-generated method stub
	final HashMap<String, String> hashMap = new HashMap<String, String>();
	button[0].setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("拦截黑名单方式");
		builder.setItems(items_blacklist, new OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (which) {
			case 0:
			    hashMap.put("blacklist_block_way", "0");
			    textView[0].setText(items_blacklist[0]);
			    intent = new Intent(context,
				    Call_Block_Service.class);
			    context.startService(intent);
			    intent = new Intent(context,
				    Sms_Block_Service.class);
			    context.startService(intent);
			    break;
			case 1:
			    hashMap.put("blacklist_block_way", "1");
			    textView[0].setText(items_blacklist[1]);
			    intent = new Intent(context,
				    Call_Block_Service.class);
			    context.startService(intent);
			    intent = new Intent(context,
				    Sms_Block_Service.class);
			    context.stopService(intent);
			    break;
			case 2:
			    hashMap.put("blacklist_block_way", "2");
			    textView[0].setText(items_blacklist[2]);
			    intent = new Intent(context,
				    Call_Block_Service.class);
			    context.stopService(intent);
			    intent = new Intent(context,
				    Sms_Block_Service.class);
			    context.startService(intent);
			    break;
			case 3:
			    hashMap.put("blacklist_block_way", "3");
			    textView[0].setText(items_blacklist[3]);
			    intent = new Intent(context,
				    Call_Block_Service.class);
			    context.stopService(intent);
			    intent = new Intent(context,
				    Sms_Block_Service.class);
			    context.stopService(intent);
			    break;
			default:
			    break;
			}
			Toast.makeText(context,
				"启用黑名单拦截方式为:" + items_blacklist[which],
				Toast.LENGTH_SHORT).show();
			BlackListSharedPreferences.saveSettingMessage(context,
				hashMap);
		    }
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	    }
	});
	button[1].setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("拦截来电方式");
		builder.setItems(items_callblock, new OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Message message = mHandler.obtainMessage();
			switch (which) {
			case 0:
			    hashMap.put("call_block_way", "0");
			    textView[1].setText(items_callblock[0]);
			    message.what = OP_CANCEL;
			    mHandler.dispatchMessage(message);
			    break;
			case 1:
			    hashMap.put("call_block_way", "1");
			    textView[1].setText(items_callblock[1]);
			    message.what = OP_REGISTER_POWER_OFF;
			    mHandler.dispatchMessage(message);
			    break;
			case 2:
			    hashMap.put("call_block_way", "2");
			    textView[1].setText(items_callblock[2]);
			    message.what = OP_REGISTER_DOWNTIME;
			    mHandler.dispatchMessage(message);
			    break;
			case 3:
			    hashMap.put("call_block_way", "3");
			    textView[1].setText(items_callblock[3]);
			    message.what = OP_REGISTER_BlANK_PHONE;
			    mHandler.dispatchMessage(message);
			    break;
			default:
			    break;
			}
			Toast.makeText(context,
				"启用来电拦截方式为:" + items_callblock[which],
				Toast.LENGTH_SHORT).show();
			BlackListSharedPreferences.saveSettingMessage(context,
				hashMap);

		    }
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	    }
	});
	button[3].setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		final View layout = ((Activity) context).getLayoutInflater()
			.inflate(R.layout.mydialog_timepicker, null);
		final TimePicker timePicker = (TimePicker) layout
			.findViewById(R.id.timePicker1);
		final TimePicker timePicker2 = (TimePicker) layout
			.findViewById(R.id.timePicker2);
		final TextView textView = (TextView) layout
			.findViewById(R.id.textView1);
		final TextView textView2 = (TextView) layout
			.findViewById(R.id.textView2);
		time = SettingsFragement.this.textView[3].getText().toString();
		String temp[] = time.split("-");
		String temp2[] = temp[0].split(":");
		String temp3[] = temp[1].split(":");
		int currentHour_start = Integer.parseInt(temp2[0]);
		int currentMinute_start = Integer.parseInt(temp2[1]);
		int currentHour_end = Integer.parseInt(temp3[0]);
		int currentMinute_end = Integer.parseInt(temp3[1]);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(currentHour_start);
		timePicker.setCurrentMinute(currentMinute_start);
		textView.setText("开始时间是"
			+ String.format("%0" + 2 + "d", currentHour_start)
			+ ":"
			+ String.format("%0" + 2 + "d", currentMinute_start));
		timePicker
			.setOnTimeChangedListener(new OnTimeChangedListener() {

			    @Override
			    public void onTimeChanged(TimePicker view,
				    int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				textView.setText("开始时间是"
					+ String.format("%0" + 2 + "d",
						hourOfDay) + ":"
					+ String.format("%0" + 2 + "d", minute));
			    }
			});

		timePicker2.setIs24HourView(true);
		timePicker2.setCurrentHour(currentHour_end);
		timePicker2.setCurrentMinute(currentMinute_end);
		textView2.setText("结束时间是"
			+ String.format("%0" + 2 + "d", currentHour_end) + ":"
			+ String.format("%0" + 2 + "d", currentMinute_end));
		timePicker2
			.setOnTimeChangedListener(new OnTimeChangedListener() {

			    @Override
			    public void onTimeChanged(TimePicker view,
				    int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				textView2.setText("结束时间是"
					+ String.format("%0" + 2 + "d",
						hourOfDay) + ":"
					+ String.format("%0" + 2 + "d", minute));
			    }
			});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("设置夜间免打扰模式时间").setView(layout)
			.setPositiveButton("确定", new OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				// TODO Auto-generated
				// method stub
				SettingsFragement.this.textView[3].setText(String
					.format("%0" + 2 + "d",
						timePicker.getCurrentHour())
					+ ":"
					+ String.format("%0" + 2 + "d",
						timePicker.getCurrentMinute())
					+ "-"
					+ String.format("%0" + 2 + "d",
						timePicker2.getCurrentHour())
					+ ":"
					+ String.format("%0" + 2 + "d",
						timePicker2.getCurrentMinute()));
				hashMap.put("night_block_way",
					SettingsFragement.this.textView[3]
						.getText().toString());
				BlackListSharedPreferences.saveSettingMessage(context, hashMap);
			    }
			}).setNegativeButton("取消", null);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();// 让对话框显示
	    }
	});

	switches[0].setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
		    relativeLayout[0].setVisibility(View.VISIBLE);
		    hashMap.put("night_block_switch", "1");
		} else {
		    relativeLayout[0].setVisibility(View.GONE);
		    hashMap.put("night_block_switch", "0");
		}
		BlackListSharedPreferences.saveSettingMessage(context, hashMap);
	    }
	});
	switches[1].setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
		    relativeLayout[1].setVisibility(View.VISIBLE);
		    hashMap.put("meeting_block_switch", "1");
		} else {
		    relativeLayout[1].setVisibility(View.GONE);
		    hashMap.put("meeting_block_switch", "0");
		}
		BlackListSharedPreferences.saveSettingMessage(context, hashMap);
	    }
	});
    }

    public void init() {
	// TODO Auto-generated method stub
	button = new Button[6];
	button[0] = (Button) view.findViewById(R.id.toggleboutton1);
	button[1] = (Button) view.findViewById(R.id.toggleboutton2);
	button[2] = (Button) view.findViewById(R.id.toggleboutton3);
	button[3] = (Button) view.findViewById(R.id.toggleboutton4);
	button[4] = (Button) view.findViewById(R.id.toggleboutton5);
	button[5] = (Button) view.findViewById(R.id.toggleboutton6);

	textView = new TextView[6];
	textView[0] = (TextView) view.findViewById(R.id.textView1);
	textView[1] = (TextView) view.findViewById(R.id.textView2);
	textView[2] = (TextView) view.findViewById(R.id.textView3);
	textView[3] = (TextView) view.findViewById(R.id.textView4);
	textView[4] = (TextView) view.findViewById(R.id.textView5);
	textView[5] = (TextView) view.findViewById(R.id.textView6);

	switches = new Switch[2];
	switches[0] = (Switch) view.findViewById(R.id.switch1);
	switches[1] = (Switch) view.findViewById(R.id.switch2);

	relativeLayout = new RelativeLayout[2];
	relativeLayout[0] = (RelativeLayout) view
		.findViewById(R.id.RelativeLayout1);
	relativeLayout[1] = (RelativeLayout) view
		.findViewById(R.id.RelativeLayout2);
    }

}
