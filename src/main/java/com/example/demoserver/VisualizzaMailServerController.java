package com.example.demoserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class VisualizzaMailServerController {

    @FXML
    public javafx.scene.control.TextArea TextArea;
    @FXML
    public javafx.scene.control.TextField mittente;
    @FXML
    public javafx.scene.control.TextField oggetto;
    @FXML
    public javafx.scene.control.Label data;
    @FXML
    public javafx.scene.control.Label codice;

    private Stage stage; // Dichiarazione dell'oggetto Stage

    private DataModel model;

    private TableView<Mail> mailTableViewAgg;

    //private final GestioneMailServer gm = new GestioneMailServer(model);

    public VisualizzaMailServerController() throws FileNotFoundException {
    }

    // Metodo per iniettare l'oggetto Stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private void closeWindow() {
        stage.close(); // Chiudi la finestra corrente
    }


    @FXML
    public void back(javafx.event.ActionEvent actionEvent) {
        closeWindow();
    }
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
    public void mostraCodice() {
        codice.setText(model.getCurrentMail().getUniqueCode());
    }

    public void setMail(TableView<Mail> mailTableView) {
        mailTableViewAgg = mailTableView;
    }

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
    }
}
