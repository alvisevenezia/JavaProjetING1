package com.example.cyslide.scene.handler;

import com.example.cyslide.scene.menu.mainMenu.MainMenu;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardHandler implements EventHandler<KeyEvent> {

    MainMenu mainMenu;

    public KeyboardHandler(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    /**
     * Handles the key event.
     * @param keyEvent The key event to handle
     */
    @Override
    public void handle(KeyEvent keyEvent) {

        if(keyEvent.getCode() == KeyCode.ESCAPE){

            mainMenu.popScene();
            mainMenu.updateScene();

        }

    }
}
