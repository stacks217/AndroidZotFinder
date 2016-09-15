package edu.uci.ZotFinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.fragment.EmergencyInfoFragment;
import edu.uci.ZotFinder.fragment.RolesExpectationsFragment;

public class EmergencyActivity extends AppCompatActivity {
    FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setContentInsetsAbsolute(0,0);
        this.getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView customView = (TextView)
                LayoutInflater.from(this).inflate(R.layout.centered_toolbar_text,
                        null);
        android.support.v7.app.ActionBar.LayoutParams params = new
                android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        customView.setText(R.string.emergency_procedures);
        this.getSupportActionBar().setCustomView(customView, params);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("information").setIndicator(getResources().getString(R.string.emergency_info)),
        		EmergencyInfoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("roles").setIndicator(getResources().getString(R.string.roles_and_expectations)),
                RolesExpectationsFragment.class, null);
    }

    public void goToFacultyExpectations(View v) {
        Intent i = new Intent(this, ExpectationsActivity.class);
        i.putExtra("Title", R.string.faculty_expectations);
        i.putExtra("Body", R.string.faculty_expectations_text);
        startActivity(i);
    }

    public void goToStudentExpectations(View v) {
        Intent i = new Intent(this, ExpectationsActivity.class);
        i.putExtra("Title", R.string.student_expectations);
        i.putExtra("Body", R.string.student_expectations_text);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        findViewById(R.id.shade).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        findViewById(R.id.shade).setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.shade).setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }
}
