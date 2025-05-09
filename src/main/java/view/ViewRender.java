package view;

import model.records.Response;
import save3.GameSaver;
import utils.App;
import variables.Session;


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

    public void run() {

        Menu menu;
        while ((menu = Session.getCurrentMenu()) != Menu.EXIT) {
            menu.checkCommand(input);
        }
        try {
            GameSaver.saveGame(App.getInstance().getCurrentGame(), "game.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
