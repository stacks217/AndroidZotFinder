package edu.uci.ZotFinder.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import edu.uci.ZotFinder.data.BuildingDatabase;
import edu.uci.ZotFinder.data.DepartmentDatabase;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.data.ServicesDatabase;

public class DatabaseBuilderService extends IntentService {

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            loadDatabase();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DatabaseBuilderService(){
        super("DatabaseBuilderService");
    }

    public void loadDatabase() throws PackageManager.NameNotFoundException {
        long start = SystemClock.elapsedRealtime();
        BuildingDatabase buildingDatabase = new BuildingDatabase(this);
        DepartmentDatabase departmentDatabase = new DepartmentDatabase(this);
        ServicesDatabase servicesDatabase = new ServicesDatabase(this);

        BufferedReader br = null;
        String line = "";
        InputStream buildingIS = null;
        InputStream departmentIS = null;
        InputStream servicesIS = null;

        SharedPreferences settings = getSharedPreferences("ZotFinder Preferences", 0);
        boolean addedDatabase = settings.getBoolean("addedDatabase", false);
        int appVersion = settings.getInt("appVersion", 0);
        int packageVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        if (appVersion < packageVersion)
        {
            settings.edit().putInt("appVersion", packageVersion);
            Log.v("DatabaseBuilderService", "Starting...");
            buildingIS = getResources().openRawResource(R.raw.building_file);
            try {
                buildingDatabase.clear();
                buildingDatabase.open();
                br = new BufferedReader(new InputStreamReader(buildingIS));
                while ((line = br.readLine()) != null)
                {
                    String[] buildingInfo = line.split(",", -1);
                    buildingDatabase.addToDatabase(buildingInfo);
                }
                buildingIS.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                buildingDatabase.close();
            }

            departmentIS = getResources().openRawResource(R.raw.departments_file);
            try {
                departmentDatabase.clear();
                departmentDatabase.open();
                br = new BufferedReader(new InputStreamReader(departmentIS));
                while ((line = br.readLine()) != null)
                {
                    String[] departmentInfo = line.split(",", -1);
                    departmentDatabase.addToDatabase(departmentInfo);
                }
                departmentIS.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                departmentDatabase.close();
            }

            servicesIS = getResources().openRawResource(R.raw.campus_services);
            try {
                servicesDatabase.clear();
                servicesDatabase.open();
                br = new BufferedReader(new InputStreamReader(servicesIS));
                while ((line = br.readLine()) != null)
                {
                    String[] servicesInfo = line.split(",", -1);
                    servicesDatabase.addToDatabase(servicesInfo);
                }
                servicesIS.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                servicesDatabase.close();
            }
            addedDatabase = true;

            settings = getSharedPreferences("ZotFinder Preferences", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("addedDatabase", addedDatabase);
            editor.commit();
            Log.v("DatabaseBuilderService", "Finished in " + (SystemClock.elapsedRealtime() - start) + "ms" );
        }
    }
}
