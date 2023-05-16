package Model;

import java.io.*;

import Model.Server.ClientHandler;

public class GuestHandler implements ClientHandler {
    BufferedReader in;
    PrintWriter out;

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        /*
         * Reads a string from the client until line-break character.
         * string starts with "Q," for query or "C," for challenge.
         * the rest of the string (seperated by ",")
         * indicate the names of the books, except for the last word that indicates the
         * query itself.
         * Using DictionaryManager will return the answer as a string "true" or
         * "false" followed by a line break character.
         * The conversation with the client will end after one query.
         * 
         * "1234, func(), word"
         */

        String[] userLine = null;
        try {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            out = new PrintWriter(outToClient, true);
            userLine = in.readLine().split(",");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String operator = userLine[0];
        String[] args = new String[userLine.length - 1];
        System.arraycopy(userLine, 1, args, 0, (userLine.length - 1));

        //DictionaryManager dm = DictionaryManager.get();

        // if (operator.equals("Q")) {
        //     if (dm.query(args)) {
        //         out.println("true");
        //     } else {
        //         out.println("false");
        //     }
        // } else if (operator.equals("C")) {
        //     if (dm.challenge(args)) {
        //         out.println("true");
        //     } else {
        //         out.println("false");
        //     }
        // } else {
        //     out.println("wrong operator");
        // }

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