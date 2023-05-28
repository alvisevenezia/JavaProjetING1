package com.example.cyslide.map;

import com.example.cyslide.scene.game.mouvement.MovementDirection;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class TilePane extends Pane {

    private int x, y,id;
    private boolean empty,isWall;
    private Map parent;
    private Image image;

    /**

     Constructs a TilePane object.
     @param x The x-coordinate of the tile.
     @param y The y-coordinate of the tile.
     @param isEmpty Determines if the tile is empty.
     @param isWall Determines if the tile is a wall.
     @param parent The parent map of the tile.
     */
    public TilePane(int x, int y, boolean isEmpty, boolean isWall,Map parent) {
        this.x = x;
        this.y = y;
        this.empty = isEmpty;
        this.isWall = isWall;
        this.parent = parent;

    }

    /**
     Determines the possible movement direction of the tile.
     @return The movement direction of the tile, or {@link MovementDirection#NONE} if it cannot move.
     */
    public MovementDirection canMove(){

        if(isWall())return MovementDirection.NONE;

        for(MovementDirection movementDirection : MovementDirection.values()){

            if(movementDirection == MovementDirection.NONE){
                continue;
            }

            if(parent.getTileByCoord(x+ movementDirection.getX(),y+ movementDirection.getY()) != null
               && parent.getTileByCoord(x+ movementDirection.getX(),y+ movementDirection.getY()).isEmpty()
               && !parent.getTileByCoord(x+ movementDirection.getX(),y+ movementDirection.getY()).isWall()){
                return movementDirection;
            }

        }

        return MovementDirection.NONE;

    }

    /**
     Gets the x-coordinate of the tile.
     @return The x-coordinate of the tile.
     */
    public int getX() {
        return x;
    }
    /**
     Sets the x-coordinate of the tile.
     @param x The x-coordinate to set.
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     Gets the y-coordinate of the tile.
     @return The y-coordinate of the tile.
     */
    public int getY() {
        return y;
    }
    /**
     Sets the y-coordinate of the tile.
     @param y The y-coordinate to set.
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     Checks if the tile is empty.
     @return {@code true} if the tile is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return empty;
    }
    /**
     Checks if the tile is a wall.
     @return {@code true} if the tile is a wall, {@code false} otherwise.
     */
    public boolean isWall() {
        return isWall;
    }
    /**
     Sets the empty status of the tile.
     @param empty The empty status to set.
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
    /**
     Gets the parent map of the tile.
     @return The parent map of the tile.
     */
    public Map getParentMap() {
        return parent;
    }
    /**
     Sets the image of the tile.
     @param image The image to set.
     */
    public void setImage(Image image) {
        this.image = image;
    }
    /**
     Gets the image of the tile.
     @return The image of the tile.
     */
    public Image getImage() {
        return image;
    }
    /**
     Sets the ID of the tile.
     @param id The ID to set.
     */
    public void setTileId(int id) {
        this.id = id;
    }
    /**
     Gets the ID of the tile.
     @return The ID of the tile.
     */
    public int getTileId() {
        return id;
    }

    /**
     * Get the type of the tile
     * @return the type of the tile
     */
    public String getTileType() {
        if(isWall())return "w";
        if(isEmpty())return "0";
        return "1";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public void setParent(Map parent) {
        this.parent = parent;
    }
}
