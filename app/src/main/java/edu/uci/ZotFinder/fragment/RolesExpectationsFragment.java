package edu.uci.ZotFinder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.uci.ZotFinder.R;

public class RolesExpectationsFragment extends Fragment {
	
	   @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
               Bundle savedInstanceState) {
           View rolesExpectations= inflater.inflate(R.layout.activity_roles_expectations, container, false);
		return rolesExpectations;
	   }
}