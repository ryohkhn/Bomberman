package model;

public interface AI {
    void stop(); // stop for chosing new direction
    void chooseDirection();
    public interface AIIntelligent extends AI {
    }
}
