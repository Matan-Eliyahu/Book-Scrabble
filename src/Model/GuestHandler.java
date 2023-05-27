package model;

import java.io.*;

import model.logic.Player;
import model.server.ClientHandler;

public class GuestHandler implements ClientHandler {
    private HostModel hm;
    private BufferedReader in;
    private PrintWriter out;

    

    public GuestHandler() {
        this.hm = HostModel.getHM();
    }

    private boolean isValid(String request) {
        /*
         * Checks if the guest reqest is valid
         * if the guest id is 0 - he is trying to connect and get his id
         */

        String[] params = request.split(",");

        if (params.length != 3) {
            // PRINT DEBUG
            System.out.println("HOST : Protocol error - invalid");
            return false;
        }

        int id = Integer.parseInt(params[0]);
        String modifier = params[1];

        if (id == 0 && modifier == "connectMe") { // id == 0 for initializaition
            return true;
        }

        if (HostModel.getHM().getPlayersByID().get(id) == null) { // No player exist
            // PRINT DEBUG
            System.out.println("HOST : Protocol error - No player exist");
            return false;
        }

        if (HostModel.getHM().getPlayersByID().get(id).getID() != id) { // No ID
            // PRINT DEBUG
            System.out.println("HOST : Protocol error - No matching ID");
            return false;
        }

        return true;
    }

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        /*
         * handles a client
         * one conversation
         * one request & answer
         */

        try {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            out = new PrintWriter(outToClient, true);
            String request = in.readLine();

            if (isValid(request)) {
                String[] req = request.split(",");
                String guestID = req[0];
                String modifier = req[1];
                String guestValue = req[2];

                switch (modifier) {

                    // All model cases
                    case "connectMe":
                        connectHandler(guestID, guestValue);
                    case "myBookChoice":
                        addBookHandler(guestID, guestValue);
                    case "tryPlaceWord":
                        queryHandler(guestID, guestValue);
                    case "challenge":
                        challengeHandler(guestID, guestValue);
                    case "pullTiles":
                        pullTilesHandler(guestID, guestValue);
                    case "skipTurn":
                        skipTurnHandler(guestID, guestValue);
                    case "quitGame":
                        quitHandler(guestID, guestValue);
                    case "getMyName":
                        getNameHandler(guestID, guestValue);
                    case "getMyID":
                        getIdHandler(guestID, guestValue);
                    case "getMyScore":
                        scoreHandler(guestID, guestValue);
                    case "isMyTurn":
                        myTurnHandler(guestID, guestValue);
                    case "getCurrentBoard":
                        boardHandler(guestID, guestValue);
                    case "getMyTiles":
                        tilesHandler(guestID, guestValue);
                    case "getMyWords":
                        wordsHandler(guestID, guestValue);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void connectHandler(String guestID, String guestName) {
        if (guestID != "0") {
            // PRINT DEBUG
            System.out.println("HOST: id of guest is not 0");
        }
        if (guestID == "0") {
            this.hm.getPlayersByName().put(guestName, new Player(guestName, HostModel.generateID(), false));
            out.println("0,connectMe,true");
            // PRINT DEBUG
            System.out.println("HOST: " + guestName + " is Connected!");
        } else {
            // PRINT DEBUG
            System.out.println("HOST: failed to connect guest - " + guestName);
        }
    }

    private void addBookHandler(String guestID, String guestBook) {
    }

    private void queryHandler(String guestID, String wordParams) {
    }

    private void challengeHandler(String guestID, String challengeParams) {
    }

    private void pullTilesHandler(String guestID, String count) {
    }

    private void skipTurnHandler(String guestID, String bool) {
    }

    private void quitHandler(String guestID, String bool) {
    }

    private void getNameHandler(String guestID, String bool) {
    }

    private void getIdHandler(String guestID, String guestName) {
        if (guestID != "0") {
            // PRINT DEBUG
            System.out.println("HOST: id of guest is not 0 - request ID failed");
        }
        if (guestID == "0") {
            String id = String.valueOf(this.hm.getPlayersByName().get(guestName).getID());
            out.println("0,getMyID,"+id);
            // PRINT DEBUG
            System.out.println("HOST: " + guestName + " gets is ID - "+id);
        } else {
            // PRINT DEBUG
            System.out.println("HOST: failed to guest " + guestName+" ID");
        }
    }

    private void scoreHandler(String guestID, String bool) {
    }

    private void myTurnHandler(String guestID, String bool) {
    }

    private void boardHandler(String guestID, String bool) {
    }

    private void tilesHandler(String guestID, String bool) {
    }

    private void wordsHandler(String guestID, String bool) {
    }

    @Override
    public void close() {
        /* responsible to close the streams for this handler */

        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}