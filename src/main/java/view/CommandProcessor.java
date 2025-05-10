package view;

import command.CommandClass;
import model.records.Response;

import java.util.Map;
import java.util.function.Function;

public interface CommandProcessor {
    default void processCommand(String command) {
        var map = getFunctionsMap();
        for (var pair : map.entrySet()) {
            CommandClass commandProcessor = pair.getKey();
            if (commandProcessor.matches(command)) {
                Function<String[], Response> function = pair.getValue();
               try {
                    Response response = function.apply(commandProcessor.getParameters(command));
                    if (response.message() != null && !response.message().isEmpty())
                        System.out.println(response.message());
                } catch (Exception e) {
//                    System.out.println(e.getMessage());
                   e.printStackTrace();
                }
                return;
            }
        }
        System.out.println("invalid command!");
    }

    Map<CommandClass, Function<String[], Response>> getFunctionsMap();
}
