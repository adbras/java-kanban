package server;

import http.HttpTaskServer;
import com.google.gson.*;

import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.Status;
import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Путь /tasks")
public class HttpTaskManagerTasksTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
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

    @DisplayName("Удалить задачу")
    @Test
    public void testDeletingTaskUsingHttp() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Testing task 1",
                Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, JANUARY, 31, 12, 0));
        Task task2 = new Task("Task2", "Testing task 2",
                Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2025, JANUARY, 31, 12, 15));
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getTasks();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @DisplayName("Получить задачи успешно")
    @Test
    public void testGettingTaskListUsingHttpSuccessfully() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Description1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2022, JANUARY, 1, 0, 0));
        Task task2 = new Task("Task2", "Description2", Status.DONE, Duration.ofMinutes(10), LocalDateTime.of(2022, JANUARY, 1, 0, 20));
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> parsed = gson.fromJson(jsonArray, new EpicTypeToken().getType());
        assertEquals(200, response.statusCode());
        assertEquals(2, parsed.size(), "Некорректный размер списка");
    }

    @DisplayName("Некорректные запросы")
    @Test
    public void testUsingUnsupportedHttpRequests() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString("")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Обработан некорректный путь");
        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(501, response.statusCode(), "Данный метод не предусмотрен для запроса");

    }
}

class TaskTypeToken extends TypeToken<List<Task>> {
}