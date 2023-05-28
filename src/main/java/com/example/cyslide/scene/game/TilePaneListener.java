package com.example.cyslide.scene.game;

import com.example.cyslide.map.Map;
import com.example.cyslide.map.MapLoader;
import com.example.cyslide.map.TilePane;
import com.example.cyslide.scene.game.mouvement.MovementDirection;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class TilePaneListener implements EventHandler<MouseEvent> {

    private Map currentMap;

    /**
     * Constructs a TilePaneListener with the specified map.
     * @param currentMap The map associated with the listener
     */
    public TilePaneListener(Map currentMap) {
        this.currentMap = currentMap;
    }

    /**
     * Handles the mouse event by swapping tiles in the map.
     * @param mouseEvent The MouseEvent triggering the handling
     */
    @Override
    public void handle(MouseEvent mouseEvent) {

        MovementDirection direction;

        currentMap.swapTile(
                ((TilePane)mouseEvent.getSource()).getX(),
                ((TilePane)mouseEvent.getSource()).getY(),
                direction = currentMap.getTileByCoord(
                        ((TilePane)mouseEvent.getSource()).getX(),
                        ((TilePane)mouseEvent.getSource()).getY()).canMove(),
                (GridPane) ((TilePane)mouseEvent.getSource()).getParent());


        if(direction != MovementDirection.NONE)currentMap.incrementMvtCounter();
        if(currentMap.checkWin()){
            //set the new record

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You won !");
            alert.setHeaderText("You won !");
            alert.setContentText("You won in " + currentMap.getMvtCounter() + " movements !");

            alert.showAndWait();

            if(currentMap.getMvtCounter() < currentMap.getRecord()){
                currentMap.setRecord(currentMap.getMvtCounter());
                try {
                    MapLoader.saveMap(currentMap,"./map/"+currentMap.getMapName()+".txt");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                MapLoader.unloackNextMap(Integer.parseInt(currentMap.getMapName().substring(3)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
