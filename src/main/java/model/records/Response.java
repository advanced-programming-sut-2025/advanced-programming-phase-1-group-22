package model.records;

public record Response(String message) {

    public static Response empty() {
        return new Response("");
    }
}
