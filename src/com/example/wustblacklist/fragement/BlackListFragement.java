package com.example.wustblacklist.fragement;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wustblacklist.R;
import com.example.wustblacklist.db.DBOpenHelper_main;
import com.example.wustblacklist.db.OperationBlackList;


public class BlackListFragement extends Fragment {
    private TextView textView;
    private ListView listView;
    private Context context;
    private OperationBlackList operationBlackList;
    private Cursor cursor;
    private View view;
    private SimpleCursorAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	view = inflater.inflate(R.layout.blacklist, null);
	context = getActivity();
	init();
	loadData();
	addListener();
	return view;
    }

    public void addListener() {
	// TODO Auto-generated method stub
	listView.setOnItemLongClickListener(new OnItemLongClickListener() {

	    @Override
	    public boolean onItemLongClick(AdapterView<?> parent, View view,
		    int position, long id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		TextView textView1 = (TextView) view
			.findViewById(R.id.textView1);
		TextView textView2 = (TextView) view
			.findViewById(R.id.textView2);
		final String name = textView1.getText().toString();
		final String number = textView2.getText().toString();
		if (!name.equals("陌生人")) {
		    builder.setTitle(name);
		}else{
		    builder.setTitle(number);
		}
		String[] items = new String[] { "呼叫", "发短信", "删除"};
		builder.setItems(items, new OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Uri uri;
			Intent intent;
			switch (which) {
			case 0:
			    uri = Uri.parse("tel:" + number);
			    intent = new Intent(Intent.ACTION_CALL, uri);
			    startActivity(intent);
			    break;
			case 1:
			    uri = Uri.parse("smsto://"+number);
			    intent = new Intent(Intent.ACTION_SENDTO, uri);
			    intent.putExtra("sms_body", "The SMS text");
			    startActivity(intent);
			    break;
			case 2:
			    operationBlackList = new OperationBlackList(context);
			    Boolean flag = operationBlackList.deleteBlackList("blacklist",
				    "number=?", new String[] { number });
			    if (flag) {
				if (!name.equals("陌生人")) {
				    Toast.makeText(context, "已成功删除" + name,
					    Toast.LENGTH_SHORT).show();
				}else{
				    Toast.makeText(context, "已成功删除" + number,
						Toast.LENGTH_SHORT).show();
				}
				cursor.requery();
				adapter.notifyDataSetChanged();
				textView.setText("已有" + cursor.getCount() + "人在黑名单中");
			    } else {
				Toast.makeText(context, "删除失败",
					Toast.LENGTH_SHORT).show();
			    }
			    break;
			default:
			    break;
			}
		    }
		});
		AlertDialog alertDialog=builder.create();
		alertDialog.show();
		return true;
	    }

	});
    }

    public void loadData() {
	// TODO Auto-generated method stub
	DBOpenHelper_main helper = new DBOpenHelper_main(context);
	SQLiteDatabase db = helper.getWritableDatabase();
	cursor = db.rawQuery("select * from blacklist order by _id ", null);
	@SuppressWarnings("deprecation")
	SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(context,
		R.layout.blacklist_item, cursor, new String[] { "name",
			"number" },
		new int[] { R.id.textView1, R.id.textView2 });
	if (cursor.getCount() != 0) {
	    listView.setAdapter(adapter1);
	    textView.setText("已有" + cursor.getCount() + "人在黑名单中");
	} else {
	    textView.setText("无数据");
	}
	adapter=adapter1;
    }

    public void init() {
	// TODO Auto-generated method stub
	textView = (TextView) view.findViewById(R.id.textView1);
	listView = (ListView) view.findViewById(R.id.listView1);
	operationBlackList = new OperationBlackList(context);
    }

    @Override
    public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
    }
}
