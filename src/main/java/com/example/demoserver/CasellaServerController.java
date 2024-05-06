package com.example.demoserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CasellaServerController {

    private DataModel model;
    private GestioneMailServer gms;

    @FXML
    public TableView<Mail> mailTableView;


    @FXML
    public void handleApriMailDoubleClick(MouseEvent mouseEventVis) throws IOException {
        if (mouseEventVis.getButton().equals(MouseButton.PRIMARY) && mouseEventVis.getClickCount() == 2) {
            gms.apriMailSelezionata(model, mailTableView, 1);
        }
    }

    public void initModel(DataModel model) throws IOException {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;

        model.loadData();
        //model.inviaMailaClient();
        model.configurazioneIniziale();
        model.aggiornaTableVIew(mailTableView);

        gms = new GestioneMailServer(model);
        gms.attivaServer();
    }

}


