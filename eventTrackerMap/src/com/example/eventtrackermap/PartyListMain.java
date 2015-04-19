package com.example.eventtrackermap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PartyListMain extends Activity {
Button addParty_bt;
ListView lv;
SQLController dbcon;
TextView partyID_tv, partyTheme_tv, partyLocation_tv, partyFood_tv, partyDressing_tv, partyComment_tv;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_party_organizer);
    dbcon = new SQLController(this);
    dbcon.open();
    
    addParty_bt = (Button) findViewById(R.id.addParty_bt_id);
    lv = (ListView) findViewById(R.id.partyList_id);
    
 
    addParty_bt.setOnClickListener(new OnClickListener() {
             public void onClick(View v) {
                      Intent add_party = new Intent(PartyListMain.this, Add_newparty.class);
                      startActivity(add_party);  
              }
    });

   
    Cursor cursor = dbcon.readData();
    String[] from = new String[] {DBhelper.PARTY_ID, DBhelper.PARTY_THEME, 
    		DBhelper.PARTY_LOCATION, DBhelper.PARTY_FOOD, DBhelper.PARTY_DRESSING,
    		DBhelper.PARTY_COMMENT};
    int[] to = new int[] {R.id.party_id, R.id.partyTheme_id,
    		R.id.partyLocation_id,R.id.partyFood_id,
    		R.id.partyDressing_id,R.id.partyComment_id};
    
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
    		PartyListMain.this, R.layout.partylist_view, cursor, from, to);

    adapter.notifyDataSetChanged();
    lv.setAdapter(adapter);

    
    lv.setOnItemClickListener(new OnItemClickListener() {
        
        public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
        	
        	partyID_tv = (TextView) view.findViewById(R.id.party_id);
        	partyTheme_tv = (TextView) view.findViewById(R.id.partyTheme_id);
        	partyLocation_tv = (TextView) view.findViewById(R.id.partyLocation_id);
        	partyFood_tv = (TextView) view.findViewById(R.id.partyFood_id);
        	partyDressing_tv = (TextView) view.findViewById(R.id.partyDressing_id);
        	partyComment_tv = (TextView) view.findViewById(R.id.partyComment_id);
        	
            String partyID_val = partyID_tv.getText().toString();
            String partyTheme_val = partyTheme_tv.getText().toString();
            String partyLocation_val = partyLocation_tv.getText().toString();
            String partyFood_val = partyFood_tv.getText().toString();
            String partyDressing_val = partyDressing_tv.getText().toString();
            String  partyComment_val = partyComment_tv.getText().toString();
            
            Intent modify_party = new Intent(getApplicationContext(),
                    Modify_party.class);
            modify_party.putExtra("partyID", partyID_val);
            modify_party.putExtra("partyTheme", partyTheme_val);
            modify_party.putExtra("partyLocation", partyLocation_val);
            modify_party.putExtra("partyFood", partyFood_val);
            modify_party.putExtra("partyDressing", partyDressing_val);
            modify_party.putExtra("partyComment", partyComment_val);
            startActivity(modify_party);
        }
    });

} // create method end

}// class end

