package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import model.User;
import utils.HibernateUtil;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    public static final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
    private final EntityManager entityManager = emf.createEntityManager();

    @Override
    public Optional<User> save(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("something went wrong in addUser in UserRepositoryImpl: " + e.getMessage());
        }
        User user1 = entityManager.find(User.class, user.getUsername());
        return Optional.ofNullable(user1);
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            entityManager.getTransaction().begin();
            User user = entityManager.find(User.class, id);
            entityManager.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("something went wrong in findUserById in UserRepositoryImpl: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            TypedQuery<User> userTypedQuery = entityManager.createQuery("from User s where s.username = ?1", User.class);
            userTypedQuery.setParameter(1, username);
            User singleResult = userTypedQuery.getSingleResult();
            entityManager.getTransaction().commit();
            return Optional.of(singleResult);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("something went wrong in findUserByUsername in UserRepositoryImpl:" + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> deleteById(int id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            User person = entityManager.find(User.class, id);
            entityManager.remove(person);
            entityManager.getTransaction().commit();
            return Optional.of(person);
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("something went wrong in delete in UserRepositoryImpl: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional changePassword(String username, String newPassword) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Optional<User> byUsername = findByUsername(username);
            if (byUsername.isPresent()) {
                User foundUser = byUsername.get();
                foundUser.setPassword(newPassword);
                entityManager.getTransaction().commit();
                return Optional.of(foundUser);
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("something went wrong in updatePassword in UserRepositoryImpl: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional changeEmail(String username, String newEmail) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Optional<User> byUsername = findByUsername(username);
            if (byUsername.isPresent()) {
                User foundUser = byUsername.get();
                foundUser.setEmail(newEmail);
                entityManager.getTransaction().commit();
                return Optional.of(foundUser);
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("something went wrong in updateEmail in UserRepositoryImpl: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional changeUsername(String username, String newUsername) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Optional<User> byUsername = findByUsername(username);
            if (byUsername.isPresent()) {
                User foundUser = byUsername.get();
                foundUser.setUsername(newUsername);
                entityManager.getTransaction().commit();
                return Optional.of(foundUser);
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("something went wrong in updateUsername in UserRepositoryImpl: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional changeNickname(String username, String newNickname) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Optional<User> byUsername = findByUsername(username);
            if (byUsername.isPresent()) {
                User foundUser = byUsername.get();
                foundUser.setNickname(newNickname);
                entityManager.getTransaction().commit();
                return Optional.of(foundUser);
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("something went wrong in updateNickName in UserRepositoryImpl: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getHIghestEarnedMoney(String username) {
        Optional<User> byUsername = findByUsername(username);
        if (byUsername.isPresent()) {
            User foundUser = byUsername.get();
            return Optional.of(foundUser.getHighestMoneyEarned());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getPlayedGamse(String username) {
        Optional<User> byUsername = findByUsername(username);
        if (byUsername.isPresent()) {
            User foundUser = byUsername.get();
            return Optional.of(foundUser.getNumberOfPlayedGames());
        }
        return Optional.empty();
    }

}
