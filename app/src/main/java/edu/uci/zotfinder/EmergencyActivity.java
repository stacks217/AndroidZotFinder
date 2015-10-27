package edu.uci.zotfinder;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

public class EmergencyActivity extends FragmentActivity {
	ViewPager  mViewPager;
    TabPageAdapter TabAdapter;
    ActionBar actionBar;
    
    TabHost mTabHost;
    TabsAdapter mTabsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        
//        getSupportActionBar().hide();
        
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        
        mViewPager = (ViewPager)findViewById(R.id.pager);
        
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        mTabsAdapter.addTab(mTabHost.newTabSpec("information").setIndicator("Emergency Information"), 
        		EmergencyInfoFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("roles").setIndicator("Roles & Expectations"),
                RolesExpectationsFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }
    
    public static class TabsAdapter extends FragmentPagerAdapter
    implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
		
		static final class TabInfo {
		    private final String tag;
		    private final Class<?> clss;
		    private final Bundle args;
		
		    TabInfo(String _tag, Class<?> _class, Bundle _args) {
		        tag = _tag;
		        clss = _class;
		        args = _args;
		    }
		}
		
		static class DummyTabFactory implements TabHost.TabContentFactory {
		    private final Context mContext;
		
		    public DummyTabFactory(Context context) {
		        mContext = context;
		    }
		
		    @Override
		    public View createTabContent(String tag) {
		        View v = new View(mContext);
		        v.setMinimumWidth(0);
		        v.setMinimumHeight(0);
		        return v;
		    }
		}
		
		public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
		    super(activity.getSupportFragmentManager());
		    mContext = activity;
		    mTabHost = tabHost;
		    mViewPager = pager;
		    mTabHost.setOnTabChangedListener(this);
		    mViewPager.setAdapter(this);
		    mViewPager.setOnPageChangeListener(this);
		}
		
		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
		    tabSpec.setContent(new DummyTabFactory(mContext));
		    String tag = tabSpec.getTag();
		
		    TabInfo info = new TabInfo(tag, clss, args);
		    mTabs.add(info);
		    mTabHost.addTab(tabSpec);
		    notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
		    return mTabs.size();
		}
		
		@Override
		public Fragment getItem(int position) {
		    TabInfo info = mTabs.get(position);
		    return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}
		
		@Override
		public void onTabChanged(String tabId) {
		    int position = mTabHost.getCurrentTab();
		    mViewPager.setCurrentItem(position);
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}
		
		@Override
		public void onPageSelected(int position) {
		    // Unfortunately when TabHost changes the current tab, it kindly
		    // also takes care of putting focus on it when not in touch mode.
		    // The jerk.
		    // This hack tries to prevent this from pulling focus out of our
		    // ViewPager.
		    TabWidget widget = mTabHost.getTabWidget();
		    int oldFocusability = widget.getDescendantFocusability();
		    widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		    mTabHost.setCurrentTab(position);
		    widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
    }
    
    public void viewInfo(View v)
	{
		int id = v.getId();
		
		Bundle bundle = new Bundle();

		if(id == R.id.topActiveShooterButton || id == R.id.activeShooterButton) {
			bundle.putString("emergencyName", getString(R.string.active_shooter_title));
			bundle.putString("emergencyInfo", getString(R.string.active_shooter_text));
		} else if (id == R.id.topEarthquakeButton || id == R.id.earthquakeButton){
			bundle.putString("emergencyName", getString(R.string.earthquake_title));
			bundle.putString("emergencyInfo", getString(R.string.earthquake_text));
		} else if (id == R.id.topEmergencyPreparednessButton || id == R.id.emergencyPreparednessButton){
			bundle.putString("emergencyName", getString(R.string.emergency_preparedness_title));
			bundle.putString("emergencyInfo", getString(R.string.emergency_preparedness_text));
		} else if (id == R.id.topFireButton || id == R.id.fireButton){
			bundle.putString("emergencyName", getString(R.string.fire_title));
			bundle.putString("emergencyInfo", getString(R.string.fire_text));
		} else if (id == R.id.topPersonsInDistressButton || id == R.id.personsInDistressButton){
			bundle.putString("emergencyName", getString(R.string.persons_in_distress_title));
			bundle.putString("emergencyInfo", getString(R.string.persons_in_distress_text));
		} else if (id == R.id.bombThreatButton){
			bundle.putString("emergencyName", getString(R.string.bomb_threat_title));
			bundle.putString("emergencyInfo", getString(R.string.bomb_threat_text));
		} else if (id == R.id.evacuationButton){
			bundle.putString("emergencyName", getString(R.string.evacuation_title));
			bundle.putString("emergencyInfo", getString(R.string.evacuation_text));
		} else if (id == R.id.evacuationDisabilitiesButton){
			bundle.putString("emergencyName", getString(R.string.evacuation_disabilities_title));
			bundle.putString("emergencyInfo", getString(R.string.evacuation_disabilities_text));
		} else if (id == R.id.hazardousMaterialButton){
			bundle.putString("emergencyName", getString(R.string.hazardous_material_title));
			bundle.putString("emergencyInfo", getString(R.string.hazardous_material_text));
		} else if (id == R.id.hazardousMaterialShelterButton){
			bundle.putString("emergencyName", getString(R.string.hazardous_material_shelter_title));
			bundle.putString("emergencyInfo", getString(R.string.hazardous_material_shelter_text));
		} else if (id == R.id.medicalEmergencyButton){
			bundle.putString("emergencyName", getString(R.string.medical_emergency_title));
			bundle.putString("emergencyInfo", getString(R.string.medical_emergency_text));
		} else if (id == R.id.suspiciousPackageButton){
			bundle.putString("emergencyName", getString(R.string.suspicious_package_title));
			bundle.putString("emergencyInfo", getString(R.string.suspicious_package_text));
		} else if (id == R.id.violenceButton){
			bundle.putString("emergencyName", getString(R.string.violence_title));
			bundle.putString("emergencyInfo", getString(R.string.violence_text));
		} else if (id == R.id.utilityFailureButton){
			bundle.putString("emergencyName", getString(R.string.utility_failure_title));
			bundle.putString("emergencyInfo", getString(R.string.utility_failure_text));
		}

		 //Setup the Intent that will start the next Activity
	    Intent emergencyProcedureActivity = new Intent(this, EmergencyProcedureActivity.class); 
	    
	    //Assumes this references this instance of Activity A
	    //puts the bundle into the intent:"personInfoActivity"
	    emergencyProcedureActivity.putExtras(bundle);
	    startActivity(emergencyProcedureActivity);	
	}
    
    public void goToStudentExpectations(View view) {
		Intent intent = new Intent(this,StudentExpectationsActivity.class);
		startActivity(intent);
	}
	
	public void goToFacultyExpectations(View view) {
		Intent intent = new Intent(this,FacultyExpectationsActivity.class);
		startActivity(intent);
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
		}

		//method to go to activity: DialerActivity
		//creates intent used to store the information of a different activity within this activity
		//startActivity(intent) changes the current
		public void goToEmergencyDialer(View view) { 
			Intent intent = new Intent(this,DialerActivity.class);
			startActivity(intent);
		}
		
		

}
