package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {
    
    @Test(timeout=SECOND)
    public void testDelete() {
        IList<String> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }
        // System.out.println(list.toString());
        list.delete(5);
        // System.out.println(list.toString());
        assertEquals("6", list.get(5));
    }
    
    @Test(timeout=SECOND)
    public void testDelete2() {
        IList<String> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }
        list.delete(8);
        assertEquals(list.size(), 9);
    }
}
