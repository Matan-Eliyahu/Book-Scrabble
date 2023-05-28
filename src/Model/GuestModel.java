package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import model.logic.*;

public class GuestModel extends Observable implements GameModel {

    // Connectivity :
    private Socket hostSocket;
    private PrintWriter out;
    private Scanner in;

    // Profiles :
    private Player myPlayer;
    String myName;

    // Game :
    private Tile[][] myBoard;

    public GuestModel() {

    }

    private boolean isValid(String answer, String indicator) {
        String[] params = answer.split(",");
        String myID = String.valueOf(getMyID());
        if (params.length != 3 || params[0] != myID || params[1] != indicator) {
            // PRINT DEBUG
            System.out.println("GUEST " + myPlayer.getName() + ": Protocol error\n");
            return false;
        }
        return true;
    }

    @Override
    public void connectMe(String myName, String ip, int port) {
        /*
         * Connects to the host server via socket
         * sets the guest player profile
         * (Gets unique ID from the host)
         */
        try {
            this.hostSocket = new Socket(ip, port);
            try {
                out = new PrintWriter(hostSocket.getOutputStream(), true);
                in = new Scanner(hostSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.myName = myName;
        if (connectTest()) { // Successfully completed
            int id = getMyID();
            this.myPlayer = new Player(myName, id, false); // Sets player with given ID from the host
            // PRINT DEBUG
            System.out.println("GUSET " + myName + ": my Player profile is set up\n");
            System.out.println(this.myPlayer);
        }

    }

    private boolean connectTest() {

        // Acknowledge test :
        out.println("0,connectMe," + this.myName);
        String[] answer = in.nextLine().split(",");

        // if (answer[0] == "0" && answer[1] == "connectMe") {
        // String value = answer[2];

        // }
        // }
        if (answer[2].equalsIgnoreCase("true")) {
            // PRINT DEBUG
            System.out.println("GUEST: Connections test passed successfully\n");
            return true;
        }
        // PRINT DEBUG
        System.out.println("GUEST: Connections test failed - Unrecognized error");
        return false;

    }

    @Override
    public void tryPlaceWord(String word, int row, int col, boolean vertical) {
        /*
         * Informs the host that a query has been sent from this guest
         * host responds the score modifier
         */

        out.println(getMyID() + ",tryPlaceWord," + word + ":" + row + ":" + col + ":" + vertical);
        String answer = in.nextLine();

        if (isValid(answer, "tryPlaceWord")) {
            int modifier = Integer.parseInt(answer.split(",")[2]);
            switch (modifier) {

                case -1:
                    /* Not board legal */

                    // PRINT DEBUG
                    System.out.println("GUEST " + myPlayer.getName() + ": your word is not board legal\n");

                case 0:
                    /* Some word is not dictionary legal */

                    // PRINT DEBUG
                    System.out
                            .println("GUEST " + myPlayer.getName() + ": some word placement is not dictionay legal\n");
                default:

                    /*
                     * Placement earns points
                     * Update points, board, tiles
                     */

                    // this.myBoard = Board.getBoard().getTiles();
                    this.myPlayer.addPoints(modifier);
                    pullTiles(7 - myPlayer.getTiles().size());
                    setChanged();
                    notifyObservers();

            }
        }

    }

    @Override
    public void challenge(String word, int row, int col, boolean vertical) {
        /*
         * Informs the host that a challenge has been sent from this guest
         * host responds the challenge score modifier
         */

        out.println(getMyID() + ",challenge," + word + "," + row + "," + col + "," + vertical);
        String answer = in.nextLine();

        if (isValid(answer, "challenge")) {
            int score = Integer.parseInt(answer.split(",")[2]);

            if (score > 0) {

                /*
                 * Challenge was successful
                 * Update points, board, tiles
                 */

                this.myPlayer.addPoints(score);
                // this.myBoard = Board.getBoard().getTiles();
                pullTiles(7 - myPlayer.getTiles().size());
                setChanged();
                notifyObservers();

                // PRINT DEBUG
                System.out.println("GUEST " + myPlayer.getName() + ": The challenge was successful!\n");

            } else if (score < 0) {

                /*
                 * Challenge failed
                 * You lose points
                 */

                this.myPlayer.addPoints(score);
                // PRINT DEBUG
                System.out.println("GUEST " + myPlayer.getName() + ": The challenge failed, you lose points\n");
            } else {

                /* Some error occurred */

                // PRINT DEBUG
                System.out.println("GUEST " + myPlayer.getName() + ": problem with challenge (maybe returned 0)\n");
            }
        }
    }

    @Override
    public void pullTiles(int count) {

        /* how we do it ??? */

    }

    @Override
    public void skipTurn() {
        /*
         * Informs the host that this guest want to skip turn
         * host responds true or false if operation was successful
         */

        out.println(getMyID() + ",skipTurn,true");
        String answer = in.nextLine();

        if (isValid(answer, "skipTurn")) {
            String value = answer.split(",")[2];
            if (value == "true") {
                this.myPlayer.setMyTurn(false);
                // PRINT DEBUG
                System.out.println("GUEST " + myPlayer.getName() + ": Skip my turn\n");
            } else {
                // PRINT DEBUG
                System.out.println("GUEST " + myPlayer.getName() + ": Some error occurred while skipTurn\n");
            }

        }
    }

    @Override
    public void myBookChoice(String bookName) {
        out.println(getMyID() + ",myBookChoice," + bookName);
        String answer = in.nextLine();

        if (isValid(answer, "myBookChoice")) {
            String value = answer.split(",")[2];
            if (value == "true") {
                // PRINT DEBUG
                System.out.println("GUEST " + myPlayer.getName() + ": Selected book has been updated");
            } else {
                // PRINT DEBUG
                System.out.println("GUEST " + myPlayer.getName() + ": Some error occurred while sending myBookChoice");
            }
        }
    }

    @Override
    public void quitGame() {
        /*
         * need to close all streams ....
         * close the communication with host ...
         */
        try {
            this.hostSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getMyName() {
        if (this.myPlayer != null) {
            return this.myPlayer.getName();
        } else
            return "null";
    }

    @Override
    public int getMyID() {
        if (this.myPlayer == null) {

            // Send request for my ID :
            out.println("0,getMyID," + myName);
            String answer = in.nextLine();
            // Validation :
            // if (isValid(answer, "getMyID")) {
            // }
            int id = Integer.parseInt(answer.split(",")[2]);
            return id;
        }
        return this.myPlayer.getID();
    }

    @Override
    public int getMyScore() {

        // Send request for my ID :
        out.println(getMyID() + ",getMyScore,true");
        String answer = in.nextLine();

        // Validation :
        if (isValid(answer, "getMyScore")) {
            int score = Integer.parseInt(answer.split(",")[2]);
            return score;
        } else {
            // PRINT DEBUG
            System.out.println("GUEST " + myPlayer.getName() + ": problem with getMyScore\n");
            return -1;
        }
    }

    @Override
    public boolean isMyTurn() {
        // Send request for my ID :
        out.println(getMyID() + ",isMyTurn,true");
        String answer = in.nextLine();

        // Validation :
        if (isValid(answer, "isMyTurn")) {
            String bool = answer.split(",")[2];
            if (bool == "true") {
                this.myPlayer.setMyTurn(true);
                return true;
            } else {
                // PRINT DEBUG
                System.out.println("GUEST " + myPlayer.getName() + ": it is not my turn\n");
                return false;
            }

        } else {
            // PRINT DEBUG
            System.out.println("GUEST " + myPlayer.getName() + ": problem with isMyTurn\n");
            return false;
        }
    }

    @Override
    public Tile[][] getCurrentBoard() {

        /* how we do it ??? */

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentBoard'");
    }

    @Override
    public ArrayList<Tile> getMyTiles() {
        return this.myPlayer.getTiles();
    }

    @Override
    public ArrayList<Word> getMyWords() {
        return this.myPlayer.getWords();
    }

}
