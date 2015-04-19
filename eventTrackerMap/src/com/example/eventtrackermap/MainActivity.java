package com.example.eventtrackermap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private GoogleMap myMap, myMap2;
	private LatLng position = null;
	private Geocoder gc = null;
	private Marker mark = null;
	TabHost tabs;
	TabHost.TabSpec spec;
	TabHost.TabSpec spec2;

	// coordinates CSU decimal format. e.g., xxx.xxxxxx
	// locate initial coordinates at Bentley
	double latitude = 42.3889167;
	double longitude = -71.2208033;
	
		
		/**
		{ "	465 Huntington Ave, Boston, MA 02115",
			"77 Massachusetts Avenue, Cambridge, MA 02139",
			"4 Yawkey Way, Boston, MA 02215" }; **/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		
		Intent intent = getIntent(); // get reference to Intent contain addresses from user
		ArrayList<String> placeList= intent.getStringArrayListExtra("destinationList");
		
		String[] addressBook = new String[placeList.size()];
		for(int i = 0; i< placeList.size(); i++) {
			
			addressBook[i] = placeList.get(i);
			Log.d("Place",addressBook[i] );
		}
		
		
		tabs = (TabHost) findViewById(R.id.tabhost);
		tabs.setup();

		// Initialize a TabSpec for tab1 and add it to the TabHost
		spec = tabs.newTabSpec("tag1"); // create new tab specification
		spec.setContent(R.id.tab1); // add tab view content
		spec.setIndicator("Normal Map View"); // put text on tab
		tabs.addTab(spec); // put tab in TabHost container
		tabs.setCurrentTab(0);

		spec2 = tabs.newTabSpec("tag2"); // create new tab specification
		spec2.setContent(R.id.tab2); // add tab view content
		spec2.setIndicator("Hybrid Map View"); // put text on tab
		tabs.addTab(spec2);

		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				42.331, -71.09), 13));

		myMap2 = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map2)).getMap();
		myMap2.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		myMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				42.331, -71.09), 13));

		for (int i = 0; i < addressBook.length; i++) {

			myMap.addMarker(new MarkerOptions()
					.position(getLatLng(addressBook[i])).title("" + (i + 1))
					.snippet(addressBook[i])
					// .snippet(addressBook[i])
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));

			myMap2.addMarker(new MarkerOptions()
					.position(getLatLng(addressBook[i])).title("" + (i + 1))
					.snippet(addressBook[i])
					// .snippet(addressBook[i])
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));

		}

		List<LatLng> listLatLng = new ArrayList<LatLng>();
		for (int i = 0; i < addressBook.length; i++) {

			listLatLng.add(getLatLng(addressBook[i]));

		}

		myMap.addPolyline(new PolylineOptions().addAll(listLatLng).width(5)
				.color(Color.MAGENTA));

		myMap2.addPolyline(new PolylineOptions().addAll(listLatLng).width(8)
				.color(Color.BLUE));

	}// onCreate

	public String getGeoCoordinates(String add) {

		String lat = null;
		String locality = null;
		double latitude = 0;
		double longitude = 0;
		Geocoder geo = new Geocoder(getApplicationContext(),
				Locale.getDefault());

		try {
			List<Address> geoList = geo.getFromLocationName(add, 1);

			Address geoAdd = geoList.get(0);
			locality = geoAdd.getLocality();
			latitude = geoAdd.getLatitude();
			longitude = geoAdd.getLongitude();
			lat = locality + "--" + latitude + "--" + longitude;
			Log.w("Coordinates", latitude + "  " + longitude);
			// Toast.makeText(this, gAdd, Toast.LENGTH_LONG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w("myLog", e);
			e.printStackTrace();
		}
		return lat;

	}

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
			// TODO Auto-generated catch block
			Log.w("myLog", e);
			e.printStackTrace();
		}
		return latlng;

	}

}// class