package edu.uci.ZotFinder.activity;

import java.util.HashMap;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import edu.uci.ZotFinder.data.BuildingDatabase;
import edu.uci.ZotFinder.R;

public class PersonInfoActivity extends AppCompatActivity {
    String number;
    String name;
    String email;
    String officeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_person_info);

        Intent intent = getIntent();
        HashMap<String,String> personResults = (HashMap<String, String>) intent.getSerializableExtra("person");

        //grabs the name from the intent passed from SearchActivity
        //puts it into set textView:"personInfoHeaderTextView"
        name = personResults.get("name");

        //grabs the officeLocation from the intent passed from SearchActivity
        //puts it into set textView:"officeLocation
        officeLocation = personResults.get("address");
        TextView officeTextView = (TextView) findViewById(R.id.officeLocation);
        if (TextUtils.isEmpty(officeLocation)) {
            officeTextView.setVisibility(View.GONE);
            findViewById(R.id.locateButton).setVisibility(View.GONE);
            findViewById(R.id.officeHeader).setVisibility(View.GONE);
        } else {
            officeTextView.setText(officeLocation);
        }

        //some faculty do not have a phone number so this will put the text only if it is a valid phone number
        //if the number provided is equal to "N/A" then a textview will not be set
        number = personResults.get("phoneNumber");
        if (!number.equals("N/A") && !TextUtils.isEmpty(number))
        {
            TextView phoneTextView = (TextView) findViewById(R.id.phoneNumber);
            phoneTextView.setText(number);
        }
        else
        {
            findViewById(R.id.phoneNumber).setVisibility(View.GONE);
            findViewById(R.id.phoneHeader).setVisibility(View.GONE);
            findViewById(R.id.callButton).setVisibility(View.GONE);
        }

        //grabs the email from the intent passed from SearchActivity
        //puts it into set textView:"email"
        email = personResults.get("email");
        TextView emailTextView = (TextView) findViewById(R.id.email);
        emailTextView.setText(email);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                        intent.putExtra("SPLASH", false);
                     startActivity(intent);
             } else {
                 new AlertDialog.Builder(this)
                         .setMessage("This location appears to be far from campus so it will not be " +
                                 "shown on the map. Would you like to view this location in Google Maps?")
                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(officeLocation));
                                 Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                 mapIntent.setPackage("com.google.android.apps.maps");
                                 if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                     startActivity(mapIntent);
                                 } else {
                                     new AlertDialog.Builder(PersonInfoActivity.this)
                                             .setMessage("You do not have Google Maps installed.")
                                             .setNeutralButton("OK", null)
                                             .create()
                                             .show();
                             }
                         }
             })
                         .setNegativeButton("No", null)
                         .create()
                         .show();
             }
    }

    public void call(View v){
        if (!number.equals("N/A") && !TextUtils.isEmpty(number)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
    }
    
    public void sendEmail(View v){
    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("play/Text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}