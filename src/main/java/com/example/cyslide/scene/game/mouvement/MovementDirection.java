package com.example.cyslide.scene.game.mouvement;

public enum MovementDirection {

    UP(0,1),RIGHT(-1,0),DOWN(0,-1),LEFT(1,0),NONE(0,0);

    int x;
    int y;

    /**
     * Constructs a MovementDirection with the specified x and y values.
     * @param x The x-coordinate value of the movement direction
     * @param y The y-coordinate value of the movement direction
     */
    MovementDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the y-coordinate value of the movement direction.
     * @return The y-coordinate value
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the x-coordinate value of the movement direction.
     * @return The x-coordinate value
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the opposite movement direction.
     * @return The opposite movement direction
     */
    public MovementDirection opposite(){

        switch (this){
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return NONE;
        }
    }


}


