package view;

import variables.Session;
import view.mainMenu.ExitMenu;

import java.util.Scanner;

public class ViewRender {
    public void run() {
        Scanner input = new Scanner(System.in);
        Menu menu;
        while ((menu = Session.getCurrentMenu()) != Menu.EXIT) {
            menu.checkCommand(input);
        }
    }
}
