package edu.uci.ZotFinder.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;
import edu.uci.ZotFinder.data.BuildingDatabase;
import edu.uci.ZotFinder.data.DepartmentDatabase;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.data.SearchPersonTask;
import edu.uci.ZotFinder.data.ServicesDatabase;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener{
	//bundle is global so that it may be accessed from outside the getData() method
	Bundle bundle;
	ListAdapter listAdapter;
	SimpleAdapter simpleAdapter;
	ListView searchResults;
	Cursor cursor;
	SQLiteDatabase buildingDb;
	SQLiteDatabase departmentDb;
	SQLiteDatabase serviceDb;
	int searchChooser; //-1=person; 0=building; 1=department ; 2=services
	SearchView searchView = null;
    SearchPersonTask searchPersonTask = null;
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        //Create the search view
	        searchView = new SearchView(this);
	        //searchView.setQueryHint("Press Button Below");
            searchView.setQueryHint("Search for Person");
            searchChooser = -1;
            ToggleButton personButton = (ToggleButton) findViewById(R.id.personTab);
            personButton.setChecked(true);
	        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
	        searchView.setOnQueryTextListener(this);
	        searchView.setIconified(false);



	        menu.add(0,1,0, "Search")
	            .setIcon(R.drawable.ic_search)
	            .setActionView(searchView)
	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	        menu.findItem(1).expandActionView();
	        return true;
	    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

		BuildingDatabase buildingDatabase = new BuildingDatabase(this);
		DepartmentDatabase departmentDatabase = new DepartmentDatabase(this);
		ServicesDatabase servicesDatabase = new ServicesDatabase(this);

		buildingDb = buildingDatabase.getReadableDatabase();
		departmentDb = departmentDatabase.getReadableDatabase();
		serviceDb = servicesDatabase.getReadableDatabase();
        searchResults = (ListView) findViewById(R.id.search_list);
        searchResults.setOnItemClickListener(this);
	}
					   
	
	public void choosePersonSearch(View view){
		searchChooser = -1;
		searchView.setQueryHint("Search for Person");
		ToggleButton personButton = (ToggleButton) findViewById(R.id.personTab);
		personButton.setChecked(true);
		ToggleButton buildingButton = (ToggleButton) findViewById(R.id.buildingTab);
		buildingButton.setChecked(false);
		ToggleButton departmentButton = (ToggleButton) findViewById(R.id.departmentTab);
		departmentButton.setChecked(false);
		ToggleButton serviceTab = (ToggleButton) findViewById(R.id.serviceTab);
		serviceTab.setChecked(false);
		searchResults.setAdapter(null);
	}
	public void chooseBuildingSearch(View view){
		searchChooser = 0;
		searchView.setQueryHint("Search for Building");
		ToggleButton personButton = (ToggleButton) findViewById(R.id.personTab);
		personButton.setChecked(false);
		ToggleButton buildingButton = (ToggleButton) findViewById(R.id.buildingTab);
		buildingButton.setChecked(true);
		ToggleButton departmentButton = (ToggleButton) findViewById(R.id.departmentTab);
		departmentButton.setChecked(false);
		ToggleButton serviceTab = (ToggleButton) findViewById(R.id.serviceTab);
		serviceTab.setChecked(false);
		search(" ");
	}
	public void chooseDepartmentSearch(View view){
		searchChooser = 1;
		searchView.setQueryHint("Search for Department");
		ToggleButton personButton = (ToggleButton) findViewById(R.id.personTab);
		personButton.setChecked(false);
		ToggleButton buildingButton = (ToggleButton) findViewById(R.id.buildingTab);
		buildingButton.setChecked(false);
		ToggleButton departmentButton = (ToggleButton) findViewById(R.id.departmentTab);
		departmentButton.setChecked(true);
		ToggleButton serviceTab = (ToggleButton) findViewById(R.id.serviceTab);
		serviceTab.setChecked(false);
		search(" ");
	}
	public void chooseServiceSearch(View view){
		searchChooser = 2;
		searchView.setQueryHint("Search for Service");
		ToggleButton personButton = (ToggleButton) findViewById(R.id.personTab);
		personButton.setChecked(false);
		ToggleButton buildingButton = (ToggleButton) findViewById(R.id.buildingTab);
		buildingButton.setChecked(false);
		ToggleButton departmentButton = (ToggleButton) findViewById(R.id.departmentTab);
		departmentButton.setChecked(false);
		ToggleButton serviceTab = (ToggleButton) findViewById(R.id.serviceTab);
		serviceTab.setChecked(true);
		search(" ");
	}
	
	@SuppressWarnings("deprecation")
	public void search(String input){
		List<HashMap<String, String>> personResults = null;
		if (searchChooser == 0){
			cursor = buildingDb.rawQuery("SELECT _id, buildingName, buildingNumber, buildingAddress, buildingAbbr FROM building WHERE buildingName || ' ' || buildingNumber || buildingAbbr LIKE ?" ,
					new String[]{"%" + input + "%"});
			if(cursor.moveToFirst())
			{
				listAdapter = new SimpleCursorAdapter(
						this, 
						R.layout.activity_building_list_item, 
						cursor, 
						new String[] {"buildingName", "buildingNumber", "buildingAddress", "buildingAbbr"},
						new int[] {R.id.buildingName,R.id.buildingNumber, R.id.buildingAddress, R.id.buildingAbbr});
				searchResults.setAdapter(listAdapter);
			}
			else
				Toast.makeText(getApplicationContext(), "No Results Found. Please Try again.", Toast.LENGTH_LONG).show();
		}	else if (searchChooser == 1){
			cursor = departmentDb.rawQuery("SELECT _id, departmentName, departmentAddress FROM department WHERE departmentName || ' ' || departmentAddress LIKE ?", 
					new String[]{"%" + input + "%"});
			if(cursor.moveToFirst())
			{
				listAdapter = new SimpleCursorAdapter(
						this, 
						R.layout.activity_department_list_item, 
						cursor, 
						new String[] {"departmentName", "departmentAddress"}, 
						new int[] {R.id.departmentName, R.id.departmentAddress});
                searchResults.setAdapter(listAdapter);
			}
			else
				Toast.makeText(getApplicationContext(), "No Results Found. Please Try again.", Toast.LENGTH_LONG).show();
		} else if (searchChooser == -1){
			String searchInput = input;
			searchInput = searchInput.toLowerCase();
			searchInput = searchInput.trim();
            if (searchPersonTask != null) {
                searchPersonTask.cancel(true);
            }
            startProgress();
            searchPersonTask = new SearchPersonTask(this, searchResults);
            searchPersonTask.execute(searchInput);

		}
		else if (searchChooser == 2){
			cursor = serviceDb.rawQuery("SELECT _id, serviceName, serviceAddress FROM service WHERE serviceName || ' ' || serviceAddress LIKE ?", 
					new String[]{"%" + input + "%"});
			if(cursor.moveToFirst())
			{
				listAdapter = new SimpleCursorAdapter(
						this, 
						R.layout.activity_services_list_item, 
						cursor, 
						new String[] {"serviceName", "serviceAddress"}, 
						new int[] {R.id.serviceName, R.id.serviceAddress});
                searchResults.setAdapter(listAdapter);
			}
			else
				Toast.makeText(getApplicationContext(), "No Results Found. Please Try again.", Toast.LENGTH_LONG).show();
		}
	}

    @Override
	public void onDestroy() {
        super.onDestroy();
        if (searchPersonTask != null) {
            searchPersonTask.cancel(true);
        }
    }
	
    public void onItemClick(AdapterView parent, View view, int position, long id){
    	Bundle bundle = new Bundle();
    	if (searchChooser == 0)
    	{
	    	Intent intent = new Intent(this, BuildingInfoActivity.class);
	    	Cursor cursor = (Cursor) listAdapter.getItem(position);
	    	bundle.putInt("BUILDING_ID", cursor.getInt(cursor.getColumnIndex("_id")));
	    	intent.putExtras(bundle);
	    	startActivity(intent);
    	}
    	else if (searchChooser == 1)
    	{
	    	Intent intent = new Intent(this, DepartmentInfoActivity.class);
	    	Cursor cursor = (Cursor) listAdapter.getItem(position);
	    	bundle.putInt("DEPARTMENT_ID", cursor.getInt(cursor.getColumnIndex("_id")));
	    	intent.putExtras(bundle);
	    	startActivity(intent);
    	}
    	else if (searchChooser == -1)
    	{
	    	HashMap<String, String> listResults = null;
            listResults = (HashMap<String, String>) simpleAdapter.getItem(position);
            String uciNetId = listResults.get("ucinetid");
            String urlToGetResult = "https://directory.uci.edu/index.php?uid=" + uciNetId + "&basic_keywords=" + uciNetId + "&modifier=Exact+Match&basic_submit=Search&checkbox_employees=Employees&form_type=basic_search";
            new RetrieveDirectoryResult().execute(urlToGetResult, listResults.get("ucinetid"));
    	}
    	else if (searchChooser == 2){
    		Intent intent = new Intent(this, ServicesInfoActivity.class);
	    	Cursor cursor = (Cursor) listAdapter.getItem(position);
	    	bundle.putInt("SERVICE_ID", cursor.getInt(cursor.getColumnIndex("_id")));
	    	intent.putExtras(bundle);
	    	startActivity(intent);
    	}
    }

    public List<HashMap<String, String>> readSingleResultStream(String output, String input) throws InterruptedException, ExecutionException {
        List<HashMap<String, String>> personResults = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rowid","1");
        output = output.replaceAll("\\r\\n\\t|\\r|\\n|\\t", "");
        if(!(output.isEmpty()))
        {
            String name = output.split("<span class=\"label\">Name</span><span class=\"resultData\">")[1].split("</span></p>")[0];
            map.put("name", name);
            String ucinetid = input;
            if (output.contains("<span class=\"label\">UCInetID</span><span class=\"resultData\">")) {
                ucinetid = output.split("<span class=\"label\">UCInetID</span><span class=\"resultData\">")[1].split("</span></p>")[0];
            }
            map.put("ucinetid", ucinetid);
            if (output.contains("Title </span></TD>")){

                String title = output.split("<TD class=\"positioning_cell\"><span class=\"table_label\">Title</span></TD><TD><span class=\"table_data\">")[1].split("</span></TD>")[0];
                map.put("title", title);
                String department = output.split("<TD class=\"positioning_cell\"><span class=\"table_label\">Department</span></TD><TD><span class=\"table_data\">")[1].split("</span></TD>")[0];
                map.put("department", department);

            }
            else{
                String department = output.split("<TD class=\"positioning_cell\"><span class=\"table_label\">Department</span></TD><TD><span class=\"table_data\">")[1].split("</span></TD>")[0];
                map.put("title", department);
            }
            if(output.contains("Address</span></TD>")){
                String address = output.split("<TD class=\"positioning_cell\"><span class=\"table_label\">Address</span></TD><TD><span class=\"table_data\">")[1].split("</span></TD>")[0];
                map.put("address", address);
            }
            if(output.contains("<span class=\"label\">Phone</span><span class=\"resultData\">")){
                String phoneNumber = output.split("<span class=\"label\">Phone</span><span class=\"resultData\">")[1].split("</span></p>")[0];
                map.put("phoneNumber",phoneNumber);
            }
            else
                map.put("phoneNumber","N/A");
            if(output.contains("p><span class=\"label\">Fax</span><span class=\"resultData\">")){
                String faxNumber = output.split("p><span class=\"label\">Fax</span><span class=\"resultData\">")[1].split("</span></p>")[0];
                map.put("faxNumber",faxNumber);
            }
            else
                map.put("faxNumber", "N/A");
            String email = ucinetid + "@uci.edu";
            map.put("email", email);
            personResults.add(map);
        }
        return personResults;
    }

    public class RetrieveDirectoryResult extends AsyncTask<String, Void, Intent> {
        protected Intent doInBackground(String... input) {
            HashMap<String, String> personResults = null;
            try {
                personResults = readSingleResultStream(retrieveDirectoryResult(input[0]), input[1]).get(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(SearchActivity.this, PersonInfoActivity.class);
            intent.putExtra("person",personResults);
            return intent;
        }

        protected void onPostExecute(Intent result) {
            startActivity(result);
        }
    }
	public String retrieveDirectoryResult(String urlString) {
        StringBuilder outputBuilder = new StringBuilder();
        String output = "";
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                outputBuilder.append(inputLine);
            in.close();
            output = outputBuilder.toString();
            output = output.replaceAll("\\r\\n\\t|\\r|\\n|\\t", "");

            } catch (Exception e) {
                Log.d("Exception download url", e.toString());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        return output;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		searchView.clearFocus();
        search(query);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
        if (searchChooser >= 0 || searchChooser == -1 && newText.length() > 1) {
            search(newText);
        }
		return false;
	}


    public void startProgress() {
        View progress = findViewById(R.id.progress_horizontal);
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    public void stopProgress(SimpleAdapter simpleAdapter) {
        this.simpleAdapter = simpleAdapter;
        View progress = findViewById(R.id.progress_horizontal);
        if (progress != null) {
            progress.setVisibility(View.INVISIBLE);
        }
    }
}
