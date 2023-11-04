
/**  first part of project1A.
 *   Deque implemented by Linked List
 *  @author Owenun
 */


public class LinkedListDeque<T>  {

    /** inner class Node. */
    public class Node {
        /** the item stored on this node. */
        private T item;
        /** the Node before this Node. **/
        private Node pre;
        /** the Node after this Node. **/
        private Node next;

        /** constructor for Node. */
        public Node(T n, Node ppre, Node nnext) {
            item = n;
            pre = ppre;
            next = nnext;
        }

        /** constructor for Node.(especially for sentinel node). */
        public Node(Node ppre, Node nnext) {
            pre = ppre;
            next = nnext;
        }
    }
    /** sentinel node. */
    private Node sentinel;
    /** size of the deque. */
    private int size;

    /** constructor for deque. */
    public LinkedListDeque() {
        sentinel = new Node(null, null);
        sentinel.pre = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }


    public void addFirst(T item) {
        Node newList = new Node(item, sentinel, sentinel.next);
        sentinel.next.pre = newList;
        sentinel.next = newList;
        size++;

    }


    public void addLast(T item) {
        Node newList = new Node(item, sentinel.pre, sentinel);
        sentinel.pre.next = newList;
        sentinel.pre = newList;
        size++;
    }


    public boolean isEmpty() {
        return size == 0;
    }


    public int size() {
        return size;
    }

    public void printDeque() {
        Node cur = sentinel.next;
        while (cur != sentinel) {
            System.out.print(cur.item + " ");
            cur = cur.next;
        }

    }


    public T removeFirst() {
        if (size == 0) return null;
        T ret =sentinel.next.item;
        sentinel.next.next.pre = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return ret;
    }


    public T removeLast() {
        if (size == 0) return null;
        T ret = sentinel.pre.item;
        sentinel.pre.pre.next = sentinel;
        sentinel.pre = sentinel.pre.pre;
        size--;
        return ret;
    }


    public T get(int index) {
        if (index >= size) return null;
        Node cur = sentinel.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.item;
    }

    public T getRecursive(int index) {
        if (index >= size) return null;
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node cur, int index) {
        if (index > 0) {
            cur = cur.next;
            return  getRecursiveHelper(cur, index - 1);
        }
        return cur.item;

    }
}
