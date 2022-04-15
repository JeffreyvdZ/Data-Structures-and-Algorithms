package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class OrderedArrayListTest {
    OrderedArrayList<Product> products;
    Product product1, product2, product3, product4, product5, product6, product7;

    @BeforeEach
    private void setup(){
        products = new OrderedArrayList<>(Comparator.comparing(Product::getBarcode));
        product1 = new Product(5L, "food", 15.00);
        product2 = new Product(4L, "food2", 12.50);
        product3 = new Product(3L, "food3", 6.00);
        product4 = new Product(2L, "food4", 2.00);
        product5 = new Product(1L, "food5", 20.00);
        product6 = new Product(10L, "food6", 10.00);
        product7 = new Product(11L, "food7", 7.00);
        products.addAll(List.of(product1,product2,product3,product4,product5));

    }
    @Test
    public void sortingTest(){
        products.sort();
        assertEquals(5, products.getnSorted());
        //Barcode sorting check
        assertEquals(product5, products.get(0));
        assertEquals(product1, products.get(products.size()-1));
    }


    @Test
    public void addAndRemoveKeepSortedPartOfListCorrectTest(){
        products.sort();
        //the whole array should be sorted, so nSorted = products.size()
        assertEquals(products.getnSorted(), products.size());

        products.add(3, product7);
        for (Product p: products) {
            System.out.println(p);
        }
        for (int i = 0; i < products.size(); i++) {
            System.out.println("i: "+ i + " " + products.get(i));
        }
        //nSorted should be changed now to the first 3 products, because that's the only sorted part left.
        assertEquals(3, products.getnSorted());
        //nSorted shouldn't be equal to the size now.
        assertNotEquals(products.size(), products.getnSorted());

        //Removing a product in the unsorted part of list.
        products.remove(product7);

        for (int i = 0; i < products.size(); i++) {
            System.out.println("i: "+ i + " " + products.get(i));
        }

        //nSorted should not be changed.
        assertEquals(3, products.getnSorted());

        //Removing a product in the sorted part of list.
        products.remove(product4);

        for (int i = 0; i < products.size(); i++) {
            System.out.println("i: "+ i + " " + products.get(i));
        }
        //nSorted should be decrease by one.
        assertEquals(1, products.getnSorted());
    }

    @Test
    public void iterativeBinarySearchIndexTest(){
        products.sort();
        //Find index of product in array
        assertEquals(4, products.indexOfByIterativeBinarySearch(product1));
        assertEquals(0, products.indexOfByIterativeBinarySearch(product5));
        //Find product in products array
        for (Product p: products) {
            assertEquals(p, products.get(products.indexOfByIterativeBinarySearch(p)));
        }
        //Returns -1 if index is not found
        assertEquals(-1, products.indexOfByIterativeBinarySearch(product6));
    }

    @Test
    public void RecursiveBinarySearchTest(){
        products.sort();
        //Find index of product in array
        assertEquals(4, products.indexOfByRecursiveBinarySearch(product1,0,0));
        assertEquals(0, products.indexOfByRecursiveBinarySearch(product5,0,0));
        //Find product in products array
        for (Product p: products) {
            assertEquals(p, products.get(products.indexOfByRecursiveBinarySearch(p,0,0)));
        }
        //Returns -1 if index is not found
        assertEquals(-1, products.indexOfByRecursiveBinarySearch(product6,0,0));
    }
}
