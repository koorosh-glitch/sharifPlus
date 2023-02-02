package com.sharifplus.Authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sharifplus.*;
import java.io.Console;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public abstract class User {
    static HashMap<Long, User> allUsers = App.users;
    private static boolean isLogged = false;
    public static User currentUsr;
    public boolean isAdmin;
    public boolean isEmployee;
    public boolean isClient;
    public final String name;
    public final long userId;
    protected final byte[] pawrdHsh;
    public LinkedList<Order> OrderHistory = new LinkedList<>();

    public User(String passwrd, String name) throws java.security.NoSuchAlgorithmException {
        userId = (long) Math.floor(Math.random() * Math.pow(10, 5));
        pawrdHsh = hash(passwrd);
        this.name = name;
    }

    public static void createUsr() throws NoSuchAlgorithmException {
        Scanner reader = App.reader;
        System.out.print("Enter Username : ");
        String userName = getUsername(reader);
        System.out.print(IO.Green + "Succesful!\n" + IO.Reset + " Enter Password : ");
        String passwrd = getPassword(reader);
        System.out.println(IO.Green + "Succesful \n");
        User usr = new Client(passwrd, userName);
        allUsers.put(usr.userId, usr);
        IO.PrintCheckMark();
        System.out.println(IO.Green + "Succesful... User " + IO.Blue + userName + IO.Reset + " Created At "
                + java.time.LocalDateTime.now());
        IO.logInfo("A New Account Has Been Created With Username " +IO.Cyan + userName + IO.Reset);
    }

    private static String getUsername(Scanner reader) {
        String output = reader.nextLine();
        if (output.length() == 0) {
            IO.printError(IO.Magenta + "Auth Error:" + IO.Red + "  Does Not Meet Minimum Length! Enter Another : ");
            getUsername(reader);
        }
        for (User usr : allUsers.values()) {
            if (usr.name.equals(output)) {
                IO.printError(IO.Magenta + "Auth Error:" + IO.Red + "  Username Already Exist ! Pick Another : ");
                getUsername(reader);
            }
        }
        return output;
    }

    private static String getPassword(Scanner reader) {
        Console console = System.console();
        char[] password = console.readPassword();
        if (password.length == 0) {
            IO.printError(IO.Magenta + "Auth Error:" + IO.Red + "  Invalid Length ! Pick Another : ");
            getPassword(reader);
        }
        String output = "";
        for (int i = 0; i < password.length; i++) {
            output += password[i];
        }
        return output;
    }

    public static void logIn() throws NoSuchAlgorithmException {
        Scanner reader = App.reader;
        System.out.print("Enter Username : (or enter " + IO.Red + "cancel" + IO.Reset + " to exit)\n \t");
        String name = reader.nextLine();
        if (name.equals("cancel")) {
            return;
        }
        if (name.length() == 0) {
            IO.printError(IO.Magenta + "Auth Error:" + IO.Red + "  Invalid Length !");
            logIn();
        }
        for (User usr : allUsers.values()) {
            if (usr.name.equals(name)) {
                System.out.println("Enter Password : ");
                String passwrd = getPassword(reader);
                if (usr.compareHashes(hash(passwrd))) {
                    System.out.println(IO.Green + "  Logged In As " + IO.Blue + usr.name + IO.Reset);
                    if (usr.isAdmin) {
                        System.out.println("Acoount Mode : " + usr.getPrivilage());
                    } else if (usr.isClient) {
                        System.out.println("Account Mode : " + usr.getPrivilage());
                    } else if (usr.isEmployee) {
                        System.out.println("Account Mode : " + usr.getPrivilage());
                    }
                    isLogged = true;
                    currentUsr = usr;
                    IO.logInfo("Login By : ");
                    return;
                } else {
                    IO.printError(IO.Magenta + "Auth Error:" + IO.Red + "  Wrong Passwrord !");
                    return;
                }
            }
        }
        logIn();
    }

    public static void logOut() {
        System.out.println(IO.Cyan + "Logged Out" + IO.Reset);
        IO.logInfo("Logout By : ");
        currentUsr = null;
        isLogged = false;
    }

    private boolean compareHashes(byte[] hash) {
        if (hash.length != pawrdHsh.length) {
            return false;
        }
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] != pawrdHsh[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLogged() {
        return isLogged;
    }

    public void setPrivilages() throws NoSuchAlgorithmException {
        Scanner reader = App.reader;
        System.out.print("Enter Admin Password : ");
        User admin = App.admin;
        String passwrd = getPassword(reader);
        if (admin.compareHashes(hash(passwrd))) {
            System.out.print("Enter Access Level " + IO.Magenta + "Admin" + IO.Yellow + " Employee" + IO.Green
                    + " Client : " + IO.Reset);
            switch (reader.nextLine()) {
                case "Admin":
                    isAdmin = true;
                    isClient = false;
                    isEmployee = false;
                    IO.logInfo("User " + IO.Cyan + name + IO.Reset + " Has Been Escalated To " + IO.Magenta + "Admin" + IO.Reset);
                    break;
                case "Employee":
                    isAdmin = false;
                    isClient = false;
                    isEmployee = true;
                    IO.logInfo("User " + IO.Cyan + name + IO.Reset + " Has Been Escalated To " + IO.Yellow + "Employee" + IO.Reset);
                    break;
                case "Client":
                    isAdmin = false;
                    isClient = true;
                    isEmployee = false;
                    IO.logInfo("User " + IO.Cyan + name + IO.Reset + " Has Been Escalated To " + IO.Green + "Client" + IO.Reset);
                    break;
                default:
                    IO.printError("Invalid Access Level !");
                    return;
            }
        }
    }

    private static byte[] hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());
        byte[] output = md.digest();
        md.reset();
        return output;
    }

    public void printOrderHistory() {
        Order.list(true, currentUsr);
        IO.logInfo("Purchase Hisotry Generated For : ");
    }

    public String getPrivilage() {
        if (isAdmin) {
            return IO.Magenta + "Admin" + IO.Reset;
        } else if (isEmployee) {
            return IO.Yellow + "Employee" + IO.Reset;
        } else {
            return IO.Green + "Client" + IO.Reset;
        }
    }
}
