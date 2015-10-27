package edu.uci.zotfinder;


import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AboutUsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

//		getSupportActionBar().setTitle("About Us");
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setIcon(R.drawable.ic_action_previous_item);

		View commonFooter = findViewById(R.id.footer);
		Button dialerButton = (Button) commonFooter.findViewById(R.id.dialerLinkButton);
		Button emergencyButton = (Button) commonFooter.findViewById(R.id.emergencyLinkButton);
		Button mapButton = (Button) commonFooter.findViewById(R.id.mapLinkButton);	
		mapButton.setBackgroundResource(R.drawable.map_icon);
		emergencyButton.setBackgroundResource(R.drawable.emergency_icon);
		dialerButton.setBackgroundResource(R.drawable.dialer_icon);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	};
	
	public void emailGeneralInquiry(View view)
	{
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("play/Text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"zotfinderl@gmail.com"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zotfinder Android: Comment/General Question]");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
	
	public void emailBugReport(View view)
	{
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("play/Text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"zotfinderl@gmail.com"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zotfinder Android: Bug Issue]");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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

}
