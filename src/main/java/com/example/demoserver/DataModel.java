package com.example.demoserver;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Comparator;

public class DataModel {

    public ObservableList<Mail> emailList = FXCollections.observableArrayList();

    private final ObjectProperty<Mail> currentMail = new SimpleObjectProperty<>(null);

    public DataModel() throws FileNotFoundException {}

    public ObjectProperty<Mail> currentMailProperty() {
        return currentMail;
    }

    public final Mail getCurrentMail() {
        return currentMailProperty().get();
    }

    public final void setCurrentMail(Mail mail) {
        currentMailProperty().set(mail);
    }

    public ObservableList<Mail> getEmailList() {
        return emailList;
    }

    public void setEmailList(ObservableList<Mail> email) {
        this.emailList = email;
    }

    public void addEmail(Mail mail) {
        this.emailList.add(mail);
    }

    public void setUltimoId(int id) {

    }

    public int getUltimoId() {

        if (emailList.isEmpty()) {
            return 0;
        }
        else {
            return emailList.getLast().getId();
        }
    }

    public void aggiornaTableVIew(TableView<Mail> tableView) {
        tableView.setItems(emailList);
    }




    public JSONArray componiJsonArrayDaInviare(String destinatari, int ultimoId) {
        /*
        for(Mail mail : getEmailList()) {
            if(mail.getDestinatario().contains(destinatario)) {
                if(mail.getId()>ultimoId) {
                    jsonArray.put(mail.getJobj());
                    mail.setStato("ricevuta");
                }
            }
        }

         */
        return new JSONArray(emailList.filtered((mail1 -> mail1.getDestinatari().contains(destinatari)&&(mail1.getId()>ultimoId))));
        //return new JSONArray(emailList.filtered((mail1 -> mail1.getDestinatari().contains(destinatari))));

    }

    public void caricaMailDaStringa(JSONArray jsonArray) throws IOException {

        //JSONArray jsonArray = new JSONArray(JsonString);
        // Itera su ciascun oggetto JSON nell'array
        // Leggi i singoli campi dall'oggetto JSON

        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            // Leggi i singoli campi dall'oggetto JSON
            Integer id = getUltimoId()+1;
            String mittente = jsonObj.getString("mittente");
            String destinatario = jsonObj.getString("destinatari");
            String oggetto = jsonObj.getString("oggetto");
            String testoMail = jsonObj.getString("testoMail");
            LocalDateTime data = LocalDateTime.parse(jsonObj.getString("data"));
            String uniqueCode = jsonObj.getString("uniqueCode");

            String stato = "ricevuta";

            jsonObj.put("id", id);

            Mail mail = new Mail(id, mittente, destinatario, oggetto, testoMail, data.toString(), uniqueCode, stato);
            this.addEmail(mail);
        }

        this.aggiornaFile();

    }

    String jarPath = DataModel.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    File jarFile = new File(jarPath);
    File fileDati = new File(jarFile.getParent(), "dati.json");

    //public File file = new File("src/main/java/com/example/demoServer/dati.json");

    public FileReader fileReader = new FileReader(fileDati);
    JSONTokener tokener = new JSONTokener(fileReader);
    JSONArray jsonArray = new JSONArray(tokener);


    public void aggiornaFile() throws IOException {
        // Scrivi la struttura dati Java aggiornata nel file JSON
        FileWriter writer = new FileWriter(fileDati);
        JSONArray jsonArray = new JSONArray(this.emailList);
        writer.write(jsonArray.toString());
        writer.close();
    }

    public void loadData() {

        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;

            aggiungiMailDaJson(jsonObj);

        }
        emailList.sort(Comparator.comparing(Mail::getId));
    }

    private void aggiungiMailDaJson(JSONObject jsonObj) {
        // Leggi i singoli campi dall'oggetto JSON
        Integer id = jsonObj.getInt("id");
        String mittente = jsonObj.getString("mittente");
        String destinatario = jsonObj.getString("destinatari");
        String oggetto = jsonObj.getString("oggetto");
        String testoMail = jsonObj.getString("testoMail");
        LocalDateTime data = LocalDateTime.parse(jsonObj.getString("data"));
        String uniqueCode = jsonObj.getString("uniqueCode");
        String stato = jsonObj.getString("stato");

        Mail mail = new Mail(id, mittente, destinatario, oggetto, testoMail, data.toString(), uniqueCode, stato);

        this.addEmail(mail);
    }


    private int server_port;

    public void configurazioneIniziale() throws IOException {

        String pathConf = jarFile.getParent()+"\\configurazione.json";

        String content = new String(Files.readAllBytes(Path.of(pathConf)));

        JSONObject jobj = new JSONObject(content);

        server_port = jobj.getInt("server_port");
    }

    public int getServer_port() {
        return server_port;
    }
}
