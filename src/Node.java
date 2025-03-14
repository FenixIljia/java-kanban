public class Node {
    Node next;
    Node prev;
    Task data;

    Node(Node prev, Task data, Node next) {
        this.next = next;
        this.prev = prev;
        this.data = data;
    }
}
