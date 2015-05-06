package com.example.eventtrackermap;

import java.io.IOException;
import java.util.*;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import android.graphics.Color;
import android.location.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.widget.*;

public class MapCreate extends Activity {
	
	
	private GoogleMap myMap, myMap2; // Creating 2 google map objects
	private LatLng position = null;
	private Geocoder gc = null;
	//private Marker mark = null;
	
	//Adding tabhosts to layouts
	TabHost tabs;
	TabHost.TabSpec spec;
	TabHost.TabSpec spec2;

	ArrayList<String> placeList; // Arraylist storing destination list passed from intent
	ArrayList<String> memos; // Arraylist storing memo list passed from intent
	ArrayList<String> destNames;
	
	List<LatLng> listLatLng; // List of latlng objects to store geopoints passed from intent
	String tripName; // String to store trip name passed from intent
	double d;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Extracting data from intent

		Intent mIntent = getIntent(); 
		 placeList = mIntent
				.getStringArrayListExtra("destinationList");
		 
		 memos = mIntent
				.getStringArrayListExtra("tripMemos"); 
		 
		 destNames = mIntent
			.getStringArrayListExtra("tripPlaces"); 
		 
		 tripName = mIntent.getStringExtra(tripName);
		 

		 //Transferring list content to array
		String[] addressBook = new String[placeList.size()];
		for (int i = 0; i < placeList.size(); i++) {

			addressBook[i] = placeList.get(i);
			Log.d("Place", addressBook[i]);
			Log.d("GeoCoordinates", getLatLng(addressBook[i]).latitude + "");
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

		//Adding a normal map to first tab
		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				38, -97), 3f));

		//Adding a hybrid map to second map
		myMap2 = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map2)).getMap();
		myMap2.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		myMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				38, -97), 3f));
	
		
		//Going through list of destination and adding markers for each place on both maps
		for (int i = 0; i < addressBook.length; i++) {
			myMap.addMarker(new MarkerOptions()
					.position(getLatLng(addressBook[i])).title("" + (i + 1))
					.snippet(addressBook[i])
					// .snippet(addressBook[i])
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mapicon1)));

			myMap2.addMarker(new MarkerOptions()
					.position(getLatLng(addressBook[i])).title("" + (i + 1))
					.snippet(addressBook[i])
					// .snippet(addressBook[i])
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mapicon1)));

			// Storing geocoordinates in arrayList
			listLatLng = new ArrayList<LatLng>();
			for (int j = 0; j < addressBook.length; j++) {
				listLatLng.add(getLatLng(addressBook[j]));
				d = calcTripDistance(listLatLng);

			}
			
			
			//Adding polyline to map with normal terrain
			myMap.addPolyline(new PolylineOptions().addAll(listLatLng).width(5)
					.color(Color.MAGENTA));
			//Adding polyline to map with hybrid terrain
			myMap2.addPolyline(new PolylineOptions().addAll(listLatLng)
					.width(8).color(Color.BLUE));
			
			//Adding marker listeners to markers on map
			myMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				public boolean onMarkerClick(Marker m) {
					
					//When marker is clicked, a info window pops up
					m.showInfoWindow();
					return true;

				}
			});
			
			
			myMap.setInfoWindowAdapter(new InfoWindowAdapter() {

				// Use default InfoWindow frame
				@Override
				public View getInfoWindow(Marker arg0) {
					
				
					return null;
				}

				// Defines the contents of the InfoWindow
				@Override
				public View getInfoContents(Marker m) {

					// Getting view from the layout file info_window_layout
					View v = getLayoutInflater().inflate(
							R.layout.marker_info_content, null);
                   
					LatLng coord = new LatLng(m.getPosition().latitude, m.getPosition().longitude);
					int pos = listLatLng.indexOf(coord);
				
					String add = "Address: " + placeList.get(pos);
					String tMemo = "Notes for this place: " + memos.get(pos);
					String name = destNames.get(pos);

					
					// Getting reference to the TextView to set latitude
					TextView tvLat = (TextView) v.findViewById(R.id.txtBudget);
					tvLat.setText(add);

					TextView Hotel2 = (TextView) v.findViewById(R.id.Hotel2);
					Hotel2.setText(tMemo);
					TextView Hotel3 = (TextView) v.findViewById(R.id.Hotel3);
					Hotel3.setText("Destination Name: " +  name);

					
	
					
					// Returning the view containing InfoWindow contents
					return v;

				}
			});
			
			
			myMap2.setInfoWindowAdapter(new InfoWindowAdapter() {

				// Use default InfoWindow frame
				@Override
				public View getInfoWindow(Marker arg0) {
					
				
					return null;
				}

				// Defines the contents of the InfoWindow
				@Override
				public View getInfoContents(Marker m) {

					// Getting view from the layout file info_window_layout
					View v = getLayoutInflater().inflate(
							R.layout.marker_info_content, null);
                   
					LatLng coord = new LatLng(m.getPosition().latitude, m.getPosition().longitude);
					int pos = listLatLng.indexOf(coord);
				
					String add = "Address: " + placeList.get(pos);
					String tMemo = "Notes for this place: " + memos.get(pos);
					String name = destNames.get(pos);

					
					// Getting reference to the TextView to set latitude
					TextView tvLat = (TextView) v.findViewById(R.id.txtBudget);
					tvLat.setText(add);

					TextView Hotel2 = (TextView) v.findViewById(R.id.Hotel2);
					Hotel2.setText(tMemo);
					TextView Hotel3 = (TextView) v.findViewById(R.id.Hotel3);
					Hotel3.setText("Destination Name: " +  name);

					
	
					
					// Returning the view containing InfoWindow contents
					return v;

				}
			});
			
			
		}

	}// onCreate


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

	// Calculates distance for entire trip
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

	// Calculate distance between 2 geocoordinates. This method is used to
	// calculate entire trip distance
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

	//Method converting degrees to radians
	private double deg2rad(double deg) {

		return (deg * Math.PI / 180.0);

	}

	//Method to converting radians to degrees
	private double rad2deg(double rad) {

		return (rad * 180 / Math.PI);

	}
	

}// class