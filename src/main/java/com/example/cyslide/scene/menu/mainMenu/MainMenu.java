package com.example.cyslide.scene.menu.mainMenu;

import com.example.cyslide.scene.handler.KeyboardHandler;
import com.example.cyslide.scene.levelEditor.LevelEditorRender;
import com.example.cyslide.scene.menu.gameMenu.GameMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MainMenu extends Application {

    private LinkedList<Scene> scenes;
    private Stage stage;

    /**
     * The main method that launches the JavaFX application.
     * @param args The command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Hello World!");
        launch(args);
    }

    /**
     * Adds a scene to the list of scenes.
     * @param scene The scene to be added
     */
    public void addScene(Scene scene) {
        scenes.add(scene);
    }

    /**
     * Removes the last scene from the list of scenes.
     */
    public void popScene() {
        scenes.removeLast();
    }

    /**
     * Updates the currently displayed scene.
     * If there are scenes in the list, sets the stage's scene to the last scene.
     * If the scene list is empty, exits the application.
     */
    public void updateScene() {
        if (scenes.size() > 0) {
            stage.setScene(scenes.getLast());
        } else {
            Platform.exit();
        }
    }

    /**
     * The entry point for the JavaFX application.
     * @param stage The primary stage for this application
     * @throws Exception If an exception occurs during the start method
     */
    @Override
    public void start(Stage stage) throws Exception {

        scenes = new LinkedList<Scene>();
        this.stage = stage;
        stage.addEventHandler(KeyEvent.KEY_PRESSED, new KeyboardHandler(MainMenu.this));

        VBox mainContainer = new VBox();

        Button playButton = new Button("Play");
        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                   addScene(GameMenu.createMainMenuScene(MainMenu.this));
                   updateScene();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        Button levelEditorButton = new Button("Level Editor");
        levelEditorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                LevelEditorRender levelEditorMenu = new LevelEditorRender();
                addScene(levelEditorMenu.createLevelEditorScene(MainMenu.this));
                updateScene();
            }
        });

        mainContainer.getChildren().addAll(playButton, levelEditorButton);

        addScene(new Scene(mainContainer, 800, 600));
        updateScene();
        stage.show();

    }

}
