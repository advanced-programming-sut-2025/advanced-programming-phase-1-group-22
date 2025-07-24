package io.github.some_example_name.common.utils;

import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;

public class HibernateUtil {
    @Getter
    public static  EntityManagerFactory entityManagerFactory;

    static {
       // entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }
}
