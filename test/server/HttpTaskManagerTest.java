package server;

import HTTP.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import manager.Managers;
import manager.Status;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
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

@DisplayName("Пути /history и /prioritized")
public class HttpTaskManagerTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager taskManager = Managers.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTest() throws IOException, IOException {
    }

    @BeforeEach
    public void init() {

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
        taskServer.startServer();
        Task task1 = new Task("Task1", "Description1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2022, JANUARY, 1, 0, 20));
        Task task2 = new Task("Task2", "Description2", Status.DONE, Duration.ofMinutes(10), LocalDateTime.of(2022, JANUARY, 1, 0, 0));
        Epic epic1 = new Epic(1, "epic1", "desc1", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Epic epic2 = new Epic(1, "epic2", "desc2", Duration.ofMinutes(10),
                LocalDateTime.of(2025, 6, 11, 11, 00));
        Epic epic3 = new Epic(1, "epic2", "desc2", Duration.ofMinutes(10),
                LocalDateTime.of(2024, 6, 11, 11, 00));
        Subtask subtask1 = new Subtask("Subtask1", "desc1", Status.NEW, epic1.getId(), Duration.ofMinutes(60),
                LocalDateTime.of(2025, 6, 12, 11, 00));
        Subtask subtask2 = new Subtask("Subtask2", "desc2", Status.NEW, epic2.getId(), Duration.ofMinutes(60),
                LocalDateTime.of(2025, 6, 12, 11, 00));
        Subtask subtask3 = new Subtask("Subtask3", "desc3", Status.DONE, epic3.getId(), Duration.ofMinutes(60),
                LocalDateTime.of(2025, 6, 12, 11, 00));

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    @DisplayName("Сохраняется ли история при просмотре через http и получение истории через http")
    @Test
    public void testHistoryManagerInHttpServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url;
        HttpRequest request;
        String path = "http://localhost:8080/tasks/";
        for (int i = 1; i < 3; i++) {
            url = URI.create(path + i);
            request = HttpRequest.newBuilder().uri(url).GET().build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        url = URI.create("http://localhost:8080/history");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> parsed = gson.fromJson(jsonArray, new TaskTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, parsed.size(), "Некорректный размер списка");
        assertEquals("Task1", parsed.get(0).getTaskName(), "Некорректный  первый элемент списка");
    }


    @DisplayName("Получить задачи в порядке приоритета через http ")
    @Test
    public void testPrioritizeSortingInHttpServer() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> parsed = gson.fromJson(jsonArray, new TaskTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals("Task2", parsed.get(0).getTaskName(), "Некорректный первый элемент списка");
    }
}