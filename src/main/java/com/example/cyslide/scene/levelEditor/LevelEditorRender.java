package com.example.cyslide.scene.levelEditor;

import com.example.cyslide.map.Map;
import com.example.cyslide.map.MapLoader;
import com.example.cyslide.map.TilePane;
import com.example.cyslide.scene.menu.mainMenu.MainMenu;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LevelEditorRender {

    private Map editedMap;
    private boolean placeWall = false;
    private boolean placeEmpty = false;


    public LevelEditorRender() {

    }

    public GridPane generateGridPaneFromMap(int sizeX, int sizeY){

        GridPane mapPane = new GridPane();
        mapPane.setStyle("-fx-background-color: #FFFFFF");

        List<Image> subImageList = null;

        if(editedMap == null){

            editedMap = new Map(null,0,"CustomMap",sizeX,sizeY);

        }else{

            editedMap.setSizeX(sizeX);
            editedMap.setSizeY(sizeY);
            editedMap.clearTiles();

            if(editedMap.getImageURL() != null){

                subImageList = new MapLoader().breakImage(editedMap.getImageURL(), sizeY, sizeX,600/sizeX,600/sizeY);
            }

        }

        //Create new map
        for(int idY = 0; idY < sizeY; idY++){

            for(int idX = 0; idX < sizeX; idX++){

                TilePane tile = new TilePane(idX,idY,false,false,editedMap);

                if(editedMap.getImageURL() != null){
                    assert subImageList != null;
                    BackgroundImage backgroundImage = new BackgroundImage(subImageList.get(sizeX * idY + idX), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                    tile.setBackground(new Background(backgroundImage));
                    tile.setImage(subImageList.get(sizeX * idY + idX));
                }else{

                    tile.setStyle("-fx-background-color: #FFF00F");
                }
                tile.setPrefSize((double) 600 /sizeX, (double) 600 /sizeY);

                //set broder style
                BorderStroke borderStyle = new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderStroke.THICK);
                tile.setBorder(new Border(borderStyle));
                editedMap.addTile(tile);

                mapPane.add(tile,idX,idY);

            }

        }

        mapPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e ->{

            int coordX = (int)(e.getX()/(600/editedMap.getSizeX()));
            int coordY = (int)(e.getY()/(600/editedMap.getSizeY()));

            System.out.println("X" + coordX + "Y" + coordY);

            TilePane clickedPane = editedMap.getTileByCoord(coordX,coordY);

            if(placeWall){

                clickedPane.setStyle("-fx-background-color: #000000");
                clickedPane.setWall(true);
                clickedPane.setEmpty(false);

            }else if(placeEmpty){

                clickedPane.setStyle("-fx-background-color: #FFFFFF");
                clickedPane.setWall(false);
                clickedPane.setEmpty(true);

            }else{

                clickedPane.setBackground(new Background(new BackgroundImage(clickedPane.getImage(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
                clickedPane.setWall(false);
                clickedPane.setEmpty(false);
            }

        });

        return mapPane;

    }

    public Scene createLevelEditorScene(MainMenu mainMenu){

        BorderPane levelEditorPane = new BorderPane();
        Scene levelEditorScene = new Scene(levelEditorPane,800,600);

        int sizeX = 5;
        int sizeY = 5;

        GridPane mapPane = generateGridPaneFromMap(sizeX,sizeY);

        levelEditorPane.setCenter(mapPane);

        HBox editorMenuPane = new HBox();
        editorMenuPane.setPrefSize(200,600);
        editorMenuPane.setStyle("-fx-background-color: #007aab");
        editorMenuPane.setAlignment(Pos.CENTER);

        VBox editorMenuButtonPane = new VBox();
        editorMenuButtonPane.setAlignment(Pos.CENTER);

        editorMenuPane.getChildren().add(editorMenuButtonPane);

        Button saveButton = new Button("Save");

        editorMenuButtonPane.getChildren().add(saveButton);

        Button wallButton = new Button("Wall");
        wallButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                placeWall = true;
                placeEmpty = false;
            }
        });
        editorMenuButtonPane.getChildren().add(wallButton);


        Button emptyButton = new Button("Empty");
        emptyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                placeEmpty = true;
                placeWall = false;
            }
        });
        editorMenuButtonPane.getChildren().add(emptyButton);


        Button tileButton = new Button("Tile");
        tileButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                placeEmpty = false;
                placeWall = false;
            }
        });
        editorMenuButtonPane.getChildren().add(tileButton);

        TextField mapFileURLField = new TextField();
        mapFileURLField.setPromptText("Map file URL");

        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                editedMap.setImageURL(mapFileURLField.getText());

                //get mapID but looking at the last mapID in the map folder
                File mapFolder = new File(".\\map");
                File[] mapFiles = mapFolder.listFiles();
                int mapID = mapFiles.length;

                editedMap.setMapName("map"+mapID);
                editedMap.setRecord(Integer.MAX_VALUE);

                try {
                    MapLoader.saveMap(editedMap,".\\map\\map"+mapID+".txt");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Label sizeXLabel = new Label("Size X "+ sizeX);
        Label sizeYLabel = new Label("Size Y "+ sizeY);

        editorMenuButtonPane.getChildren().addAll(sizeXLabel,sizeYLabel);

        Slider sizeXSlider = new Slider();
        sizeXSlider.setMin(2);
        sizeXSlider.setMax(25);
        sizeXSlider.setValue(sizeX);
        sizeXSlider.setShowTickLabels(true);
        sizeXSlider.setShowTickMarks(true);
        sizeXSlider.setMajorTickUnit(5);
        sizeXSlider.setMinorTickCount(1);
        sizeXSlider.setBlockIncrement(1);
        sizeXSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            int value = (int) Math.round(new_val.doubleValue());
            sizeXLabel.setText("Size X: " + value);
        });

        Slider sizeYSlider = new Slider();
        sizeYSlider.setMin(2);
        sizeYSlider.setMax(25);
        sizeYSlider.setValue(sizeY);
        sizeYSlider.setShowTickLabels(true);
        sizeYSlider.setShowTickMarks(true);
        sizeYSlider.setMajorTickUnit(5);
        sizeYSlider.setMinorTickCount(1);
        sizeYSlider.setBlockIncrement(1);
        sizeYSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            int value = (int) Math.round(new_val.doubleValue());
            sizeYLabel.setText("Size Y: " + value);
        });

        editorMenuButtonPane.getChildren().addAll(mapFileURLField,sizeXSlider,sizeYSlider);

        Button createMapButton = new Button("Create map");
        createMapButton.addEventHandler(MouseEvent.MOUSE_CLICKED,e ->{

                editedMap.setImageURL(mapFileURLField.getText());
                GridPane newMapPane = generateGridPaneFromMap((int)sizeXSlider.getValue(),(int)sizeYSlider.getValue());

                levelEditorPane.setCenter(newMapPane);

        });
        editorMenuButtonPane.getChildren().add(createMapButton);

        levelEditorPane.setRight(editorMenuPane);


        return levelEditorScene;
    }


}
