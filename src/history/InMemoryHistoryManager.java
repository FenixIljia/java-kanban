package history;

import data.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private int size = 0;
    private HashMap<Integer, Node> history = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getIdentifier())) {
            if (tail.data.getIdentifier() != task.getIdentifier()) {
                remove(task.getIdentifier());
                linkLast(task);
                size++;
            }
        } else {
            linkLast(task);
            size++;
        }
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            size--;
        }
    }

    public int getSize() {
        return size;
    }

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);

        tail = newNode;

        if (oldTail == null) {

            head = newNode;
        } else {
            oldTail.next = newNode;
        }

        history.put(task.getIdentifier(), newNode);
    }

    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            tail = prevNode;
        }

        history.remove(node.data.getIdentifier());
    }

    private List<Task> getTasks() {
        if (head == null) {
            return null;
        }

        LinkedList<Task> tasks = new LinkedList<>();
        Node node = head;

        tasks.add(head.data);

        while (node.next != null) {
            tasks.add(node.next.data);
            node = node.next;
        }

        return tasks;
    }
}
