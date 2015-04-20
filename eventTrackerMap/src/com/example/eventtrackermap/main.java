package com.example.eventtrackermap;


import java.util.Locale;
import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.net.Uri;
import android.speech.tts.TextToSpeech.OnInitListener;


public class main extends Activity implements OnClickListener, OnInitListener {
	private Button btnMeeting;
	private Button btnTrip;
	private Button btnParty;
	private TextToSpeech speaker;
	private TextView aniText;

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
		
		speaker = new TextToSpeech(this, this);
		
		aniText = (TextView) findViewById(R.id.aniText);
		fadeAnimation(aniText);
		

	}//end of on create

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case 1, click on button Meeting
		case R.id.btnMeeting:
			Intent i1 = new Intent(main.this, newMeeting.class);
			speak("Now you are able to create a new meeting");
			startActivity(i1);
			break;

		// case 2, click on button Trip
		case R.id.btnTrip:
			Intent i2 = new Intent(main.this, Destination.class);
			speak("Now you are able to create a new trip");
			startActivity(i2);
			break;

		 //case 3, click on button Meeting
		 case R.id.btnParty:
		 Intent i3 = new Intent(main.this, PartyListMain.class);
		 speak("Click add party button to create a new party");
		 startActivity(i3);
		 break;

		}
	}
	
	public void speak(String output){
    	speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null);}
	
	@Override
	public void onInit(int status) {
    	if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // If a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);}
	}
	
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu items for use in the action bar
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.maininterface, menu);
	        return super.onCreateOptionsMenu(menu);
	    }
	  
	  @Override
		 public boolean onOptionsItemSelected(MenuItem item) {
		     // Handle presses on the action bar items
		     switch (item.getItemId()) { 
		         case R.id.exitApp:
		        	 finish();
		             return true;
		         default:
		             return super.onOptionsItemSelected(item);
		     }
	  }
	  
	  private void fadeAnimation(View view) {
		  Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
		  aniText.startAnimation(animation);

	  }

	  

}
