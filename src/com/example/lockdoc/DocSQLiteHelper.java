package com.example.lockdoc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DocSQLiteHelper extends SQLiteOpenHelper{
	
	
	private static final String LOGTAG = "LOCDOCDB";
	
	private static final String DATABASE_NAME = "docs.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_DOCS = "docs";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_DATE = "date";
	//TODO add image column
	
	private static final String TABLE_CREATE = 
			"create table" + TABLE_DOCS + " (" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_NAME + " text not null" +
			COLUMN_TYPE + " text not null" +
			COLUMN_DATE + " text not null" +
			");";
	

	public DocSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		Log.i(LOGTAG, "Table has been created");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCS);
		onCreate(db);
	}

}
