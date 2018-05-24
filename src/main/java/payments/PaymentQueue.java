package payments;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Евгений on 18.05.2018.
 */

public class PaymentQueue<T> {

    LinkedList<T> queue = new LinkedList<T>();

    public void push (T elem) {
        queue.offer(elem);
    }

    public T pop () {
        return queue.remove();
    }

    public T peek () {
        return queue.getFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public String toString() {
        return queue.toString();
    }


    private class QueueIterator implements Iterator {

        private int i = 0;

        public boolean hasNext() {
            if (i < queue.size()) {
                return true;
            }
            return false;
        }

        public T next() {
            if (i < queue.size()) {
                return queue.get(i++);
            }
            return  null;
        }

        public void remove() {
            System.out.println(i);
            queue.remove(i);
        }
    }


    Iterator iterator() {
        return new QueueIterator();
    }

}
