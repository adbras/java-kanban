package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.Task;


public class BaseHttpHandler implements HttpHandler {
    protected Gson gson;
    protected TaskManager manager;

    public BaseHttpHandler(Gson gson, TaskManager manager) {
        this.gson = gson;
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }

    protected void sendError(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public Optional<Integer> getId(HttpExchange exchange) {
        String[] pathParts = Arrays.stream(exchange.getRequestURI().getPath().split("/")).filter(Predicate.not(String::isEmpty)).toArray(String[]::new);
        try {
            return Optional.of(Integer.parseInt(pathParts[1]));
        } catch (ArrayIndexOutOfBoundsException exception) {
            return Optional.empty();
        }
    }

    public boolean isGet(String methodType) {
        return "GET".equals(methodType);
    }

    public boolean isPost(String methodType) {
        return "POST".equals(methodType);
    }

    public boolean isDelete(String methodType) {
        return "DELETE".equals(methodType);
    }

    protected void sendSuccessfullyDefault(HttpExchange h, String text) throws IOException {
        sendSuccessfully(h, text, 200);
    }

    protected void sendSuccessfully(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    private void parseTaskPostRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (jsonString.isEmpty()) {
            sendLengthRequired(exchange);
            return;
        }
        Task task = gson.fromJson(jsonString, Task.class);
        if (task.getId() == 0) {
            createTask(exchange, task);
        } else {
            updateTask(exchange, task);
        }
    }

    protected void sendLengthRequired(HttpExchange h) throws IOException {
        sendError(h, "Заполните тело запроса!", 411);
    }

    private void createTask(HttpExchange exchange, Task task) throws IOException {
        int result = manager.addNewTask(task);

        sendSuccessfully(exchange, "Задача создана успешно!", 201);

    }

    //обновить задачу
    private void updateTask(HttpExchange exchange, Task task) throws IOException {
        if (manager.findTaskById(task.getId()) == null) {
            sendTaskNotFound(exchange);
            return;
        }
        manager.updateTask(task);

        sendSuccessfully(exchange, "Задача обновлена успешно!", 201);

    }

    protected void sendTaskNotFound(HttpExchange h) throws IOException {
        sendError(h, "По этому id ничего не найдено!", 404);
    }

    protected void sendNotImplemented(HttpExchange h) throws IOException {
        sendError(h, "Некорректный метод запроса!", 501);
    }

    protected void sendTimeTaken(HttpExchange h) throws IOException {
        sendError(h, "Данное время выполнения уже занято!", 406);
    }

}