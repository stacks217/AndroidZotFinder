package edu.uci.ZotFinder.activity;

import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.data.ServicesDatabase;

public class ServicesInfoActivity extends AppCompatActivity {
	
	protected TextView serviceAddress;
	protected String serviceNameString;
	protected String serviceAddressString;
	protected float serviceLongitude;
	protected float serviceLatitude;
    protected int serviceId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_services_info);

        serviceId = getIntent().getExtras().getInt("SERVICE_ID");
        SQLiteDatabase db = (new ServicesDatabase(this)).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, serviceName, serviceAddress, serviceLongitude, serviceLatitude FROM service WHERE _id=?",
                new String[]{""+serviceId});

        if (cursor.getCount() == 1)
        {
            cursor.moveToFirst();

            serviceNameString = cursor.getString(cursor.getColumnIndex("serviceName"));

            serviceAddress = (TextView) findViewById(R.id.serviceAddress);
            serviceAddressString = cursor.getString(cursor.getColumnIndex("serviceAddress"));
            serviceAddress.setText(serviceAddressString);

            serviceLongitude = Float.valueOf(cursor.getString(cursor.getColumnIndex("serviceLongitude")));
            serviceLatitude = Float.valueOf(cursor.getString(cursor.getColumnIndex("serviceLatitude")));
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(serviceNameString);
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
	
	public void plotPoint(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 4);
		bundle.putInt("service_ID", serviceId);
		bundle.putString("serviceName", serviceNameString);
		bundle.putString("serviceAddress", serviceAddressString);
		bundle.putFloat("serviceLongitude", serviceLongitude);
		bundle.putFloat("serviceLatitude", serviceLatitude);
		intent.putExtras(bundle);
        intent.putExtra("SPLASH", false);
		startActivity(intent);
	}
}
