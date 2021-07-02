package main.java.com.hit.client;

import main.java.com.hit.view.CacheUnitView;

import java.beans.PropertyChangeEvent;

/**
 * Separation layer between GUI event and CacheUnitClient.
 */
public class CacheUnitClientObserver
        extends java.lang.Object
        implements java.beans.PropertyChangeListener {

    private CacheUnitClient client;
    private String requestStatus;
    private CacheUnitView view;

    public CacheUnitClientObserver() {
        client = new CacheUnitClient();
        requestStatus = "";
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        view = (CacheUnitView) evt.getSource();
        requestStatus = client.send((String) evt.getNewValue());
        String[] strings = client.getStrings();
        if (requestStatus.equals("success")) {
            view.setCapacity(strings[0]);
            view.setAlgorithm(strings[1]);
            view.setTotalRequest(strings[2]);
            view.setTotalDataModels(strings[3]);
            view.setSwapsDataModels(strings[4]);
        }
        view.updateUIData(requestStatus);
    }
}
