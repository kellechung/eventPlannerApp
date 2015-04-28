package com.example.eventtrackermap;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
 
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
 
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 

public class Modify_party extends FragmentActivity implements OnItemSelectedListener{
	SQLController dbcon;
	EditText partyTheme_et, comment_et, location_et, dressing_et;
	TextView date_tv;
	String [] food = {"Food Provided", "No Food"};
	int selection_index;
	
	long party_ID;
	
	static final int DATE_DIALOG_ID = 0;
	private int mYear;
	private int mMonth;
	private int mDay;
		
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	LatLng latLng;
	private static final float zoom = 15.0f;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_party);
		
		dbcon = new SQLController(this);//back end assigning Context, attach it to dbcon SQLController
		dbcon.open();
		
		SupportMapFragment supportMapFragment = (SupportMapFragment)
		        getSupportFragmentManager().findFragmentById(R.id.map_mod);
		        // Getting a reference to the map
		        googleMap = supportMapFragment.getMap();
		        	
		partyTheme_et = (EditText) findViewById(R.id.partyTheme_et_mod);
		configureImagebutton();
		date_tv = (TextView) findViewById(R.id.showMyDate_mod);
		/*make date textView shows current date automatically*/	
		final Calendar c = Calendar.getInstance();//Constructs a new instance of the Calendar subclass 
	    mYear = c.get(Calendar.YEAR);
	    mMonth = c.get(Calendar.MONTH);
	    mDay = c.get(Calendar.DAY_OF_MONTH);  
	    	
	/**/	
		comment_et = (EditText) findViewById(R.id.comment_et_mod);
		location_et = (EditText) findViewById(R.id.location_et_mod);
		dressing_et = (EditText) findViewById(R.id.dressing_et_mod);
		
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
		String partyDate = i.getStringExtra("partyDate");//we don't need updateDisplay() in onCreate, this one set the textView is enough
		String partyComment = i.getStringExtra("partyComment");
		String partyLocation = i.getStringExtra("partyLocation");
		String partyDressing = i.getStringExtra("partyDressing");
		String partyFood = i.getStringExtra("partyFood");
		//for the sql "select" use
		party_ID = Long.parseLong(partyID);
		
		partyTheme_et.setText(partyTheme);
		date_tv.setText(partyDate);
		comment_et.setText(partyComment);
		location_et.setText(partyLocation);
		dressing_et.setText(partyDressing);
		//partyFood is a String consists of integer, so parse it
		int pre_select = Integer.parseInt(partyFood);
		spin.setSelection(pre_select);//pre_select is a integer been transferred around
		
		/*map creation*/
		String location = location_et.getText().toString();
		if(location!=null && !location.equals("")){
            new GeocoderTask().execute(location);
		}
	}    //onCreate end
	
	private void updateDisplay() {
	    this.date_tv.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	        		//Appends the string representation of the specified int value.
	                .append(mMonth + 1).append("-")
	                .append(mDay).append("-")
	                .append(mYear).append(" "));
	}

	//make click-able button
	private void configureImagebutton() {
		ImageButton mPickDate=(ImageButton) findViewById(R.id.myDatePickerButton_mod);
		
		mPickDate.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	             showDialog(DATE_DIALOG_ID);//click the calendar image button, call onCreate Calendar view
	            }
			});
		}
	//onCreate the Calendar view, only a view, non-select-able
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:	//(context the dialog is to run in, callback,y,m,d)
				return new DatePickerDialog(this, mDateSetListener,mYear, mMonth, mDay);
		}					
			return null;
	}	
	//enable Calendar view select-able
	//a widget(spinner) for selecting a date//.The callback used to indicate the user is done filling in the date.
	private DatePickerDialog.OnDateSetListener mDateSetListener =
		new DatePickerDialog.OnDateSetListener() {
	   		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	        mYear = year;
	        mMonth = monthOfYear;
	        mDay = dayOfMonth;
	        updateDisplay();
	   		}
	};
	
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		selection_index= position;
	} 

	public void onNothingSelected(AdapterView<?> parent) {
		}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.menuextramod, menu);
	    return true;
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) { 
		case R.id.save_btn_mod:
			updateParty();
			this.returnHome();
			return true;
		
		case R.id.email_btn_mod:
			updateParty();
			
			sendEmail(); 
			return true;
		case R.id.delete_btn:
			dbcon.deleteData(party_ID);
			this.returnHome();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void updateParty(){
		String partyTheme_upd = partyTheme_et.getText().toString();
		String date_upd = date_tv.getText().toString();
		String comment_upd = comment_et.getText().toString();
		String location_upd = location_et.getText().toString();
		String dressing_upd = dressing_et.getText().toString();
		int selection_index_upd =selection_index;
		dbcon.updateData(party_ID, new Party(partyTheme_upd, date_upd, comment_upd, location_upd, dressing_upd, selection_index_upd));
	}
	
	




public void sendEmail(){			
	
	String partyTheme = partyTheme_et.getText().toString();
	String date = date_tv.getText().toString();
	String comment = comment_et.getText().toString();
	String location = location_et.getText().toString();
	String dressing = dressing_et.getText().toString();
	int food_idx = selection_index;
		
    Intent emailIntent = new Intent(Intent.ACTION_SEND);
    emailIntent.setData(Uri.parse("mailto:"));
    emailIntent.setType("text/plain");

    //insert recipients, subject and text content
    
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, partyTheme);
    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, you are invited to our party."+"\n"+"\n"+
    					"Subject: "+ partyTheme +"\n"+			
    					"Date: "+ date + "\n"+			
   		  				"Detail: "+ comment + "\n" +
    					"Location: " + location + "\n" +
  		  				"Dress code: "+ dressing + "\n" +
    					"Food: "+ food[food_idx] + "\n" + "\n" + 
    					"Please respond to this email if you have further inquiry. Thank you!"); 

    try {
  	  if(partyTheme.trim().length()==0 || date.trim().length()==0|| comment.trim().length()==0 ||
  			  location.trim().length()==0 || dressing.trim().length()==0 || food[food_idx].trim().length()==0)
  		    		{
  				  		Toast.makeText(this, 
  					         "Please enter all information required.", Toast.LENGTH_SHORT).show();
  		    		}
  	  else{
  	  startActivity(Intent.createChooser(emailIntent, "Send mail..."));
  	 
  	  finish();
        Log.i("Finished sending email...", "");}
    } catch (android.content.ActivityNotFoundException ex) {
       Toast.makeText(this, 
       "There is no email client installed.", Toast.LENGTH_SHORT).show();
    }
 }
	
	public void returnHome() {

		Intent home_intent = new Intent(getApplicationContext(),
				PartyListMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(home_intent);
	}
	
private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
		
		protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
		
		protected void onPostExecute(List<Address> addresses) {
			 
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                return;
            }
 
            // Clears all the existing markers on the map
            googleMap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
 
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
 
                googleMap.addMarker(markerOptions);
 
                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((latLng), zoom));
            }
		}
	}
}

