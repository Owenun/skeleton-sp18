package lab9;

import java.util.*;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author xiaozhicong
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    public static void main(String[] args) {
        BSTMap<Character, Integer> map = new BSTMap<>();
        System.out.println("hi" + 10);
        map.put('c', 1);
        map.put('b', 2);
        map.put('a', 1);
        map.remove('c');
        map.put('d', 5);
        Random r = new Random(1);
        int count = 0;
        while(map.size() < 26) {
            int i = Math.abs(r.nextInt() % 26);
            map.put((char) ('a' + i), i);
            count++;
        }
        System.out.println(map.keySet());
        System.out.println(count);
    }

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) return null;
        if (key.compareTo(p.key) == 0) return p.value;
        else if (key.compareTo(p.key) < 0) return getHelper(key, p.left);
        else return getHelper(key, p.right);
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed.");
        }
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {


        if (key.compareTo(p.key) == 0) {
            p.value = value;
            return p;
        } else if (key.compareTo(p.key) < 0) {
            if (p.left == null) {
                Node n = new Node(key, value);
                p.left = n;
                size++;
                return n;
            }  else {
                return putHelper(key, value, p.left);
            }
        }else {
            if (p.right == null) {
                Node n = new Node(key, value);
                p.right = n;
                size++;
                return n;
            } else {
                return putHelper(key, value, p.right);
            }
        }
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Null values not allowed.");
        }
        if (root == null) {
            root = new Node(key, value);
            size++;
        }
        else putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        return keySetHelper(root, keys);
    }

    private Set<K> keySetHelper(Node p, Set<K> keys) {
        if (p == null) return keys;
        keys = keySetHelper(p.left, keys);
        keys.add(p.key);
        keys = keySetHelper(p.right, keys);
        return keys;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed.");
        }

        Node p = null;
        Node cur = root;

        while (true) {
            if (cur == null) return null;
            if (key.compareTo(cur.key) == 0) break;
            else if (key.compareTo(cur.key) < 0) {
                p = cur;
                cur = cur.left;
            } else {
                p = cur;
                cur = cur.right;
            }
        }
        V val = cur.value;

        if (cur.left == null && cur.right == null) {
            if (p == null) {
                root = null;
            }
            else if ( p.left == cur) p.left = null;
            else if (p.right == cur) p.right = null;
        } else {
            ArrayList<Node> nodes = findSubNode(cur);
            Node sp = nodes.get(0);
            Node sn = nodes.get(1);
            replaceNode(cur, sp, sn);
            if (p == null ) {
                root = sn;
            }
            else if (cur == p.left) p.left = sn;
            else if (cur == p.right) p.right = sn;
        }
        size--;
        return val;
    }

    private void replaceNode(Node cur, Node sp, Node sn) {
        if (sn.left == null && sn.right == null) {
            if (sn == sp.left) sp.left = null;
            if (sn == sp.right) sp.right = null;
            sn.left = cur.left;
            sn.right = cur.right;
        }
        else if (sn.left != null) {
            if (sn == sp.left) sp.left = sn.left;
            if (sn == sp.right) sp.right = sn.left;
            sn.left = cur.left;
            sn.right = cur.right;
        } else {
            if (sn == sp.left) sp.left = sn.right;
            if (sn == sp.right) sp.right = sn.right;
            sn.left = cur.left;
            sn.right = cur.right;
        }
    }

    private ArrayList<Node> findSubNode(Node cur) {
        Node sp = cur;
        Node sn = cur.left != null ? cur.left : cur.right;
        if (cur.left != null) {
            while (sn.right != null) {
                sp = sn;
                sn = sn.right;
            }
        } else {
            while (sn.left != null) {
                sp = sn;
                sn = sn.left;
            }
        }
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(sp);
        nodes.add(sn);
        return nodes;
    }
    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
