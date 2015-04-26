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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
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
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MapCreate extends Activity {
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
	double d;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent mIntent = getIntent(); // get reference to Intent contain
										// addresses from user
		ArrayList<String> placeList = mIntent
				.getStringArrayListExtra("destinationList");

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

		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				42.331, -71.09), 10));

		myMap2 = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map2)).getMap();
		myMap2.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		myMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				42.331, -71.09), 10));

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

			// Storing geocoordinates in array
			List<LatLng> listLatLng = new ArrayList<LatLng>();
			for (int j = 0; j < addressBook.length; j++) {

				listLatLng.add(getLatLng(addressBook[j]));
				d = calcTripDistance(listLatLng);

			}

			myMap.addPolyline(new PolylineOptions().addAll(listLatLng).width(5)
					.color(Color.MAGENTA));

			myMap2.addPolyline(new PolylineOptions().addAll(listLatLng)
					.width(8).color(Color.BLUE));

			myMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				public boolean onMarkerClick(Marker m) {

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

					// Getting reference to the TextView to set latitude
					TextView tvLat = (TextView) v.findViewById(R.id.txtBudget);
					// tvLat.setText("ok");

					// Getting reference to the TextView to set longitude
					EditText amount = (EditText) v
							.findViewById(R.id.budgetAmount);
					amount.setHint("Estimated expenses");

					// Returning the view containing InfoWindow contents
					return v;

				}
			});
		}

	}// onCreate

	public  String getGeoCoordinates(String add) {

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

	private double deg2rad(double deg) {

		return (deg * Math.PI / 180.0);

	}

	private double rad2deg(double rad) {

		return (rad * 180 / Math.PI);

	}

}// class