package com.example.eventtrackermap;

import android.app.Activity;
import android.database.*;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.view.View.OnClickListener;
import java.util.*;
import android.content.*;

public class Destination extends ListActivity implements OnClickListener {

	// Declaring widgets
	
	ListView list;
	EditText destName, edtTextZip, edtTextMemos, edtTextStreetAdress,
			edtTextCity;
	Button btnAdd, btnUpdate, btnSave, btnNext, btnDelete, btnMapView;
	String dName, streetAdd, city, memos, zip;
	place newDest;
	String tName;


	// ArrayList of type place to store multiple destinations for a given trip
	private ArrayList<place> destination = new ArrayList<place>();
	ArrayList<String> placeName = new ArrayList<String>();
	ArrayAdapter<String> aa;
	int itemPos; // to keep track of destination number in list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);

		
		//Getting trip info from trip class through intent
		Intent fromTrip = getIntent(); 
		tName = fromTrip.getStringExtra("tripVal");
		int nPeople = fromTrip.getIntExtra("peopleVal", 1);

		

		TextView txtDestinationHeader = (TextView) findViewById(R.id.txtDestinationHeader);
		txtDestinationHeader.setText(tName);
		destName = (EditText) findViewById(R.id.edtTextDestinationName);
		edtTextZip = (EditText) findViewById(R.id.edtTextZip);
		edtTextMemos = (EditText) findViewById(R.id.edtTextMemos);
		edtTextStreetAdress = (EditText) findViewById(R.id.edtTextStreetAdress);
		edtTextCity = (EditText) findViewById(R.id.edtTextCity);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnMapView = (Button) findViewById(R.id.btnMapView);

		// List adapter configuration
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, placeName);

		setListAdapter(aa);
		aa.notifyDataSetChanged();
		btnAdd.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnMapView.setOnClickListener(this);

		// setting hints to guide user inputs
		destName.setHint("Name destination");
		edtTextZip.setHint("Zip Code");
		edtTextMemos.setHint("Notes for this destination");
		edtTextStreetAdress.setHint("Street Address");
		edtTextCity.setHint("City");

	}

	public void onClick(View v) {

		// getting the user inputs
		dName = destName.getText().toString();
		streetAdd = edtTextStreetAdress.getText().toString();
		city = edtTextCity.getText().toString();
		memos = edtTextMemos.getText().toString();
		zip = edtTextZip.getText().toString();

		switch (v.getId()) {

		case (R.id.btnAdd): {

			// btnAdd causes a new object destination to be added to an
			// arrayList of destination
			// The list displayed to user is a string list with destination name
			// only. So there are 2 array list - one that contains place and
			// another
			// one that contains just the place name. 2 lists are kept in
			// parallel

			newDest = new place(dName, streetAdd, city, zip, memos);
			destination.add(newDest);
			placeName.add(dName);
			aa.notifyDataSetChanged();
			Toast.makeText(this, newDest.getDestinationName(),
					Toast.LENGTH_SHORT).show();
			clear();
			break;
		}

		case (R.id.btnUpdate): {
			// Allows user to make edits to destination in list
			placeName.set(itemPos, dName);
			aa.notifyDataSetChanged();
			place tempPlace = destination.get(itemPos);
			destination.set(itemPos, tempPlace);
			clear();
			break;
		}

		case (R.id.btnDelete): {
			// deletes the destination name selected and the destination
			// clears the user inputs

			placeName.remove(itemPos);
			aa.notifyDataSetChanged();
			destination.remove(itemPos);
			Toast.makeText(this, "Destination deleted", Toast.LENGTH_SHORT)
					.show();
			clear();
			break;

		}

		case (R.id.btnNext): {
			// clicking on next button will bring the user to the hotel activity
			// Need to add a check here for empty destination
			Bundle b = new Bundle();
			b.putString("cityName", city);
			b.putString("strAdd", streetAdd);
			b.putString("memo", memos);
			b.putString("zipCode", zip);
			b.putStringArrayList("destList", placeName);

			ArrayList<String> dList = new ArrayList<String>();
			for (int i = 0; i < destination.size(); i++) {
				dList.add(destination.get(i).getAddress());
			}

			Intent tripE = new Intent(this, tripExpense.class);
			tripE.putStringArrayListExtra("destinationList", dList);
			startActivity(tripE);

			break;

		}
		
		
		

		case (R.id.btnMapView): {
			// On click, it brings the user to map view
			// Map has the destinations entered by user as markers
			if (destination == null)
				Toast.makeText(this, "There are no destination name",
						Toast.LENGTH_SHORT).show();

			else {

				ArrayList<String> dList = new ArrayList<String>();
				for (int i = 0; i < destination.size(); i++) {
					dList.add(destination.get(i).getAddress());
				}

				Intent mapView = new Intent(this, MapCreate.class);
				mapView.putStringArrayListExtra("destinationList", dList);
				startActivity(mapView);
			}
			break;

		}

		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		itemPos = position;
		Toast.makeText(this, "Position " + itemPos, Toast.LENGTH_SHORT).show();
		destName.setText(placeName.get(itemPos));
		edtTextZip.setText(destination.get(itemPos).getCity());
		edtTextMemos.setText(destination.get(itemPos).getTripMemos());
		edtTextStreetAdress.setText(destination.get(itemPos).getStreedAdd());
		edtTextCity.setText(destination.get(itemPos).getCity());

	}

	// Clear() resets edit texts
	public void clear() {
		destName.setText("");
		edtTextZip.setText("");
		edtTextMemos.setText("");
		edtTextStreetAdress.setText("");
		edtTextCity.setText("");
	}

}
