package model;

import java.util.Random;

public class Bonus{

  public enum Type {
        // Additional bombs
        Bomb("resources/bonus_bomb.png") {
            @Override
            protected void grantBonus(Player player) {
            	player.addAmmo();
            }
        },

        // Increases firepower
        Fireup("resources/bonus_fireup.png") {
            @Override
            protected void grantBonus(Player player) {
            	player.addFirepower(false);
            }
        },

        // Increases firepower to max
        Firemax("resources/bonus_firemax.png") {
            @Override
            protected void grantBonus(Player player) {
            	player.addFirepower(true);
            }
        },

        // Increases speed
        Speed("resources/bonus_speed.png") {
            @Override
            protected void grantBonus(Player player) {
            	player.addSpeed();
            }
        },

        // Adds ability for explosions to pierce soft walls
        Pierce("resources/bonus_pierce.png") {
            @Override
            protected void grantBonus(Player player) {
            	player.setPierce(true);
            }
        },

        // Adds ability to kick bombs
        Kick("resources/bonus_kick.png") {
            @Override
            protected void grantBonus(Player player) {
            	player.setKick(true);
            }
        },

        // Reduces time for bomb to detonate
        Timer() {
            @Override
            protected void grantBonus(Player player) {
                player.reduceTimer(15);
            }
        };

        private String sprite;

        /**
         * Sets the sprite of the bonus type.
         * @param sprite bonus sprite
         */
        Type(String sprite) {
            this.sprite = sprite;
        }

        Type() {
            this.sprite = "";
        }
        /**
         * To be overridden by bonus types. Grants bonuses to player.
         * @param player Player object to be granted bonus
         */
        protected abstract void grantBonus(Player player);

    }

    private Type type;

    /**
     * Construct a bonus of type. Type can be random.
     * @param position Coordinates of this object in the game world
     * @param type Type of bonus
     */
    public Bonus(Type type) {
        this.type = type;
    }

    // Random bonuss
    private static Bonus.Type[] bonuses = Bonus.Type.values();
    private static Random random = new Random();
    static final Bonus.Type randomBonus() {
        return bonuses[random.nextInt(bonuses.length)];
    }

    /**
     * Grants bonuses to player.
     * @param player Player object to be granted bonus
     */
    void grantBonus(Player player) {
        this.type.grantBonus(player);
    }

	public String getSprite() {
		return this.type.sprite;
	}
}
