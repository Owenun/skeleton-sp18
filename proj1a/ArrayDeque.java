/**
 * second part of project1 A.
 * deque implemented by array
 * @author Owenun
 *
 */

public class ArrayDeque<T>{

    /** array to save data */
    private T[] array;
    /** size of deque */
    private int size;
    /*length of array */
    private int length;
    /*the front of deque */
    private int front;
    /* the last of deque, and the last always store null which means it is
    * always a symbol */
    private int last;

    /* the constructor of the Deque */
    public ArrayDeque() {
        array = (T[]) new Object[8];
        size = 0;
        length = 8;
        front = 4;
        last = 4;
    }

    /* minus the pointer by one, and if pointer equal 0 then return
    * length - 1, which means the deque is a circle */
    private int minusOne(int index) {
        if (index == 0) {
            return length - 1;
        }
        return index - 1;
    }

    /* plus the pointer by one, and if pointer equal length - 1, return the
    * 0 which is the first index of array */
    private int plusOne(int index, int module) {
        index %= module;
        if (index == module - 1) {
            return 0;
        }
        return index + 1;
    }


    /* grow the deque to twice length */
    private void grow() {
        T[] newArray = (T[]) new Object[length * 2];
        int ptr1 = front;
        int ptr2 = length;
        while(ptr1 != last) {
            newArray[ptr2] = array[ptr1];
            ptr1 = plusOne(ptr1, length);
            ptr2 = plusOne(ptr2, length * 2);

        }
        front = length;
        last = ptr2;
        array = newArray;
        length = 2 * length;
    }

    /* shrink the deque to half-length, and adapt to new front length brabra*/
    private void shrink() {
        T[] newArray = (T[]) new Object[length / 2];
        int ptr1 = front;
        int ptr2 = length / 4;
        while(ptr1 != last) {
            newArray[ptr2] = array[ptr1];
            ptr1 = plusOne(ptr1, length);
            ptr2 = plusOne(ptr2, length / 2);
        }
        front = length / 4;
        last = ptr2;
        array = newArray;
        length = length / 2;
    }


    /* add to the front of deque */
    public void addFirst(T item) {
        if (size == length - 1) grow();
        front = minusOne(front);

        array[front] = item;
        size++;
    }

    /* add to the last of deque */
    public void addLast(T item) {
        if (size == length - 1) {
            grow();
        }
        array[last] = item;
        last = plusOne(last, length);
        size++;

    }

    /* @return true if deque is empty */
    public boolean isEmpty() {
        return size == 0;
    }

    /* return the size of deque */
    public int size() {
        return size;
    }

    /* print the deque from front ro last */
    public void printDeque() {
        int ptr = front;
        while (ptr != last) {
            System.out.print(array[front] + " ");
            ptr = plusOne(ptr, length);
        }
    }

    /* remove the front item of deque */
    public T removeFisrt() {
        if (length >= 16 && length / size >= 4) {
            shrink();
        }
        if (size == 0) return null;

        T ret = array[front];
        front = plusOne(front, length);
        size--;
        return ret;
    }

    /* remove the last item of the deque */
    public T removeLast() {
        if (length > 16 && length / size >= 4) {
            shrink();
        }
        if (size == 0) return null;
        last = minusOne(last);
        size = size - 1;
        return array[last];
    }

    /* get the item whose index is the right one of deque */
    public T get(int index) {
        if (index > size) return null;
        int ptr = front;
        for (int i = 0; i < index; i++) {
            ptr = plusOne(ptr, length);
        }
        return array[ptr];
    }
}
