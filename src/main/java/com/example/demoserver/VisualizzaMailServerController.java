package com.example.demoserver;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class VisualizzaMailServerController {

    @FXML
    public javafx.scene.control.TextArea TextArea;
    @FXML
    public javafx.scene.control.TextField mittente;
    @FXML
    public javafx.scene.control.TextField oggetto;
    @FXML
    public javafx.scene.control.Label data;

    private DataModel model;

    public void mostraTesto() {
        TextArea.setText(model.getCurrentMail().getTestoMail());
    }

    public void mostraMittente() {
        mittente.setText(model.getCurrentMail().getMittente());
    }

    public void mostraOggetto() {
        oggetto.setText(model.getCurrentMail().getOggetto());
    }

    public void mostraData() {
        data.setText(model.getCurrentMail().getData());
    }

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
    }
}
