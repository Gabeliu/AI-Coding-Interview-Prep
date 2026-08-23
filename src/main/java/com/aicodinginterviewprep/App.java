package com.aicodinginterviewprep;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        loadCustomFonts();

        // Basic JavaFX scaffold for the project setup
        stage.setTitle("AI Coding Interview Prep");
        stage.setWidth(1024);
        stage.setHeight(720);

        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.switchToScene("home");
        stage.show();
    }

    private void loadCustomFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Figtree-Regular.ttf"), 17);
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"), 14);
    }

    public static void main(String[] args) {
        launch();
    }
}
