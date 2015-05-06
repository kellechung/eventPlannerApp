package com.example.eventtrackermap;

import java.util.ArrayList;
import java.util.Calendar;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;//to enable date picker
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.net.Uri;


public class newMeeting extends Activity{
	
	private TextView mDateDisplay;
	private ImageButton mPickDate;
	private ImageButton addContact;
	
	//create variables for storing meetings and sending emails
	private String subject; //store meeting subject
	private String date; //store meeting date	
	private String location; //store meeting location	
	private String fromTime;//store time start
	private String toTime; //store time end
	private String description; //store description entered
	
	//define widgets to enter above inputs
	private EditText meetingSubject; 
	private EditText meetingLocation;
	private TextView meetingDate;
	private EditText meetingFromTime;
	private EditText meetingToTime;
	private EditText meetingDescription;
	
	
	//initialize SQLite
	SQLiteDatabase db;
	
	//create variables for sending email
	private ListView emailList; //listview to display selected email addresses from contact
	private ArrayList<String> emails;
	private ArrayAdapter<String> aa;
	
	//create variables for datepicker
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	static final int PICK_CONTACT_REQUEST = 1;
	private static final String DEBUG_TAG = "Widgets1";
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting);
        
        meetingSubject = (EditText)findViewById(R.id.meetingSubjectEnter);//read entered meeting subject
        meetingDate= (TextView)findViewById(R.id.showMyDate);//read selected date
        meetingLocation= (EditText)findViewById(R.id.meetingLocationEnter);//read entered location
        meetingFromTime= (EditText)findViewById(R.id.EnterTimefrom);//read entered from time
        meetingToTime= (EditText)findViewById(R.id.EnterTimeTo);//read entered to time
        meetingDescription= (EditText)findViewById(R.id.meetingDescriptionEnter);//read entered description
        
        //initial arraylist and arrayadapter to store selected email addresses into arrarylist
        emailList = (ListView)findViewById(android.R.id.list);
        emails = new ArrayList<String>();
    	aa = new ArrayAdapter<String> (this,R.layout.mylist,emails);
        emailList.setAdapter(aa);
        
        //mDateDisplay = (TextView) findViewById(R.id.showMyDate);
        configureImagebutton();
        
        //initialize database
        db=openOrCreateDatabase("meetings",MODE_PRIVATE, null);
        //create the meeting table that has six columns: subject, date, location, timestart, timeend and description
        db.execSQL("CREATE TABLE IF NOT EXISTS meetings (subject VARCHAR2, date VARCHAR2," +
        		"location VARCHAR2, timeStart VARCHAR2, timeEnd VARCHAR2, description VARCHAR2);");
       
    };
    
    //configure two imageButtons: date picker and add email address
    private void configureImagebutton() {
		ImageButton mPickDate=(ImageButton) findViewById(R.id.myDatePickerButton);
	    //the image button implements date picker
		mPickDate.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                showDialog(DATE_DIALOG_ID);
	            }
	});
		
		//configure image button: add contact
		ImageButton addContact= (ImageButton) findViewById(R.id.addContact);
		addContact.setOnClickListener(new OnClickListener(){
			@Override
            public void onClick(View v) 
            {
                 Intent getContacts = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                 startActivityForResult(getContacts, PICK_CONTACT_REQUEST);
            }   
         });
   
     // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
     // display the current date right to the calendar image button
        updateDisplay();
    }
     
    //this method is used to update date displayed by textview when a new date other than today's date is picked by user
	private void updateDisplay() {
	    this.meetingDate.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(mMonth + 1).append("-")
	                .append(mDay).append("-")
	                .append(mYear).append(" "));
	}
	
	//set listener for the date picker
	private DatePickerDialog.OnDateSetListener mDateSetListener =
		    new DatePickerDialog.OnDateSetListener() {
		        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		            mYear = year;
		            mMonth = monthOfYear;
		            mDay = dayOfMonth;
		            updateDisplay();
		        }
		    };
	
    @Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		return new DatePickerDialog(this, mDateSetListener,mYear, mMonth, mDay);}
		       return null;
		    }		
    
    
    //inflate action bar with three icons: save, send email and back
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meeting, menu);
        return super.onCreateOptionsMenu(menu);
    }

     //method to get email address address for selected person from Contacts   
	 @Override 
	 protected void onActivityResult(int reqCode, int resultCode, Intent data)
	 	{
		 if (resultCode == RESULT_OK) {
		        switch (reqCode) 
		        {
		        case PICK_CONTACT_REQUEST:
		            Cursor cursor = null;
		            String email = "";
		            try {
		                Uri result = data.getData();
		                Log.v(DEBUG_TAG, "Got a contact result: " + result.toString());

		                // get the contact id from the Uri
		                String id = result.getLastPathSegment();

		                // query for everything email
		                cursor = getContentResolver().query(Email.CONTENT_URI,  null, Email.CONTACT_ID + "=?", new String[] { id }, null);

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
		                if(email!=""){
		                	emails.add(email);
		                	aa.notifyDataSetChanged();}
		                else if (email.length() == 0) 
		                {
		                    Toast.makeText(this, "No Email for Selected Contact",Toast.LENGTH_LONG).show();
		                }
		            }
		            break;
		        }

		    } else {
		        Log.w(DEBUG_TAG, "Warning: activity result not ok");
		    }
	 	}
	 
	 //handle clicks on action bar
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     // Handle presses on the action bar items
	     switch (item.getItemId()) { 
	     	case R.id.sendEmail: //case 1, press send email button
	     		sendEmail(); 
	     		return true;
	         case R.id.saving://case 2, press save button
	        	 storeMeeting();
	             return true;
	         case R.id.viewMeeting://case 3, press viewmeeting button
	        	 viewMeeting();
	             return true;
	         case R.id.exit://case 4, press exit button
	        	 alertExit();
	             return true;
	         default:
	             return super.onOptionsItemSelected(item);
	     }
	 }
	 
	 //create method to send email
	 protected void sendEmail() {
	      Log.i("Send email", "");
	      //read email addresses store in arrarylist and store them to recipient list
	      String[] TO = emails.toArray(new String[emails.size()]);
	      
	      //read user inputs and store them to attributes of a meeting 
	      subject= meetingSubject.getText().toString(); 
		  date= meetingDate.getText().toString();
		  location= meetingLocation.getText().toString();
		  fromTime= meetingFromTime.getText().toString();
		  toTime= meetingToTime.getText().toString();
		  description= meetingDescription.getText().toString();
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      emailIntent.setData(Uri.parse("mailto:"));
	      emailIntent.setType("text/plain");

	      //insert recipients, subject and text content
	      emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
	      //formatting an email to be sent
	      emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, you are invited to attend a study group meeting."+"\n\n"+
	    		  				"Details of the meeting invitation"+"\n"+"\n"+"Subject: "+subject+"\n"+
	    		  				"Date: "+ date+ "\n"+ "Duration: "+fromTime+" - "+toTime+ "\n"+"Location: "+location+
	    		  				"\n"+"Description: "+description+ "\n"+"\n"+"Please respond to this email if you can't attend. Thank you!"); 

	      try {
	    	  //check input, if any necessary information is not entered, a meeting could not be sent
	    	  if(subject.trim().length()==0|| date.trim().length()==0|| location.trim().length()==0||
	    			     fromTime.trim().length()==0|| toTime.trim().length()==0 || description.trim().length()==0)
	    		    		{
	    				  		Toast.makeText(newMeeting.this, 
	    					         "Please enter all information required.", Toast.LENGTH_SHORT).show();
	    		    		}
	    	  else{
	    	  startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	          finish();
	          Log.i("Finished sending email...", "");}
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(newMeeting.this, 
	         "There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	   }
	 
	protected void storeMeeting(){
		subject= meetingSubject.getText().toString(); 
		  date= meetingDate.getText().toString();
		  location= meetingLocation.getText().toString();
		  fromTime= meetingFromTime.getText().toString();
		  toTime= meetingToTime.getText().toString();
		  description= meetingDescription.getText().toString();
		//check input, if any necessary information is not entered, a meeting could not be saved
		  if(subject.trim().length()==0|| date.trim().length()==0|| location.trim().length()==0||
		     fromTime.trim().length()==0|| toTime.trim().length()==0 || description.trim().length()==0)
	    		{
			  		Toast.makeText(newMeeting.this, 
				         "Please enter all information required.", Toast.LENGTH_SHORT).show();
	    		}
		  else {db.execSQL("INSERT INTO meetings VALUES('"+subject+"','"+date+"','"+location+"','"+fromTime+"','"+toTime+
	    				   "','"+description+"');");
	    		Toast.makeText(newMeeting.this, 
				         "Your meeting has been saved.", Toast.LENGTH_SHORT).show();}
	}
	
	//method to display stored meeting, when no meeting has been stored, warning will be shown by toast.
	//Otherwise, a StringBuffer would append stored information on context
	protected void viewMeeting(){
		  date= meetingDate.getText().toString();
		  location= meetingLocation.getText().toString();
		  fromTime= meetingFromTime.getText().toString();
		  toTime= meetingToTime.getText().toString();
		  description= meetingDescription.getText().toString();
		  Cursor c=db.rawQuery("SELECT * FROM meetings", null);
  			if(c.getCount()==0)
  			{
  				Toast.makeText(newMeeting.this, 
				         "No meeting found.", Toast.LENGTH_SHORT).show();
  			}
  		StringBuffer buffer=new StringBuffer();
  		while(c.moveToNext())
  		{
  			buffer.append("Subject: "+c.getString(0)+"\n");
  			buffer.append("Date: "+c.getString(1)+"\n");
  			buffer.append("Location: "+c.getString(2)+"\n");
  			buffer.append("Start Time: "+c.getString(3)+"\n");
  			buffer.append("End Time: "+c.getString(4)+"\n");
  			buffer.append("Description: "+c.getString(5)+"\n\n");
  		}
  		showMessage("Meeting Details", buffer.toString());
  	}
  	
    //a method build to display message, used by viewMeeting method
	public void showMessage(String title,String message)
    {
    	Builder builder=new Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.show();
	}
    
    //this method alerts the user when it trying to exit without saving the current meeting
	public void alertExit(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit without saving?");
        builder.setMessage("Click NO and your meeting will be automaticaly saved");

        //add the option button Yes
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                finish();
            }

        });

       //add the option button NO
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
                storeMeeting();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
 
    }

	  
	 
}
