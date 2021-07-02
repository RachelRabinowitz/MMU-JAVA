package main.java.com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;

/**
 * This class implements command-line interface.
 */
public class CLI extends java.lang.Object
        implements Runnable {

    private Scanner reader;
    private DataOutputStream writer;
    private PropertyChangeSupport listeners;

    public CLI(InputStream in, OutputStream out) {
        reader = new Scanner(in);
        writer = new DataOutputStream(out);
        listeners = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.removePropertyChangeListener(pcl);
    }

    public void write(String string) {
        try {
            writer.writeUTF(string);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String request;
        write("Please insert a command\n");
        while (true) {
            try {
                request = reader.nextLine().toUpperCase();
                switch (request) {
                    case ("EXIT"):
                    case ("STOP"):
                    case("SHUTDOWN"):
                        write("Shutdown server\n");
                        listeners.firePropertyChange("command", null, "shutdown");
                        try {
                            reader.close();
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    case ("START"):
                        write("Starting server.......\n");
                        listeners.firePropertyChange("command", null, "start");
                        break;
                    default:
                        writer.writeUTF("Not a valid command\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
