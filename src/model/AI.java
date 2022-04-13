package model;

public interface AI {
    void stop();
    void chooseDirection();
    public interface AIIntelligent extends AI {
    }
}
