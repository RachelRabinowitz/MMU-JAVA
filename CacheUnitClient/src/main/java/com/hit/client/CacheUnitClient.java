package main.java.com.hit.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class manages client connection to server and treatment of the recurrent reaction.
 */
public class CacheUnitClient extends java.lang.Object {

    private Socket myServer;
    private DataOutputStream output;
    private DataInputStream input;
    private String[] strings;


    public CacheUnitClient() {

    }

    public String[] getStrings() {
        return strings;
    }

    public String send(String request) {
        try {
            myServer = new Socket("127.0.0.1", 12345);
            output = new DataOutputStream(myServer.getOutputStream());
            input = new DataInputStream(myServer.getInputStream());
            output.writeUTF(request);
            output.flush();
            StringBuilder builder = new StringBuilder();
            String content = "";
            do {
                content = input.readUTF();
                builder.append(content);
            } while (input.available() != 0);
            content = builder.toString();
            if (content.startsWith("[[")) {
                content = content.substring(2, builder.length() - 1).substring(builder.indexOf("]"));
                strings = content.split(",");
            } else {
                strings = content.substring(builder.indexOf(",") + 1, builder.length() - 1).split(",");
            }
            output.close();
            input.close();
            myServer.close();
        } catch (IOException e) {
            return "failed";
        }
        return "success";
    }
}
