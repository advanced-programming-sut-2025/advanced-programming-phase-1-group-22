package io.github.some_example_name.model;

public record Notification<T, K>(NotificationType type, T data, K source) {
}
