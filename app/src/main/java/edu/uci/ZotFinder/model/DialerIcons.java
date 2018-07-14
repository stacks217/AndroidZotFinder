package edu.uci.ZotFinder.model;

import java.util.ArrayList;
import java.util.List;
import edu.uci.ZotFinder.R;

public class DialerIcons implements ImageLabelData{

    private List<DialerIconModel> data;

    @Override
    public List<DialerIconModel> getData() {
        return data;
    }

    public DialerIcons() {
        data = new ArrayList<DialerIconModel>();
        data.add(new DialerIconModel(R.drawable.communityserviceofficer, R.string.community_service_officer, "9498247233"));
        data.add(new DialerIconModel(R.drawable.counselingcenter, R.string.counseling_center, "9498246457"));
        data.add(new DialerIconModel(R.drawable.emergencymanagement, R.string.emergency_management, "9498247147"));
        data.add(new DialerIconModel(R.drawable.environmentalhealthandsafety, R.string.environmental_health, "9498246200"));
        data.add(new DialerIconModel(R.drawable.facilitiesmanagement, R.string.facilities_management, "9498245444"));
        data.add(new DialerIconModel(R.drawable.oit, R.string.oit, "9498242222"));
        data.add(new DialerIconModel(R.drawable.pddispatch, R.string.police_department_dispatch, "9498245223"));
        data.add(new DialerIconModel(R.drawable.studenthealth, R.string.student_health, "9498245301"));
        data.add(new DialerIconModel(R.drawable.uciemergencyinfoline, R.string.uci_emergency_info_line, "8664786397"));
    }
}
