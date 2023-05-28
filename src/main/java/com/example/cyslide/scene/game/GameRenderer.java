package com.example.cyslide.scene.game;

import com.example.cyslide.map.Map;
import com.example.cyslide.map.TilePane;
import com.example.cyslide.scene.game.mouvement.MovementDirection;
import com.example.cyslide.solver.Node;
import com.example.cyslide.solver.Solver;
import com.example.cyslide.solver.SolverType;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameRenderer {

    private Map currentMap;
    private List<Node> solution;
    public GameRenderer(Map currentMap) {
        this.currentMap = currentMap;
    }


    /**
     * Creates a game scene with a GridPane containing tiles based on the current map.
     * @param movementLabel label to update with the number of movements
     * @return The created game pane.
     */
    public GridPane generatePane(Label movementLabel){
        GridPane pane = new GridPane();
        pane.setPrefSize(800,600);

        //create a Pane for each tile of the map
        for(TilePane tile : currentMap.getTilePaneList()){

            //create pane for each tile and sets its size based on the number per row
            int paneSizeX = 600 / currentMap.getSizeX();
            int paneSizeY = 600 / currentMap.getSizeY();
            tile.setPrefSize(paneSizeX,paneSizeY);

            //set broder style
            BorderStroke borderStyle = new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderStroke.THICK);
            tile.setBorder(new Border(borderStyle));

            //set tile style according to its type
            if(tile.isEmpty()) tile.setStyle("-fx-background-color: #FFFFFF");
            else if(tile.isWall()) tile.setStyle("-fx-background-color: #000000");
            else{

                BackgroundImage backgroundImage = new BackgroundImage(tile.getImage(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                tile.setBackground(new Background(backgroundImage));
            }

            //add listener to the pane
            tile.addEventHandler(MouseEvent.MOUSE_CLICKED, new TilePaneListener(currentMap));
            tile.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

                movementLabel.setText("Movements : " + currentMap.getMvtCounter());

            });

            //create a Pane for each tile of the map
            pane.add(tile, tile.getX(), tile.getY());

        }

        return pane;
    }

    /**
     Creates a game scene with a GridPane containing tiles based on the current map.
     The tiles are sized and styled based on their properties, and a listener is added to handle tile clicks.
     The game scene is then shuffled using the dummyShuffle() method.
     @return The created game scene.
     @throws NullPointerException if the current map is null.
     */
    public Scene createGameScene(){

        if(currentMap == null) throw new NullPointerException("Map is null");

        BorderPane borderPane = new BorderPane();

        HBox sidePane = new HBox();
        sidePane.setPrefSize(200,600);
        sidePane.setStyle("-fx-background-color: #FFFFFF");
        sidePane.setAlignment(Pos.CENTER);

        VBox sidePaneTop = new VBox();
        sidePaneTop.setAlignment(Pos.CENTER);

        //create a label to display the number of movements
        Label movementLabel = new Label("Movements : 0");
        movementLabel.setStyle("-fx-font-size: 20px");
        sidePaneTop.getChildren().add(movementLabel);

        //create a button to solve the map
        Button solveButton = new Button("Solve");
        solveButton.setStyle("-fx-font-size: 20px");
        sidePaneTop.getChildren().add(solveButton);

        //create a button to reset the map
        Button solveByStepButton = new Button("Solve by Step");
        solveByStepButton.setStyle("-fx-font-size: 20px");
        sidePaneTop.getChildren().add(solveByStepButton);

        //Create a button to shuffle the map
        Button shuffleButton = new Button("Shuffle");
        shuffleButton.setStyle("-fx-font-size: 20px");
        sidePaneTop.getChildren().add(shuffleButton);

        sidePane.getChildren().add(sidePaneTop);

        GridPane pane = generatePane(movementLabel);

        solveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //solve the map

                Solver solver = new Solver();

                List<Node> solution =  solver.solve(currentMap, SolverType.A_STAR);

                if(solution == null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No solution found");
                    alert.setHeaderText("No solution found");
                    alert.setContentText("No solution found");
                    alert.showAndWait();
                    return;
                }

                int emptyX = 0;
                int emptyY = 0;

                for(TilePane tilePane : currentMap.getTilePaneList()){
                    if(tilePane.isEmpty()){
                        emptyX = tilePane.getX();
                        emptyY = tilePane.getY();
                    }
                }

                int counter = 0;

                for(Node node : solution){

                    System.out.println(node.getMovementDirection());

                    currentMap.swapTile(emptyX,emptyY,node.getMovementDirection().opposite(),pane);
                    currentMap.incrementMvtCounter();

                    movementLabel.setText("Movements : " + currentMap.getMvtCounter());

                    emptyY += node.getMovementDirection().opposite().getY();
                    emptyX += node.getMovementDirection().opposite().getX();

                }

                solver = null;
            }
        });

        solveByStepButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                Solver solver = new Solver();

                if(solution == null || solution.size() <2) solution =  solver.solve(currentMap, SolverType.A_STAR);

                if(solution == null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No solution found");
                    alert.setHeaderText("No solution found");
                    alert.setContentText("No solution found");
                    alert.showAndWait();
                    return;
                }

                int emptyX = 0;
                int emptyY = 0;

                for(TilePane tilePane : currentMap.getTilePaneList()){
                    if(tilePane.isEmpty()){
                        emptyX = tilePane.getX();
                        emptyY = tilePane.getY();
                    }
                }

                if(currentMap.checkWin()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("You won !");
                    alert.setHeaderText("You won !");
                    alert.setContentText("You won in " + currentMap.getMvtCounter() + " movements !");

                    alert.showAndWait();
                    return;

                }

                Node node = solution.remove(1);

                System.out.println(node.getMovementDirection());

                currentMap.swapTile(emptyX,emptyY,node.getMovementDirection().opposite(),pane);
                currentMap.incrementMvtCounter();

                movementLabel.setText("Movements : " + currentMap.getMvtCounter());

                emptyY += node.getMovementDirection().opposite().getY();
                emptyX += node.getMovementDirection().opposite().getX();

                solver = null;

            }
        });

        shuffleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //shuffle the map
                dummyShuffle(pane,30);
                currentMap.setMvtCounter(0);
                movementLabel.setText("Movements : " + currentMap.getMvtCounter());
            }
        });

        borderPane.setCenter(pane);
        borderPane.setRight(sidePane);



        return new Scene(borderPane,800,600);

    }

    /**
     Shuffles the tiles in a GridPane by performing random tile swaps. Take a random Tiel and make it move is possible to the empty tile. Swap the tile with the empty tile.
     @param pane The GridPane containing the tiles to be shuffled.
     */

    public void dummyShuffle(GridPane pane,int nbShuffle){

        int shuffleCounter = 0;

        while( shuffleCounter < nbShuffle) {

            Random r = new Random();

            int randomX = r.nextInt(currentMap.getSizeX());
            int randomY = r.nextInt(currentMap.getSizeY());

            TilePane randomTile = currentMap.getTileByCoord(randomX, randomY);

            MovementDirection direction = randomTile.canMove();

            if (direction != MovementDirection.NONE) {

                currentMap.swapTile(randomX, randomY, direction, pane);
                shuffleCounter++;
            }
        }
    }

    /**
     Performs a random shuffle of the tiles in a GridPane.
     @param pane The GridPane containing the tiles to be shuffled.
     */

    public void randomShuffle(GridPane pane){

        int emptyX = 0;
        int emptyY = 0;

        for(TilePane tile : currentMap.getTilePaneList()){
            if(tile.isEmpty()){
                emptyX = tile.getX();
                emptyY = tile.getY();
            }
        }

        TilePane emptyTile = currentMap.getTileByCoord(emptyX,emptyY);

        Random r = new Random();

        for(int i = 0; i < 100; i++){

            int sourceX = r.nextInt(currentMap.getSizeX());
            int sourceY = r.nextInt(currentMap.getSizeY());
            int toSwapWithX = r.nextInt(currentMap.getSizeX());
            int toSwapWithY = r.nextInt(currentMap.getSizeY());


            currentMap.swapTwoTiles(sourceX,sourceY,toSwapWithX,toSwapWithY,pane);

        }

    }

}
