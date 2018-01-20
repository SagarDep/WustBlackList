package com.example.wustblacklist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper_main extends SQLiteOpenHelper {
    private static String name = "WustBlackList.db";// 表示数据库的名称
    private static int version = 1;// 表示数据库的版本号码

    public DBOpenHelper_main(Context context) {
	super(context, name, null, version);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	// TODO Auto-generated method stub
	db.execSQL("create table if not exists blacklist(_id integer primary key autoincrement,name varchar(10),number varchar(30))");
	db.execSQL("create table if not exists callblock(_id integer primary key autoincrement,name varchar(10),number varchar(30),time varchar(30))");
	db.execSQL("create table if not exists smsblock(_id integer primary key autoincrement,name varchar(10),number varchar(30),time varchar(30),sms varchar(120))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO Auto-generated method stub
	db.execSQL("drop table if exists blacklist");
	db.execSQL("drop table if exists callblock");
	db.execSQL("drop table if exists smsblock");
	onCreate(db);
    }
}
