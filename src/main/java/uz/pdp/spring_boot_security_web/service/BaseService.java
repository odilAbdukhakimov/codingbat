package uz.pdp.spring_boot_security_web.service;

import java.util.List;

public interface BaseService<T, T1> {
    List<T> getList();

    T getById(int id);

    boolean delete(int id);

    T add(T1 t1);
}
