package edu.uci.ZotFinder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import edu.uci.ZotFinder.R;
import edu.uci.ZotFinder.activity.DialerActivity;
import edu.uci.ZotFinder.activity.EmergencyActivity;
import edu.uci.ZotFinder.activity.MainActivity;

public class FooterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View footer = inflater.inflate(R.layout.fragment_footer, container, false);
        footer.findViewById(R.id.mapFooter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(getActivity() instanceof MainActivity)) {
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.putExtra("SPLASH", false);
                    getActivity().startActivity(i);
                }
            }
        });
        footer.findViewById(R.id.emergencyFooter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(getActivity() instanceof EmergencyActivity)) {
                    Intent i = new Intent(getActivity(), EmergencyActivity.class);
                    getActivity().startActivity(i);
                }
            }
        });
        footer.findViewById(R.id.dialerFooter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(getActivity() instanceof DialerActivity)) {
                    Intent i = new Intent(getActivity(), DialerActivity.class);
                    getActivity().startActivity(i);
                }
            }
        });

        if (getActivity() instanceof MainActivity) {
            ((ImageView)footer.findViewById(R.id.mapFooter)).setImageResource(R.drawable.map_icon_pressed);
        } else if (getActivity() instanceof EmergencyActivity) {
            ((ImageView)footer.findViewById(R.id.emergencyFooter)).setImageResource(R.drawable.emergency_icon_pressed);
        } else {
            ((ImageView)footer.findViewById(R.id.dialerFooter)).setImageResource(R.drawable.dialer_icon_pressed);
        }
        return footer;
    }
}
