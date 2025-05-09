import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.persistence.EntityManagerFactory;
import model.Game;
import model.Salable;
import save.GameSaver;
import save.GameState;
import save.SalableKeyDeserializer;
import save.SalableKeySerializer;
import utils.App;
import utils.HibernateUtil;
import view.ViewRender;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        EntityManagerFactory entityManagerFactory = HibernateUtil.getEntityManagerFactory();
//        entityManagerFactory.createEntityManager();
//        String json = new GameState(App.getInstance().getCurrentGame()).toJson();
//        GameState state = GameState.fromJson(json);
        new ViewRender().run();
//        Game game = new Game(); // فرض بر اینکه مقداردهی شده
//        GameSaver.save(game, "saved_game.json");

        //Game loadedGame = GameSaver.load("saved_game.json");

    }
}