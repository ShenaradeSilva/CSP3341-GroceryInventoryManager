package com.csp3341.grocery;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();

        while (true) {
            System.out.println("1. Add Perishable Product \n" +
                    "2. Add Non-Perishable Product \n" +
                    "3. List Products \n" +
                    "4. Generate Reports \n" +
                    "5. Exit \n");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\n\nADD PERISHABLE PRODUCT: \n");

                    System.out.println("Product ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Product Name: ");
                    String name = scanner.nextLine();

                    System.out.println("Price: ");
                    double price = scanner.nextDouble();

                    System.out.println("Quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Category (Dairy, Produce, Meat, Beverages, Canned Food, Dried Food: ");
                    Category category = Category.valueOf(scanner.nextLine().toUpperCase());

                    System.out.println("Expiry Date (YYYY-MM-DD): ");
                    String expiryDate = scanner.nextLine();

                    manager.addProduct(new Perishable(id, name, price, quantity, category, expiryDate));
                }

                case 2 -> {

                }

                case 3 -> manager.listAllProducts();

                case 4 -> manager.generateInventoryReport();

                case 5 -> {
                    System.out.println("Exiting...." +
                            "Thank you for using Grocery Inventory Management System!");
                    return;
                }

                default -> System.out.println("Error! Invalid Option!");
            }
        }
    }
}
