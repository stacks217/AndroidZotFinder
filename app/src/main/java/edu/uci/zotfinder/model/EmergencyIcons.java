package edu.uci.ZotFinder.model;

import java.util.ArrayList;
import java.util.List;
import edu.uci.ZotFinder.R;

public class EmergencyIcons implements ImageLabelData {

    private List<EmergencyIconModel> data;

    @Override
    public List<EmergencyIconModel> getData() {
        return data;
    }

    public EmergencyIcons() {
        data = new ArrayList<EmergencyIconModel>();
        data.add(new EmergencyIconModel(R.drawable.active_shooter, R.string.active_shooter, R.string.active_shooter_title, R.string.active_shooter_text));
        data.add(new EmergencyIconModel(R.drawable.bomb_threat, R.string.bomb_threat, R.string.bomb_threat_title, R.string.bomb_threat_text));
        data.add(new EmergencyIconModel(R.drawable.earthquake, R.string.earthquake, R.string.earthquake_title, R.string.earthquake_text));
        data.add(new EmergencyIconModel(R.drawable.emergency_preparedness, R.string.emergency_preparedness, R.string.emergency_preparedness_title, R.string.emergency_preparedness_text));
        data.add(new EmergencyIconModel(R.drawable.evacuation, R.string.evacuation, R.string.evacuation_title, R.string.evacuation_text));
        data.add(new EmergencyIconModel(R.drawable.evacuation_disabilities, R.string.evacuation_disabilities, R.string.evacuation_disabilities_title, R.string.evacuation_disabilities_text));
        data.add(new EmergencyIconModel(R.drawable.fire, R.string.fire, R.string.fire_title, R.string.fire_text));
        data.add(new EmergencyIconModel(R.drawable.hazardous_material, R.string.hazardous_material, R.string.hazardous_material_title, R.string.hazardous_material_text));
        data.add(new EmergencyIconModel(R.drawable.hazardous_material_shelter, R.string.hazardous_material_shelter, R.string.hazardous_material_shelter_title,
                R.string.hazardous_material_shelter_text));
        data.add(new EmergencyIconModel(R.drawable.medical_emergency, R.string.medical_emergency, R.string.medical_emergency_title, R.string.medical_emergency_text));
        data.add(new EmergencyIconModel(R.drawable.persons_in_distress, R.string.persons_in_distress, R.string.persons_in_distress_title, R.string.persons_in_distress_text));
        data.add(new EmergencyIconModel(R.drawable.secure_in_place, R.string.secure_in_place, R.string.secure_in_place_title, R.string.secure_in_place_text));
        data.add(new EmergencyIconModel(R.drawable.suspicious_package, R.string.suspicious_package, R.string.suspicious_package_title, R.string.suspicious_package_text));
        data.add(new EmergencyIconModel(R.drawable.violence, R.string.violence, R.string.violence_title, R.string.violence_text));
        data.add(new EmergencyIconModel(R.drawable.utility_failure, R.string.utility_failure, R.string.utility_failure_title, R.string.utility_failure_text));
    }
}
