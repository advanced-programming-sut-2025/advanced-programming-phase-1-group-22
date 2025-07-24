package io.github.some_example_name.common.model.records;

public record Response(String message, boolean shouldBeBack) {
    public Response(String message, boolean shouldBeBack) {
        this.message = message;
        this.shouldBeBack = shouldBeBack;
    }

    public Response(String message) {
        this(message, false);
    }

    public static Response empty() {
        return new Response("", false);
    }

    @Override
    public String toString() {
        return message;
    }
}
