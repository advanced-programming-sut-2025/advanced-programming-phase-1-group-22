package io.github.some_example_name.view;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.model.records.Response;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public interface CommandProcessor {
    default void processCommand(String command, Consumer<Response> output) {
        var map = getFunctionsMap();
        for (var pair : map.entrySet()) {
            CommandClass commandProcessor = pair.getKey();
            if (commandProcessor.matches(command)) {
                Function<String[], Response> function = pair.getValue();
               try {
                    Response response = function.apply(commandProcessor.getParameters(command));
                    if (response.message() != null && !response.message().isEmpty())
                        output.accept(response);
                       // System.out.println(response.message());
                } catch (Exception e) {
                   output.accept(new Response(e.getMessage()));
                    //System.out.println(e.getMessage());
                }
                return;
            }
        }
        output.accept(new Response("invalid command!"));
       // System.out.println("invalid command!");
    }

    Map<CommandClass, Function<String[], Response>> getFunctionsMap();
}
