package com.example.eventtrackermap;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
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

public class Add_newparty extends Activity implements OnItemSelectedListener  {

SQLController dbcon;	
EditText partyTheme_et, comment_et, location_et, dressing_et;
private TextView meetingDate;
String [] food = {"Food Provided", "No Food"};
int selection_index;
static final int DATE_DIALOG_ID = 0;
private int mYear;
private int mMonth;
private int mDay;



private NotificationManager mNotificationManager;
private Notification notifyDetails;
private int SIMPLE_NOTFICATION_ID;
private String contentTitle = "New Party Notification";
private String contentText = "Get back to Application by clicking me";
private String tickerText = "New Party, Click Me !!!";

protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.partyinfo);
	
	dbcon = new SQLController(this);//back end assigning Context, attach it to dbcon SQLController
	dbcon.open();
	
	//items order follows UI design
	partyTheme_et = (EditText) findViewById(R.id.partyTheme_et);
	
/*make button click-able, and able to show the select-able date dialogue*/
	configureImagebutton();
	meetingDate= (TextView)findViewById(R.id.showMyDate);
/*make date textView shows current date automatically*/	
	final Calendar c = Calendar.getInstance();//Constructs a new instance of the Calendar subclass 
    mYear = c.get(Calendar.YEAR);
    mMonth = c.get(Calendar.MONTH);
    mDay = c.get(Calendar.DAY_OF_MONTH);  
    updateDisplay();	
/**/	
	comment_et = (EditText) findViewById(R.id.comment_et);
	location_et = (EditText) findViewById(R.id.location_et);
	dressing_et = (EditText) findViewById(R.id.dressing_et);
	
/*Food Spinner*/
	Spinner spin = (Spinner) findViewById(R.id.f);
	spin.setOnItemSelectedListener(this);
	ArrayAdapter<String> aa = new ArrayAdapter<String>(
			this,
			android.R.layout.simple_spinner_item,
			food);
	aa.setDropDownViewResource(
			   android.R.layout.simple_spinner_dropdown_item);  
			spin.setAdapter(aa);
/*Food Spinner*/

			mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		    Intent notifyIntent = new Intent(this, Add_newparty.class);
			
			PendingIntent pendingIntent = PendingIntent.getActivity(
					this, 0, notifyIntent,
					android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
			
			notifyDetails = new Notification.Builder(this)
			
							.setContentTitle(contentTitle)    //set Notification text and icon
							.setContentText(contentText)
							.setSmallIcon(R.drawable.droid)	
							
							.setTicker(tickerText)			//set status bar text
							
							.setWhen(System.currentTimeMillis())    //timestamp when event occurs
							
							//Set title, text and pending intent to fire when notify() executed 
							.addAction(R.drawable.icon, contentTitle, pendingIntent)
							
							//set Android to vibrate when notified
							.setVibrate(new long[] {1000, 1000, 1000, 1000})
							 						
							.build();
} 	//onCreate end

private void updateDisplay() {
    this.meetingDate.setText(
        new StringBuilder()
                // Month is 0 based so add 1
        		//Appends the string representation of the specified int value.
                .append(mMonth + 1).append("-")
                .append(mDay).append("-")
                .append(mYear).append(" "));
}

//make click-able button
private void configureImagebutton() {
	ImageButton mPickDate=(ImageButton) findViewById(R.id.myDatePickerButton);
	
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
	 selection_index = position;
} 

public void onNothingSelected(AdapterView<?> parent) {
	}

//top right corner button
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menuextra, menu);
    return true;
}

public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) { 
	
	case R.id.save_btn:
		createNewParty();
		//save comes with notification
		mNotificationManager.notify(SIMPLE_NOTFICATION_ID,
				notifyDetails);
		Intent main = new Intent(Add_newparty.this, PartyListMain.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(main);
		return true;
	
	case R.id.email_btn:
		sendEmail(); 
		return true;
	
	case R.id.view_Party_btn:
		alertExit();
		
		return true;
	default:
		return super.onOptionsItemSelected(item);
	}
}

public void createNewParty(){
	String partyTheme = partyTheme_et.getText().toString();
	String date = meetingDate.getText().toString();
	String comment = comment_et.getText().toString();
	String location = location_et.getText().toString();
	String dressing = dressing_et.getText().toString();
	int food_idx = selection_index;
							//refer to the controller in the Party.class
	dbcon.insertData(new Party(partyTheme, date, comment, location, dressing, food_idx));
	}



public void sendEmail(){			//this creates an String[] array with size()
	String partyTheme = partyTheme_et.getText().toString();
	String date = meetingDate.getText().toString();
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
  		  				"Dress code: "+ dressing + "\n"+ 
    					"Food: "+ food[food_idx] + "\n" + "\n" +
    					"Please respond to this email if you have further inquiry. Thank you!"); 

    
    try {
  	  if(partyTheme.trim().length()==0|| date.trim().length()==0|| comment.trim().length()==0||
  			location.trim().length()==0|| dressing.trim().length()==0)
  		    		{
  				  		Toast.makeText(Add_newparty.this, 
  					         "Please enter all information required.", Toast.LENGTH_SHORT).show();
  		    		}
  	  else{
  		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
  	
        finish();
        Log.i("Finished sending email...", "");}
    } catch (android.content.ActivityNotFoundException ex) {
       Toast.makeText(Add_newparty.this, 
       "There is no email client installed.", Toast.LENGTH_SHORT).show();
    }
    
 }


public void alertExit(){
	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setTitle("Exit without saving?");
    builder.setMessage("Click NO and your party will be automaticaly saved");

    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            // Do nothing but close the dialog

            dialog.dismiss();
            finish();
        }

    });

    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Do nothing
            dialog.dismiss();
            //when clicked yes in the dialog, it will save the existing input
          
        }
    });

    AlertDialog alert = builder.create();
    alert.show();
}

}//class end


