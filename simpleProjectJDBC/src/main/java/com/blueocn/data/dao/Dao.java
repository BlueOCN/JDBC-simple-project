package main.java.com.blueocn.data.dao;

import java.util.List;
import java.util.Optional;

public interface Dao <T, Id extends Integer> {
    List<T> getAll();
    T create(T entity);
    Optional<T> getOne(Id id);
    T update(T entity);
    void delete(Id id);
}
