package com.sharifplus.Store;

import com.sharifplus.*;
import java.util.*;

public class Resturant extends Store {
    String Menu = "Burger Fried-Chicken Pizza Steak French-Fries Salad";
    Scanner reader = App.reader;

    public void getMenu() {
        String alignment = "|%-40s";
        System.out.format("+--------------------------+-----------------------------+\n");
        System.out.format("|        " + IO.Magenta + "Food" + IO.Reset + "          |            " + IO.Yellow + "Appetizer" + IO.Reset + "       |\n");
        System.out.format("+--------------------------+-----------------------------+\n");
        String[] arr = Menu.split(" ");
        for (int i = 0; i < 4 ;i++) {
            System.out.format(alignment,((storage.isAvailable(Order.find(arr[i]))) ? IO.Green : IO.Red) + arr[i] + IO.Reset);
            if (i < 2) {
                System.out.format(alignment,((storage.isAvailable(Order.find(arr[i + 4]))) ? IO.Green : IO.Red) + arr[i + 4] + IO.Reset);
            }
            System.out.print("|\n");
        }
        System.out.format("+--------------------------+-----------------------------+\n");
    }

    public void addOrder(String input) {

    }
}
