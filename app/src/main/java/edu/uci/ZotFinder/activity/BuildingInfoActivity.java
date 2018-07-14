package edu.uci.ZotFinder.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.uci.ZotFinder.data.BuildingDatabase;
import edu.uci.ZotFinder.R;

public class BuildingInfoActivity extends AppCompatActivity {

	protected TextView buildingAddress;
	protected TextView buildingNumber;
    protected TextView buildingAbbr;
	protected ImageView buildingImage;
	protected String buildingNameString;
	protected String buildingAddressString;
	protected String buildingNumberString;
    protected String buildingAbbrString;
    protected int buildingId;
    protected float buildingLongitude;
    protected float buildingLatitude;
    protected String imageUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_building_info);

        buildingId = getIntent().getExtras().getInt("BUILDING_ID");
        SQLiteDatabase db = (new BuildingDatabase(this)).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, buildingName, buildingNumber, buildingAddress, buildingLongitude, buildingLatitude, buildingAbbr FROM building WHERE _id=?",
                new String[]{""+buildingId});

        if (cursor.getCount() == 1)
        {
            cursor.moveToFirst();

            buildingNameString = cursor.getString(cursor.getColumnIndex("buildingName"));

            buildingAddress = (TextView) findViewById(R.id.buildingAddress);
            buildingAddressString = cursor.getString(cursor.getColumnIndex("buildingAddress"));
            buildingAddress.setText(buildingAddressString);

            buildingNumber = (TextView) findViewById(R.id.buildingNumber);
            buildingNumberString = cursor.getString(cursor.getColumnIndex("buildingNumber"));
            buildingNumberString = buildingNumberString.replaceAll(" ","");
            buildingNumber.setText(buildingNumberString);
            if (TextUtils.isEmpty(buildingNumberString.trim())) {
                findViewById(R.id.bldgNumber).setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.bldgNumber).setVisibility(View.VISIBLE);
            }

            buildingAbbr = (TextView) findViewById(R.id.buildingAbbr);
            buildingAbbrString = cursor.getString(cursor.getColumnIndex("buildingAbbr"));
            buildingAbbr.setText(buildingAbbrString);
            if (TextUtils.isEmpty(buildingAbbrString.trim())) {
                findViewById(R.id.abbrBuilding).setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.abbrBuilding).setVisibility(View.VISIBLE);
            }

            buildingLongitude = Float.valueOf(cursor.getString(cursor.getColumnIndex("buildingLongitude")));

            buildingLatitude = Float.valueOf(cursor.getString(cursor.getColumnIndex("buildingLatitude")));

            buildingImage = (ImageView) findViewById(R.id.building_image);
            imageUrl = "https://eee.uci.edu/images/buildings/" + buildingNumberString + ".jpg";
            Bitmap img = null;
            try {
                img = new RetrieveDirectoryResultTask().execute(imageUrl).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(img!=null)
                buildingImage.setImageBitmap(img);
            else
                buildingImage.setBackgroundResource(R.drawable.uci_campus);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(buildingNameString);
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
		Intent intent = new Intent(this,MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 1);
		bundle.putInt("BUILDING_ID", buildingId);
		bundle.putString("buildingName", buildingNameString);
		bundle.putString("buildingAddress", buildingAddressString);
		bundle.putString("buildingNumber", buildingNumberString);
        bundle.putString("buildingAbbr", buildingAbbrString);
		bundle.putFloat("buildingLongitude", buildingLongitude);
		bundle.putFloat("buildingLatitude", buildingLatitude);
		intent.putExtras(bundle);
        intent.putExtra("SPLASH", false);
		startActivity(intent);
	}
	

	private class RetrieveDirectoryResultTask extends AsyncTask<String, Integer,Bitmap> {

		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap img = null;
			try{
				URL url = new URL(imageUrl);
				HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				InputStream is = connection.getInputStream();
				img = BitmapFactory.decodeStream(is);
			} catch (Exception e) {
					Log.d("Exception while downloading url", e.toString());
			 	}
			return img;
		}
	}
}
