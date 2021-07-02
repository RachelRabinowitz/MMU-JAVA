package main.java.com.hit.server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.services.CacheUnitController;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A class that handles requests received from the server and returns a response to clients.
 * @param <T> Generic request body type.
 */
public class HandleRequest<T> implements java.lang.Runnable {

    private Socket socket;
    private CacheUnitController unitController;
    private DataInputStream reader;
    private DataOutputStream writer;
    private Request<DataModel<T>[]> request;
    private String req;


    HandleRequest(java.net.Socket s, CacheUnitController<T> controller) {
        socket = s;
        unitController = controller;
        try {
            reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                req = reader.readUTF();
            } catch (IOException e) {
                try {
                    socket.close();
                    System.out.println("client closed connection");
                    break;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            Type ref = new TypeToken<Request<DataModel<T>[]>>() {
            }.getType();
            request = new Gson().fromJson(req, ref);
            String action = request.toString();
            if (action.equals("UPDATE")) {
                try {
                    writer.writeUTF(String.valueOf(unitController.update(request.getBody())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (action.equals("DELETE")) {
                try {
                    writer.writeUTF(String.valueOf(unitController.delete(request.getBody())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (action.equals("GET")) {
                try {
                    ArrayList<Object> data = unitController.get(request.getBody());
                    data.set(0, new Gson().toJson(data.get(0)));
                    writer.writeUTF(String.valueOf(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        unitController.onServerExit();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
