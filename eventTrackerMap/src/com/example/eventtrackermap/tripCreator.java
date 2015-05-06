package com.example.eventtrackermap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.view.View.OnClickListener;
import java.util.*;

public class tripCreator extends Activity implements OnClickListener {

	EditText edtTripName, edtNumPeople; //edit text to get trip name and num people going to trip from user
	Button btnCreateTrip; //button to create trip and start next activity
	String tripName; // String storing tripName
	String numPeople; //String storing number of people 

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip);

		//Inflating menu	
		edtTripName = (EditText) findViewById(R.id.edtTripName);
		edtNumPeople = (EditText) findViewById(R.id.edtNumPeople);
		btnCreateTrip = (Button) findViewById(R.id.btnCreateTrip);
		//Adding listener to button createTrip
		btnCreateTrip.setOnClickListener(this);

	}

	@Override
	//On click btnCreate
	// Get user inputs from editText
	//Create toast objects displaying errors to user if wrong inputs are entered
	public void onClick(View v) {
	
		tripName = edtTripName.getText().toString();
		numPeople = edtNumPeople.getText().toString();
		
		if (tripName.equals("")) {
			Toast.makeText(this, "Trip name cannot be null", Toast.LENGTH_SHORT)
					.show();
		}

		if (numPeople.equals("")) {
			Toast.makeText(this, "Please enter number of people going on trip",
					Toast.LENGTH_SHORT).show();
		}

		int countPeople = Integer.parseInt(numPeople);

		if (countPeople == 0) {
			Toast.makeText(
					this,
					"Number of people going on trip has to be greater than zero",
					Toast.LENGTH_SHORT).show();
		}

		//Passing data through intent objs
		Intent trip = new Intent(this, Destination.class);
		trip.putExtra("tripVal", tripName);
		Log.d("CountPeople", "" + countPeople);
		trip.putExtra("peopleVal", countPeople);
		startActivity(trip);

	}

}
