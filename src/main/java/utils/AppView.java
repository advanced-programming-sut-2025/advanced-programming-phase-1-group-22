package utils;

import java.util.Scanner;
import model.Menus;

public class AppView {

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        while (App.getInstance().getCurrentMenu() != Menus.Exit) {
            App.getInstance().getCurrentMenu().getMenu().checkCommand(scanner);
        }
    }
}