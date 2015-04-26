package com.example.eventtrackermap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

//This class calculates the final trip cost

public class tripCosts extends Activity implements OnClickListener {

	TextView txtFinalCost;
	DecimalFormat f = new DecimalFormat("$##.00");
	static final int PICK_CONTACT_REQUEST = 1;
	String DEBUG_TAG = "EMAIL_DEBUG_TAG";
	final int PICK1 = Menu.FIRST + 1;
	final int PICK2 = Menu.FIRST + 2;

	// create variables for sending email
	private ListView emailList;
	private ArrayList<String> emails;
	private ArrayAdapter<String> aa;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.final_trip_cost);
		emailList = (ListView) findViewById(android.R.id.list);
		emails = new ArrayList<String>();
		aa = new ArrayAdapter<String>(this, R.layout.mylist, emails);
		emailList.setAdapter(aa);

		Intent i = getIntent();
		double finalCost = i.getDoubleExtra("totalCost", 0);

		txtFinalCost = (TextView) findViewById(R.id.txtFinalCost);

		txtFinalCost.setText(f.format(finalCost));
	}

	@Override
	public void onClick(View v) {

	}

	protected void sendEmail() {
		Log.i("Send email", "");
		String[] TO = emails.toArray(new String[emails.size()]);

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");

		// insert recipients, subject and text content
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Upcoming trip");
		emailIntent.putExtra(Intent.EXTRA_TEXT,
				"Please respond to this email if you can't attend. Thank you!");

		try {

			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			finish();
			Log.i("Finished sending email...", "");
		} catch (Exception ex) {

		}
	}

	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (reqCode) {
			case PICK_CONTACT_REQUEST:
				Cursor cursor = null;
				String email = "";
				try {
					Uri result = data.getData();
					Log.v(DEBUG_TAG,
							"Got a contact result: " + result.toString());

					// get the contact id from the Uri
					String id = result.getLastPathSegment();

					// query for everything email
					cursor = getContentResolver().query(Email.CONTENT_URI,
							null, Email.CONTACT_ID + "=?", new String[] { id },
							null);

					int emailIdx = cursor.getColumnIndex(Email.DATA);

					// let's just get the first email
					if (cursor.moveToFirst()) {
						email = cursor.getString(emailIdx);

						Log.v(DEBUG_TAG, "Got email: " + email);
					} else {
						Log.w(DEBUG_TAG, "No results");
					}
				} catch (Exception e) {
					Log.e(DEBUG_TAG, "Failed to get email data", e);
				} finally {
					if (cursor != null) {
						cursor.close();
					}
					if (email != "") {
						emails.add(email);
						aa.notifyDataSetChanged();
					} else if (email.length() == 0) {
						Toast.makeText(this, "No Email for Selected Contact",
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			}

		} else {
			Log.w(DEBUG_TAG, "Warning: activity result not ok");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tripcosts, menu);

		super.onCreateOptionsMenu(menu);

		MenuItem item1 = menu.add(0, PICK1, Menu.NONE, "ADD CONTACT");
		MenuItem item2 = menu.add(0, PICK2, Menu.NONE, "SEND EMAIL");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int itemID = item.getItemId();

		switch (itemID) {
		case PICK1:
			Intent getContacts = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(getContacts, PICK_CONTACT_REQUEST);
			return true;
		case PICK2:
			sendEmail();
			return true;

		default:
			super.onOptionsItemSelected(item);
		}

		return false;

	}

}
