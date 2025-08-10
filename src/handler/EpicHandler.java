package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(Gson gson, TaskManager manager) {
        super(gson, manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = Arrays.stream(exchange.getRequestURI().getPath().split("/")).filter(Predicate.not(String::isEmpty)).toArray(String[]::new)[0];
        if (!path.equals("epics")) {
            sendError(exchange, "Некорректный запрос!", 400);
            return;
        }
        String methodType = exchange.getRequestMethod();
        Optional<Integer> epicId = getId(exchange);
        if (epicId.isEmpty()) { //"/epics"
            if (isGet(methodType)) {
                sendSuccessfullyDefault(exchange, gson.toJson(manager.getEpics()));
            } else if (isPost(methodType)) {
                parseEpicPostRequest(exchange);
            } else {
                sendNotImplemented(exchange);
            }
        } else { //"/epics/{id}"
            if (isGetSubtasks(exchange)) {
                getEpicSubtaskListById(exchange, epicId);
            } else if (isGet(methodType)) {
                getEpicById(exchange, epicId);
            } else if (isDelete(methodType)) {
                deleteEpicById(exchange, epicId);
            } else {
                sendNotImplemented(exchange);
            }
        }
    }

    private boolean isGetSubtasks(HttpExchange exchange) {
        String[] pathParts = Arrays.stream(exchange.getRequestURI().getPath().split("/")).filter(Predicate.not(String::isEmpty)).toArray(String[]::new);
        try {
            if (pathParts[2].equals("subtasks"))
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    private void deleteEpicById(HttpExchange exchange, Optional<Integer> epicId) throws IOException {
        manager.deleteEpicById(epicId.get());
        sendSuccessfullyDefault(exchange, "Эпик удален успешно!");
    }

    private void getEpicById(HttpExchange exchange, Optional<Integer> epicId) throws IOException {
        Epic epic = manager.findEpicById(epicId.get());

        sendSuccessfullyDefault(exchange, gson.toJson(epic.getId()));

    }

    private void getEpicSubtaskListById(HttpExchange exchange, Optional<Integer> epicId) throws IOException {
        Epic epic = manager.findEpicById(epicId.get());


        sendSuccessfullyDefault(exchange, gson.toJson(manager.findEpicById(epic.getId())));

    }

    private void parseEpicPostRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (jsonString.isEmpty()) {
            sendLengthRequired(exchange);
            return;
        }
        Epic epic = gson.fromJson(jsonString, Epic.class);
        if (epic.getId() == 0) {
            createEpic(exchange, epic);
        } else {
            updateEpic(exchange, epic);
        }
    }

    private void createEpic(HttpExchange exchange, Epic epic) throws IOException {
        int result = manager.addNewEpic(epic);
        if (result != 0) {
            sendSuccessfully(exchange, "Эпик создан успешно!", 201);
        } else {
            sendTimeTaken(exchange);
        }
    }

    private void updateEpic(HttpExchange exchange, Epic epic) throws IOException {

        manager.updateEpicStatus(epic);
        sendSuccessfully(exchange, "Эпик обновлен успешно!", 201);

    }
}