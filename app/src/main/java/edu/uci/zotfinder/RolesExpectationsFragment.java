package edu.uci.zotfinder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RolesExpectationsFragment extends Fragment {
	
	   @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
               Bundle savedInstanceState) {
           View rolesExpectations= inflater.inflate(R.layout.roles_expectations_frag, container, false);
		return rolesExpectations;
	   }
	   
	  
}