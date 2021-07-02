package main.java.com.hit.services;

import main.java.com.hit.algorithm.LRUAlgoCacheImpl;
import main.java.com.hit.algorithm.MFUAlgoCacheImpl;
import main.java.com.hit.algorithm.Random;
import main.java.com.hit.dao.DaoFileImpl;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.memory.CacheUnit;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class responsible for coordinating information management on the hard disk and RAM.
 * @param <T> Generic RAM and hard disk content type.
 */
public class CacheUnitService<T> extends java.lang.Object {

    private DaoFileImpl daoFile;
    private final CacheUnit cacheUnit;
    private LRUAlgoCacheImpl<Long, DataModel<T>> algoCache;
    private ArrayList list;
    private int totalRequest;
    private int totalDataModels;
    private int swapsDataModels;

    public CacheUnitService() {
        try {
            daoFile = new DaoFileImpl("src/main/resources/datasource.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        algoCache = new LRUAlgoCacheImpl<>(10);
        cacheUnit = new CacheUnit(algoCache);
        list = new ArrayList();
        for (int i = 0; i < 6; i++) {
            list.add(0);
        }
        totalRequest = 0;
        totalDataModels = 0;
        swapsDataModels = 0;
    }

    /**
     * updates data.
     * @return if the update succeed.
     */
    public boolean basicUpdate(DataModel<T>[] dataModels) {
        totalRequest++;
        totalDataModels += dataModels.length;
        try {
            DataModel<T>[] tempDataModels = cacheUnit.putDataModels(dataModels);
            for (int i = 0; i < tempDataModels.length; i++) {
                if (tempDataModels[i] != null) {
                    daoFile.save(tempDataModels[i]);
                    swapsDataModels++;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * deletes data.
     * @return if the deletion succeed.
     */
    public boolean basicDelete(DataModel<T>[] dataModels) {
        totalRequest++;
        totalDataModels += dataModels.length;
        Long[] ids = new Long[dataModels.length];
        for (int i = 0; i < dataModels.length; i++) {
            daoFile.delete(dataModels[i]);
            ids[i] = dataModels[i].getId();
        }
        try {
            cacheUnit.removeDataModels(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return the requested information.
     */
    public DataModel<T>[] basicGet(DataModel<T>[] dataModels) throws Exception {
        totalRequest++;
        totalDataModels += dataModels.length;
        Long[] ids = new Long[dataModels.length];
        DataModel removedCache;
        for (int i = 0; i < dataModels.length; i++) {
            ids[i] = dataModels[i].getId();
        }
        DataModel<T>[] values = cacheUnit.getDataModels(ids);
        for (int i = 0; i < dataModels.length; i++) {
            if (values[i] == null) {
                if (daoFile.find(ids[i]) != null) {
                    removedCache = algoCache.putElement(ids[i], daoFile.find(ids[i]));
                    if (removedCache != null) {
                        daoFile.save(removedCache);
                        swapsDataModels++;
                    }
                }
                values[i] = daoFile.find(ids[i]);
            }
        }
        return values;
    }

    /**
     * @return update request result and statistics.
     */
    public ArrayList<Object> update(DataModel<T>[] dataModels) {
        list.set(0, basicUpdate(dataModels));
        statisticsData();
        return list;
    }

    /**
     * @return delete request result and statistics.
     */
    public ArrayList<Object> delete(DataModel<T>[] dataModels) {
        list.set(0, basicDelete(dataModels));
        statisticsData();
        return list;
    }

    /**
     * @return get request result and statistics.
     */
    public ArrayList<Object> get(DataModel<T>[] dataModels) throws Exception {
        list.set(0, basicGet(dataModels));
        statisticsData();
        return list;
    }

    /**
     * update statistics according to last request.
     */
    private void statisticsData() {
        list.set(1, algoCache.getCapacity());
        String algo = String.valueOf(algoCache.getClass());
        if (algo.contains("LRU")) {
            algo = "LRU";
        } else if (algo.contains("MFU")) {
            algo = "MFU";
        } else {
            algo = "Random";
        }
        list.set(2, algo);
        list.set(3, totalRequest);
        list.set(4, totalDataModels);
        list.set(5, swapsDataModels);
    }

    /**
     * saves ram to hard disk when client or server closes connection.
     */
    public void onServerExit() {
        Map<Long, DataModel<T>> tempRam = algoCache.getRam();
        for (Long key : tempRam.keySet()) {
            daoFile.save(tempRam.get(key));
        }
    }
}
