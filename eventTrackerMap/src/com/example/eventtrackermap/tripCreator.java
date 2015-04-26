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

	EditText edtTripName, edtNumPeople;
	Button btnCreateTrip;
	String tripName;
	String numPeople;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip);

		edtTripName = (EditText) findViewById(R.id.edtTripName);
		edtNumPeople = (EditText) findViewById(R.id.edtNumPeople);
		btnCreateTrip = (Button) findViewById(R.id.btnCreateTrip);

	
		btnCreateTrip.setOnClickListener(this);

	}

	@Override
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

		Intent trip = new Intent(this, Destination.class);
		trip.putExtra("tripVal", tripName);
		Log.d("CountPeople", "" + countPeople);
		trip.putExtra("peopleVal", countPeople);
		startActivity(trip);

	}

}
