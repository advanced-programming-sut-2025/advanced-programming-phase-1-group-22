package repository;

import jakarta.persistence.*;
import model.User;
import utils.HibernateUtil;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
//    public static final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
//    private final EntityManager entityManager = emf.createEntityManager();

    @Override
    public Optional<User> save(User user) {
//        EntityManager entityManager = emf.createEntityManager();
//        EntityTransaction transaction = null;
//        try {
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//            entityManager.persist(user);
//            transaction.commit();
//            return Optional.of(user);
//        } catch (Exception e) {
//            if (transaction != null && transaction.isActive()) {
//                transaction.rollback();
//            }
//            System.err.println("something went wrong in saveUser in UserRepositoryImpl: " + e.getMessage());
//            return Optional.empty();
//        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(int id) {
//        try {
//            entityManager.getTransaction().begin();
//            User user = entityManager.find(User.class, id);
//            entityManager.getTransaction().commit();
//            return Optional.of(user);
//        } catch (Exception e) {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//            System.err.println("something went wrong in findUserById in UserRepositoryImpl: " + e.getMessage());
//        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
//        EntityManager entityManager = emf.createEntityManager();
//        try {
//            TypedQuery<User> query = entityManager.createQuery(
//                    "SELECT u FROM User u WHERE u.username = ?1", User.class);
//            query.setParameter(1, username);
//
//            User user = query.getSingleResult();
//            return Optional.of(user);
//        } catch (NoResultException e) {
//            return Optional.empty();
//        } catch (NonUniqueResultException e) {
//            throw new IllegalStateException("Multiple users with same username: " + username);
//        }
        return Optional.empty();
    }

    @Override
    public Optional<User> deleteById(int id) {
//        EntityManager entityManager = emf.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            User person = entityManager.find(User.class, id);
//            entityManager.remove(person);
//            entityManager.getTransaction().commit();
//            return Optional.of(person);
//        } catch (Exception e) {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//            System.err.println("something went wrong in delete in UserRepositoryImpl: " + e.getMessage());
//        }
        return Optional.empty();
    }

    @Override
    public Optional changePassword(String username, String newPassword) {
//        EntityManager entityManager = emf.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            Optional<User> byUsername = findByUsername(username);
//            if (byUsername.isPresent()) {
//                User foundUser = byUsername.get();
//                foundUser.setPassword(newPassword);
//                entityManager.merge(foundUser);
//                entityManager.getTransaction().commit();
//                return Optional.of(foundUser);
//            }
//        } catch (Exception e) {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//            System.err.println("something went wrong in updatePassword in UserRepositoryImpl: " + e.getMessage());
//        }
        return Optional.empty();
    }

    @Override
    public Optional changeEmail(String username, String newEmail) {
//        EntityManager entityManager = emf.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            Optional<User> byUsername = findByUsername(username);
//            if (byUsername.isPresent()) {
//                User foundUser = byUsername.get();
//                foundUser.setEmail(newEmail);
//                entityManager.merge(foundUser);
//                entityManager.getTransaction().commit();
//                return Optional.of(foundUser);
//            }
//        } catch (Exception e) {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//            System.err.println("something went wrong in updateEmail in UserRepositoryImpl: " + e.getMessage());
//        }
        return Optional.empty();
    }

    @Override
    public Optional changeUsername(String username, String newUsername) {
//        EntityManager entityManager = emf.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            Optional<User> byUsername = findByUsername(username);
//            if (byUsername.isPresent()) {
//                User foundUser = byUsername.get();
//                foundUser.setUsername(newUsername);
//                entityManager.merge(foundUser);
//                entityManager.getTransaction().commit();
//                return Optional.of(foundUser);
//            }
//        } catch (Exception e) {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//            System.err.println("something went wrong in updateUsername in UserRepositoryImpl: " + e.getMessage());
//        }
        return Optional.empty();
    }

    @Override
    public Optional changeNickname(String username, String newNickname) {
//        EntityManager entityManager = emf.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            Optional<User> byUsername = findByUsername(username);
//            if (byUsername.isPresent()) {
//                User foundUser = byUsername.get();
//                foundUser.setNickname(newNickname);
//                entityManager.merge(foundUser);
//                entityManager.getTransaction().commit();
//                return Optional.of(foundUser);
//            }
//        } catch (Exception e) {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//            System.err.println("something went wrong in updateNickName in UserRepositoryImpl: " + e.getMessage());
//        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getHIghestEarnedMoney(String username) {
//        Optional<User> byUsername = findByUsername(username);
//        if (byUsername.isPresent()) {
//            User foundUser = byUsername.get();
//            return Optional.of(foundUser.getHighestMoneyEarned());
//        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getPlayedGamse(String username) {
//        Optional<User> byUsername = findByUsername(username);
//        if (byUsername.isPresent()) {
//            User foundUser = byUsername.get();
//            return Optional.of(foundUser.getNumberOfPlayedGames());
//        }
        return Optional.empty();
    }

}
