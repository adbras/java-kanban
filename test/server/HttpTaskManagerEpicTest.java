package server;

import HTTP.HttpTaskServer;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Путь /epics")
public class HttpTaskManagerEpicTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerEpicTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    @DisplayName("Получить эпики")
    @Test
    public void testGettingEpicListUsingHttp() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "epic1", "desc1", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Epic epic2 = new Epic(2, "epic2", "desc2", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Epic> parsed = gson.fromJson(jsonArray, new EpicTypeToken().getType());
        assertEquals(200, response.statusCode());

        assertEquals(2, parsed.size(), "Некорректный размер списка");
    }

    @DisplayName("Добавить эпик")
    @Test
    public void testAddingEpicUsingHttp() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "epic1", "desc1", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        String taskJson = gson.toJson(epic1);
        manager.addNewEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = manager.getEpics();
        assertNotNull(tasksFromManager, "Эпики не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals("epic1", tasksFromManager.get(0).getTaskName(), "Некорректное имя эпика");
    }


    @DisplayName("Удалить эпик")
    @Test
    public void testDeletingEpicUsingHttp() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "epic1", "desc1", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Epic epic2 = new Epic(1, "epic2", "desc2", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> tasksFromManager = manager.getEpics();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество эпиков");
    }

    @DisplayName("Некорректные запросы")
    @Test
    public void testUsingUnsupportedHttpRequests() throws IOException, InterruptedException {
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString("")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Обработан некорректный путь");
        url = URI.create("http://localhost:8080/epics");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(501, response.statusCode(), "Данный метод не предусмотрен для запроса");

    }
}

class EpicTypeToken extends TypeToken<List<Epic>> {
}