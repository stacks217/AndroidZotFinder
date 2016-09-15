package edu.uci.ZotFinder.activity;

import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import edu.uci.ZotFinder.data.DepartmentDatabase;
import edu.uci.ZotFinder.R;

public class DepartmentInfoActivity extends AppCompatActivity {
	

	protected TextView departmentAddress;
	protected TextView departmentPhoneNumber;
	protected TextView departmentWebsite;
	protected String departmentNameString;
	protected String departmentAddressString;
	protected String departmentPhoneNumberString;
	protected String departmentWebsiteString;
	protected float departmentLongitude;
	protected float departmentLatitude;
    protected int departmentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_info);

        departmentId = getIntent().getExtras().getInt("DEPARTMENT_ID");
        SQLiteDatabase db = (new DepartmentDatabase(this)).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, departmentName, departmentPhoneNumber, departmentAddress, departmentWebsite, departmentLongitude, departmentLatitude FROM department WHERE _id=?",
                new String[]{""+departmentId});

        if (cursor.getCount() == 1)
        {
            cursor.moveToFirst();

            departmentNameString = cursor.getString(cursor.getColumnIndex("departmentName"));

            departmentPhoneNumber = (TextView) findViewById(R.id.departmentPhoneNumber);
            departmentPhoneNumberString = cursor.getString(cursor.getColumnIndex("departmentPhoneNumber"));
            departmentPhoneNumber.setText(departmentPhoneNumberString);

            departmentAddress = (TextView) findViewById(R.id.departmentAddress);
            departmentAddressString = cursor.getString(cursor.getColumnIndex("departmentAddress"));
            departmentAddress.setText(departmentAddressString);

            departmentWebsite = (TextView) findViewById(R.id.departmentWebsite);
            departmentWebsiteString = cursor.getString(cursor.getColumnIndex("departmentWebsite"));
            departmentWebsite.setText(departmentWebsiteString);


            departmentLongitude = Float.valueOf(cursor.getString(cursor.getColumnIndex("departmentLongitude")));
            departmentLatitude = Float.valueOf(cursor.getString(cursor.getColumnIndex("departmentLatitude")));
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(departmentNameString);
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
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 2);
		bundle.putInt("DEPARTMENT_ID", departmentId);
		bundle.putString("departmentName", departmentNameString);
		bundle.putString("departmentAddress", departmentAddressString);
		bundle.putString("departmentPhoneNumber", departmentPhoneNumberString);
		bundle.putFloat("departmentLongitude", departmentLongitude);
		bundle.putFloat("departmentLatitude", departmentLatitude);
		intent.putExtras(bundle);
        intent.putExtra("SPLASH", false);
		startActivity(intent);
	}
	
	}
