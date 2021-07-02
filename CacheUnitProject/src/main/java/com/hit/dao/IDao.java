package main.java.com.hit.dao;


public interface IDao<ID extends java.io.Serializable, T> {

    void save(T entity) throws Exception;

    void delete(T entity);

    T find(ID id) throws Exception;
}
