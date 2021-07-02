package main.java.com.hit.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.com.hit.dm.DataModel;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * This class manages the removal and insertion of hard disk memory pages.
 *
 * @param <T> Generic page value type.
 */

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

    private String filePath;
    private int capacity;
    private List<DataModel<T>> hardDisk;

    public DaoFileImpl(String filePath) throws Exception {
        this.filePath = filePath;
        this.capacity = 1024;
        readFile();
    }

    public DaoFileImpl(String filePath, int capacity) throws Exception {
        this.filePath = filePath;
        this.capacity = capacity;
        readFile();
    }

    public List<DataModel<T>> getHardDisk() {
        return hardDisk;
    }

    public void setHardDisk(List<DataModel<T>> hardDisk) {
        this.hardDisk = hardDisk;
    }

    @Override
    public void save(DataModel<T> entity) {
        if (entity == null) {
            return;
        }
        for (int i = 0; i < hardDisk.size(); i++) {
            if (hardDisk.get(i).getId().equals(entity.getId())) {
                System.out.println("A page with this ID already exists, Replace content..");
                hardDisk.remove(i);
            }
        }
        if (hardDisk.size() < capacity) {
            hardDisk.add(entity);
            writeFile();
        } else {
            System.out.println("Hard disk is full");
        }
    }

    @Override
    public void delete(DataModel<T> entity) {
        hardDisk.removeIf(item -> item.getId().equals(entity.getId()) &&
                item.getContent().equals(entity.getContent()));
        writeFile();
    }

    @Override
    public DataModel<T> find(Long aLong) throws Exception {
        if (aLong == null) {
            throw new Exception("Please enter a valid ID");
        }
        for (int i = 0; i < hardDisk.size(); i++) {
            if (hardDisk.get(i).getId().equals(aLong)) {
                return hardDisk.get(i);
            }
        }
        System.out.println("Page not found");
        return null;
    }

    //A function designed to read information from a file into an array simulates hard memory
    private void readFile() {
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader(filePath);
            hardDisk = new ArrayList<>(capacity);
            Type listType = new TypeToken<ArrayList<DataModel<T>>>() {
            }.getType();
            ArrayList<DataModel<T>> tempHardDisk = gson.fromJson(fileReader, listType);
            if (tempHardDisk != null) {
                hardDisk = tempHardDisk;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //A function designed to write information from an array simulates the hard memory into a file
    private void writeFile() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(hardDisk, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
