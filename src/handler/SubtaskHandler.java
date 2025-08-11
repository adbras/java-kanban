package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class SubtaskHandler extends BaseHttpHandler {
    public SubtaskHandler(Gson gson, TaskManager manager) {
        super(gson, manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = Arrays.stream(exchange.getRequestURI().getPath().split("/")).filter(Predicate.not(String::isEmpty)).toArray(String[]::new)[0];
        if (!path.equals("subtasks")) {
            sendError(exchange, "Некорректный запрос!", 400);
            return;
        }
        String methodType = exchange.getRequestMethod();
        Optional<Integer> subtaskId = getId(exchange);
        if (subtaskId.isEmpty()) { //"/subtasks"
            if (isGet(methodType)) {
                sendSuccessfullyDefault(exchange, gson.toJson(manager.getSubtasks()));
            } else if (isPost(methodType)) {
                parseSubtaskPostRequest(exchange);
            } else {
                sendNotImplemented(exchange);
            }
        } else {
            if (isGet(methodType)) {
                getSubtaskById(exchange, subtaskId);
            } else if (isDelete(methodType)) {
                deleteSubtaskById(exchange, subtaskId);
            } else {
                sendNotImplemented(exchange);
            }
        }
    }

    private void deleteSubtaskById(HttpExchange exchange, Optional<Integer> subtaskId) throws IOException {
        manager.deleteSubtaskById(subtaskId.get());
        sendSuccessfullyDefault(exchange, "Подзадача удалена успешно!");
    }

    private void getSubtaskById(HttpExchange exchange, Optional<Integer> subtaskId) throws IOException {
        Subtask subtask = manager.findSubtaskById(subtaskId.get());

        sendSuccessfullyDefault(exchange, gson.toJson(subtask.getId()));

    }

    private void parseSubtaskPostRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (jsonString.isEmpty()) {
            sendLengthRequired(exchange);
            return;
        }
        Subtask subTask = gson.fromJson(jsonString, Subtask.class);
        if (subTask.getId() == 0) {
            createSubtask(exchange, subTask);
        } else {
            updateSubtask(exchange, subTask);
        }
    }

    private void createSubtask(HttpExchange exchange, Subtask subTask) throws IOException {

        int result = manager.addNewSubtask(subTask);
        if (result != 0) {
            sendSuccessfully(exchange, "Подзадача создана успешно!", 201);
        } else {
            sendTimeTaken(exchange);
        }
    }

    private void updateSubtask(HttpExchange exchange, Subtask subTask) throws IOException {
        try {
            manager.updateSubtask(subTask);
            sendSuccessfully(exchange, "Подзадача обновлена успешно!", 201);
        } catch (IllegalArgumentException e) {
            sendError(exchange, e.getMessage(), 409);
        }
    }
}