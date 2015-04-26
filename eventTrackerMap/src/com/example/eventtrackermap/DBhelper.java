package com.example.eventtrackermap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
	
	public static final String TABLE_PARTY = "party";
	 public static final String PARTY_ID = "_id";
	 public static final String PARTY_THEME = "theme";
	 public static final String PARTY_DATE = "date";
	 public static final String PARTY_COMMENT = "comment";
	 public static final String PARTY_LOCATION = "location";
	 public static final String PARTY_DRESSING = "dressing";
	 public static final String PARTY_FOOD = "food";
	 
	 
	 static final String DB_NAME = "PARTY.DB";
	 static final int DB_VERSION = 1;
	 
	 private static final String CREATE_TABLE = "create table "
			+ TABLE_PARTY + "(" + 
			  PARTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
			  PARTY_THEME + " TEXT NOT NULL, " + 
			  PARTY_DATE + " TEXT NOT NULL, " + 
			  PARTY_COMMENT + " TEXT NOT NULL," +
			  PARTY_LOCATION + " TEXT NOT NULL, " +
			  PARTY_DRESSING + " TEXT NOT NULL, " +
			  PARTY_FOOD + " TEXT NOT NULL);";
	//Create a helper object to create, open, and/or manage a database.
	 public DBhelper(Context context) {	
		 super(context, DB_NAME, null, DB_VERSION); 
	
	 }
	
	 public void onCreate(SQLiteDatabase db) {
		 db.execSQL(CREATE_TABLE);
		}	
	
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTY);
		 onCreate(db);
		}
	
	
}

