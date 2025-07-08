package io.github.some_example_name;

import jakarta.persistence.EntityManagerFactory;
import io.github.some_example_name.utils.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = HibernateUtil.getEntityManagerFactory();
        entityManagerFactory.createEntityManager();
    }
}
