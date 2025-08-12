package server;

import http.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Путь /subtasks")
public class HttpTaskManagerSubtaskTest {

    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);

    public HttpTaskManagerSubtaskTest() throws IOException {
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


    @DisplayName("Некорректные запросы")
    @Test
    public void testUsingUnsupportedHttpRequests() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString("")).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Обработан некорректный путь");

        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(501, response.statusCode(), "Данный метод не предусмотрен для запроса");

    }
}