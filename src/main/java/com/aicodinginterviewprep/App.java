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
        try {
            var figtreeStream = getClass().getResourceAsStream("/fonts/Figtree-Regular.ttf");
            if (figtreeStream != null) {
                Font.loadFont(figtreeStream, 17);
            }

            var jetbrainsStream = getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf");
            if (jetbrainsStream != null) {
                Font.loadFont(jetbrainsStream, 14);
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to load custom fonts: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
