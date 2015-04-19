package com.example.eventtrackermap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Modify_party extends Activity implements OnClickListener, OnItemSelectedListener {
	EditText partyTheme_et, location_et, dressing_et, comment_et;
	String [] food = {"Food Provided", "No Food"};
	Button update_bt, delete_bt;
	public static String selection;
	long party_ID;
	SQLController dbcon;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_party);

		dbcon = new SQLController(this);
		dbcon.open();

		partyTheme_et = (EditText) findViewById(R.id.partyTheme_et_mod);
		location_et = (EditText) findViewById(R.id.location_et_mod);
		dressing_et = (EditText) findViewById(R.id.dressing_et_mod);
		comment_et = (EditText) findViewById(R.id.comment_et_mod);
		update_bt = (Button) findViewById(R.id.update_bt);
		delete_bt = (Button) findViewById(R.id.delete_bt);
		update_bt.setOnClickListener(this);
		delete_bt.setOnClickListener(this);
		
		Spinner spin = (Spinner) findViewById(R.id.f_mod);
		spin.setOnItemSelectedListener(this);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_spinner_item,  //Android supplied Spinner item format
				food);
		aa.setDropDownViewResource(
				   android.R.layout.simple_spinner_dropdown_item);  
				spin.setAdapter(aa);
	

		Intent i = getIntent();
		//retrieve
		String partyID = i.getStringExtra("partyID");
		String partyTheme = i.getStringExtra("partyTheme");
		String partyLocation = i.getStringExtra("partyLocation");
		String partyFood = i.getStringExtra("partyFood");
		String partyDressing = i.getStringExtra("partyDressing");
		String partyComment = i.getStringExtra("partyComment");
		
		party_ID = Long.parseLong(partyID);
		
		partyTheme_et.setText(partyTheme);
		location_et.setText(partyLocation);
		dressing_et.setText(partyDressing);
		comment_et.setText(partyComment);
		
		update_bt.setOnClickListener(this);
		delete_bt.setOnClickListener(this);

	}

	public void onClick(View v) {
	
		switch (v.getId()) {
		case R.id.update_bt:
			String partyTheme_upd = partyTheme_et.getText().toString();
			String location_upd = location_et.getText().toString();
			String dressing_upd = dressing_et.getText().toString();
			String comment_upd = comment_et.getText().toString();
			
			dbcon.updateData(party_ID, new Party(partyTheme_upd, location_upd, selection, dressing_upd, comment_upd));
			this.returnHome();
			break;

		case R.id.delete_bt:
			dbcon.deleteData(party_ID);
			this.returnHome();
			break;
		}
	}

	public void returnHome() {

		Intent home_intent = new Intent(getApplicationContext(),
				PartyListMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(home_intent);
	}
	
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		 selection = food[position].toString();
	} 

	public void onNothingSelected(AdapterView<?> parent) {
		}
}


