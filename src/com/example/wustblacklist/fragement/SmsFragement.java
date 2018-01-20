package com.example.wustblacklist.fragement;

import java.util.ArrayList;

import com.example.wustblacklist.R;
import com.example.wustblacklist.R.layout;
import com.example.wustblacklist.db.DBOpenHelper_main;
import com.example.wustblacklist.db.OperationBlackList;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class SmsFragement extends Fragment {
    private Button button;
    private ListView listView;
    private Context context;
    private OperationBlackList operationBlackList;
    private Cursor cursor;
    private View view;
    private SimpleCursorAdapter adapter;
    private ArrayList<String> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	view = inflater.inflate(R.layout.smslist, null);
	context = getActivity();
	init();
	loadData();
	addListener();
	return view;
    }

    public void loadData() {
	// TODO Auto-generated method stub
	DBOpenHelper_main helper = new DBOpenHelper_main(context);
	SQLiteDatabase db = helper.getWritableDatabase();
	cursor = db.rawQuery("select * from smsblock order by _id desc", null);
	@SuppressWarnings("deprecation")
	SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(context,
		R.layout.log_item, cursor, new String[] { "name", "number",
			"time" }, new int[] { R.id.textView1, R.id.textView2,
			R.id.textView3 },
		CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	if (cursor.getCount() != 0) {
	    listView.setAdapter(adapter1);
	}
	arrayList = new ArrayList<String>();
	if (cursor.moveToFirst()) {
	    do {
		String sms = cursor.getString(cursor.getColumnIndex("sms"));
		arrayList.add(sms);
	    } while (cursor.moveToNext());
	}

	adapter = adapter1;
    }

    public void addListener() {
	// TODO Auto-generated method stub
	button.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("ɾ���绰��������¼");
		builder.setMessage("��ȷ��ɾ���绰��������¼��ȫ����ϵ����");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated
			// method stub
			operationBlackList = new OperationBlackList(context);
			Boolean flag = operationBlackList.deleteBlackList(
				"smsblock", null, null);
			if (flag) {
			    Toast.makeText(context, "��ɾ�����ж��ź�������¼",
				    Toast.LENGTH_SHORT).show();
			    cursor.requery();
			    adapter.notifyDataSetChanged();
			} else {
			    Toast.makeText(context, "��ʱ���κζ������ؼ�¼",
				    Toast.LENGTH_SHORT).show();
			}
		    }
		});
		builder.setNegativeButton("ȡ��", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	    }
	});
	listView.setOnItemLongClickListener(new OnItemLongClickListener() {

	    @Override
	    public boolean onItemLongClick(AdapterView<?> parent, View view,
		    final int position, long id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		TextView textView = (TextView) view
			.findViewById(R.id.textView1);
		TextView textView2 = (TextView) view
			.findViewById(R.id.textView2);
		final TextView textView3 = (TextView) view
			.findViewById(R.id.textView3);
		final String name = textView.getText().toString();
		final String number = textView2.getText().toString();
		if (!name.equals("İ����")) {
		    builder.setTitle(name);
		} else {
		    builder.setTitle(number);
		}
		String[] items = new String[] { "�鿴��������", "�ظ�����", "����",
			"�ָ����ֻ��ռ���", "ɾ���Ự" };
		builder.setItems(items, new OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Uri uri;
			Intent intent;
			switch (which) {
			case 0:
			    new AlertDialog.Builder(context)
				    .setTitle("��������")
				    .setMessage(
					    "�������ԣ�" + number + "\n" + "�������ݣ�"
						    + arrayList.get(position))
				    .setPositiveButton("ȷ��", null).show();
			    break;
			case 1:
			    uri = Uri.parse("smsto://" + number);
			    intent = new Intent(Intent.ACTION_SENDTO, uri);
			    intent.putExtra("sms_body", "The SMS text");
			    startActivity(intent);
			    break;
			case 2:
			    uri = Uri.parse("tel:" + number);
			    intent = new Intent(Intent.ACTION_CALL, uri);
			    startActivity(intent);
			    break;
			case 3:
			    ContentResolver resolver = context
				    .getContentResolver();
			    uri = Uri.parse("content://sms/");
			    ContentValues values = new ContentValues();
			    values.put("address", number);
			    values.put("type", 1);
			    values.put("date", System.currentTimeMillis());
			    values.put("body", arrayList.get(position));
			    resolver.insert(uri, values);
			    operationBlackList = new OperationBlackList(context);
			    operationBlackList.deleteBlackList("smsblock",
				    "time=?", new String[] { textView3
					    .getText().toString() });
			    // ��Ҫ��������simplecursoradapter��ˢ��
			    arrayList.remove(position);
			    cursor.requery();
			    adapter.notifyDataSetChanged();
			    Toast.makeText(context, "�ѳɹ��ָ����ŵ��ռ����У�",
				    Toast.LENGTH_SHORT).show();
			    break;
			case 4:
			    operationBlackList = new OperationBlackList(context);
			    Boolean flag = operationBlackList.deleteBlackList(
				    "smsblock", "time=?",
				    new String[] { textView3.getText()
					    .toString() });
			    if (flag) {
				if (!name.equals("İ����")) {
				    Toast.makeText(context, "�ѳɹ�ɾ��" + name,
					    Toast.LENGTH_SHORT).show();
				} else {
				    Toast.makeText(context, "�ѳɹ�ɾ��" + number,
					    Toast.LENGTH_SHORT).show();
				}
				// ��Ҫ��������simplecursoradapter��ˢ��
				arrayList.remove(position);
				cursor.requery();
				adapter.notifyDataSetChanged();
			    } else {
				Toast.makeText(context, "ɾ��ʧ��",
					Toast.LENGTH_SHORT).show();
			    }
			    break;

			default:
			    break;
			}
		    }
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		return true;
	    }

	});
    }

    public void init() {
	// TODO Auto-generated method stub
	listView = (ListView) view.findViewById(R.id.listView1);
	operationBlackList = new OperationBlackList(context);
	button = (Button) view.findViewById(R.id.button1);
    }

    @Override
    public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
    }
}
