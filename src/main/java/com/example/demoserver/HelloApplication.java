package com.example.demoserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tableViewServer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        CasellaServerController casellaController = fxmlLoader.getController();

        DataModel model = new DataModel(casellaController);

        casellaController.initModel(model, stage);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}