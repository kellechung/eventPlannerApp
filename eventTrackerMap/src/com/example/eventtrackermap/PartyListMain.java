package com.example.eventtrackermap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PartyListMain extends Activity {
Button addParty_btn;
ListView lv;
SQLController dbcon;
//only theme TextView is visiable
TextView partyID_tv, partyTheme_tv, partyDate_tv, partyComment_tv, partyLocation_tv, partyDressing_tv, partyFood_tv;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_party_organizer);
  //instantiate-->create a reference of current Context in the back end, so Context can do lots of things
  //dbcon is not a Context but it comes with invisible Context that has been assigned to "this", dbcon includes/brings a Context
    dbcon = new SQLController(this);
    dbcon.open();//dbcon is a SQLController that can create database, .open() is a sqlcontroller method, it includes the dbhelper who is the real reference of Context, and dbhelper can create database
    
    addParty_btn = (Button) findViewById(R.id.addParty_btn_id);
    lv = (ListView) findViewById(R.id.partyList_id);
   //Add Party button being clicked
    addParty_btn.setOnClickListener(new OnClickListener() {
             public void onClick(View v) {
                      Intent add_party = new Intent(PartyListMain.this, Add_newparty.class);
                      startActivity(add_party);  
              }
    });
/*Cursor Adaptor start */
    //run through whole database
    Cursor cursor = dbcon.readData();
    String[] from = new String[] {DBhelper.PARTY_ID, DBhelper.PARTY_THEME, DBhelper.PARTY_DATE,
    		DBhelper.PARTY_COMMENT, DBhelper.PARTY_LOCATION, DBhelper.PARTY_DRESSING,
    		DBhelper.PARTY_FOOD};
    int[] to = new int[] {R.id.party_id, R.id.partyTheme_id, R.id.partyDate_id,
    		R.id.partyComment_id, R.id.partyLocation_id,
    		R.id.partyDressing_id, R.id.partyFood_id};
    
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
    		PartyListMain.this, R.layout.partylist_view, cursor, from, to);
    		//(context,listView,data content,column name, xml id)
    adapter.notifyDataSetChanged();
    lv.setAdapter(adapter);
/*Cursor Adaptor end */
    
    //listView item being clicked, to update/delete ONE data ROW
    lv.setOnItemClickListener(new OnItemClickListener() {
        
        public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
        	
        	partyID_tv = (TextView) view.findViewById(R.id.party_id);
        	partyTheme_tv = (TextView) view.findViewById(R.id.partyTheme_id);
        	partyDate_tv = (TextView) view.findViewById(R.id.partyDate_id);
        	partyComment_tv = (TextView) view.findViewById(R.id.partyComment_id);
        	partyLocation_tv = (TextView) view.findViewById(R.id.partyLocation_id);
           	partyDressing_tv = (TextView) view.findViewById(R.id.partyDressing_id);
        	partyFood_tv = (TextView) view.findViewById(R.id.partyFood_id);
        	
            String partyID_val = partyID_tv.getText().toString();
            String partyTheme_val = partyTheme_tv.getText().toString();
            String partyDate_val = partyDate_tv.getText().toString();
            String  partyComment_val = partyComment_tv.getText().toString();
            String partyLocation_val = partyLocation_tv.getText().toString();
            String partyDressing_val = partyDressing_tv.getText().toString();
            String partyFood_val = partyFood_tv.getText().toString();
            
            Intent modify_party = new Intent(getApplicationContext(),
                    Modify_party.class);//this class is to update/delete data row
            modify_party.putExtra("partyID", partyID_val);
            modify_party.putExtra("partyTheme", partyTheme_val);
            modify_party.putExtra("partyDate", partyDate_val);
            modify_party.putExtra("partyComment", partyComment_val);
            modify_party.putExtra("partyLocation", partyLocation_val);
            modify_party.putExtra("partyDressing", partyDressing_val);
            modify_party.putExtra("partyFood", partyFood_val);
            startActivity(modify_party);
        }
    });

} // onCreate end

}// class end

