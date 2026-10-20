# Casella di Posta – Server

Server per un sistema di posta elettronica semplificato, realizzato in **Java 21** con **JavaFX**. Gestisce le caselle di posta degli utenti, riceve le mail inviate dai client tramite socket TCP e le rende disponibili ai destinatari. Dispone di un'interfaccia grafica per monitorare l'attività in tempo reale.

## Funzionalità

- Server TCP multi-thread: ogni connessione client è gestita in un thread dedicato.
- Scambio di messaggi in formato **JSON** (una riga per richiesta/risposta).
- Due comandi supportati:
  - `invia`: il client invia le nuove mail; il server le salva e risponde con le mail destinate al mittente con `id` maggiore di `ultimoId`.
  - `ricevi`: il server restituisce al client le mail non ancora ricevute (`id` > `ultimoId`).
- Persistenza su file `dati.json`, aggiornato a ogni modifica.
- Interfaccia JavaFX con:
  - tabella di tutte le mail presenti sul server;
  - finestra di dettaglio della mail selezionata (oggetto, mittente, data, testo);
  - area di log (DEBUG / INFO / ERRORE) delle operazioni del server.

## Requisiti

- JDK 21
- Maven (o il wrapper incluso `./mvnw`)
- Dipendenze gestite da Maven: JavaFX 21 (`javafx-controls`, `javafx-fxml`)
- Libreria [`org.json`](https://github.com/stleary/JSON-java) (`Library/json-20240205.jar`), inclusa nel repository e da aggiungere al classpath/module path

## Struttura del progetto

```
src/main/java/com/example/demoserver/
├── HelloApplication.java            # Avvio dell'app JavaFX
├── App.java                         # Main "launcher" (usato per il jar)
├── CasellaServerController.java     # Controller della finestra principale (tabella + log)
├── VisualizzaMailServerController.java  # Controller della finestra di dettaglio mail
├── GestioneMailServer.java          # Server socket e gestione dei client
├── DataModel.java                   # Modello dati, lettura/scrittura JSON, configurazione
└── Mail.java                        # Entità mail
src/main/resources/com/example/demoserver/   # Viste FXML
Library/                             # Dipendenza org.json
out/artifacts/demoServer_jar/        # Jar pre-compilato e dati.json di esempio
```

## Configurazione

All'avvio il server legge due file nella **stessa cartella del jar / delle classi compilate**:

| File | Contenuto |
|------|-----------|
| `configurazione.json` | Configurazione del server, ad esempio `{ "server_port": 8189 }` (la porta è a scelta) |
| `dati.json` | Elenco delle mail (array JSON). Un esempio è in `out/artifacts/demoServer_jar/dati.json` |

Formato di una mail in `dati.json`:

```json
{
  "id": 1,
  "mittente": "mario.rossi@example.com",
  "destinatari": "luigi.verdi@example.com",
  "oggetto": "Ciao",
  "testoMail": "Testo del messaggio",
  "data": "10/05/2024 17:30:51",
  "uniqueCode": "32c0c70f-a49a-43e2-b12f-550cfe4e5de6",
  "stato": "ricevuta"
}
```

> Nota: il percorso di `configurazione.json` è costruito con il separatore `\` di Windows (vedi `DataModel.java`), quindi l'avvio su macOS/Linux può richiedere un adattamento.

## Build ed esecuzione

```bash
./mvnw clean javafx:run
```

In alternativa usa il jar pre-compilato in `out/artifacts/demoServer_jar/` (con `dati.json` e `configurazione.json` nella stessa cartella).

## Protocollo client–server

Richiesta (una riga JSON):

```json
{ "comando": "invia", "mittente": "mario.rossi@example.com", "ultimoId": 5, "jsonArray": [ { "...": "mail" } ] }
```

Risposta: array JSON con le mail del mittente aventi `id` > `ultimoId`.

## Note

Il client corrispondente è un'applicazione separata.
