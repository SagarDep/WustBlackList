package com.example.wustblacklist.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wustblacklist.db_service.BlackListService;

public class OperationBlackList implements BlackListService{
    private DBOpenHelper_main helper = null;
    private int DBVersion = 0;
    
    public OperationBlackList(Context context) {
	helper=new DBOpenHelper_main(context);
    }

    public int getVersion() {
	return DBVersion;
    }

    @Override
    public boolean addBlackList(String database_name,ContentValues values) {
	// TODO Auto-generated method stub
	boolean flag = false;
	SQLiteDatabase database = null;
	long id = -1;
	try {
	    database = helper.getWritableDatabase();
	    id = database.insert(database_name, null, values);
	    flag = (id != -1 ? true : false);
	} catch (Exception e) {
	    // TODO: handle exception
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	return flag;
    }

    @Override
    public boolean deleteBlackList(String database_name,String whereClause, String[] whereArgs) {
	// TODO Auto-generated method stub
	boolean flag = false;
	SQLiteDatabase database = null;
	int count = 0;
	try {
	    database = helper.getWritableDatabase();
	    count = database.delete(database_name, whereClause, whereArgs);
	    flag = (count > 0 ? true : false);
	} catch (Exception e) {
	    // TODO: handle exception
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	return flag;
    }

    @Override
    public boolean updateBlackList(String database_name,ContentValues values, String whereClause,
	    String[] whereArgs) {
	boolean flag = false;
	SQLiteDatabase database = null;
	int count = 0;// 影响数据库的行数
	try {
	    database = helper.getWritableDatabase();
	    count = database.update(database_name, values, whereClause, whereArgs);
	    flag = (count > 0 ? true : false);
	} catch (Exception e) {
	    // TODO: handle exception
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	return flag;
    }

    @Override
    public boolean viewBlackList(String database_name,String selection, String[] selectionArgs) {
	// TODO Auto-generated method stub
	SQLiteDatabase database = null;
	Cursor cursor = null;
	Map<String, String> map = new HashMap<String, String>();
	try {
	    database = helper.getReadableDatabase();
	    cursor = database.query(true, database_name, null, selection,
		    selectionArgs, null, null, null, null);
	    int cols_len = cursor.getColumnCount();
	    while (cursor.moveToNext()) {
		for (int i = 0; i < cols_len; i++) {
		    String cols_name = cursor.getColumnName(i);
		    String cols_value = cursor.getString(cursor
			    .getColumnIndex(cols_name));
		    if (cols_value == null) {
			cols_value = "";
		    }
		    map.put(cols_name, cols_value);
		}
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	} finally {
	    if (database != null) {
		database.close();
	    }
	}
	if (!map.isEmpty()) {
	    return true;
	}
	return false;
    }
}
