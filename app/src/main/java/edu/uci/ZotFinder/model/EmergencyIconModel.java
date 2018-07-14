package edu.uci.ZotFinder.model;


public class EmergencyIconModel extends ImageLabelModel{
    private int title;
    private int text;

    public EmergencyIconModel(int image, int label, int title, int text) {
        super(image, label);
        this.title = title;
        this.text = text;
    }

    public int getTitle() {
        return title;
    }

    public int getText() {
        return text;
    }
}
