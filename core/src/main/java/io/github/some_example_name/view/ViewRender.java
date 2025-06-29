package io.github.some_example_name.view;

import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.saveGame.GameSaver;
import io.github.some_example_name.saveGame.GameSerializer;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.variables.Session;

import java.util.Scanner;

public class ViewRender {
    private final Scanner input = new Scanner(System.in);  // Instance variable

    public Response getResponse() {
        String response = input.nextLine();
        return new Response(response);
    }

    public void showResponse(Response response) {
        System.out.println(response.message());
    }

    public void run() {
        Menu menu;
        while ((menu = Session.getCurrentMenu()) != Menu.EXIT) {
            menu.checkCommand(input);
        }
//        ObjectMapper mapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addKeySerializer(Salable.class, new SalableKeySerializer());
//        module.addKeyDeserializer(Salable.class, new SalableKeyDeserializer());
//        mapper.registerModule(module);
//        Map<Salable, Integer> data = new HashMap<>();
//        data.put(new AnimalProduct(AnimalProductType.MILK), 5);
//        data.put(new AnimalProduct(AnimalProductType.BIG_MILK), 3);
//        String json = null;
//        try {
//            json = mapper.writeValueAsString(data);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Serialized JSON:\n" + json);
//
//
//        Map<Salable, Integer> result = null;
//        try {
//            result = mapper.readValue(json, new TypeReference<Map<Salable, Integer>>() {});
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Deserialized Map:");
//        result.forEach((k, v) -> System.out.println(k + " => " + v));
    }
}

