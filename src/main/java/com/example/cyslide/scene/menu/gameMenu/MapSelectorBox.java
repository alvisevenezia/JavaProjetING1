package com.example.cyslide.scene.menu.gameMenu;

import com.example.cyslide.map.MapLoader;
import com.example.cyslide.scene.game.GameRenderer;
import com.example.cyslide.scene.menu.mainMenu.MainMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class MapSelectorBox extends HBox {

    int currentMap = 0;

    /**
     Constructs a MapSelectorBox.
     @param mainMenu The MainMenu instance.
     */
    public MapSelectorBox(MainMenu mainMenu) {
        super();

        //create background fill
        BackgroundFill buttonFill = new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill labelFill = new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY);


        HBox currentMapContainer = new HBox();
        currentMapContainer.prefHeightProperty().bind(heightProperty());
        currentMapContainer.prefWidthProperty().bind(widthProperty().divide(3));
        currentMapContainer.setAlignment(Pos.CENTER);


        VBox currentMapLabelContainer = new VBox();
        currentMapLabelContainer.setAlignment(Pos.CENTER);

        //create current map label
        Label currentMapID = new Label();
        HBox.setHgrow(currentMapID, Priority.ALWAYS);
        currentMapID.setContentDisplay(ContentDisplay.CENTER);
        currentMapID.setText("#" + currentMap);
        currentMapID.setTextFill(Color.WHITE);

        currentMapLabelContainer.getChildren().add(currentMapID);

        Button playButton = new Button("Play");
        playButton.prefHeightProperty().bind(heightProperty());


        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            System.out.println("Play button clicked");
            GameRenderer gameRenderer = null;
            try {
                gameRenderer = new GameRenderer(MapLoader.loadMap(".\\map\\map"+currentMap+".txt"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            mainMenu.addScene(gameRenderer.createGameScene());
            mainMenu.updateScene();
        });

        currentMapLabelContainer.getChildren().add(playButton);
        currentMapContainer.getChildren().add(currentMapLabelContainer);

        //create previous map button
        Button previousMapButton = new Button();
        HBox.setHgrow(previousMapButton, Priority.ALWAYS);
        previousMapButton.prefHeightProperty().bind(heightProperty());
        previousMapButton.prefWidthProperty().bind(widthProperty().divide(3));
        //create previous map button image background
        BackgroundImage previousMapButtonImage = new BackgroundImage(new Image(Objects.requireNonNull(this.getClass().getResource("/assets/left-arrow.png")).toExternalForm()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, true, true, true, false));
        //set previous map button background
        previousMapButton.setBackground(new Background(previousMapButtonImage));
        //set previous map button on click event
        previousMapButton.setOnAction(event -> {
            if(isMapUnlocked(currentMap - 1)){

                currentMap--;
                currentMapID.setText("#" + currentMap);
            }
        });


        //create next map button
        Button nextMapButton = new Button();
        HBox.setHgrow(nextMapButton, Priority.ALWAYS);
        nextMapButton.prefHeightProperty().bind(heightProperty());
        nextMapButton.prefWidthProperty().bind(widthProperty().divide(3));
        //create next map button image background
        BackgroundImage nextMapButtonImage = new BackgroundImage(new Image(Objects.requireNonNull(this.getClass().getResource("/assets/right-arrow.png")).toExternalForm()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, true, true, true, false));
        //set next map button background
        nextMapButton.setBackground(new Background(nextMapButtonImage));
        //set next map button on click event
        nextMapButton.setOnAction(event -> {
            if(isMapUnlocked(currentMap + 1)){
                currentMap++;
                currentMapID.setText("#" + currentMap);
            }
        });

        getChildren().add(previousMapButton);
        getChildren().add(currentMapContainer);
        getChildren().add(nextMapButton);
    }

    /**
     Checks if a map with the specified ID exists.
     @param mapID The ID of the map to check.
     @return true if the map exists, false otherwise.
     */
    public boolean mapExists(int mapID){
        return new File(".\\map\\map" + mapID + ".txt").exists();
    }

    public boolean isMapUnlocked(int mapID){
        if(mapExists(mapID)){
            try {
                return new Scanner(new File(".\\map\\map" + mapID + ".txt")).nextLine().split(";")[5].equalsIgnoreCase("1");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else{
            return false;
        }
    }


    /**
     Returns the current map ID.
     @return The current map ID.
     */
    public int getCurrentMap() {
        return currentMap;
    }

    /**
     Retrieves the current record for the selected map.
     @return The current record as a string.
     @throws FileNotFoundException if the map file is not found.
     */
    public String getCurrentRecord() throws FileNotFoundException {

        File map = new File(".\\map\\map" + currentMap + ".txt");

        return new Scanner(map).nextLine().split(";")[4];

    }
}
