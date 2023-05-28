package com.example.cyslide.map;

import com.example.cyslide.scene.game.mouvement.MovementDirection;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.*;

public class Map {

    private List<TilePane> tilePaneList;
    private List<Integer> goalList;
    private List<Image> subImageList;
    private int sizeX,sizeY,record;
    private int mvtCounter;
    private String mapName;
    private Image mapImage;
    private String imageURL;
    private boolean unlocked;

    /**
     Constructs a Map object.
     @param tilePaneList The list of TilePanes in the map.
     @param mvtCounter The movement counter.
     @param mapName The name of the map.
     @param sizeX The size of the map in the X direction.
     @param sizeY The size of the map in the Y direction.
     */
    public Map(List<TilePane> tilePaneList, int mvtCounter, String mapName, int sizeX, int sizeY) {

        this.tilePaneList = new ArrayList<TilePane>();
        this.mvtCounter = 0;
        this.mapName = mapName;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.record = 0;
        this.mapImage = null;
        this.imageURL = null;
        this.unlocked = false;
        this.goalList = new ArrayList<>();

    }

    /**
     Retrieves the TilePane at the specified coordinates.
     @param x The X coordinate.
     @param y The Y coordinate.
     @return The TilePane at the specified coordinates, or null if not found.
     */public TilePane getTileByCoord(int x,int y){

        for(TilePane tilePane : tilePaneList){
            if(tilePane.getX() == x && tilePane.getY() == y){
                return tilePane;
            }
        }

        return null;

    }

    /**
     Swaps the positions of two tiles on the map.
     @param x1 The X coordinate of the first tile.
     @param y1 The Y coordinate of the first tile.
     @param x2 The X coordinate of the second tile.
     @param y2 The Y coordinate of the second tile.
     @param pane The GridPane containing the tiles.
     */
    public void swapTwoTiles(int x1, int y1, int x2, int y2, GridPane pane){

        TilePane tilePane = getTileByCoord(x1,y1);
        TilePane landingTilePane = getTileByCoord(x2,y2);

        if(tilePane.isEmpty() || landingTilePane.isEmpty()){
            return;
        }

        int bufferX = tilePane.getX();
        int bufferY = tilePane.getY();

        tilePane.setX(landingTilePane.getX());
        tilePane.setY(landingTilePane.getY());

        pane.getChildren().remove(tilePane);
        pane.add(tilePane,landingTilePane.getX(),landingTilePane.getY());

        landingTilePane.setX(bufferX);
        landingTilePane.setY(bufferY);

        pane.getChildren().remove(landingTilePane);
        pane.add(landingTilePane,bufferX,bufferY);

    }

    /**
     Swaps a tile with the adjacent tile in the specified movement direction.
     @param x The X coordinate of the tile to swap.
     @param y The Y coordinate of the tile to swap.
     @param direction The movement direction.
     @param pane The GridPane containing the tiles.
     */
    public void swapTile(int x, int y, MovementDirection direction, GridPane pane){

        if(direction == MovementDirection.NONE){
            return;
        }

        TilePane tilePane = getTileByCoord(x,y);
        TilePane landingTilePane = getTileByCoord(x+direction.getX(),y+direction.getY());

        if(landingTilePane == null){
            return;
        }

        int bufferX = tilePane.getX();
        int bufferY = tilePane.getY();

        tilePane.setX(landingTilePane.getX());
        tilePane.setY(landingTilePane.getY());

        pane.getChildren().remove(tilePane);
        pane.add(tilePane,landingTilePane.getX(),landingTilePane.getY());

        landingTilePane.setX(bufferX);
        landingTilePane.setY(bufferY);

        pane.getChildren().remove(landingTilePane);
        pane.add(landingTilePane,bufferX,bufferY);

    }

    /**
     * Checks if the game is won.
     * @return The movement counter.
     */
    public boolean checkWin(){
        int i = 0;
        for(TilePane tilePane : tilePaneList){

            if(tilePane.getTileId() == tilePane.getY()*sizeX+tilePane.getX()+1 || tilePane.isEmpty()){
                i++;
            }
            else{
               return false;
            }
        }

        System.out.println("You won!");
        return true;
    }


    // Getters and setters

    /**
     * Sets the list of sub-images for tiles.
     * @param subImageList List of sub-images
     */
    public void setSubImageList(List<Image> subImageList) {
        this.subImageList = subImageList;
    }

    /**
     * Retrieves the list of sub-images for tiles.
     * @return List of sub-images
     */
    public List<Image> getSubImageList() {
        return subImageList;
    }

    /**
     * Adds a sub-image to the list of sub-images for tiles.
     * @param image Sub-image to add
     */
    public void addSubImage(Image image) {
        subImageList.add(image);
    }

    /**
     * Retrieves the size in the X direction.
     * @return Size in the X direction
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * Retrieves the size in the Y direction.
     * @return Size in the Y direction
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * Retrieves the list of tile panes.
     * @return List of tile panes
     */
    public List<TilePane> getTilePaneList() {
        return tilePaneList;
    }

    /**
     * Sets the list of tile panes.
     * @param tilePaneList List of tile panes
     */
    public void setTilePaneList(List<TilePane> tilePaneList) {
        this.tilePaneList = tilePaneList;
    }

    /**
     * Adds a tile pane to the list of tile panes.
     * @param tilePane Tile pane to add
     */
    public void addTile(TilePane tilePane) {
        tilePaneList.add(tilePane);
    }

    /**
     * Sets the record value.
     * @param record Record value
     */
    public void setRecord(int record) {
        this.record = record;
    }

    /**
     * Sets the size in the X direction.
     * @param sizeX Size in the X direction
     */
    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    /**
     * Sets the size in the Y direction.
     * @param sizeY Size in the Y direction
     */
    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    /**
     * Sets the movement counter value.
     * @param mvtCounter Movement counter value
     */
    public void setMvtCounter(int mvtCounter) {
        this.mvtCounter = mvtCounter;
    }

    /**
     * Sets the name of the map.
     * @param mapName Name of the map
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * Clears the list of tile panes.
     */
    public void clearTiles() {
        tilePaneList.clear();
    }

    /**
     * Sets the image representing the map.
     * @param image Image representing the map
     */
    public void setImage(Image image) {
        this.mapImage = image;
    }

    /**
     * Increments the movement counter.
     */
    public void incrementMvtCounter() {
        mvtCounter++;
    }

    /**
     *  Retrieves the movement counter.
     * @return The movement counter.
     */
    public int getMvtCounter() {
        return mvtCounter;
    }

    /**
     * Retrieves record of the map.
     * @return Record of the maps
     */
    public int getRecord() {
        return record;
    }

    /**
     * Retrieves the name of the map.
     * @return The name of the map.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Retrieves the URL of the image representing the map.
     * @return URL of the image representing the map.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Sets the URL of the image representing the map.
     * @param imgSource URL of the image representing the map.
     */
    public void setImageURL(String imgSource) {
        this.imageURL = imgSource;
    }

    /**
     * Return if map is unlocked
     * @return unlocked
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Set if map is unlocked
     * @param unlocked set if map is unlocked
     */
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    /**
     *
     * @return the goalList
     */
    public List<Integer> getGoalList() {
        return goalList;
    }

    /**
     * Retrieves the empty tile
     * @return the empty tile
     */
    public TilePane getEmptyTile() {

        for(TilePane tilePane : tilePaneList){
            if(tilePane.isEmpty()){
                return tilePane;
            }
        }
        return null;

    }
}
