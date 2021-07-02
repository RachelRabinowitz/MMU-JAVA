package main.java.com.hit.memory;

import main.java.com.hit.algorithm.IAlgoCache;
import main.java.com.hit.dm.DataModel;


/**
 * This class implement the IAlgoCache methods to memory page arrays.
 *
 * @param <T> page value type.
 */

public class CacheUnit<T> {
    private IAlgoCache<Long, DataModel<T>> algoCache;

    public CacheUnit(IAlgoCache<Long, DataModel<T>> algoCache) {
        this.algoCache = algoCache;
    }

    public DataModel<T>[] getDataModels(Long[] ids) {
        DataModel<T>[] reqPages = new DataModel[ids.length];
        DataModel<T> tempPage = null;
        for (int i = 0; i < ids.length; i++) {
            if (algoCache.getElement(ids[i]) == null) {
                tempPage = null;
            } else {
                if (algoCache.getElement(ids[i]) != null && algoCache.getElement(ids[i]).getContent() != null) {
                    tempPage = new DataModel(ids[i], algoCache.getElement(ids[i]).getContent());
                } else {
                    tempPage = null;
                }
            }
            reqPages[i] = tempPage;
        }
        return reqPages;
    }

    public DataModel<T>[] putDataModels(DataModel<T>[] dataModels) {
        DataModel<T>[] reqPages = new DataModel[dataModels.length];
        for (int i = 0; i < dataModels.length; i++) {
            if (dataModels[i] != null && dataModels[i].getContent() != null) {
                reqPages[i] = algoCache.putElement(dataModels[i].getId(), dataModels[i]);
            }
        }
        return reqPages;
    }

    public void removeDataModels(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            algoCache.removeElement(ids[i]);
        }
    }
}
