package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private void linkLast(Task task) {
        final Node node = new Node(task, last, null);
        if (last != null) {
            last.next = node;
        } else {
            first = node;
        }
        last = node;
        nodeMap.put(task.getId(), node);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void addTask(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeNode(task.getId());
        }

        linkLast(task);
    }

    private void removeNode(int id) {
        final Node node = nodeMap.remove(id);
        if (node == null) return;

        if (node == first) {
            first = node.next;
        } else if (node.next == null) {
            node.prev.next = null;
            last = node.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        node.prev = null;
        node.next = null;
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
}