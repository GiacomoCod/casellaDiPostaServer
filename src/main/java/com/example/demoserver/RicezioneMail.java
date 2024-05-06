package com.example.demoserver;

import org.json.JSONArray;

public class RicezioneMail {
    private String comando;
    private String mittente;
    private JSONArray jsonArray;

    public RicezioneMail(String comando, String mittente, JSONArray jsonArraytoSend) {
        this.comando = comando;
        this.mittente = mittente;
        this.jsonArray = jsonArraytoSend;
    }

    public String getComando() {
        return comando;
    }
    public void setComando(String comando) {
        this.comando = comando;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }
    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
    public String getMittente() {
        return mittente;
    }
}
