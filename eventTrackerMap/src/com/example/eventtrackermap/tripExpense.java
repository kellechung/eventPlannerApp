package com.example.eventtrackermap;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import com.google.android.gms.maps.model.LatLng;

import android.app.ListActivity;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class tripExpense extends ListActivity implements OnClickListener {

	EditText edtTextItemName; // Edit text to allow user to enter item name
	EditText edtItemCost; // Edit text to allow user to enter item cost
	EditText edtGasPrice; // Edit text to allow user to enter gas price
	EditText edtMilePerGallon; // Edit text to allow user to enter mileage
								// obtained per gallon

	Button btnAdd; // button to add item to list
	Button btnUpdate; // button to update item to list
	Button btnDelete; // button to delete item in list
	Button btnNext; // button to navigate to next activity

	ListView list; // List containing miscellaneous item cost pertaining to
					// trip
	TextView txtFuelCost; // Display for fuel cost
	TextView txtTotalFuelCost; // Display of total fuel cost

	double totalDistance = 0; // Variable to store calculated trip distance
	ArrayAdapter<String> aa; // Array adapter for expense list
	ArrayList<String> expenseList = new ArrayList<String>();
	int itemPos; // to keep track of destination number in list

	final double DEFAULT_GAS_PRICE = 2.50; // default variable storing gas price
											// per gallon
	final int AVG_MILES_PER_GALLON = 35;// default variable storing gas
										// efficiency of vehicle
	int numPeople; // variable storing number of people going to trip

	String tripName; // variable storing name of trip
	ArrayList<String> placeList; // Arraylist storing list of places on trip

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_expense_tracker);

		txtFuelCost = (TextView) findViewById(R.id.txtFuelCost);
		txtTotalFuelCost = (TextView) findViewById(R.id.txtTotalFuelCost);
		edtTextItemName = (EditText) findViewById(R.id.edtTextItemName);
		edtItemCost = (EditText) findViewById(R.id.edtItemCost);
		edtGasPrice = (EditText) findViewById(R.id.edtGasPrice);
		edtGasPrice.setText(DEFAULT_GAS_PRICE + "");
		edtMilePerGallon = (EditText) findViewById(R.id.edtMilePerGallon);
		edtMilePerGallon.setText("35");
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnNext = (Button) findViewById(R.id.btnNext);

		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, expenseList);
		setListAdapter(aa);
		aa.notifyDataSetChanged();

		Intent mIntent = getIntent();
		// Intent passed to activity
		// Extracting info from intent objs - list of destination, trip name and
		// numPeople going
		placeList = mIntent.getStringArrayListExtra("destinationList");

		numPeople = mIntent.getIntExtra("numPeople", 1);
		tripName = mIntent.getStringExtra("tripName");
		Log.d("ADDRESS", placeList.get(0));

		// Storing geocoordinates in array
		List<LatLng> listLatLng = new ArrayList<LatLng>();

		for (int j = 0; j < placeList.size(); j++) {
			listLatLng.add(getLatLng(placeList.get(j)));
		}

		// Calculating total trip distance from list of stored latlng objs
		if (listLatLng.size() > 1) {
			totalDistance = calcTripDistance(listLatLng);
		}

		else {
			totalDistance = 0;
		}

		txtFuelCost.setText("Trip Distance (Miles) : "
				+ Math.round(totalDistance));

		DecimalFormat f = new DecimalFormat("$##.00");

		// Formatting fuel costs and displaying basic assumptions to user
		txtTotalFuelCost.setText("Total Fuel Cost ($): "
				+ f.format(totalDistance * DEFAULT_GAS_PRICE
						/ AVG_MILES_PER_GALLON));

		// Adding listener to buttons
		btnAdd.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnNext.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		String itemName = edtTextItemName.getText().toString();
		String itemCost = edtItemCost.getText().toString();
		Double milesPerGallon = Double.parseDouble(edtMilePerGallon.getText()
				.toString());
		String listItem = "";
		switch (v.getId()) {

		// Allows user to enter item cost and name to list
		// Trapping invalid user inputs through if else statements
		case (R.id.btnAdd): {


			if (itemName.equals("")) {
				Toast.makeText(this, "Enter item name", Toast.LENGTH_SHORT)
						.show();
			}
			if (itemCost.equals("")) {
				Toast.makeText(this, "Please enter item cost",
						Toast.LENGTH_SHORT).show();
			} else {

				listItem = "ITEM " + itemName + " - $ " + itemCost;
				expenseList.add(listItem);
				aa.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(),
						"Item added to expense list", Toast.LENGTH_SHORT)
						.show();

				clear();
			}

			break;

		}
		//Allow user to update info in list
		
		case (R.id.btnUpdate): {
			if (itemName.equals("")) {
				Toast.makeText(this, "Enter item name", Toast.LENGTH_SHORT)
						.show();
			}
			if (itemCost.equals("")) {
				Toast.makeText(this, "Please enter item cost",
						Toast.LENGTH_SHORT).show();
			} else {

				expenseList.set(itemPos, "ITEM " + itemName + " - $  "
						+ itemCost);
				aa.notifyDataSetChanged();
				clear();
			}
			break;
		}
		// Allow user to delete items in list
		case (R.id.btnDelete): {
			expenseList.remove(itemPos);
			aa.notifyDataSetChanged();
			Toast.makeText(this, "item removed", Toast.LENGTH_SHORT).show();
			break;
		}
		
		
		//Allow user to navigate to next activity
		// Create intent objects to send info to next activity
		case (R.id.btnNext): {
			
			//Calculates total gas price from edit texts data
			Double gasPrice = Double.parseDouble(edtGasPrice.getText()
					.toString());
			Double totalGasPrice = totalDistance * gasPrice / milesPerGallon;

			double expenses = 0;

			for (int i = 0; i < expenseList.size(); i++) {

				int pos = expenseList.get(i).indexOf("$");
				String ex = expenseList.get(i).substring(pos + 2);
				expenses += Double.parseDouble(ex);
			}

			Log.d("ExpenseCalculation", "" + expenses);
			
			//Passing data to next activity through intents
			Double totalCosts = totalGasPrice + expenses;
			Intent tripFinal = new Intent(this, tripCosts.class);
			tripFinal.putExtra("totalCost", totalCosts);
			tripFinal.putStringArrayListExtra("dest", placeList);
			tripFinal.putExtra("numPeople", numPeople);
			tripFinal.putExtra("tripName", tripName);
			startActivity(tripFinal);
			break;
		}

		}

	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// String dName = placeName.get(position);
		itemPos = position;
		Toast.makeText(this, "Position " + itemPos, Toast.LENGTH_SHORT).show();

		String selectedItem = expenseList.get(itemPos);
		int dollarPos = selectedItem.indexOf("$");
		String iName = selectedItem.substring(5, (dollarPos - 2));
		String iCost = selectedItem.substring(dollarPos + 1);

		edtTextItemName.setText(iName);
		edtItemCost.setText(iCost);

	}

	
	//Method to get a latlng object from an address aka fwd geocoding
	public LatLng getLatLng(String add) {

		LatLng latlng = null;
		double latitude = 0;
		double longitude = 0;
		Geocoder geo = new Geocoder(getApplicationContext(),
				Locale.getDefault());

		try {
			List<Address> geoList = geo.getFromLocationName(add, 1);
			Address geoAdd = geoList.get(0);
			latitude = geoAdd.getLatitude();
			longitude = geoAdd.getLongitude();
			latlng = new LatLng(latitude, longitude);
			Log.w("Coordinates", latitude + "  " + longitude);
			return latlng;

		} catch (IOException e) {
			Log.w("myLog", e);
			e.printStackTrace();
		}
		return latlng;

	}

	// Calculates distance for entire trip from a list of geo points
	public double calcTripDistance(List<LatLng> listGeoPoints) {
		double tripDistance = 0;

		// For n points, there should be n-1 distances
		for (int k = 0; k < listGeoPoints.size() - 1; k++) {
			double firstPointLat = listGeoPoints.get(k).latitude;
			double firstPointLng = listGeoPoints.get(k).longitude;
			double secondPointLat = listGeoPoints.get(k + 1).latitude;
			double secondPointLng = listGeoPoints.get(k + 1).longitude;
			double dist = calcDistanceBetweenPoints(firstPointLat,
					firstPointLng, secondPointLat, secondPointLng);
			tripDistance += dist;
			return tripDistance;

		}

		return tripDistance;
	}

	// Calculate distance between 2 geocoordinates.
	private double calcDistanceBetweenPoints(double lat1, double lon1,
			double lat2, double lon2) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);

	}

	//Method converting degrees to radian
	private double deg2rad(double deg) {

		return (deg * Math.PI / 180.0);

	}
	
	//Method converting radians to degrees
	private double rad2deg(double rad) {

		return (rad * 180 / Math.PI);
	}

	// Method to reset & clear edit texts from user inputs
	public void clear() {
		edtTextItemName.setText("");
		edtItemCost.setText("");
	}
}
