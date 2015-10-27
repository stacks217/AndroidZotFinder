package edu.uci.zotfinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreenActivity extends Activity {
	
	//set time in milliseconds of how long the splash screen will remain on the screen
	private boolean addedDatabase;
	private static int SPLASH_TME_OUT = 3000;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
			new Handler().postDelayed(new Runnable(){
				
				@Override
				public void run(){
					loadDatabase();
					Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
				     startActivity(intent);
				     
				     finish();
				}
			}, SPLASH_TME_OUT);	
    }

	public void loadDatabase()
	{
        BuildingDatabase buildingDatabase = new BuildingDatabase(this);
		DepartmentDatabase departmentDatabase = new DepartmentDatabase(this);
		ServicesDatabase servicesDatabase = new ServicesDatabase(this);
		
		BufferedReader br = null;
		String line = "";
		InputStream buildingIS = null;
		InputStream departmentIS = null;
		InputStream servicesIS = null;
        
        SharedPreferences settings = getSharedPreferences("ZotFinder Preferences", 0);
		addedDatabase = settings.getBoolean("addedDatabase", false);
		if (!addedDatabase)
		{
			buildingIS = getResources().openRawResource(R.raw.building_file);
			try {
				br = new BufferedReader(new InputStreamReader(buildingIS));
				while ((line = br.readLine()) != null)
				{
					String[] buildingInfo = line.split(",");
					buildingDatabase.addToDatabase(buildingInfo);
				}
				buildingIS.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			departmentIS = getResources().openRawResource(R.raw.departments_file);
			try {
				br = new BufferedReader(new InputStreamReader(departmentIS));
				while ((line = br.readLine()) != null)
				{
					String[] departmentInfo = line.split(",");
					departmentDatabase.addToDatabase(departmentInfo);
				}
				departmentIS.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			servicesIS = getResources().openRawResource(R.raw.campus_services);
			try {
				br = new BufferedReader(new InputStreamReader(servicesIS));
				while ((line = br.readLine()) != null)
				{
					String[] servicesInfo = line.split(",");
					servicesDatabase.addToDatabase(servicesInfo);
				}
				servicesIS.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			addedDatabase = true;
			
			settings = getSharedPreferences("ZotFinder Preferences", 0);
			SharedPreferences.Editor editor = settings.edit();
		    editor.putBoolean("addedDatabase", addedDatabase);
		    editor.commit();
		}
	}
}
