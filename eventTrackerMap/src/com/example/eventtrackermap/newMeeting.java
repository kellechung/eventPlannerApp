package com.example.eventtrackermap;

import java.util.ArrayList;
import java.util.Calendar;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.net.Uri;

//testing 

public class newMeeting extends Activity{
	
	private TextView mDateDisplay;
	private ImageButton mPickDate;
	private ImageButton addContact;
	
	//create variables for storing meetings and sending emails
	private String subject;
	private String date;
	private String location;
	private String fromTime;
	private String toTime;
	private EditText meetingSubject;
	private EditText meetingLocation;
	private TextView meetingDate;
	private EditText meetingFromTime;
	private EditText meetingToTime;
	
	
	//initialize SQLite
	SQLiteDatabase db;
	
	//create variables for sending email
	private ListView emailList;
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
        
        
        emailList = (ListView)findViewById(android.R.id.list);
        emails = new ArrayList<String>();
    	aa = new ArrayAdapter<String> (this,R.layout.mylist,emails);
        emailList.setAdapter(aa);
        
        //mDateDisplay = (TextView) findViewById(R.id.showMyDate);
        configureImagebutton();
        
        //initialize database
        db=openOrCreateDatabase("meetings",MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS meetings (subject VARCHAR2, date VARCHAR2," +
        		"begintime VARCHAR2, endtime VARCHAR2, location VARCHAR2);");
       
    };
    
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
     

	private void updateDisplay() {
	    this.meetingDate.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(mMonth + 1).append("-")
	                .append(mDay).append("-")
	                .append(mYear).append(" "));
	}
	
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
    
    //inflate action bard with three icons: save, send email and back
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
	     		storeMeeting();
	     		sendEmail(); 
	     		return true;
	         case R.id.saving://case 2, press save button
	        	 storeMeeting();
	             return true;
	         case R.id.viewMeeting://case 3, press viewmeeting button
	        	 showMeeting(addContact);
	             return true;
	         case R.id.exit://case 4, press exit button
	        	 finish();
	             return true;
	         default:
	             return super.onOptionsItemSelected(item);
	     }
	 }
	 
	 //create method to send email
	 protected void sendEmail() {
	      Log.i("Send email", "");
	      String[] TO = emails.toArray(new String[emails.size()]);
	      subject= meetingSubject.getText().toString();
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      emailIntent.setData(Uri.parse("mailto:"));
	      emailIntent.setType("text/plain");


	      emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
	      emailIntent.putExtra(Intent.EXTRA_TEXT, "For testing purpose"); //need to write a method for output formatting

	      try {
	         startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	         finish();
	         Log.i("Finished sending email...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	         Toast.makeText(newMeeting.this, 
	         "There is no email client installed.", Toast.LENGTH_SHORT).show();
	      }
	   }
	 
	 
	 //create method to store a meeting into datebase
	 protected void storeMeeting(){
		  subject= meetingSubject.getText().toString(); 
		  date= meetingDate.getText().toString();
		  location= meetingLocation.getText().toString();
		  fromTime= meetingFromTime.getText().toString();
		  toTime= meetingToTime.getText().toString();
		  db.execSQL("INSERT INTO  meetings VALUES('"+subject+"','"+date+"','"+fromTime+"', '"+toTime+"', '"+location+"');");
		  Toast.makeText(newMeeting.this, 
			         "Your meeting has been saved.", Toast.LENGTH_SHORT).show();
	 }
	 
	 //create method to display current meetings
	 public void showMeeting(View view)
	 {
	    Cursor c=db.rawQuery("SELECT * from meetings", null);
	    int count= c.getCount();
	    c.moveToFirst();
	    TableLayout tableLayout = new TableLayout(getApplicationContext());
	    tableLayout.setVerticalScrollBarEnabled(true);
	    TableRow tableRow;
	    TextView textView,textView1,textView2,textView3,textView4,textView5;
	    tableRow = new TableRow(getApplicationContext());
	    textView=new TextView(getApplicationContext());
	    textView.setText("Subject");
	    textView.setTextColor(Color.RED);
	   	textView.setTypeface(null, Typeface.BOLD);
	   	textView.setPadding(20, 20, 20, 20);
	   	tableRow.addView(textView);
	    textView4=new TextView(getApplicationContext());
	   	textView4.setText("Date");
	   	textView4.setTextColor(Color.RED);
	   	textView4.setTypeface(null, Typeface.BOLD);
	   	 textView4.setPadding(20, 20, 20, 20);
	   	tableRow.addView(textView4);
	     textView5=new TextView(getApplicationContext());
	   	textView5.setText("Time Begin");
	   	textView5.setTextColor(Color.RED);
	   	textView5.setTypeface(null, Typeface.BOLD);
	   	textView5.setPadding(20, 20, 20, 20);
	   	tableRow.addView(textView5);
	    tableLayout.addView(tableRow);
	      for (Integer j = 0; j < count; j++)
	      {
	          tableRow = new TableRow(getApplicationContext());
	          textView1 = new TextView(getApplicationContext());
	          textView1.setText(c.getString(c.getColumnIndex("subject")));
	          textView2 = new TextView(getApplicationContext());
	          textView2.setText(c.getString(c.getColumnIndex("date")));
	          textView3 = new TextView(getApplicationContext());
	          textView3.setText(c.getString(c.getColumnIndex("begintime")));
	          textView1.setPadding(20, 20, 20, 20);
	          textView2.setPadding(20, 20, 20, 20);
	          textView3.setPadding(20, 20, 20, 20);
	          tableRow.addView(textView1);
	          tableRow.addView(textView2);
	          tableRow.addView(textView3);
	          tableLayout.addView(tableRow);
	          c.moveToNext() ;
	      }
	      setContentView(tableLayout);
	 db.close();
	 }
	 
}
