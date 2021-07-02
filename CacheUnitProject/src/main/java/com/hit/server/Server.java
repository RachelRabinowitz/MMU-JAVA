package main.java.com.hit.server;

import main.java.com.hit.services.CacheUnitController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class receives connection requests and sends them to handler class.
 */
public class Server extends java.lang.Object implements PropertyChangeListener, Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private boolean flag;
    private boolean socketFlag;
    private CacheUnitController controller;

    public Server() {
        try {
            socket = new Socket();
            serverSocket = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = new CacheUnitController();
        flag = false;
        socketFlag = false;
    }

    /**
     * Handles signals from the CLI
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals("start") && !flag) {
            flag = true;
            socketFlag = true;
            new Thread(this).start();
        } else if (evt.getNewValue().equals("shutdown")) {
            socketFlag = false;
            try {
                socket.close();
                controller.onServerExit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            flag = false;
        }
    }

    @Override
    public void run() {
        while (flag) {
            try {
                socket = serverSocket.accept();
                if(!socketFlag){
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socketFlag) {
                HandleRequest handleRequest = new HandleRequest(socket, controller);
                new Thread(handleRequest).start();
            }
        }
    }
}
