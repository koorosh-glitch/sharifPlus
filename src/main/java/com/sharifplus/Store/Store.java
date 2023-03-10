package com.sharifplus.Store;

import com.sharifplus.*;
import com.sharifplus.Authentication.User;
import java.util.*;
import com.sharifplus.Products.*;

public abstract class Store {

    Storage storage = App.storage;
    LinkedList<Order> queue = App.stack;
    Scanner reader = App.reader;
    public final String Menu;
    String[] arr;

    public Store(String Menu) {
        this.Menu = Menu;
        arr = Menu.split(" ");
    }

    public abstract void getMenu();

    public void Handle() {
        String input;
        while (true) {
            System.out.println("Available Commands : " + IO.Yellow + "\n \t -Get Menu" + "\n \t -Add Order "
                    + IO.Magenta + "Product0 Product1 ..." + IO.Yellow + "\t -List Ingredients "
                    + IO.Magenta + "Product" + IO.Yellow + "\n \t -Back\n" + IO.Reset);
            input = reader.nextLine();
            int length = input.length();
            if (input.equals("Get Menu")) {
                getMenu();
            } else if (length >= 9 && input.substring(0, 9).equals("Add Order")) {
                addOrder(input);
            } else if (input.equals("Back")) {
                return;
            } else if (input.length() >= 17 && input.substring(0, 17).equals("List Ingredients ")) {
                Product tmp = Order.find(input.substring(17));
                if (tmp == null) {
                    IO.printError("No Such Product !");
                    return;
                }
                outer: for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equals(tmp.name)) {
                        break outer;
                    }
                    if (i == arr.length - 1) {
                        IO.printError("That Product Is Not On The Menu (Maybe Try Another Division)");
                        return;
                    }
                }
                tmp.printIngredients();
            } else {
                IO.printError("Invalid Command !");
            }
        }
    }

    public void addOrder(String input) {
        User usr = User.currentUsr;
        Order order;
        try {
            order = new Order(usr, input);
        } catch (NoSuchProduct e) {
            IO.printError("No Such Product !");
            return;
        }
        outer: for (Product product : order.products) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].equals(product.name)) {
                    continue outer;
                }
                if (i == arr.length - 1) {
                    IO.printError("Invalid Product : " + product.name);
                    return;
                }
            }
        }
        if (storage.isAvailable(order)) {
            IO.PrintCheckMark();
            System.out.println(IO.Green + "\t Order Created On " + IO.Yellow + java.time.LocalDateTime.now() + IO.Reset
                    + "\tOrder Id : " + IO.Blue + order.ID + IO.Reset);
            queue.add(order);
            usr.OrderHistory.add(order);
            IO.logInfo("A New Order " + Order.printOrder(order) + " Has Been Created By: ");
        } else {
            IO.printError(IO.Yellow + "Unfortunately This Order Is Not Available Right Now :(");
        }
    }
}
