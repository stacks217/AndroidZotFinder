package edu.uci.ZotFinder.activity;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.uci.ZotFinder.R;

public class AboutUsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("About Us");
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
	
	public void emailGeneralInquiry(View view)
	{
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("play/Text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"zotfinder@gmail.com"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zotfinder Android: Comment/General Question]");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
	
	public void emailBugReport(View view)
	{
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("play/Text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"zotfinder@gmail.com"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Zotfinder Android: Bug Issue]");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
}
