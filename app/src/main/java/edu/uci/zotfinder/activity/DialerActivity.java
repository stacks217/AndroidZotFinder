package edu.uci.ZotFinder.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.controller.ImageLabelAdapter;
import edu.uci.ZotFinder.model.DialerIcons;

public class DialerActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        GridView gridview = (GridView) findViewById(R.id.dialerGridView);
        final DialerIcons dialerIcons = new DialerIcons();
        gridview.setAdapter(new ImageLabelAdapter(this, dialerIcons, R.layout.dialer_item));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + dialerIcons.getData().get(position).getPhoneNumber()));
                startActivity(intent);
            }
        });
        Toolbar myToolbar = (Toolbar) findViewById(R.id.empty_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setContentInsetsAbsolute(0,0);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView customView = (TextView)
                LayoutInflater.from(this).inflate(R.layout.centered_toolbar_text,
                        null);
        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        customView.setText(R.string.dialer_title);
        this.getSupportActionBar().setCustomView(customView, params);
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