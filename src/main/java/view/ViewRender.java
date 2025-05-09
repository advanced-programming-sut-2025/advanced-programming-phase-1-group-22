package view;

import model.Game;
import model.records.Response;
import save.GameSaver;
import utils.App;
import variables.Session;
import view.mainMenu.ExitMenu;

import java.io.IOException;
import java.util.Scanner;

public class ViewRender {
    private static Scanner input = new Scanner(System.in);

    public static Response getResponse() {
        String response = input.nextLine();
        return new Response(response);
    }

    public static void showResponse(Response response) {
        System.out.println(response.message());
    }

    public void run() throws IOException {

        Menu menu;
        while ((menu = Session.getCurrentMenu()) != Menu.EXIT) {
            menu.checkCommand(input);
        }
//        GameSaver.save(App.getInstance().getCurrentGame(), "saved_game");

    }
}
