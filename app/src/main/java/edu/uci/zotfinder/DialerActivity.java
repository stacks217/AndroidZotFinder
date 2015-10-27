package edu.uci.zotfinder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DialerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialer);
		
//		getSupportActionBar().setTitle("Emergency Dialer");
		
		View commonFooter = findViewById(R.id.footer);
		Button dialerButton = (Button) commonFooter.findViewById(R.id.dialerLinkButton);
		Button emergencyButton = (Button) commonFooter.findViewById(R.id.emergencyLinkButton);
		Button mapButton = (Button) commonFooter.findViewById(R.id.mapLinkButton);	
		mapButton.setBackgroundResource(R.drawable.map_icon);
		emergencyButton.setBackgroundResource(R.drawable.emergency_icon);
		dialerButton.setBackgroundResource(R.drawable.dialer_icon_pressed);
	}
	
	//Dialer Methods

	//method to switch intent to phone's internal dialing activity
	//creates intent used to store the information of a different activity within this activity
	//Uri is a data structure used to store data passed from one activity to another
	//tel:xxxxxxxxx is predefined data format used for telephone number
	//startActivity(intent) changes the current
	public void call911(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:911"));
		startActivity(intent);	
	}
	
	public void callCSO(View view){
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:9498247233"));
		startActivity(intent);	
	}
	 
	public void callUciPoliceDept(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:9498245223"));
		startActivity(intent); 	
	}
	 
	public void callEmergencyMgmt(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:9498247147"));
		startActivity(intent);  
	}
	
	public void callEnvironmentalHealth(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:9498246200"));
		startActivity(intent);  
	}
	 
	public void callCounselingCenter(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:9498246457"));
		startActivity(intent);
	}
	 
	public void callStudentHealth(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:9498245301"));
		startActivity(intent);  
	}
	 
	public void callFacilitiesMgmtService(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:9498245444"));
		startActivity(intent); 
	}
	
	public void callOit(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:9498242222"));
		startActivity(intent);  	
	}
	 
	public void callEmergencyInfoLine(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:8664786397"));
		startActivity(intent); 
	}

	//method to go to activity: MainActivity
	//creates intent used to store the information of a different activity within this activity
	//startActivity(intent) changes the current activity to the intent activity
	public void goToMap(View view) { 
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	 
	//method to go to activity: EmergencyInfoActivity
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
	}
}
