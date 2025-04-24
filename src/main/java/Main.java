import jakarta.persistence.EntityManagerFactory;
import utils.HibernateUtil;
import view.ViewRender;

public class Main {
    public static void main(String[] args) {
//        EntityManagerFactory entityManagerFactory = HibernateUtil.getEntityManagerFactory();
//        entityManagerFactory.createEntityManager();
        new ViewRender().run();
    }
}