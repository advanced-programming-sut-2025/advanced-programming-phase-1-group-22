package view;

import model.records.Response;
import variables.Session;
import view.mainMenu.ExitMenu;

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
    }
}
