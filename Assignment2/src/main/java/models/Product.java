package models;

import java.util.Arrays;

import java.util.Comparator;
import java.util.List;

public class Product  {
    private final long barcode;
    private String title;
    private double price;

    public Product(long barcode) {
        this.barcode = barcode;
    }

    public Product(long barcode, String title, double price) {
        this(barcode);
        this.title = title;
        this.price = price;
    }

    /**
     * parses product information from a textLine with format: barcode, title, price
     *
     * @param textLine
     * @return a new Product instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Product fromLine(String textLine) {
        Product newProduct = null;
        List<String> productList = Arrays.asList(textLine.split(","));
        newProduct = new Product(Long.parseLong(productList.get(0)), productList.get(1).trim(), Double.parseDouble(productList.get(2)));
        return newProduct;
    }

    public long getBarcode() {
        return barcode;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Product)) return false;
        return this.getBarcode() == ((Product) other).getBarcode();
    }

    public String toString() {
        return this.barcode + "/" + this.title + "/" + this.price;
    }

    static class ComparatorByBarcode implements Comparator<Product> {
        @Override
        public int compare(Product p1, Product p2) {
            if (p1.getBarcode() < p2.getBarcode()) return -1;
            if (p1.getBarcode() > p2.getBarcode()) return 1;
            else return 0;
        }
    }

    // TODO add public and private methods as per your requirements
}
