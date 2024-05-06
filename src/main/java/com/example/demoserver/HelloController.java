package com.example.demoserver;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        String codiceUnivoco = java.util.UUID.randomUUID().toString();

        System.out.println(codiceUnivoco);
        welcomeText.setText(codiceUnivoco);
    }

}