package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(Gson gson, TaskManager manager) {
        super(gson, manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = Arrays.stream(exchange.getRequestURI().getPath().split("/")).filter(Predicate.not(String::isEmpty)).toArray(String[]::new)[0];
        if (!path.equals("tasks")) {
            sendError(exchange, "Некорректный запрос!", 400);
            return;
        }
        String methodType = exchange.getRequestMethod();
        Optional<Integer> taskId = getId(exchange);
        if (taskId.isEmpty()) {
            if (isGet(methodType)) {
                sendSuccessfullyDefault(exchange, gson.toJson(manager.getTasks()));
            } else if (isPost(methodType)) {
                parseTaskPostRequest(exchange);
            } else {
                sendNotImplemented(exchange);
            }
        } else {
            if (isGet(methodType)) {
                getTaskById(taskId, exchange);
            } else if (isDelete(methodType)) {
                deleteTaskById(taskId, exchange);
            } else {
                sendNotImplemented(exchange);
            }
        }
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

    private void createTask(HttpExchange exchange, Task task) throws IOException {
        int result = manager.addNewTask(task);
        if (result != 0) {
            sendSuccessfully(exchange, "Задача создана успешно!", 201);
        } else {
            sendTimeTaken(exchange);
        }
    }

    private void updateTask(HttpExchange exchange, Task task) throws IOException {
        manager.updateTask(task);
        sendSuccessfully(exchange, "Задача обновлена успешно!", 201);
    }

    private void getTaskById(Optional<Integer> taskId, HttpExchange exchange) throws IOException {
        Task optionalTask = manager.findTaskById(taskId.get());

        sendSuccessfullyDefault(exchange, gson.toJson(optionalTask.getId()));

    }

    private void deleteTaskById(Optional<Integer> taskId, HttpExchange exchange) throws IOException {
        manager.deleteTaskById(taskId.get());
        sendSuccessfullyDefault(exchange, "Задача удалена успешно!");
    }
}


