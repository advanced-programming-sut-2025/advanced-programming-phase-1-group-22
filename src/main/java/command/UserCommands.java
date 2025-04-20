package command;

public class UserCommands extends CommandClass {
    public static final UserCommands USERNAME = new UserCommands("^[a-zA-Z0-9-]+$");
    public static final UserCommands EMAIL = new UserCommands("^(?=[A-Za-z0-9][A-Za-z0-9._-]*[A-Za-z0-9]@)(?!.*\\.\\.)[A-Za-z0-9._-]+@(?!-)[A-Za-z0-9-]+(\\.[A-Za-z]{2,})+$");
    public static final UserCommands password = new UserCommands("^[A-Za-z0-9?><,\"';:\\\\/|\\[\\]}{+=)(*&^%$#!-]+$");

    UserCommands(String regex) {
        super(regex);
    }
}
