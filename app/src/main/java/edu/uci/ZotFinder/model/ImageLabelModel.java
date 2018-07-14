package edu.uci.ZotFinder.model;

public class ImageLabelModel {

    private int image;
    private int label;


    public ImageLabelModel(int image, int label) {
        this.image = image;
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    public int getImage() {
        return image;
    }
}
