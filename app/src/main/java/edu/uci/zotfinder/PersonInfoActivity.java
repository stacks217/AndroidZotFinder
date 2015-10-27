package edu.uci.zotfinder;

import java.util.HashMap;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonInfoActivity extends Activity {
    String number;
    String name;
    String email;
    String officeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_person_info);
            
//    		getSupportActionBar().setHomeButtonEnabled(true);
//    		getSupportActionBar().setIcon(R.drawable.ic_action_previous_item);
            
    		View commonFooter = findViewById(R.id.footer);
    		Button dialerButton = (Button) commonFooter.findViewById(R.id.dialerLinkButton);
    		Button emergencyButton = (Button) commonFooter.findViewById(R.id.emergencyLinkButton);
    		Button mapButton = (Button) commonFooter.findViewById(R.id.mapLinkButton);	
    		mapButton.setBackgroundResource(R.drawable.map_icon);
    		emergencyButton.setBackgroundResource(R.drawable.emergency_icon);
    		dialerButton.setBackgroundResource(R.drawable.dialer_icon);
            
            Intent intent = getIntent();
            HashMap<String,String> personResults = (HashMap<String, String>) intent.getSerializableExtra("person");
            Log.w("com.example.test2app",personResults.get("name"));
    
    
            //grabs the name from the intent passed from SearchActivity
            //puts it into set textView:"personInfoHeaderTextView"
            name = personResults.get("name");
//            getSupportActionBar().setTitle(name);
            
            //grabs the officeLocation from the intent passed from SearchActivity
            //puts it into set textView:"officeLocation
            officeLocation = personResults.get("address");
            TextView officeTextView = (TextView) findViewById(R.id.officeLocation);
            officeTextView.setText(officeLocation);    
    
            //some faculty do not have a phone number so this will put the text only if it is a valid phone number
            //if the number provided is equal to "N/A" then a textview will not be set
    number = personResults.get("phoneNumber");
    if (!number.equals("N/A"))
    {
                TextView phoneTextView = (TextView) findViewById(R.id.phoneNumber);
                phoneTextView.setText(number);    
    }
    else
    {
    	TextView phoneTextView = (TextView) findViewById(R.id.phoneNumber);
        phoneTextView.setText("N/A");    
    }
    
    //grabs the email from the intent passed from SearchActivity
          //puts it into set textView:"email"
    email = personResults.get("email");
            TextView emailTextView = (TextView) findViewById(R.id.email);
            emailTextView.setText(email);  
            
    }
    
    //goes to the phone's internal dial screen with the number given
    //if the number is equal to "N/A" meaning the faculty member did not have a phone number, nothing will happen
    public void call(View view) {
             Intent intent = new Intent(Intent.ACTION_DIAL);
             if(!(number.equals("N/A"))) {
                     intent.setData(Uri.parse("tel:" + number));
                     startActivity(intent); 
             }
    }
    
    public void plotPoint(View view)
    {
            String buildingLocation = officeLocation;
            for(int i=1; i<buildingLocation.length(); i++)
            {
                    if(Character.isDigit(buildingLocation.charAt(0)))
                    {
                            buildingLocation = buildingLocation.substring(1);
                    }
                    else
                    {
                            buildingLocation = buildingLocation.substring(1);
                            break;
                    }
            }
            
            //hardcoded solution for DBH
            if(buildingLocation.equals("Donald Bren Hall"))
            {
                    buildingLocation = "Bren Hall";
            }
            
            SQLiteDatabase buildingDb = (new BuildingDatabase(this)).getReadableDatabase();
            Cursor cursor = buildingDb.rawQuery("SELECT _id, buildingName, buildingNumber, buildingAddress, buildingLongitude, buildingLatitude FROM building WHERE buildingName like ?",
                    new String[]{buildingLocation});
        
             if (cursor.getCount() == 1)
             {
                     cursor.moveToFirst();
                     Intent intent = new Intent(this,MainActivity.class);
                     Bundle bundle = new Bundle();
                     bundle.putInt("type", 3);
                     bundle.putString("personName", name);
                     bundle.putString("officeAddress", officeLocation);
                     bundle.putFloat("officeLatitude", cursor.getFloat(cursor.getColumnIndex("buildingLatitude")));
                     bundle.putFloat("officeLongitude", cursor.getFloat(cursor.getColumnIndex("buildingLongitude")));
                     intent.putExtras(bundle);
                     startActivity(intent);
             }
    }
    
    public void sendEmail(View v)
    {
    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("play/Text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    
    //Back button
            public void finishActivity(View v){
                    finish();
                }
    
    //Footer Methods
    //method to go to activity: MainActivity
    //creates intent used to store the information of a different activity within this activity
    //startActivity(intent) changes the current activity to the intent activity
    public void goToMap(View view) { 
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
    }
     
    //method to go to activity: EmergencyActivity
    //creates intent used to store the information of a different activity within this activity
    //startActivity(intent) changes the current activity to the intent activity
    public void goToEmergencyInfo(View view) { 
            Intent intent = new Intent(this,EmergencyActivity.class);
            startActivity(intent);
    }

    //method to go to activity: DialerActivity
    //creates intent used to store the information of a different activity within this activity
    //startActivity(intent) changes the current
    public void goToEmergencyDialer(View view) { 
            Intent intent = new Intent(this,DialerActivity.class);
            startActivity(intent);
    }

}