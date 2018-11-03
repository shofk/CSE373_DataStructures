package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * Array Dictionary class
 * @author Stephanie Palmer, Shohei F. Koshiro
 *
 * @param <K> Generic type for key
 * @param <V> Generic type for value
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    /** keeps the pairs */
    private Pair<K, V>[] pairs;

    /** size of the array */
    private int arraySize;
    
    /** location of the last element */
    private int nElements;

    /**
     * Constructor : creates an array dictionary
     */
    public ArrayDictionary() {
        this.arraySize = 100;
        this.nElements = 0;
        this.pairs = this.makeArrayOfPairs(this.arraySize);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     * will update size to ten, back will equal the front. when back = size-1 need new array
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int size) {
        return (Pair<K, V>[]) (new Pair[size]);

    }

    /**
     * Returns the value corresponding to the given key.
     * @param key key used to store the value.
     * @return value corresponding to the given key.
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V get(K key) {
        int index = this.getIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException("Specified key not in the dictionary");
        }
        return this.pairs[index].getValue();
    }

    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replace its value with the given one.
     * @param key key used to store the value
     * @param value value corresponds to the key
     */
    @Override
    public void put(K key, V value) {
        int index = this.getIndex(key);
        if (index == -1) {
            // when key is new
            if (this.arraySize == this.nElements) {
                this.copyOver();
            }
            this.pairs[this.nElements] = new Pair<>(key, value);
            this.nElements++;
        } else {
            // when the pair already exists, only replace the value
            this.pairs[index].setValue(value);
        }
    }
    
    /**
     * Creates a larger array of pairs and transfer the pairs to the new array
     */
    private void copyOver() {
        // double the array size
        this.arraySize *= 2;
        Pair<K, V>[] newArray = this.makeArrayOfPairs(this.arraySize);
        // enter the existing elements into the new array
        for (int i =0; i < this.nElements; i++) {
            newArray[i] = pairs[i];
        }
        this.pairs = newArray;
    }

    /**
     * Remove the key-value pair corresponding to the given key from the dictionary.
     * @param key key user wants to remove
     * @return value corresponds to the key
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V remove(K key) {
        int index = this.getIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException("Unable to delete. No such key is found");
        }
        // keep the value to return
        V current = this.pairs[index].getValue();
        this.pairs[index] = null;
        if (this.nElements > 0) {
          // pull the last element to the removed spot
          this.pairs[index] = this.pairs[nElements -1]; 
          pairs[nElements-1] = null;
        }
        this.nElements--;
        return current;
    }
    
    /**
     * Returns the index of the given key.
     * @param key Key user wants to find
     * @return index the key resides; -1 if key not in the dictionary
     */
    private int getIndex(K key) {
        for (int i = 0; i < this.size(); i++) {
            if (key != null && pairs[i].getKey() != null) {
                if (key.equals(pairs[i].getKey())) {
                    return i;
                }
            } else if (key == null && pairs[i].getKey() == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     * @param key key the user wants to search
     * @return if the given key exists in the dictionary
     */
    @Override
    public boolean containsKey(K key) {
        return this.getIndex(key) != -1;
    }

    /**
     * Returns the number of key-value pairs stored in this dictionary.
     * @return number of pairs in the dictionary
     */
    @Override
    public int size() {
        return nElements;
    }
    
    /**
     * Object which holds the key and value
     * @param <K> generic type for the key
     * @param <V> generic type for the value
     */
    private static class Pair<K, V> {
        /** key corresponding the value */
        public K key;
        /** value held */
        public V value;

        /**
         * Constructor : creates a new pair of key and value
         * @param key key corresponding the value
         * @param value new value to be held
         */
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * toString method
         * @return String representation of the pair
         */
        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
        
        /**
         * Gets the key
         * @return key
         */
        public K getKey() {
            return key;
        }
        
        /**
         * Gets the value
         * @return value
         */
        public V getValue() {
            return value;
        }
        
        /**
         * Sets a new value
         * @param value new value to be set
         */
        public void setValue(V value) {
            this.value = value;
        }
    }
}
