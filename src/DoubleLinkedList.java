package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * DoubleLinkedList class : each node is connected to its previous and next node
 * @author Shohei F. Koshiro
 *
 * @param <T> Generic type
 */
public class DoubleLinkedList<T> implements IList<T> {
    /** First node of the list */
    private Node<T> front;
    
    /** End node of the list */
    private Node<T> back;
    
    /** Number of elements in the list */
    private int size;

    /**
     * Constructor : creates an empty DoubleLinkedList object
     */
    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    /**
     * Adds a new node with the given item to the list
     * @param item The item the new node should hold
     */
    @Override
    public void add(T item) {
        // insert at the end
        this.insert(this.size, item);
    }

    /**
     * Removes the end node and returns the item held by the node.
     * @throws EmptyContainerException if the container is empty and there is no element to remove.
     * @return The item held by the end node.
     */
    @Override
    public T remove() {
        if (this.size == 0) {
            throw new EmptyContainerException("List empry : no items to remove.");
        }
        // delete the last node
        return this.delete(this.size - 1);
    }

    /**
     * Returns the item located at the given index.
     * @param index The index number of the node
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     * @return The item located at the given index
     */
    @Override
    public T get(int index) {
        if (this.size <= index || index < 0) {
            throw new IndexOutOfBoundsException("Index out of boundary : check the size of the list");
        }
        Node<T> node = findNode(index);
        return node.getData();
    }

    /**
     * Overwrites the element located at the given index with the new item.
     * @param index The index number of the node
     * @param item The new item to be overwritten
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public void set(int index, T item) {
        if (this.size <= index || index < 0) {
            throw new IndexOutOfBoundsException("Index out of boundary : check the size of the list");
        }
        if (this.size == 1) {
            // end case : only one node
            this.front = new Node<T>(item);
            this.back = this.front;
        } else {
            if (index == 0) {
                Node<T> newNode = new Node<T>(item, this.front.getNext());
                this.front.getNext().setPrev(newNode);
                this.front = newNode;
            } else if (index == this.size - 1) {
                Node<T> newNode = new Node<T>(this.back.getPrev(), item);
                this.back.getPrev().setNext(newNode);
                this.back = newNode;
            } else {
                // get the node to be changed
                Node<T> node = findNode(index);
                Node<T> newNode = new Node<T>(node.getPrev(), item, node.getNext());
                // set the prev and next nodes of the prev and next nodes to the new node
                newNode.getPrev().setNext(newNode);
                newNode.getNext().setPrev(newNode);
            }
        }
    }

    /**
     * Inserts the given item at the given index. If there already exists an element
     * at that index, shift over that element and any subsequent elements one index
     * higher.
     * @param index The index number of the node
     * @param item The new item to be inserted
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size() + 1
     */
    @Override
    public void insert(int index, T item) {
        if (this.size + 1 <= index || index < 0) {
            throw new IndexOutOfBoundsException("Index out of boundary : check the size of the list");
        }
        if (this.size == 0) {
            // irregular case : first item
            this.front = new Node<T>(item);
            this.back = this.front;
        } else if (index == this.size) {
            // end case : add to the end
            Node<T> newNode = new Node<T>(this.back, item);
            this.back.setNext(newNode);
            this.back = newNode;
        } else if (index == 0) {
            // end case : front
            Node<T> newNode = new Node<T>(item, this.front);
            this.front.setPrev(newNode);
            this.front = newNode;
        } else {
            // other mid-cases
            // finds the node which locates at the index
            Node<T> node = findNode(index);
            Node<T> newNode = new Node<T>(node.getPrev(), item, node);
            // set the prev and next node of the prev and next node
            newNode.getPrev().setNext(newNode);
            node.setPrev(newNode);
        }
        this.size++;
    }

    /**
     * Deletes the item at the given index. If there are any elements located at a higher
     * index, shift them all down by one.
     * @param index The index number of the node
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     * @return The item held by the deleted node
     */
    @Override
    public T delete(int index) {
        if (this.size <= index || index < 0) {
            throw new IndexOutOfBoundsException("Index out of boundary : check the size of the list");
        }
        Node<T> node = findNode(index);
        if (this.size > 1) {
            if (index == 0) {
                // end case : front
                this.front = this.front.getNext();
                this.front.setPrev(null);
            } else if (index == this.size - 1) {
                // end case : back
                this.back = this.back.getPrev();
                this.back.setNext(null);
            } else {
                // other mid cases
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
            }
            this.size--;
        } else {
            // when there's only one item left
            this.clear();
        }
        return node.getData();
    }
    
    /**
     * Releases all the nodes and resets the size to 0
     */
    public void clear() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    /**
     * Returns the index corresponding to the first occurrence of the given item
     * in the list.
     * @param item The item to be found in the list
     * @return The location of the item in the list. (-1 if not found)
     */
    @Override
    public int indexOf(T item) {
        boolean keepSearching = !this.isEmpty();
        int index = -1;
        int location = 0;
        Node<T> current = this.front;
        while (keepSearching && size > location) {
            if (item != null && current.getData() != null) {
                // equals method cannot be used when item == null
                // checks if item == current.getData()
                keepSearching = !item.equals(current.getData());
            } else if (item == null && current.getData() == null) {
                // if item == current.getData() == null stop while loop
                keepSearching = false;
                // else continue looping
            }
            if (!keepSearching) {
                // if the item is found, save the index
                index = location;
            }
            current = current.getNext();
            location++;
        }
        return index;
    }

    /**
     * Returns the number of elements in the container.
     * @return The size of the list
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns 'true' if this container contains the given element, and 'false' otherwise.
     * @param other The item to be found
     * @return If the list contains the given item
     */
    @Override
    public boolean contains(T other) {
        return this.indexOf(other) != -1;
    }
    
    /**
     * Returns the node of the given index
     * @param index The index number
     * @return Node of the specified location
     */
    private Node<T> findNode(int index) {
        Node<T> node;
        if (index > this.size/2) {
            // when node can be found in the second half of the list
            node = this.back;
            index = this.size - index - 1;
            for (int i = 0; i < index; i++) {
                node = node.getPrev();
            }
        } else {
            // when the node can be found in the first half of the list
            node = this.front;
            for (int i = 0; i < index; i++) {
                node = node.getNext();
            }
        }
        return node;
    }
    
    /**
     * toString method
     * @return String representation of the list
     */
    public String toString() {
        String print = "[";
        Iterator<T> iter = this.iterator();
        for (int i = 0; i < this.size() - 1; i++) {
            print += String.valueOf(iter.next()) + ", ";
        }
        print += String.valueOf(iter.next()) + "]";
        return print;
    }

    /**
     * Returns an iterator over the contents of this list.
     */
    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    /**
     * Node class : this node holds the item, previous node, and back node.
     * @author Shohei F. Koshiro
     * @param <E> Generic type
     */
    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        /** Item the node holds */
        public final E data;
        
        /** Node connected at front */
        public Node<E> prev;
        
        /** Node connected at back */
        public Node<E> next;

        /**
         * Constructor : creates a new node with connecting nodes
         * @param prev Previous node
         * @param data Item the node holds
         * @param next Next node
         */
        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        /**
         * Constructor : creates a new node without connecting nodes
         * @param data Item the node holds
         */
        public Node(E data) {
            this(null, data, null);
        }

        /**
         * Constructor : creates a new node with previous node only
         * @param prev Previous node
         * @param data Item the node holds
         */
        public Node(Node<E> prev, E data) {
            this(prev, data, null);
        }
        
        /**
         * Constructor : creates a new node with next node only
         * @param data Item the node holds
         * @param next Next node
         */
        public Node(E data, Node<E> next) {
            this(null, data, next);
        }
        
        /**
         * Sets the previous node
         * @param prev Previous node
         */
        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }
        
        /**
         * Sets the next node
         * @param next Next node
         */
        public void setNext(Node<E> next) {
            this.next = next;
        }
        
        /**
         * Lets the user get the previous node
         * @return Previous node
         */
        public Node<E> getPrev() {
            return this.prev;
        }
        
        /**
         * Lets the user get the next node
         * @return Next node
         */
        public Node<E> getNext() {
            return this.next;
        }
        
        /**
         * Lets the user get the item held by the node
         * @return Item held by the node
         */
        public E getData() {
            return this.data;
        }
    }

    /**
     * Iterator class : creates an iterator for DoulbeLinkedList
     * @author Shohei F. Koshiro
     *
     * @param <T> Generic type
     */
    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         * @return If next node exits
         */
        public boolean hasNext() {
            return this.current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         * @return Next item
         */
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("No new node : the iterator has reached the end.");
            }
            T item = this.current.getData();
            this.current = this.current.getNext();
            return item;
        }
    }
}
