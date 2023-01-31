package com.sharifplus;

import com.sharifplus.Authentication.User;
import com.sharifplus.Products.Product;
import com.sharifplus.Products.Appetizer.*;
import com.sharifplus.Products.Dessert.*;
import com.sharifplus.Products.Drinks.*;
import com.sharifplus.Products.Foods.*;
import java.util.LinkedList;

public class Order {
    public final long ID;
    public final User usr;
    public final LinkedList<Product> products;

    public Order(long ID, User usr, String order) {
        populateArray();
        this.ID = ID;
        this.usr = usr;
        products = new LinkedList<>();
        String[] parsed = order.split(" ");
        for (int i = 2; i < parsed.length; i++) {
            if (find(parsed[i]) == null) {
                System.out.println(IO.Yellow + " Invalid Product Name : " + IO.Red + parsed[i] + IO.Reset);
            }
        }
        for (int i = 2; i < parsed.length; i++) {
            Product product = find(parsed[i]);
            products.add(product);
            populateArray();
        }
    }

    int[] total;

    public static Product[] allProducts;

    private void populateArray() {
        allProducts = new Product[] { new Pizza(), new Burger(), new Steak(), new FriedChicken(),
                new Coffea(), new HotChocolate(), new Soda(), new Tea(), new Water(), new ChocolateCake(),
                new IceCream(),
                new VanillaCake(), new Salad(), new FrenchFries() };
    }

    public static Product find(String input) {
        Product tmp;
        for (int i = 0; i < allProducts.length; i++) {
            if ((tmp = allProducts[i]).getType().equals(input)) {
                return tmp;
            }
        }
        return null;
    }
}
