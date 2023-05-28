package com.example.cyslide.scene.menu.gameMenu;

import com.example.cyslide.scene.menu.mainMenu.MainMenu;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class GameMenu{

    /**
     Creates the main menu scene.
     @param mainMenu The MainMenu instance.
     @return The created main menu scene.
     @throws FileNotFoundException if a file is not found.
     */
    public static Scene createMainMenuScene(MainMenu mainMenu) throws FileNotFoundException {

        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);

        VBox mainContainer = new VBox();
        mainContainer.setBackground(new Background(backgroundFill));

        MapSelectorBox mapSelectorContainer = new MapSelectorBox(mainMenu);
        mapSelectorContainer.maxHeightProperty().bind(mainContainer.heightProperty().divide(4));
        mapSelectorContainer.prefHeightProperty().bind(mainContainer.heightProperty().divide(4));
        mapSelectorContainer.prefWidthProperty().bind(mainContainer.widthProperty());

        mainContainer.getChildren().add(mapSelectorContainer);

        VBox currentTurn = new VBox();
        currentTurn.maxHeightProperty().bind(mainContainer.heightProperty().divide(4));
        currentTurn.prefHeightProperty().bind(mainContainer.heightProperty().divide(4));

        HBox currentTurnLabelContainer = new HBox();
        currentTurnLabelContainer.setAlignment(Pos.CENTER);
        currentTurnLabelContainer.maxHeightProperty().bind(currentTurn.heightProperty().divide(2));

        Label currentTurnLabel = new Label("Current turn : ");
        currentTurnLabel.setTextFill(Color.WHITE);

        currentTurnLabelContainer.getChildren().add(currentTurnLabel);

        HBox currentTurnLabelDataContainer = new HBox();
        currentTurnLabelDataContainer.setAlignment(Pos.CENTER);
        currentTurnLabelDataContainer.maxHeightProperty().bind(currentTurn.heightProperty().divide(2));

        Label currentTurnLabelData = new Label();
        currentTurnLabelData.setText("0");
        currentTurnLabelData.setTextFill(Color.WHITE);

        currentTurnLabelDataContainer.getChildren().add(currentTurnLabelData);

        currentTurn.getChildren().addAll(currentTurnLabelContainer, currentTurnLabelDataContainer);

        VBox record = new VBox();
        record.maxHeightProperty().bind(mainContainer.heightProperty().divide(4));
        record.prefHeightProperty().bind(mainContainer.heightProperty().divide(4));

        HBox recordLabelContainer = new HBox();
        recordLabelContainer.setAlignment(Pos.CENTER);
        recordLabelContainer.maxHeightProperty().bind(record.heightProperty().divide(2));

        Label recordLabel = new Label("Record : "+mapSelectorContainer.getCurrentRecord());
        recordLabel.setTextFill(Color.WHITE);
        recordLabelContainer.getChildren().add(recordLabel);

        HBox recordLabelDataContainer = new HBox();
        recordLabelDataContainer.setAlignment(Pos.CENTER);
        recordLabelDataContainer.maxHeightProperty().bind(record.heightProperty().divide(2));

        Label recordLabelData = new Label();
        recordLabelData.setText(mapSelectorContainer.getCurrentRecord());
        recordLabelData.setTextFill(Color.WHITE);

        recordLabelDataContainer.getChildren().add(recordLabelData);

        record.getChildren().addAll(recordLabelContainer, recordLabelDataContainer);

        mainContainer.getChildren().add(currentTurn);
        mainContainer.getChildren().add(record);

        return new Scene(mainContainer, 800, 600);
    }

}
