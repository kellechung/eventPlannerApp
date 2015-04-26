package com.example.eventtrackermap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLController {

		private DBhelper dbhelper;
	    private Context ourcontext;
	    private SQLiteDatabase database;
	   //a method to instantiate a Context (for other class to use)
	    public SQLController(Context c) {
	        ourcontext = c;
	    }
	    
	public SQLController open() 
			throws SQLException {
	//Create a helper object to create, open, and/or manage a database.
    dbhelper = new DBhelper(ourcontext);//(provide "supervisor" i.e. Context,db name,null,db VER)	
    database = dbhelper.getWritableDatabase();//db won't be created until this method being called
    	return this;
    }
	
	public void close() {
        dbhelper.close();
    }
	//to insert new data set
	 public void insertData(Party info) {
	        ContentValues cv = new ContentValues();
	        //(key the name of the value to put, value the data for the value to put)
	        cv.put(DBhelper.PARTY_THEME, info.getTheme());
	        cv.put(DBhelper.PARTY_DATE, info.getDate());
	        cv.put(DBhelper.PARTY_COMMENT, info.getComment());
	        cv.put(DBhelper.PARTY_LOCATION, info.getLocation());
	        cv.put(DBhelper.PARTY_DRESSING, info.getDressing());
	        cv.put(DBhelper.PARTY_FOOD, info.getFood());
	        database.insert(DBhelper.TABLE_PARTY, null, cv);
	    }				//(table name, if values can be null(if not, set null), values to insert)
	 //to read data sets
	 public Cursor readData() {
	        String[] allColumns = new String[] {DBhelper.PARTY_ID, DBhelper.PARTY_THEME, DBhelper.PARTY_DATE, 
	        		DBhelper.PARTY_COMMENT, DBhelper.PARTY_LOCATION, DBhelper.PARTY_DRESSING,
	        		DBhelper.PARTY_FOOD};
	        Cursor c = database.query(DBhelper.TABLE_PARTY, allColumns, null,
	                null, null, null, null);//(table,columns,select,selectArgs,groupBy,having,orderBy)
	        if (c != null) {
	            c.moveToFirst();
	        }
	        return c;
	    }
	//to update a data set 
	 public int updateData(long partyID, Party partyUp ) {
	        ContentValues cvUpdate = new ContentValues();
	        cvUpdate.put(DBhelper.PARTY_THEME, partyUp.getTheme());
	        cvUpdate.put(DBhelper.PARTY_DATE, partyUp.getDate());
	        cvUpdate.put(DBhelper.PARTY_COMMENT, partyUp.getComment());
	        cvUpdate.put(DBhelper.PARTY_LOCATION, partyUp.getLocation());
	        cvUpdate.put(DBhelper.PARTY_DRESSING, partyUp.getDressing());
	        cvUpdate.put(DBhelper.PARTY_FOOD, partyUp.getFood());
	        int i = database.update(DBhelper.TABLE_PARTY, cvUpdate,
	                DBhelper.PARTY_ID + " = " + partyID, null);//(table,new values to insert,where,whereArgs)
	        return i;
	    }

	  // Deleting record data from table by id
	    public void deleteData(long partyID) {
	        database.delete(DBhelper.TABLE_PARTY, DBhelper.PARTY_ID + "="
	                + partyID, null);//(table,where,whereArgs)
	    }

	}
	

