package edu.uci.zotfinder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {
	//bundle is global so that it may be accessed from outside the getData() method
	Bundle bundle;
	ListAdapter listAdapter;
	SimpleAdapter simpleAdapter;
	ListView searchResults;
	Cursor cursor;
	SQLiteDatabase buildingDb;
	SQLiteDatabase departmentDb;
	SQLiteDatabase serviceDb;
	boolean addedDatabase;
	int searchChooser; //1=person; 0=building; 1=department ; 2=services
	String personOutput;
	SearchView searchView = null;
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        //Create the search view
//	        searchView = new SearchView(getSupportActionBar().getThemedContext());
//	        searchView.setQueryHint("Press Button Below");
//	        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
//	        searchView.setOnQueryTextListener(this);
//	        searchView.setIconified(false);
//	        menu.add(0,1,0, "Search")
//	            .setIcon(R.drawable.abs__ic_search)
//	            .setActionView(searchView)
//	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//	        menu.findItem(1).expandActionView();
	        return true;
	    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
//		getSupportActionBar().setTitle("Search");
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setIcon(R.drawable.ic_action_previous_item);
		
		View commonFooter = findViewById(R.id.footer);
		Button dialerButton = (Button) commonFooter.findViewById(R.id.dialerLinkButton);
		Button emergencyButton = (Button) commonFooter.findViewById(R.id.emergencyLinkButton);
		Button mapButton = (Button) commonFooter.findViewById(R.id.mapLinkButton);	
		mapButton.setBackgroundResource(R.drawable.map_icon);
		emergencyButton.setBackgroundResource(R.drawable.emergency_icon);
		dialerButton.setBackgroundResource(R.drawable.dialer_icon);
		
		BuildingDatabase buildingDatabase = new BuildingDatabase(this);
		DepartmentDatabase departmentDatabase = new DepartmentDatabase(this);
		ServicesDatabase servicesDatabase = new ServicesDatabase(this);

		buildingDb = buildingDatabase.getReadableDatabase();
		departmentDb = departmentDatabase.getReadableDatabase();
		serviceDb = servicesDatabase.getReadableDatabase();
		/*
		searchBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
	        public boolean onEditorAction(TextView v, int actionId,
	                KeyEvent event) {
	            if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
	                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                in.hideSoftInputFromWindow(searchBox
	                        .getApplicationWindowToken(),
	                        InputMethodManager.HIDE_NOT_ALWAYS);
	               // Must return true here to consume event
	               return true;
	            }
	            return false;
	        }
	    });*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	};
	
	//return false = list of people
	//return true = one person
	public boolean personSearchResultType(String inputValue) throws InterruptedException, ExecutionException	{
		String input = inputValue;
		String url = "http://directory.uci.edu/index.php?basic_keywords=" + input + "&modifier=Exact+Match&basic_submit=Search&checkbox_employees=Employees&form_type=basic_search";
		String output = new RetreiveDirectoryResultTask().execute(url).get();
		   if (output.contains("Your search"))
			   return false;
		   else
			   return true;
	}
	
	private List<HashMap<String, String>> readSingleResultStream(String inputValue) throws InterruptedException, ExecutionException {
		List<HashMap<String, String>> personResults = new ArrayList<HashMap<String, String>>();
    	//creating an http connection to make an Http Request
    	String input = inputValue;
        String url = "http://directory.uci.edu/index.php?basic_keywords=" + input + "&modifier=Exact+Match&basic_submit=Search&checkbox_employees=Employees&form_type=basic_search";
        String output = new RetreiveDirectoryResultTask().execute(url).get();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("rowid","1");
        output = output.replaceAll("\\r\\n\\t|\\r|\\n|\\t", "");
        if(!(output.isEmpty()))
        {
	        String name = output.split("<span class=\"label\">Name</span><span class=\"resultData\">")[1].split("</span></p>")[0];
	        map.put("name", name);
	        String ucinetid = output.split("<span class=\"label\">UCInetID</span><span class=\"resultData\">")[1].split("</span></p>")[0];
	        map.put("ucinetid",ucinetid);
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
	
	private List<HashMap<String, String>> readMultipleResultStream(String inputValue) throws InterruptedException, ExecutionException {
		List<HashMap<String, String>> personResults = new ArrayList<HashMap<String, String>>();
    	//creating an http connection to make an Http Request
    	String input = inputValue;
        String url = "http://directory.uci.edu/index.php?basic_keywords=" + input + "&modifier=Exact+Match&basic_submit=Search&checkbox_employees=Employees&form_type=basic_search";
        String output = new RetreiveDirectoryResultTask().execute(url).get();
        
        String[] ucinetidSplit = output.split("uid=");
        String[] nameSplit = output.split("&return=basic_keywords%3D" + input + "%26modifier%3DExact%2BMatch%26basic_submit%3DSearch%26checkbox_employees%3DEmployees%26form_type%3Dbasic_search'>");
        String[] titleSplit = output.split("<span class=\"departmentmajor\">"); 
        int j = 1;
        for(int i = 1; i < nameSplit.length; i++){
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("personid", "" +i);
        	map.put("ucinetid", ucinetidSplit[i].split("&")[0]);
        	map.put("name", nameSplit[i].split("</a>")[0]);
        	if(titleSplit[j].split("</span>")[0].contains("<br />"))
        		map.put("title",titleSplit[j].split("</span>")[0].split("<br />")[0]);
        	else
        		map.put("title",titleSplit[j].split("</span>")[0]);
        	j += 2;
        	personResults.add(map);
		}
        return personResults;
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
		//setListAdapter(null);
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
			cursor = buildingDb.rawQuery("SELECT _id, buildingName, buildingNumber, buildingAddress, buildingAbbreviation FROM building WHERE buildingName || ' ' || buildingAbbreviation || ' ' || buildingNumber LIKE ?" , 
					new String[]{"%" + input + "%"});
			if(cursor.moveToFirst())
			{
				listAdapter = new SimpleCursorAdapter(
						this, 
						R.layout.activity_building_list_item, 
						cursor, 
						new String[] {"buildingName", "buildingNumber", "buildingAddress"}, 
						new int[] {R.id.buildingName,R.id.buildingNumber, R.id.buildingAddress});
				//setListAdapter(listAdapter);
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
				//setListAdapter(listAdapter);
			}
			else
				Toast.makeText(getApplicationContext(), "No Results Found. Please Try again.", Toast.LENGTH_LONG).show();
		} else if (searchChooser == -1){
			String searchInput = input;
			searchInput = searchInput.toLowerCase();
			searchInput = searchInput.trim();
	    	if(searchInput.contains(" ")){
	    		searchInput = searchInput.replaceAll(" ", "+");
			}
			if(searchInput.matches("^[a-zA-Z]+"))
			{
				boolean resultType;
				try {
					resultType = personSearchResultType(searchInput);
					
					
					if(resultType)
						personResults = readSingleResultStream(searchInput);		
					else
						personResults = readMultipleResultStream(searchInput);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
				
				String[] from = new String[] {"name", "title"};
			    int[] to = new int[] {R.id.personName, R.id.personTitle};
				simpleAdapter = new SimpleAdapter(this,personResults, R.layout.activity_person_list_item,from,to);
				if(personResults!=null && !personResults.isEmpty()) {
					//setListAdapter(simpleAdapter);
				}
				else {
					Toast.makeText(getApplicationContext(), "No Results Found. Please Try Again.", Toast.LENGTH_LONG).show();
				}
			}
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
				//setListAdapter(listAdapter);
			}
			else
				Toast.makeText(getApplicationContext(), "No Results Found. Please Try again.", Toast.LENGTH_LONG).show();
		}
	}
	
	
	
    public void onListItemClick(ListView parent, View view, int position, long id){
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
	    	Intent intent = new Intent(this, PersonInfoActivity.class);
	    	HashMap<String, String> personResults = null;
	    	HashMap<String, String> listResults = null;
	    	try {
	    		listResults = (HashMap<String, String>) simpleAdapter.getItem(position);
				personResults = readSingleResultStream(listResults.get("ucinetid")).get(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
	    	//bundle.putSerializable("person", personResults);
	    	intent.putExtra("person",personResults);
	    	startActivity(intent);
    	}
    	else if (searchChooser == 2){
    		Intent intent = new Intent(this, ServicesInfoActivity.class);
	    	Cursor cursor = (Cursor) listAdapter.getItem(position);
	    	bundle.putInt("SERVICE_ID", cursor.getInt(cursor.getColumnIndex("_id")));
	    	intent.putExtras(bundle);
	    	startActivity(intent);
    	}
    }
    
	private class RetreiveDirectoryResultTask extends AsyncTask<String, Integer,String> {

		@Override
		protected String doInBackground(String... urls) {
			String output = "";
			HttpGet httpGet = null;
			DefaultHttpClient httpClient = null;
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			int timeoutSocket = 5000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			try{
				httpClient = new DefaultHttpClient(httpParameters);
				URI url = new URI(urls[0]);
				httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
			    output = EntityUtils.toString(httpEntity);
			    
		   		output = output.replaceAll("\\r\\n\\t|\\r|\\n|\\t", "");
			
				} catch (Exception e) {
					Log.d("Exception download url", e.toString());
			 	}
			return output;
		}
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
	 

	@Override
	public boolean onQueryTextSubmit(String query) {
		searchView.clearFocus();
		search(query);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
}
