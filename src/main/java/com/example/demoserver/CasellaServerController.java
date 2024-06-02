package com.example.demoserver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CasellaServerController {

    private DataModel model;

    private GestioneMailServer gms;

    @FXML
    public Label serverPort;

    @FXML
    private TextArea textArea;

    @FXML
    private TableView<Mail> mailTableView;

    @FXML
    public void handleApriMailDoubleClick(MouseEvent mouseEventVis) throws IOException {
        if (mouseEventVis.getButton().equals(MouseButton.PRIMARY) && mouseEventVis.getClickCount() == 2) {
            gms.apriMailSelezionata(model, mailTableView);
        }
    }

    public void initModel(DataModel model, Stage stage) throws IOException {

        if (this.model != null) {
            throw new IllegalStateException("Il modello può essere inizializzato una sola volta");
        }
        this.model = model ;

        model.setCasella(this);
        model.configurazioneIniziale();
        model.loadData();
        model.aggiornaTableVIew(mailTableView);
        setServerPortLabel();


        gms = new GestioneMailServer(model, this);
        gms.attivaServer();

        stage.setOnCloseRequest(event -> {
            try {
                  gms.disattivaServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setTextAreaPerLog(String tipo, String log) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataeora = localDateTime.format(formatter);
        Platform.runLater(() -> textArea.appendText(dataeora + " - " + tipo + " - " + log+"\n"));
    }

    public void setServerPortLabel() {
        serverPort.setText("Server port: "+model.getServer_port());
    }

    public void handleButtonConnetti(ActionEvent actionEvent) {
        gms.attivaServer();
    }

    public void handleButtonDisconnetti(ActionEvent actionEvent) throws IOException {
        gms.disattivaServer();
    }
}


