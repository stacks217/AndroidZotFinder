package edu.uci.ZotFinder.model;


import java.util.ArrayList;
import java.util.List;
import edu.uci.ZotFinder.R;

public class TopEmergencyIcons implements ImageLabelData{

    private List<EmergencyIconModel> data;

    @Override
    public List<EmergencyIconModel> getData() {
        return data;
    }

    public TopEmergencyIcons() {
        data = new ArrayList<EmergencyIconModel>();
        data.add(new EmergencyIconModel(R.drawable.active_shooter_top, R.string.active_shooter, R.string.active_shooter_title, R.string.active_shooter_text));
        data.add(new EmergencyIconModel(R.drawable.earthquake_top, R.string.earthquake, R.string.earthquake_title, R.string.earthquake_text));
        data.add(new EmergencyIconModel(R.drawable.emergency_preparedness_top, R.string.emergency_preparedness, R.string.emergency_preparedness_title, R.string.emergency_preparedness_text));
        data.add(new EmergencyIconModel(R.drawable.fire_top, R.string.fire, R.string.fire_title, R.string.fire_text));
        data.add(new EmergencyIconModel(R.drawable.persons_in_distress_top, R.string.persons_in_distress, R.string.persons_in_distress_title, R.string.persons_in_distress_text));
    }

}
