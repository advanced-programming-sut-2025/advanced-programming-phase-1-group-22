package io.github.some_example_name.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;

public class HibernateUtil {
    @Getter
    public static  EntityManagerFactory entityManagerFactory;

    static {
       // entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }
}
