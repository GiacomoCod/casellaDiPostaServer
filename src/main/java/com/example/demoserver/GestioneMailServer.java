package com.example.demoserver;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GestioneMailServer {

    private final DataModel modello;

    private final CasellaServerController csc;

    public GestioneMailServer(DataModel model, CasellaServerController csc) {
        this.modello = model;
        this.csc = csc;
    }

    public void apriMailSelezionata(DataModel model, TableView<Mail> tv) throws IOException {
        model.setCurrentMail(tv.getSelectionModel().getSelectedItem());
        Mail selectedItem = model.getCurrentMail();
        if (selectedItem != null) {

            FXMLLoader loaderVisualizza = new FXMLLoader(getClass().getResource("formVisualizzaMail.fxml"));
            Parent root = loaderVisualizza.load();
            Stage stage = new Stage();
            VisualizzaMailServerController vmcs = loaderVisualizza.getController();

            vmcs.initModel(model);
            vmcs.mostraOggetto();
            vmcs.mostraTesto();
            vmcs.mostraMittente();
            vmcs.mostraData();


            stage.setScene(new Scene(root));
            stage.show();
        }

    }

    public static ServerSocket serverSocket;
    public static boolean isRunning = true;

    public static BufferedReader reader;
    public static PrintWriter out;

    public void attivaServer() {

        class ClientHandler implements Runnable {
            private final Socket clientSocket;

            public ClientHandler(Socket clientSocket) {
                this.clientSocket = clientSocket;
            }

            @Override
            public void run () {
                try {
                    System.out.println("Leggo il file json dal client");
                    csc.setTextAreaPerLog("DEBUG","Leggo il file json dal client");

                    reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String line;
                    line = reader.readLine();

                    JSONObject obj = new JSONObject(line);

                    String comando = obj.getString("comando");
                    String mit = obj.getString("mittente");
                    int ultimoId = obj.getInt("ultimoId");
                    JSONArray jsonarray;
                    try {
                        jsonarray = obj.getJSONArray("jsonArray");
                    }
                    catch (IOError e) {
                        serverSocket.close();
                        return;
                    }

                    if(comando.equals("invia")) {

                        modello.aggiungiMailDaJsonArray(jsonarray);

                        System.out.println("JSONArray ricevuto dal client: ");
                        csc.setTextAreaPerLog("DEBUG","JSON ricevuto dal client");
                        System.out.println(jsonarray);

                        // Invia una risposta al client
                        System.out.println("Invio una risposta al client");
                        csc.setTextAreaPerLog("DEBUG","Invio una risposta al client");

                        JSONArray jsonResponse = new JSONArray(modello.getEmailList().filtered((mail1 -> mail1.getMittente().equals(mit)&&(mail1.getId()>ultimoId))));

                        OutputStream outputStream = clientSocket.getOutputStream();
                        PrintWriter writer = new PrintWriter(outputStream, true);

                        System.out.println("Chiudo la connessione");
                        csc.setTextAreaPerLog("DEBUG","Chiudo la connessione");

                        writer.println(jsonResponse);
                    }
                    else { //comando.equals("ricevi")

                        System.out.println(comando);
                        csc.setTextAreaPerLog("DEBUG",comando);

                        System.out.println("rimando indietro la lsta di mail al client");
                        csc.setTextAreaPerLog("DEBUG","rimando indietro la lista di mail al client");

                        JSONArray jsonArray = modello.componiJsonArrayDaInviare(mit, ultimoId);

                        OutputStream outputStream = clientSocket.getOutputStream();
                        PrintWriter writer = new PrintWriter(outputStream, true);
                        writer.println(jsonArray);
                    }

                    clientSocket.close();

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    csc.setTextAreaPerLog("ERRORE","Errore nell'esecuzione del thread di gestione del client: "+e.getMessage());
                }


            }

        }

        class ConnectionHandler implements Runnable {
            @Override
            public void run() {
                try {
                    while(isRunning) {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Connessione accettata da: " + clientSocket);
                        csc.setTextAreaPerLog("DEBUG","Connessione accettata da: " + clientSocket);

                        Thread clientThread = new Thread(new ClientHandler(clientSocket));
                        clientThread.start();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    csc.setTextAreaPerLog("INFO","Server disconnesso: "+e.getMessage());

                }
            }
        }
        //partenza
        try {
            serverSocket = new ServerSocket(modello.getServer_port());
            System.out.println("Server connesso e in ascolto sulla porta "+modello.getServer_port()+"...");
            csc.setTextAreaPerLog("DEBUG","Server connesso e in ascolto sulla porta "+modello.getServer_port()+"...");
            Thread connectionThread = new Thread(new ConnectionHandler());
            connectionThread.start();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            csc.setTextAreaPerLog("ERRORE","Errore nell'esecuzione del thread di gestione del client: "+e.getMessage());
        }

    }

    public void disattivaServer() throws IOException {
        serverSocket.close();
    }
}
