module com.example.cyslide.scene.menu.MainMenu {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.example.cyslide.scene.menu.mainMenu to javafx.graphics;
}