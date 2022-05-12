package model;

import java.util.Random;

public class Bonus{

    public enum Type {
        // Additional bombs
        Bomb() {
            @Override
            protected void grantBonus(Player player) {
                player.addAmmo();
            }
        },

        // Increases firepower
        Fireup(){
            @Override
            protected void grantBonus(Player player) {
                player.addFirepower(false);
            }
        },

        // Increases firepower to max
        Firemax(){
            @Override
            protected void grantBonus(Player player) {
                player.addFirepower(true);
            }
        },

        // Increases speed
        Speed(){
            @Override
            protected void grantBonus(Player player) {
                player.addSpeed();
            }
        },

        // Adds ability for explosions to pierce soft walls
        Pierce(){
            @Override
            protected void grantBonus(Player player) {
                player.setPierce(true);
            }
        },

        // Adds ability to kick bombs
        Kick(){
            @Override
            protected void grantBonus(Player player) {
                player.setKick(true);
            }
        };
        /*

        // Reduces time for bomb to detonate
        
        Timer(){
            @Override
            protected void grantBonus(Player player) {
                player.reduceTimer(15);
            }
        };
        */
        protected abstract void grantBonus(Player player);

    }


    private Type type;

    /**
     * Construct a bonus of type. Type can be random.
     * @param type Type of bonus
     */
    public Bonus(Type type) {
        this.type = type;
    }

    /**
     *  Random bonus
     */
    private static Type[] bonuses = Type.values();
    private static Random random = new Random();
    static final Type randomBonus() {
        return bonuses[random.nextInt(bonuses.length)];
    }

    /**
     * Grants bonuses to player.
     * @param player Player object to be granted bonus
     */
    void grantBonus(Player player) {
        this.type.grantBonus(player);
    }

    public Type getType(){
        return type;
    }
}
