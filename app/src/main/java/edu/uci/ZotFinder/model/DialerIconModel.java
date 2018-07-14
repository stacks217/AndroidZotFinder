package edu.uci.ZotFinder.model;

public class DialerIconModel extends ImageLabelModel{

    private String phoneNumber;

    public DialerIconModel(int image, int label, String phoneNumber) {
        super(image, label);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
