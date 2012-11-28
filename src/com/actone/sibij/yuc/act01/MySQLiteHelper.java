package com.actone.sibij.yuc.act01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_CONTACTS = "contact";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_APPID = "app_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NUMBER = "number";
	
	private static final String DATABASE_NAME = "contacts.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private final static String DATABASE_CREATE = "create table "
				+ TABLE_CONTACTS + "(" 
				+ COLUMN_ID + " integer primary key autoincrement, " 
				+ COLUMN_APPID + " text not null, "
				+ COLUMN_NAME + " text not null, "
				+ COLUMN_NUMBER +  " text not null );";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version "+oldVersion+" to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CONTACTS);
		onCreate(db);
	}


}
