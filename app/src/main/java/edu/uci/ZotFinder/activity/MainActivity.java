package edu.uci.ZotFinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.fragment.FooterFragment;
import edu.uci.ZotFinder.fragment.MapViewFragment;
import edu.uci.ZotFinder.service.DatabaseBuilderService;

public class MainActivity extends AppCompatActivity{

    private static final String MAP_VIEW_FRAGMENT_TAG = "MAP_VIEW_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Handler handler = new Handler();
        if (getIntent().getBooleanExtra("SPLASH", true)){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    findViewById(R.id.imgLogo).setVisibility(View.GONE);
//                    findViewById(R.id.my_toolbar).setVisibility(View.VISIBLE);
//                    getSupportActionBar().setDisplayUseLogoEnabled(true);
//                    getSupportActionBar().setDisplayShowHomeEnabled(true);
//                    getSupportActionBar().setLogo(R.drawable.logo);
//                    getSupportActionBar().setTitle(null);
//                    findViewById(R.id.fragmentPlaceholder).setVisibility(View.VISIBLE);
//                    findViewById(R.id.footerPlaceholder).setVisibility(View.VISIBLE);
                }
            }, 3000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentPlaceholder, new MapViewFragment(), MAP_VIEW_FRAGMENT_TAG);
                    fragmentTransaction.add(R.id.footerPlaceholder, new FooterFragment());
                    fragmentTransaction.commit();
                }
            }, 100);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentPlaceholder, new MapViewFragment(), MAP_VIEW_FRAGMENT_TAG);
                    fragmentTransaction.add(R.id.footerPlaceholder, new FooterFragment());
                    fragmentTransaction.commit();
                    fragmentManager.executePendingTransactions();
                    findViewById(R.id.imgLogo).setVisibility(View.GONE);
                    findViewById(R.id.my_toolbar).setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayUseLogoEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setLogo(R.drawable.logo);
                    getSupportActionBar().setTitle(null);
                    findViewById(R.id.fragmentPlaceholder).setVisibility(View.VISIBLE);
                    findViewById(R.id.footerPlaceholder).setVisibility(View.VISIBLE);
                }
            });
        }

        startService(new Intent(this, DatabaseBuilderService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void hideSplashScreen() {
        if (findViewById(R.id.imgLogo).getVisibility()== View.VISIBLE) {
            findViewById(R.id.imgLogo).setVisibility(View.GONE);
            findViewById(R.id.my_toolbar).setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setTitle(null);
            findViewById(R.id.fragmentPlaceholder).setVisibility(View.VISIBLE);
            findViewById(R.id.footerPlaceholder).setVisibility(View.VISIBLE);
        }
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}

