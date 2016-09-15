package edu.uci.ZotFinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import edu.uci.ZotFinder.R;

public class EmergencyProcedureActivity extends AppCompatActivity {
    
	String emergencyTitle;
	String emergencyInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_procedure);
        emergencyTitle = getIntent().getExtras().getString("emergencyName");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(emergencyTitle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_action_previous_item);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emergencyInfo = getIntent().getExtras().getString("emergencyInfo");
        WebView emergencyTextView = (WebView) findViewById(R.id.emergencyProcedureTextView);
        emergencyTextView.loadData(emergencyInfo, "text/html", "UTF-8");

	}
}
