package edu.uci.ZotFinder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import edu.uci.ZotFinder.activity.EmergencyProcedureActivity;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.controller.ImageLabelAdapter;
import edu.uci.ZotFinder.model.EmergencyIconModel;
import edu.uci.ZotFinder.model.EmergencyIcons;
import edu.uci.ZotFinder.model.TopEmergencyIcons;

public class EmergencyInfoFragment extends Fragment {

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View emergencyInfo = inflater.inflate(R.layout.fragment_emergency_info, container, false);

       /*Sets up the Top Emergency Buttons in a gridview*/
       GridView topGridview = (GridView) emergencyInfo.findViewById(R.id.topEmergencyGridView);
       final TopEmergencyIcons topEmergencyIcons = new TopEmergencyIcons();
       topGridview.setAdapter(new ImageLabelAdapter(getActivity(), topEmergencyIcons, R.layout.top_emergency_item));
       topGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               EmergencyIconModel item = topEmergencyIcons.getData().get(position);
               Bundle bundle = new Bundle();
               bundle.putString("emergencyName", getString(item.getTitle()));
               bundle.putString("emergencyInfo", getString(item.getText()));
               //Setup the Intent that will start the next Activity
               Intent emergencyProcedureActivity = new Intent(getActivity(), EmergencyProcedureActivity.class);
               emergencyProcedureActivity.putExtras(bundle);
               startActivity(emergencyProcedureActivity);
           }
       });

       /*Sets up the all the emergency buttons in a gridview*/
       GridView gridview = (GridView) emergencyInfo.findViewById(R.id.emergencyGridView);
       final EmergencyIcons emergencyIcons = new EmergencyIcons();
       gridview.setAdapter(new ImageLabelAdapter(getActivity(), emergencyIcons, R.layout.emergency_item));
       gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               EmergencyIconModel item = emergencyIcons.getData().get(position);
               Bundle bundle = new Bundle();
               bundle.putString("emergencyName", getString(item.getTitle()));
               bundle.putString("emergencyInfo", getString(item.getText()));
               //Setup the Intent that will start the next Activity
               Intent emergencyProcedureActivity = new Intent(getActivity(), EmergencyProcedureActivity.class);
               emergencyProcedureActivity.putExtras(bundle);
               startActivity(emergencyProcedureActivity);
           }
       });
       return emergencyInfo;
   }
}
