package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author xiaozhicong
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return -1;
        }
        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int hashKey = hash(key);
        if (hashKey == -1) throw new IllegalArgumentException("Null key not allowed.");

        return this.buckets[hashKey].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int hashKey = hash(key);
        if (hashKey == -1) throw new IllegalArgumentException("Null key not allowed.");

        if (buckets[hashKey].containsKey(key)) {
            buckets[hashKey].put(key, value);
        } else {
            buckets[hashKey].put(key, value);
            size++;
            if (loadFactor() > MAX_LF) resize();
        }
    }

    public void resize(){
        ArrayMap<K, V>[] oldBuckets = buckets;
        int newLength = buckets.length * 2;
        buckets = new ArrayMap[newLength];
        this.clear();
        for (ArrayMap<K, V> map : oldBuckets) {
            for (K key : map) {
                V val =  map.get(key);
                put(key, val);
            }
        }
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
        HashSet<K> keys = new HashSet<>();
        for (ArrayMap<K, V> map : buckets) {
            for(K key : map) {
                keys.add(key);
            }
        }
        return keys;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int hashKey = hash(key);
        if (hashKey == -1) throw new IllegalArgumentException("Null key not allowed.");

        V val = buckets[hashKey].remove(key);
        if (val != null) size--;
        return val;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
