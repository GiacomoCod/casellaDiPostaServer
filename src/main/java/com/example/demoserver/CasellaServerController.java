package com.example.demoserver;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class CasellaServerController {

    private DataModel model;

    private GestioneMailServer gms;

    @FXML
    public TableView<Mail> mailTableView;

    @FXML
    public void handleApriMailDoubleClick(MouseEvent mouseEventVis) throws IOException {
        if (mouseEventVis.getButton().equals(MouseButton.PRIMARY) && mouseEventVis.getClickCount() == 2) {
            gms.apriMailSelezionata(model, mailTableView);
        }
    }

    public void initModel(DataModel model, Stage stage) throws IOException {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;

        model.configurazioneIniziale();
        model.loadData();
        model.aggiornaTableVIew(mailTableView);

        gms = new GestioneMailServer(model);
        gms.attivaServer();


        stage.setOnCloseRequest(event -> {
            try {
                  gms.chiudiServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}


