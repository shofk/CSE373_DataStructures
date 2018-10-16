package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {
    
    @Test(timeout=SECOND)
    public void testExample() {
        IList<String> list = new DoubleLinkedList<>();
        for (int i = 0; i < 1000; i++) {
            list.add("" + i * Math.PI);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals("" + Math.PI * i, list.get(0));
            list.delete(0);
        }
    }
   @Test(timeout=SECOND)
   public void testDeleteFromEnd() {
       IList<String> list = new DoubleLinkedList<>();
       for (int i = 0; i < 100000; i++) {
           list.add("" + i * Math.PI);
       }
       for (int i = 99999; i >= 0; i--) {
           assertEquals("" + Math.PI*i, list.get(i));
           list.delete(i);
       }
   }
   
   @Test(timeout=SECOND)
   public void testDeleteFromNearEnd() {
       IList<String> list = new DoubleLinkedList<>();
       for (int i = 0; i < 100000; i++) {
           list.add("" + i * Math.PI);
       }
       for (int i = 0; i < 99999; i++) {
           assertEquals("" + Math.PI * 99999, list.get(list.size() - 1));
           list.delete(list.size()-2);
       }
   }
}
