package com.example.wustblacklist.db_service;

import android.content.ContentValues;

public interface BlackListService {
    public boolean addBlackList(String database_name,ContentValues values);

    public boolean deleteBlackList(String database_name,String whereClause, String[] whereArgs);
    
    public boolean updateBlackList(String database_name,ContentValues values, String whereClause,
	    String[] whereArgs);
    public boolean viewBlackList(String database_name,String selection, String[] selectionArgs);
}
