package com.example.eventtrackermap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.net.Uri;

public class main extends Activity implements OnClickListener {
	private Button btnMeeting;
	private Button btnTrip;
	private Button btnParty;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// create three buttons to call our three activities
		btnMeeting = (Button) findViewById(R.id.btnMeeting);
		btnMeeting.setOnClickListener(this);

		btnTrip = (Button) findViewById(R.id.btnTrip);
		btnTrip.setOnClickListener(this);

		btnParty = (Button) findViewById(R.id.btnParty);
		btnParty.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case 1, click on button Meeting
		case R.id.btnMeeting:
			Intent i1 = new Intent(main.this, newMeeting.class);
			startActivity(i1);
			break;

		// case 2, click on button Trip
		case R.id.btnTrip:
			Intent i2 = new Intent(main.this, Destination.class);
			startActivity(i2);
			break;

		 //case 3, click on button Meeting
		 case R.id.btnParty:
		 Intent i3 = new Intent(main.this, PartyListMain.class);
		 startActivity(i3);
		 break;

		}
	}

}
