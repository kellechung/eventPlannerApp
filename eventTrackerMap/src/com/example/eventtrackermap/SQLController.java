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
	   
	    public SQLController(Context c) {
	        ourcontext = c;
	    }
	    
	public SQLController open() 
			throws SQLException {
    dbhelper = new DBhelper(ourcontext);	
    database = dbhelper.getWritableDatabase();
    	return this;
    }
	
	public void close() {
        dbhelper.close();
    }
	
	 public void insertData(Party info) {
	        ContentValues cv = new ContentValues();
	        cv.put(DBhelper.PARTY_THEME, info.getTheme());
	        cv.put(DBhelper.PARTY_LOCATION, info.getLocation());
	        cv.put(DBhelper.PARTY_FOOD, info.getFood());
	        cv.put(DBhelper.PARTY_DRESSING, info.getDressing());
	        cv.put(DBhelper.PARTY_COMMENT, info.getComment());
	        database.insert(DBhelper.TABLE_PARTY, null, cv);
	    }
	 
	 public Cursor readData() {
	        String[] allColumns = new String[] {DBhelper.PARTY_ID, DBhelper.PARTY_THEME, 
	        		DBhelper.PARTY_LOCATION, DBhelper.PARTY_FOOD, DBhelper.PARTY_DRESSING,
	        		DBhelper.PARTY_COMMENT};
	        Cursor c = database.query(DBhelper.TABLE_PARTY, allColumns, null,
	                null, null, null, null);
	        if (c != null) {
	            c.moveToFirst();
	        }
	        return c;
	    }
	 
	 public int updateData(long partyID, Party partyUp ) {
	        ContentValues cvUpdate = new ContentValues();
	        cvUpdate.put(DBhelper.PARTY_THEME, partyUp.getTheme());
	        cvUpdate.put(DBhelper.PARTY_LOCATION, partyUp.getLocation());
	        cvUpdate.put(DBhelper.PARTY_FOOD, partyUp.getFood());
	        cvUpdate.put(DBhelper.PARTY_DRESSING, partyUp.getDressing());
	        cvUpdate.put(DBhelper.PARTY_COMMENT, partyUp.getComment());
	        int i = database.update(DBhelper.TABLE_PARTY, cvUpdate,
	                DBhelper.PARTY_ID + " = " + partyID, null);
	        return i;
	    }

	  // Deleting record data from table by id
	    public void deleteData(long partyID) {
	        database.delete(DBhelper.TABLE_PARTY, DBhelper.PARTY_ID + "="
	                + partyID, null);
	    }

	}
	

