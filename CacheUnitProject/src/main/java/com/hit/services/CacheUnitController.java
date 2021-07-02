package main.java.com.hit.services;

import main.java.com.hit.dm.DataModel;

import java.util.ArrayList;

/**
 * Separation layer between CacheUnitService class and HandleRequest class
 * @param <T> Generic RAM and hard disk content type.
 */
public class CacheUnitController<T> extends java.lang.Object {

    CacheUnitService unitService;

    public CacheUnitController() {
        unitService = new CacheUnitService();
    }

    public ArrayList<Object> update(DataModel<T>[] dataModels) {
        return unitService.update(dataModels);
    }

    public ArrayList<Object> delete(DataModel<T>[] dataModels) {
        return unitService.delete(dataModels);

    }

    public ArrayList<Object> get(DataModel<T>[] dataModels) throws Exception {
        return unitService.get(dataModels);
    }

    public void onServerExit() {
        unitService.onServerExit();
    }
}
