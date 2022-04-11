package model;

public interface AI {
    void stop();
    void chooseDirection(double d);
    public interface AIIntelligent extends AI {

    }
}
