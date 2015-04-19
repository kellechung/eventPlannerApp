package com.example.eventtrackermap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class hotelActivity extends Activity implements OnClickListener {

	EditText edtTextHotelName, edtTextCity, edtTextZip, edtTextNumNights,
			edtTextCostPerNight;
	EditText edtTextStreetAdd;
	Button btnSave, btnCalculate;
	TextView txtCostPerPerson, txtcalcCostPerPerson, txtTripName;
	int numNights;

	double costPerNights;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotelstay);

		Intent intent = getIntent(); // get reference to Intent object used to
										// start activity
		Bundle bundle = intent.getExtras();
		String c = bundle.getString("cityName");
		String x = bundle.getString("strAdd");
		String m = bundle.getString("memo");
		String b = bundle.getString("zipCode");
		ArrayList<String> aaa = bundle.getStringArrayList("destList");

		edtTextHotelName = (EditText) findViewById(R.id.edtTextHotelName);
		txtTripName = (TextView) findViewById(R.id.txtTripName);
		txtTripName.setText(c);
		edtTextCity = (EditText) findViewById(R.id.edtTextCity);
		edtTextZip = (EditText) findViewById(R.id.edtTextZip);
		edtTextNumNights = (EditText) findViewById(R.id.edtTextNumNights);
		edtTextCostPerNight = (EditText) findViewById(R.id.edtTextCostPerNight);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnCalculate = (Button) findViewById(R.id.btnCalculate);
		btnCalculate.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		String hotelName = edtTextHotelName.getText().toString();
		String city = edtTextCity.getText().toString();
		String zip = edtTextZip.getText().toString();
		// numNights = Integer.parseInt(edtTextNumNights.getText().toString());
		// costPerNights = Double.parseDouble(edtTextCostPerNight.getText()
		// .toString());

	}

	@Override
	public void onClick(View v) {

		/**
		 * switch (v.getId()) {
		 * 
		 * case (R.id.btnCalculate): {
		 * 
		 * if (edtTextNumNights.getText().toString().equals("")) {
		 * 
		 * Toast.makeText(this, "Please enter number of nights",
		 * Toast.LENGTH_SHORT).show(); }
		 * 
		 * else if (edtTextCostPerNight.getText().toString().equals("")) {
		 * Toast.makeText(this, "Please enter cost per night",
		 * Toast.LENGTH_SHORT).show(); }
		 * 
		 * else { double totalTripCost = numNights * costPerNights;
		 * 
		 * }
		 * 
		 * } }
		 **/

	}

}
