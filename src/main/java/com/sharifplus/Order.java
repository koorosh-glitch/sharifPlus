package com.sharifplus;

import com.sharifplus.Authentication.User;
import com.sharifplus.Products.NoSuchProduct;
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
    private boolean isComplete = false;

    public Order(User usr, String order) throws NoSuchProduct {
        this.ID = (long) Math.floor(Math.random() * Math.pow(10, 5));
        this.usr = usr;
        products = new LinkedList<>();
        String[] parsed = order.split(" ");
        if (parsed.length == 2) {
            throw new NoSuchProduct();
        }
        for (int i = 2; i < parsed.length; i++) {
            if (find(parsed[i]) == null) {
                System.out.println(IO.Yellow + " Invalid Product Name : " + IO.Red + parsed[i] + IO.Reset);
                throw new NoSuchProduct();
            }
        }
        for (int i = 2; i < parsed.length; i++) {
            Product product = find(parsed[i]);
            products.add(product);
        }
    }

    int[] total;

    public static Product[] allProducts = { new Pizza(), new Burger(), new Steak(), new FriedChicken(),
            new Coffea(), new HotChocolate(), new Soda(), new Tea(), new Water(), new ChocolateCake(),
            new IceCream(),
            new VanillaCake(), new Salad(), new FrenchFries() };

    public static Product find(String input) {
        Product tmp;
        for (int i = 0; i < allProducts.length; i++) {
            if ((tmp = allProducts[i]).getType().equals(input)) {
                return tmp;
            }
        }
        return null;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void Terminate() {
        isComplete = true;
    }

    public static void list(boolean all, User usr) {
        LinkedList<Order> queue = App.stack;
        boolean filtered = (usr != null);
        for (Order order : queue) {
            if ((all || !order.isComplete()) && ((filtered) ? order.usr.userId == usr.userId : true)) {
                System.out
                        .println("Order No: " + IO.Yellow + order.ID + IO.Reset
                                + ((filtered) ? " Created By User Id : " + IO.Blue
                                        + order.usr.userId : "")
                                + ((!all) ? IO.Yellow + " (pending) "
                                        : ((order.isComplete) ? IO.Green + "Terminated" : IO.Yellow + " Pending"))
                                + IO.Reset);
                System.out.println(printOrder(order));
            }
        }
    }

    public static String printOrder(Order order) {
        String output = "";
        output += "[";
        for (Product product : order.products) {
            output += " " + IO.Green + product.name + IO.Reset + ",";
        }
        output += "]";
        return output;
    }
}
