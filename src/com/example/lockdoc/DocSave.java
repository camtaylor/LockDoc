package com.example.lockdoc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DocSave {

	private static final String LOGTAG = "LOCDOCDB";

	private static final String DATABASE_NAME = "docs.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_DOCS = "docs";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_DATE = "date";

	private DocSQLiteHelper helper;
	private final Context context;
	private SQLiteDatabase database;
	
	public class DocSQLiteHelper extends SQLiteOpenHelper {


		private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_DOCS
				+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_TYPE
				+ " TEXT NOT NULL, " + COLUMN_DATE + " TEXT NOT NULL);";

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
	
	public DocSave(Context c){
		context = c;
	}
	
	public DocSave open() throws SQLException{
		helper = new DocSQLiteHelper(context);
		database = helper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		helper.close();
	}

	public long createEntry(String name, String type, String date) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_NAME, name);
		cv.put(COLUMN_TYPE, type);
		cv.put(COLUMN_DATE, date);
		return database.insert(TABLE_DOCS, null, cv);
	}

	public String getData() {
		// TODO Auto-generated method stub
		String[] columns = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_TYPE, COLUMN_DATE};
		Cursor c = database.query(TABLE_DOCS, columns, null, null, null, null, null, null);
		String result = "";
		
		int iRow = c.getColumnIndex(COLUMN_ID);
		int iName = c.getColumnIndex(COLUMN_NAME);
		int iType = c.getColumnIndex(COLUMN_TYPE);
		int iDate = c.getColumnIndex(COLUMN_DATE);
		
		//cycle through database
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iType) + " " + c.getString(iDate) + "\n";
		}
		
		return result;
	}

}
