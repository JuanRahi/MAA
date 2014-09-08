package id3;

public class Attribute{

    private int index; 
    private double gain;

    public Attribute(int index, double gain) {
        this.index = index;
        this.gain = gain;
    }

    public int getIndex() {
        return index;
    }

    public double getGain() {
        return gain;
    }
        
}
