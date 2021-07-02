package main.test.memory;

import main.java.com.hit.algorithm.MFUAlgoCacheImpl;
import main.java.com.hit.dao.DaoFileImpl;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.memory.CacheUnit;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Asserting the correctness of cache unit and daoFile algorithms.
 */

public class CacheUnitTest {

    public static DaoFileImpl daoFile;
    private static DataModel dataModel;
    private static DataModel dataModel2;
    private static DataModel dataModel3;
    private static DataModel dataModel4;
    private static DataModel dataModel5;

    public static CacheUnit cacheUnit;
    public static MFUAlgoCacheImpl mfuOb;


    @BeforeClass
    public static void beforeTest() throws Exception {
        daoFile = new DaoFileImpl("src/main/resources/datasource.json");
        dataModel = new DataModel(5L, "hi");
        dataModel2 = new DataModel(6L, "hi h");
        dataModel3 = new DataModel(7L, "hi h");
        dataModel4 = new DataModel(4L, "hi h");
        dataModel5 = new DataModel(7L, "hih h");

        mfuOb = new MFUAlgoCacheImpl(1024);
        cacheUnit = new CacheUnit(mfuOb);


    }

    @Test
    public void daoTest() throws Exception {
        daoFile.save(dataModel);
        daoFile.save(dataModel2);
        daoFile.save(dataModel3);
        daoFile.save(dataModel4);
        Assert.assertEquals(dataModel3, daoFile.find(7L));
        daoFile.save(dataModel5);

        Assert.assertNotEquals(dataModel3, daoFile.find(7L));
        Assert.assertEquals(dataModel5, daoFile.find(7L));

        Assert.assertNull(daoFile.find(9L));
        Assert.assertNotNull(daoFile.find(5L));

        Assert.assertEquals(dataModel4, daoFile.find(4L));
        Assert.assertEquals(dataModel2, daoFile.find(6L));

        Assert.assertNotNull(daoFile.find(7L));
        daoFile.delete(dataModel5);
        Assert.assertNull(daoFile.find(7L));
        daoFile.delete(dataModel3);
        Assert.assertNull(daoFile.find(7L));

        Assert.assertNotNull(daoFile.find(6L));
        daoFile.delete(dataModel2);
        Assert.assertNull(daoFile.find(6L));
    }

    @Test
    public void cacheUnitTest() {

        Long[] ids = new Long[15];
        DataModel[] dataModels = new DataModel[15];
        for (int i = 0; i < 15; i++) {
            ids[i] = Long.valueOf(i);
            dataModels[i] = new DataModel(Long.valueOf(i), i);
        }

        cacheUnit.putDataModels(dataModels);
        Assert.assertEquals(cacheUnit.getDataModels(ids)[0], dataModels[0]);
        cacheUnit.removeDataModels(ids);
        Assert.assertNull(cacheUnit.getDataModels(ids)[0]);
    }

}
