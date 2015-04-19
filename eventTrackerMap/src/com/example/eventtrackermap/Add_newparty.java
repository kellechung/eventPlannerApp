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

public class Add_newparty extends Activity implements OnClickListener, OnItemSelectedListener {
EditText partyTheme_et, location_et, dressing_et, comment_et;
String [] food = {"Food Provided", "No Food"};
Button create_bt;
public static String selection;
SQLController dbcon;


@Override
protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.partyinfo);
	
	dbcon = new SQLController(this);
	dbcon.open();
	
	partyTheme_et = (EditText) findViewById(R.id.partyTheme_et);
	location_et = (EditText) findViewById(R.id.location_et);
	dressing_et = (EditText) findViewById(R.id.dressing_et);
	comment_et = (EditText) findViewById(R.id.comment_et);
	create_bt = (Button) findViewById(R.id.create_bt);
	create_bt.setOnClickListener(this);
	
	Spinner spin = (Spinner) findViewById(R.id.f);
	spin.setOnItemSelectedListener(this);
	ArrayAdapter<String> aa = new ArrayAdapter<String>(
			this,
			android.R.layout.simple_spinner_item,  //Android supplied Spinner item format
			food);
	aa.setDropDownViewResource(
			   android.R.layout.simple_spinner_dropdown_item);  
			spin.setAdapter(aa);
}

public void onItemSelected(AdapterView<?> parent, View v, int position,
		long id) {
	 selection = food[position].toString();
} 

public void onNothingSelected(AdapterView<?> parent) {
	}

public void onClick(View v) {
	
	switch (v.getId()) {
	
	case R.id.create_bt:
		String partyTheme = partyTheme_et.getText().toString();
		String location = location_et.getText().toString();
		String food = selection;
		String dressing = dressing_et.getText().toString();
		String comment = comment_et.getText().toString();
		
		dbcon.insertData(new Party(partyTheme, location, food, dressing, comment));
		Intent main = new Intent(Add_newparty.this, PartyListMain.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(main);
		break;

	default:
		break;
	}
}



}


