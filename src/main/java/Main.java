import jakarta.persistence.EntityManagerFactory;
import save.GameState;
import utils.App;
import utils.HibernateUtil;
import view.ViewRender;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = HibernateUtil.getEntityManagerFactory();
        entityManagerFactory.createEntityManager();
        String json = new GameState(App.getInstance().getCurrentGame()).toJson();
        GameState state = GameState.fromJson(json);
        new ViewRender().run();
    }
}