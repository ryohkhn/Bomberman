package model;

public interface AI {
    void stop();
    void chooseDirection(double d);
    public interface AIIntelligent extends AI {
        // peut être redéfinir les méthodes pour l'ai intelligent
    }
}
