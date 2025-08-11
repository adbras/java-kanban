package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(Gson gson, TaskManager manager) {
        super(gson, manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = Arrays.stream(exchange.getRequestURI().getPath()
                        .split("/"))
                .filter(Predicate.not(String::isEmpty))
                .toArray(String[]::new)[0];

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
                Task task = parseTaskPostRequest(exchange);
                createOrUpdate(exchange, task);
            } else {
                sendNotImplemented(exchange);
            }
        } else {
            if (isGet(methodType)) {
                getTaskById(Optional.of(taskId.get()), exchange);
            } else if (isDelete(methodType)) {
                deleteTaskById(Optional.of(taskId.get()), exchange);
            } else {
                sendNotImplemented(exchange);
            }
        }
    }

    private void updateTask(HttpExchange exchange, Task task) throws IOException {
        try {
            manager.updateTask(task);
            sendSuccessfully(exchange, "Задача обновлена успешно!", 201);
        } catch (IllegalArgumentException e) {
            sendError(exchange, e.getMessage(), 409);
        }
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

