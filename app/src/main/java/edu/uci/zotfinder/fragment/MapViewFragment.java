package edu.uci.ZotFinder.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.uci.ZotFinder.activity.AboutUsActivity;
import edu.uci.ZotFinder.data.DirectionsJSONParser;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.activity.SearchActivity;
import edu.uci.ZotFinder.activity.MainActivity;

public class MapViewFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    //map
    private GoogleMap mMap;

    //LatLng coordinate for default Map focus/centering
    static final LatLng UCI = new LatLng(33.6455843, -117.8419771);

    //coordinate for user's current location
    //this coordinate is dynamic
    public LatLng currentLocation;
    protected LatLng destinationPoint;
    Polyline polyline = null;
    boolean directionsToggle = false;
    private static ArrayList<Marker> emergencyAreas = new ArrayList<Marker>();
    private static ArrayList<Marker> bluePhones = new ArrayList<Marker>();
    private static ArrayList<Marker> restrooms = new ArrayList<Marker>();
    //---Booleans to show or hide markers---
    protected static boolean eaShow=true;
    protected static boolean bpShow=true;
    protected static boolean rrShow=true;
    private LatLng lastRequestedFindDirections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentMapView = inflater.inflate(R.layout.fragment_map_view, container, false);

        //Initialize Map
        //Google maps requires the map to be a fragment
        //SupportFragment allows older model android phones to display the map (as opposed to just a fragment)
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMarkerClickListener(MapViewFragment.this);
                //sets current location
                double[] d = getLocation();
                currentLocation = new LatLng(d[0], d[1]);

                //add uci marker and set zoom
                if (mMap != null) {

                    //set UI settings
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().setAllGesturesEnabled(true);
                    mMap.setMyLocationEnabled(true);
                    addMarkers();

                    Bundle extras = getActivity().getIntent().getExtras();

                    if (extras != null && extras.containsKey("type")) {
                        plotPointFromExtras();
                    } else {
                        //Animates the camera to the LatLng coordinate "UCI" which acts as the center
                        //initial zoom is set to 15 but this can be changed
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCI, 15));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    }
                }
                SharedPreferences settings = getActivity().getSharedPreferences("ZotFinder Preferences", 0);
                bpShow = settings.getBoolean("bp", true);
                eaShow = settings.getBoolean("ea", true);
                rrShow = settings.getBoolean("rr", true);
                if (!bpShow) {
                    bpShow = true;
                    toggleBluePhone();
                }
                if (!eaShow) {
                    eaShow = true;
                    toggleEmergencyMarker();
                }
                if (!rrShow) {
                    rrShow = true;
                    toggleRestroom();
                }
                ((MainActivity)getActivity()).hideSplashScreen();
            }
        });

        return fragmentMapView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        plotPointFromExtras();
    }

    private void plotPointFromExtras() {
        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            if (mMap == null) {
                FragmentManager fragmentManager = getChildFragmentManager();
                SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
                mMap = mapFragment.getMap();
            }
            int type = extras.getInt("type");
            if (type == 1) {
                float latitude = extras.getFloat("buildingLatitude");
                float longitude = extras.getFloat("buildingLongitude");
                int id = extras.getInt("BUILDING_ID");
                String name = extras.getString("buildingName");
                String address = extras.getString("buildingAddress");
                String number = extras.getString("buildingNumber");

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(name)
                        .snippet("Address: " + address))
                        .showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                destinationPoint = new LatLng(latitude, longitude);
            } else if (type == 2) {
                float latitude = extras.getFloat("departmentLatitude");
                float longitude = extras.getFloat("departmentLongitude");
                int id = extras.getInt("DEPARTMENT_ID");
                String name = extras.getString("departmentName");
                String address = extras.getString("departmentAddress");
                String phoneNumber = extras.getString("departmentPhoneNumber");
                String website = extras.getString("departmentWebsite");

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(name)
                        .snippet("Address: " + address))
                        .showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                destinationPoint = new LatLng(latitude, longitude);
            } else if (type == 3) {
                float latitude = extras.getFloat("officeLatitude");
                float longitude = extras.getFloat("officeLongitude");
                String name = extras.getString("personName");
                String officeLocation = extras.getString("officeAddress");

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(name)
                        .snippet("Office Location: " + officeLocation))
                        .showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                destinationPoint = new LatLng(latitude, longitude);
            } else if (type == 4) {
                float latitude = extras.getFloat("serviceLatitude");
                float longitude = extras.getFloat("serviceLongitude");
                int id = extras.getInt("SERVICE_ID");
                String name = extras.getString("serviceName");
                String address = extras.getString("serviceAddress");

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(name)
                        .snippet("Address: " + address))
                        .showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                destinationPoint = new LatLng(latitude, longitude);
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_directions).setVisible(true);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.action_map_drawer).setVisible(true);

        SharedPreferences settings = getActivity().getSharedPreferences("ZotFinder Preferences", 0);
        menu.findItem(R.id.blue_phone).setChecked(settings.getBoolean("bp", true));
        menu.findItem(R.id.emergency_areas).setChecked(settings.getBoolean("ea", true));
        menu.findItem(R.id.restrooms).setChecked(settings.getBoolean("rr", true));

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String itemTitle = item.getTitle().toString();

        if (itemTitle.equals("Blue Phone Posts")){
            toggleBluePhone();
            item.setChecked(bpShow);
            return false;
        }
        if (itemTitle.equals("Emergency Areas")){
            toggleEmergencyMarker();
            item.setChecked(eaShow);
            return false;
        }
        if (itemTitle.equals("Restrooms")){
            toggleRestroom();
            item.setChecked(rrShow);
            return false;
        }
        if (itemTitle.equals("About Us")){
            Intent intent = new Intent(getActivity(),AboutUsActivity.class);
            startActivity(intent);
            return false;
        }
        if(itemTitle.equals("Search")){
            Intent intent = new Intent(getActivity(),SearchActivity.class);
            startActivity(intent);
            return false;
        }
        if(itemTitle.equals("Directions")){
            if(hasLocationEnabled() && destinationPoint!=null){
                findDirections(destinationPoint);
            }
            return false;
        }
        return false;
    };


    //displays directions on the screen from current location to given point
    //currently the end point is hardcoded as BP2 (33.64387631680, -117.82420840800)
    //this will be changed later to be the end destination of whatever the user picks
    public void findDirections(final LatLng xy){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED != permissionCheck) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Location permission is needed to show directions")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION_PERMISSION);
                                lastRequestedFindDirections = xy;
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
                lastRequestedFindDirections = xy;
            }
            return;
        }
        if(!directionsToggle){
            // Getting URL to the Google Directions API given start point and end point
            String url = getDirectionsUrl(currentLocation, xy);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
            directionsToggle = true;
        }
        else {
            if (polyline != null) {
                polyline.remove();
            }
            directionsToggle = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (lastRequestedFindDirections != null) {
                        findDirections(lastRequestedFindDirections);
                    }
                } else {
                    Toast.makeText(getActivity(), "Need location permission to find directions", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //String output of directions in JSON given by Google Maps
    //LatLng origin - coordinates of starting point
    //LatLng dest - coordinates of ending point
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        // lat and long of starting point
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        // lat and long of destination
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        // sensor=true if currentLocation is provided by a sensor
        // sensor=false if currentLocation is not provided by a sensor
        // has no real baring on result; just for google's purposes
        String sensor = "sensor=true";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+"mode=walking";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    // A method to download json data from url
    private String downloadUrl(String strUrl) throws IOException {
        //initializing the output string, InputStream, and HttpURLConnection used to make an http request
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            //setting url as the url provided in the parameters of the method
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            // Create buffered Reader to read the output provided by InputStream
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            //StringBuffer used to parse the bufferedReader
            StringBuffer sb = new StringBuffer();

            //puts each line read from the bufferedReader into a new line in the string:"data"
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("ExceptionDownloadingUrl", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        destinationPoint = marker.getPosition();
        if (polyline != null && directionsToggle) {
            polyline.remove();
            directionsToggle = false;
        }
        return false;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    // A class to parse the Google Places in JSON format
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);

                // Changing the color polyline according to the mode
                lineOptions.color(Color.BLUE);
            }

            if(result.size()<1){
                Toast.makeText(getActivity(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Drawing polyline in the Google Map for the i-th route
            polyline = mMap.addPolyline(lineOptions);
        }
    }

    //Toggles the Emergency Area markers on or off (depending on the state of the boolean)
    public void toggleEaMarker(View v){
        toggleEmergencyMarker();
        //animates the camera back to initial center point (UCI)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCI, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    //Toggles the Blue Phone Post markers on or off (depending on the state of the boolean)
    public void toggleBpMarker(View v){
        toggleBluePhone();
        //animates the camera back to initial center point (UCI)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCI, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    //Toggles the Restroom markers on or off (depending on the state of the boolean)
    public void toggleRrMarker(View v){
        toggleRestroom();
        //animates the camera back to initial center point (UCI)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCI, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    //Finds the current location of the user
    //sets the global LatLng coordinate:"currentLocation"
    public void findLocation(View v){
        double[] d = getLocation();
        currentLocation = new LatLng(d[0], d[1]);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }

    //Uses system's location service to provide an array of 2 doubles that define the Lat and Long of current location
    public double[] getLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;
        for (int i = 0; i < providers.size(); i++) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null)
                break;
        }
        double[] gps = new double[2];

        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }

    public void addMarkers()
    {
        InputStream emergencyIS = getResources().openRawResource(R.raw.assembly_areas);
        InputStream bluePhoneIS = getResources().openRawResource(R.raw.blue_phones);
        InputStream restroomIS = getResources().openRawResource(R.raw.restrooms);
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(emergencyIS));
            while ((line = br.readLine()) != null)
            {
                String[] input = line.split(",");
                emergencyAreas.add(mMap.addMarker(new MarkerOptions().position(new LatLng(Float.valueOf(input[2]),Float.valueOf(input[3]))).title(input[0]).icon(BitmapDescriptorFactory.fromResource(R.drawable.emergency_area_icon))));
            }
            emergencyIS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br = new BufferedReader(new InputStreamReader(bluePhoneIS));
            while ((line = br.readLine()) != null)
            {
                String[] input = line.split(",");
                bluePhones.add(mMap.addMarker(new MarkerOptions().position(new LatLng(Float.valueOf(input[4]),Float.valueOf(input[3]))).title(input[2]).icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_phone_icon))));
            }
            bluePhoneIS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br = new BufferedReader(new InputStreamReader(restroomIS));
            while ((line = br.readLine()) != null)
            {
                String[] input = line.split(",");
                restrooms.add(mMap.addMarker(new MarkerOptions().position(new LatLng(Float.valueOf(input[2]),Float.valueOf(input[1]))).title("Restroom").icon(BitmapDescriptorFactory.fromResource(R.drawable.restroom_icon))));
            }
            restroomIS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleBluePhone(){
        SharedPreferences settings = getActivity().getSharedPreferences("ZotFinder Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        //if bpShow=true
        if(bpShow){
            //hide all Blue Phone Post markers
            for (Marker m : bluePhones){
                m.setVisible(false);
            }
            bpShow = false;
            editor.putBoolean("bp", bpShow);
        }
        //else if bpShow=false
        else{
            //show all Blue Phone Post Markers
            for (Marker m : bluePhones){
                m.setVisible(true);
            }
            bpShow = true;
            editor.putBoolean("bp", bpShow);
        }
        editor.commit();
    }

    public void toggleEmergencyMarker(){
        SharedPreferences settings = getActivity().getSharedPreferences("ZotFinder Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        //if eaShow=true
        if(eaShow){
            //hide all the emergency area markers
            for(Marker m :emergencyAreas){
                m.setVisible(false);
            }
            eaShow = false;
            editor.putBoolean("ea", eaShow);
        }
        //else if eaShow=false
        else{
            //show all the emergency area markers
            for( Marker m : emergencyAreas){
                m.setVisible(true);
            }
            eaShow = true;
            editor.putBoolean("ea", eaShow);
        }
        editor.commit();
    }

    public void toggleRestroom(){
        SharedPreferences settings = getActivity().getSharedPreferences("ZotFinder Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        //if rrShow=true
        if(rrShow){
            //hide all Restroom markers
            for (Marker m : restrooms){
                m.setVisible(false);
            }
            rrShow = false;
            editor.putBoolean("rr", rrShow);
        }
        //else if
        else{
            //show all Restroom markers
            for (Marker m : restrooms){
                m.setVisible(true);
            }
            rrShow = true;
            editor.putBoolean("rr", rrShow);
        }
        editor.commit();
    }

    public boolean hasLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // Return a boolean
        if (result) {
            return result;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Location Disabled");
            builder.setMessage("This action requires that you allow your location to be enabled.");
            builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return result;
        }
    }
}
