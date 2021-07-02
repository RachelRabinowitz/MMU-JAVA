package main.java.com.hit.view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class implements GUI of the MMU using.
 */
public class CacheUnitView extends java.lang.Object {

    private JFrame frame;
    private JLabel label;
    private JPanel panel;
    private PropertyChangeSupport listeners;
    private LoadRequest loadRequest;
    private ShowStatistics statistics;
    private String capacity;
    private String algorithm;
    private String totalRequest;
    private String totalDataModels;
    private String swapsDataModels;

    public CacheUnitView() {
        frame = new JFrame("CacheUnitUi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label = new JLabel("");
//        label.setPreferredSize(new Dimension(140, 50));
        listeners = new PropertyChangeSupport(this);
        loadRequest = new LoadRequest();
        statistics = new ShowStatistics();
        capacity = "";
        algorithm = "";
        totalRequest = "0";
        totalDataModels = "";
        swapsDataModels = "";
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setTotalRequest(String totalRequest) {
        this.totalRequest = totalRequest;
    }

    public void setTotalDataModels(String totalDataModels) {
        this.totalDataModels = totalDataModels;
    }

    public void setSwapsDataModels(String swapsDataModels) {
        this.swapsDataModels = swapsDataModels;
    }

    public void start() {
        loadRequest.setOpaque(true);
        statistics.setOpaque(true);
        frame.add(loadRequest, BorderLayout.LINE_START);
        frame.add(statistics, BorderLayout.CENTER);
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 180));
        panel.add(label, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.AFTER_LAST_LINE);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.setSize(500, 400);
        frame.setVisible(true);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.removePropertyChangeListener(pcl);
    }

    public <T> void updateUIData(T t) {
        panel.setBackground(Color.GRAY);
        label.setText((String) t);
        label.setForeground(Color.WHITE);
    }

    /**
     * This class implements button to load json file.
     */
    public class LoadRequest extends JPanel implements ActionListener {

        protected JButton button;
        private JFileChooser fileChooser;
        private String request;

        public LoadRequest() {
            super(new FlowLayout());
            button = new JButton("📂 Load a request");
            button.setBorder(null);
            button.setFocusPainted(false);
            button.setVerticalTextPosition(AbstractButton.BOTTOM);
            button.setHorizontalTextPosition(AbstractButton.CENTER);
            button.setPreferredSize(new Dimension(140, 50));
            add(button);
            button.addActionListener(this);
            fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "JSON");
            fileChooser.setFileFilter(filter);
            File file = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(file);
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileChooser.showOpenDialog(this) == fileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                byte[] data = new byte[(int) file.length()];
                try {
                    fis.read(data);
                    fis.close();
                    request = new String(data, "UTF-8");
                    listeners.firePropertyChange("command", null, request);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
    /**
     * This class implements a button to view server statistics.
     */

    public class ShowStatistics extends JPanel implements ActionListener {

        protected JButton button;


        public ShowStatistics() {
            super(new FlowLayout());
            button = new JButton("📊 Show statistics");
            button.setBorder(null);
            button.setFocusPainted(false);
            button.setVerticalTextPosition(AbstractButton.BOTTOM);
            button.setHorizontalTextPosition(AbstractButton.CENTER);
            button.setPreferredSize(new Dimension(140, 50));
            add(button);
            button.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String info;
            if (totalRequest.equals("0")) {
                info = "No server requests have been received yet";
            }
            else {
                info = "<html><br>Capacity: " + capacity
                        + "<br>Algorithm: " + algorithm
                        + "<br>Total number of requests: " + totalRequest
                        + "<br>Total number of DataModels (GET/DELETE/UPDATE requests): " + totalDataModels
                        + "<br>Total number of DataModel swaps (from Cache to Disk):" + swapsDataModels + "</html>";
            }
            updateUIData(info);
        }
    }

}
