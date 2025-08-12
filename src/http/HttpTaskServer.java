package http;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import manager.TaskManager;
import handler.*;
import adapters.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        Gson gson = getGson();
        server = HttpServer.create(new InetSocketAddress(PORT), 5);

        server.createContext("/tasks", new TaskHandler(gson, manager));
        server.createContext("/epics", new EpicHandler(gson, manager));
        server.createContext("/subtasks", new SubtaskHandler(gson, manager));
        server.createContext("/history", new HistoryHandler(gson, manager));
        server.createContext("/prioritized", new PrioritizedHandler(gson, manager));

    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public void startServer() {
        server.start();
        System.out.println("HTTP сервер запущен. Порт:" + PORT);
    }

    public void stopServer() {
        server.stop(0);
        System.out.println("HTTP сервер остановлен.");
    }

}
