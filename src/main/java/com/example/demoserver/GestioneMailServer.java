package com.example.demoserver;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GestioneMailServer {

    private final DataModel modello;

    public GestioneMailServer(DataModel model) {
        this.modello = model;
    }

    public void apriMailSelezionata(DataModel model, TableView<Mail> tv, int controllo) throws IOException {
        //Mail selectedItem = recordTableView.getSelectionModel().getSelectedItem();
        //Mail selectedItem = mailTableView.getSelectionModel().getSelectedItem();
        model.setCurrentMail(tv.getSelectionModel().getSelectedItem());
        Mail selectedItem = model.getCurrentMail();
        if (selectedItem != null) {

            FXMLLoader loaderVisualizza = new FXMLLoader(getClass().getResource("formVisualizzaMail.fxml"));
            Parent root = loaderVisualizza.load();
            Stage stage = new Stage();
            VisualizzaMailServerController vmcs = loaderVisualizza.getController();

            vmcs.initModel(model);
            vmcs.setStage(stage);
            vmcs.setMail(tv);
            if (selectedItem.getMittente().equals("giacomo.codagnone@gmail.com")) {
                controllo = 0;
            }
            //vmcs.setControllo(controllo);

            vmcs.mostraOggetto();
            vmcs.mostraTesto();
            vmcs.mostraMittente();
            vmcs.mostraData();
            vmcs.mostraCodice();


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
                    System.out.println("Leggo il json dal client");
                    reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String line;
                    line = reader.readLine();

                    JSONObject obj = new JSONObject(line);

                    String comando = obj.getString("comando");
                    String mit = obj.getString("mittente");
                    JSONArray jsonarray = obj.getJSONArray("jsonArray");
                    int ultimoId = obj.getInt("ultimoId");


                    if(comando.equals("invia")) {

                        modello.caricaMailDaStringa(jsonarray);

                        System.out.println("JSONArray ricevuto dal client: ");
                        System.out.println(jsonarray);

                        // Invia una risposta al client
                        System.out.println("Invio una risposta al client");

                        JSONArray jsonResponse = new JSONArray(modello.getEmailList().filtered((mail1 -> mail1.getMittente().equals(mit)&&(mail1.getId()>ultimoId))));

                        OutputStream outputStream = clientSocket.getOutputStream();
                        PrintWriter writer = new PrintWriter(outputStream, true);
                        writer.println(jsonResponse);


                        System.out.println("Chiudo la connessione");
                    }
                    else {

                        System.out.println(comando);
                        System.out.println("rimando indietro la lsta di mail al client");

                        JSONArray jsonArray = modello.componiJsonArrayDaInviare(mit, ultimoId);

                        OutputStream outputStream = clientSocket.getOutputStream();
                        PrintWriter writer = new PrintWriter(outputStream, true);
                        writer.println(jsonArray);
                    }
                    clientSocket.close();

                } catch (IOException e) {
                    System.out.println(e.getMessage());
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
                        Thread clientThread = new Thread(new ClientHandler(clientSocket));
                        clientThread.start();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        try {
            serverSocket = new ServerSocket(modello.getServer_port());
            System.out.println("Server connesso e in ascolto sulla porta 12345...");

            Thread connectionThread = new Thread(new ConnectionHandler());
            connectionThread.start();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


}
