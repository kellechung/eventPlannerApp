package com.example.eventtrackermap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.view.View.OnClickListener;

import java.util.*;

public class Destination extends ListActivity implements OnClickListener {

	// Android widgets
	/**
	 * private TextView textDestinationHeader, txtDestinationName,
	 * streetAddress, city, notes, txtTrip;
	 **/

	// ScrollView scrollView1;
	ListView list;
	EditText destName, edtTextZip, edtTextMemos, edtTextStreetAdress,
			edtTextCity;
	Button btnAdd, btnUpdate, btnSave, btnNext, btnDelete, btnMapView;

	String dName, streetAdd, city, memos, zip;
	place newDest;
	int itemPos; // to keep track of destination number in list

	// ArrayList of type place to store multiple destinations for a given trip
	private ArrayList<place> destination = new ArrayList<place>();
	ArrayList<String> placeName = new ArrayList<String>();
	ArrayAdapter<String> aa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destination);

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

	/**
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 *           menu; this adds items to the action bar if it is present.
	 *           getMenuInflater().inflate(R.menu.destination, menu); return
	 *           true; }
	 **/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {

		// getting the user inputs
		dName = destName.getText().toString();
		streetAdd = edtTextStreetAdress.getText().toString();
		city = edtTextCity.getText().toString();
		memos = edtTextMemos.getText().toString();
		zip = edtTextMemos.getText().toString();

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

		// clicking on next button will bring the user to the hotel activity
		case (R.id.btnNext): {

			// Need to add a check here for empty destination
			Bundle b = new Bundle();
			b.putString("cityName", city);
			b.putString("strAdd", streetAdd);
			b.putString("memo", memos);
			b.putString("zipCode", zip);
			b.putStringArrayList("destList", placeName);

			Intent i = new Intent(this, hotelActivity.class);
			i.putExtras(b);
			startActivity(i);
			break;
		}

		case (R.id.btnMapView): {

			if (destination == null)
				Toast.makeText(this, "There are no destination name",
						Toast.LENGTH_SHORT).show();

			else {

				ArrayList<String> dList = new ArrayList<String>();
				for (int i = 0; i <destination.size();i++) {dList.add(destination.get(i).getAddress());}
				
				
				Intent mapView = new Intent(this, MainActivity.class);
				mapView.putStringArrayListExtra("destinationList", dList);
				startActivity(mapView);
			}
			break;

		}

		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// String dName = placeName.get(position);
		itemPos = position;
		Toast.makeText(this, "Position " + itemPos, Toast.LENGTH_SHORT).show();
		destName.setText(placeName.get(itemPos));
		edtTextZip.setText(destination.get(itemPos).getCity());
		edtTextMemos.setText(destination.get(itemPos).getTripMemos());
		edtTextStreetAdress.setText(destination.get(itemPos).getStreedAdd());
		edtTextCity.setText(destination.get(itemPos).getCity());

	}

	// Clear() resets the edit texts so user can add new inputs
	public void clear() {

		destName.setText("");
		edtTextZip.setText("");
		edtTextMemos.setText("");
		edtTextStreetAdress.setText("");
		edtTextCity.setText("");
	}

}
