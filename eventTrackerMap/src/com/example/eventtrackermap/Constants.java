package com.example.eventtrackermap;

public class Constants {


	public static final String DATABASE_NAME = "destination.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "dList";
	public static final String KEY_DNAME = "dName";
	public static final String KEY_TRIP = "trip";
	public static final String KEY_STREET_ADD = "STREET_ADD";
	public static final String KEY_CITY = "CITY";
	public static final String KEY_ZIP = "ZIP";
	public static final String KEY_MEMOS = "MEMOS";
	public static final String KEY_ID = "id integer primary key autoincrement";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS dList ("
			+ Constants.KEY_ID + ","  + " text,"
			+ KEY_TRIP +  " text," 
			+ KEY_DNAME +   " text," 
			+ KEY_STREET_ADD  +  " text," 
			+ KEY_CITY + " text," 
			+ KEY_ZIP + " text,"
			+ KEY_MEMOS +  " text" 
			+ " );";
	

}
