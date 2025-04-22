package command;

public class AccountCommands extends CommandClass {
    public static final AccountCommands LOGIN_COMMANDS = new AccountCommands("^login\\s+-u\\s+(\\S+)\\s+-p\\s+(\\S+)(?:\\s+-stay-logged-in)?$");
    public static final AccountCommands REGISTER_COMMANDS = new AccountCommands("register\\s+-u\\s+(.*?)\\s+-p\\s+(.*?)\\s+(.*?)\\s+-n(.*?)\\s+-e\\s+(.*?)\\s+-g\\s+(.*)");
    public static final AccountCommands REGISTER_COMMANDS_RANDOM_PASSWORD = new AccountCommands("register\\s+-u\\s+(.*)\\s+-p\\s+random\\s+-n(.*)\\s+-e\\s+(.*)\\s+-g\\s+(.*)");
    public static final AccountCommands CHANGE_USERNAME = new AccountCommands("change\\s+username\\s+-u\\s+(.*)");
    public static final AccountCommands CHANGE_PASSWORD = new AccountCommands("change\\s+password\\s+-p(.*)\\s+-o\\s+(.*)");
    public static final AccountCommands CHANGE_EMAIL = new AccountCommands("change\\s+email\\s+-e\\s+(.*)");
    public static final AccountCommands CHANGE_NICKNAME = new AccountCommands("change\\s+nickname\\s+-n\\s+(.*)");
    public static final AccountCommands USER_INFO = new AccountCommands("user\\s+info");
    public static final AccountCommands PICK_QUESTION = new AccountCommands("pick\\s+question\\s+-q\\s+(.*)\\s+-a\\s+(.*)\\s+-c\\s+(.*)");
    public static final AccountCommands FORGET_PASSWORD = new AccountCommands("forget\\s+password\\s+-u\\s+(.*)");
    public static final AccountCommands ANSWER = new AccountCommands("answer\\s+-a\\s+(.*)");
    public static final AccountCommands ENTER_MENU = new AccountCommands("menu\\s+enter\\s+(.*)");
    public static final AccountCommands EXIT = new AccountCommands("menu\\s+exit");
    public static final AccountCommands SHOW_CURRENT_MENU = new AccountCommands("show\\s+current\\s+menu");
    public static final AccountCommands USER_LOGOUT = new AccountCommands("user\\s+logout");

    AccountCommands(String regex) {
        super(regex);
    }
}
