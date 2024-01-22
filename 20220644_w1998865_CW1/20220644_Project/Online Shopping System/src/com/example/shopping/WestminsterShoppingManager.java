package com.example.shopping;

import java.io.*;
import java.util.*;
import javax.swing.SwingUtilities;

public class WestminsterShoppingManager implements ShoppingManager{
    private WestminsterShoppingGUI myGUI;
    public List<Product>productlist;
    public boolean isFirstTimeUser;
    public WestminsterShoppingManager() {
        productlist = new ArrayList<>();
        this.userManager = new UserManager();
        this.currentUser = null;


    }
    private UserManager userManager;
    private User currentUser;
    public void start(){
        loadProducts();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Westminster Shopping!");
        System.out.println("1. User Interface (GUI) for User");
        System.out.println("2. Console Menu for Manager");
        System.out.print("Please enter your choice: ");

        int option;
        try{
            option=scanner.nextInt();
        }catch(InputMismatchException e){
            System.out.println("Invalid input. Please enter 1 or 2");
            return;
        }
        switch (option) {
            case 1:
                handleUserInterface();
                break;
            case 2:
                displayMenu();
                break;
            default:
                System.out.print("Invalid option. Please try again");
        }
    }

    private void handleUserInterface() {
        promptUserAuthentication();
        System.out.println("Launching user interface");
        if (myGUI == null) {
            myGUI = new WestminsterShoppingGUI(this, new ShoppingCart());
            SwingUtilities.invokeLater(() -> myGUI.updateProductTable());
            myGUI.setVisible(true);
            new Scanner(System.in).nextLine(); // Wait for the user to press Enter
            displayMenu();
        } else {
            System.out.println("User interface is already running.");
            displayMenu();
        }
    }

    private void promptUserAuthentication() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Please enter your choice: ");

        int option = scanner.nextInt();

        switch (option) {
            case 1:
                registerUser();
                break;
            case 2:
                loginUser();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                promptUserAuthentication();
        }
    }

    private void registerUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your desired username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Check if the username is already taken
        if (userManager.isUsernameTaken(username)) {
            System.out.println("Username already taken. Please choose a different one.");
            promptUserAuthentication();
            return;
        }

        // Register the user
        if (userManager.registerUser(username, password)) {
            // Create a User object with the registered credentials
            User user = new User(username, password);

            // Save the User object to the file
            try {
                isFirstTimeUser=true;
                userManager.saveUserToFile(user);
                System.out.println("User successfully saved to file.");
            } catch (IOException e) {
                System.out.println("Error saving user to file: " + e.getMessage());
                // Optionally, provide additional error handling or retry logic
            }
        } else {
            System.out.println("Registration failed. Please try again.");
            promptUserAuthentication();
        }
    }

    private void loginUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username to login: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password to login: ");
        String password = scanner.nextLine();

        // Check if the entered credentials match any saved user profiles
        User foundUser = userManager.loginUser(username, password);

        if (foundUser != null) {
            // Successfully logged in
            this.currentUser = foundUser;
            System.out.println("Welcome, " + currentUser.getUserName() + "!");
            // Continue with the rest of the logic...
        } else {
            System.out.println("Invalid username or password. Please try again.");
            promptUserAuthentication(); // Retry authentication
        }
    }



    public void displayMenu(){
        Scanner scanner=new Scanner(System.in);
        int option;
        System.out.println("Welcome to the shopping manager system. Please select an option: ");
        System.out.println("1. Add a new product");
        System.out.println("2. Delete a new product");
        System.out.println("3. Print the list of products");
        System.out.println("4. Save the list of products to a file");
        System.out.println("5. Back to main menu");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");

        try{
            option=scanner.nextInt();
        }catch(InputMismatchException e){
            System.out.println("Invalid input. Please enter a number between 1 and 5.");
            return; //insert break if want to display menu again
        }
        switch(option){
            case 1:
                addProduct();
                break;
            case 2:
                removeProduct();
                break;
            case 3:
                printProduct();
                break;
            case 4:
                saveProduct();
                break;
            case 5:
                System.out.println("Returning to the main menu...");
                start();
                break;
            case 6:
                System.out.println("Exiting the system...");
                System.exit(0);
            default:
                System.out.println("Invalid input. Please enter a number from the menu");
                displayMenu();
                break;
        }
    }


    @Override
    public void addProduct() {
        // Check if the maximum limit of 50 products has been reached
        if (productlist.size() >= 50) {
            System.out.println("Maximum limit of 50 products reached. Cannot add more products.");
            displayMenu(); // Return to the main menu
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean addingMore = true;

        while (addingMore) {
            System.out.print("What type of product do you want to add? (clothing(c)/electronics(e))");
            String productType = scanner.nextLine().toLowerCase();

            // Validate input
            while (!productType.equals("c") && !productType.equals("e")) {
                System.out.print("Invalid input. Please enter 'c' for Clothing or 'e' for Electronics");
                productType = scanner.nextLine().toLowerCase();
            }

            // Add product based on type
            Product newProduct;
            if (productType.equals("c")) {
                // Ask for clothing specific attributes
                System.out.print("Enter the product name: ");
                String productName = scanner.nextLine();
                System.out.print("Enter the product ID: ");
                String productID = scanner.nextLine();
                System.out.print("Enter the price of the product: ");
                int price = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter the number of available items: ");
                int numberOfAvailableItems = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter the colour of the product: ");
                String color = scanner.nextLine();
                System.out.print("Enter the size of the product: ");
                String size = scanner.nextLine();

                // Create a new Clothing object with specific attributes
                newProduct = new Clothing(color, size, productID, productName, numberOfAvailableItems, price);
            } else {
                // Ask for Electronics specific attributes
                System.out.print("Enter the product name: ");
                String productName = scanner.nextLine();
                System.out.print("Enter the product ID: ");
                String productID = scanner.nextLine();
                System.out.print("Enter the price of the product: ");
                int price = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter the number of available items: ");
                int numberOfAvailableItems = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter the brand of the product: ");
                String brand = scanner.nextLine();
                System.out.print("Enter the warranty period of the product in years: ");
                int warrantyPeriod = scanner.nextInt();
                scanner.nextLine();

                // Create a new Electronics object with specific attributes
                newProduct = new Electronics(brand, warrantyPeriod, productID, productName, numberOfAvailableItems, price);
            }


            // Add product to list and display "Successfully added message"
            productlist.add(newProduct);
            System.out.println("Product " + newProduct.getProductName() + " with ID " + newProduct.getProductID() + " successfully added!");
            SwingUtilities.invokeLater(() -> {
                if (myGUI != null) { // Check if GUI is available
                    myGUI.updateProductTable(); // Update the table on the EDT
                }
            });

            // Ask if manager wants to add more products
            System.out.print("Do you want to add another product? (y/n)");
            String choice = scanner.nextLine().toLowerCase();

            addingMore = choice.equals("y");

            // Loop back to menu if the user does not want to add another product
            if (!addingMore) {
                System.out.println("Returning to menu...");
                displayMenu();
            }
        }
    }

    public void removeProduct() {
        Scanner scanner = new Scanner(System.in);
        boolean removingMore = true;

        while (removingMore) {
            System.out.print("Enter the ID of the product you want to remove: ");

            String productId;
            try {
                productId = scanner.nextLine();
            } catch (InputMismatchException var7) {
                System.out.print("Invalid input. Please enter a valid product ID.");
                return;
            }

            Product productToRemove = null;
            Iterator var5 = this.productlist.iterator();

            while (var5.hasNext()) {
                Product product = (Product) var5.next();
                if (product.getProductID().equals(productId)) {
                    productToRemove = product;
                    break;
                }
            }

            if (productToRemove == null) {
                System.out.println("Product with ID " + productId + " not found.");
                return;
            }

            int availableItems = productToRemove.getNumberOfAvailableItems();
            String category;
            if (availableItems > 0) {
                productToRemove.setNumberOfAvailableItems(availableItems - 1);
                if (this.myGUI != null) {
                    SwingUtilities.invokeLater(() -> {
                        this.myGUI.updateProductTable();
                    });
                }

                category = productToRemove instanceof Clothing ? "Clothing" : "Electronics";
                System.out.println(category + " item with ID " + productId + " successfully removed.");
                System.out.println("Total products left in the system: " + (this.productlist.size()-1));

                // Remove the product from the list
                productlist.remove(productToRemove);

                // Save the updated list to the file
                saveProduct();

            } else {
                System.out.println("Product with ID " + productId + " is out of stock.");
            }

            System.out.print("Do you want to delete another product? (y/n)");
            category = scanner.nextLine().toLowerCase();
            removingMore = category.equals("y");
            if (!removingMore) {
                System.out.println("Returning to menu... ");
                this.displayMenu();
            }
        }
    }

    @Override
    public void printProduct() {
        // Check if list is empty
        if (productlist.isEmpty()) {
            System.out.println("No products found in the system.");
            return;
        }

        // Sort the products alphabetically by product ID
        Collections.sort(productlist, Comparator.comparing(Product::getProductID));

        // Loop through each product and print information
        for (Product product : productlist) {
            System.out.println("**Product:** " + product.getProductName());
            System.out.println(" - ID: " + product.getProductID());
            System.out.println(" - Price: £" + product.getPrice());
            System.out.println(" - Available Items: " + product.getNumberOfAvailableItems());

            // Specify category based on product type
            if (product instanceof Electronics) {
                System.out.println(" - Category: Electronics");
            } else if (product instanceof Clothing) {
                System.out.println(" - Category: Clothing");
            }

            // Print separator for each product
            System.out.println("-------------");
        }

        // After displaying products, return to the main menu
        System.out.println("Press 'Enter' to return to the main menu...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();  // Wait for user to press Enter
        displayMenu();
    }
    public void saveProduct() {
        try {
            File products = new File("Products.txt");
            boolean products_created = products.createNewFile();
            if (products_created) {
                System.out.println("Saved to file successfully!");
            }

            if (products.exists()) {
                try {
                    FileWriter myWriter = new FileWriter("Products.txt");
                    BufferedWriter bw = new BufferedWriter(myWriter);
                    for (Product product : productlist) {
                        bw.write(String.valueOf(product));
                        bw.write("\n");
                    }
                    bw.close();
                    myWriter.close();
                    System.out.println("Successfully saved to the file.");
                    displayMenu(); // Call the displayMenu() method to go back to the main menu
                } catch (IOException ex) {
                    System.out.println("An error occurred.");
                }
            } else {
                System.out.println("Error");
            }
        } catch (IOException ex) {
            System.out.println("Error while saving");
        }
    }

    // Inside the WestminsterShoppingManager class
    public void loadProducts() {
        Set<String> addedProductIds = new HashSet<>();
        try {
            File productsFile = new File("Products.txt");
            if (productsFile.exists()) {
                Scanner fileScanner = new Scanner(productsFile);

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    // Implement a method to convert the string back to a Product object
                    Product loadedProduct = convertStringToProduct(line);
                    if (loadedProduct != null && !addedProductIds.contains(loadedProduct.getProductID())) {
                        productlist.add(loadedProduct);
                        addedProductIds.add(loadedProduct.getProductID());
                    }
                }

                fileScanner.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading products from file: " + e.getMessage());
        }
    }

    //Convert the string representation back to a Product object
    private Product convertStringToProduct(String line) {
        if (line.startsWith("Clothing{")) {
            // Extract values from the line for Clothing
            String[] parts = line.split(", ");
            if (parts.length < 6) {
                // Invalid format, return null or throw an exception
                return null;
            }

            String id = extractStringValue(parts[2]);
            String name = extractStringValue(parts[3]);
            int price = extractIntValue(parts[5]);
            int availableItems = extractIntValue(parts[4]);
            String color = extractStringValue(parts[0]);
            String size = extractStringValue(parts[1]);

            return new Clothing(color, size, id, name, availableItems, price);
        } else if (line.startsWith("Electronics{")) {
            // Extract values from the line for Electronics
            String[] parts = line.split(", ");
            if (parts.length < 6) {
                // Invalid format, return null or throw an exception
                return null;
            }

            String id = extractStringValue(parts[2]);
            String name = extractStringValue(parts[3]);
            int price = extractIntValue(parts[5]);
            int availableItems = extractIntValue(parts[4]);
            String brand = extractStringValue(parts[0]);
            int warrantyPeriod = extractIntValue(parts[1]);

            return new Electronics(brand, warrantyPeriod, id, name, availableItems, price);
        }

        // Default case, return null or throw an exception for unknown product type
        return null;
    }

    //Extracts a numerical value from a string that's formatted like a key-value pair
    private int extractIntValue(String part) {
        try {
            return Integer.parseInt(part.split("=")[1].replaceAll("[^0-9]", ""));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Handle the exception (print an error message, log, etc.)
            e.printStackTrace();
            return 0;  // or throw an exception
        }
    }

    //Extracts a text value from a string that's formatted like a key-value pair
    private String extractStringValue(String part) {
        return part.split("=")[1].replaceAll("'", "");
    }



    public static void main(String[] args){
        WestminsterShoppingManager manager=new WestminsterShoppingManager();
        manager.start();
    }


}

