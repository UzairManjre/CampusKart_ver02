
import AbstactClasses.UserDetails;
import Database.*;
import Exceptions.InsufficientStockException;
import Exceptions.InvalidInputException;
import Exceptions.ProductNotFoundException;
import Exceptions.UnauthorizedActionException;
import Exceptions.UserAlreadyExistsException;
import campuskart_ver02.classes.Moderator;
import campuskart_ver02.classes.Product;
import campuskart_ver02.classes.Storage;
import campuskart_ver02.classes.Student;
import campuskart_ver02.classes.Transaction;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static Database.ModeratorDAO.addModerator;
import static Database.StudentDAO.addStudent;
import static Database.StudentDAO.getStudentByUsername;
import static campuskart_ver02.classes.Storage.addTransaction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author uzair
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDetails loggedInUser;

    public static void main(String[] args) {
        try {
//            Storage.initializeTestData();
            DatabaseSetup.main(new String[]{});

            while (true) {
                System.out.println("\n=== CampusKart ===");
                if (loggedInUser == null) {
                    System.out.println("1. Login");
                    System.out.println("2. Register");
                    System.out.println("3. Exit");
                    System.out.print("Choose an option: ");

                    int choice;
                    try {
                        choice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        if (choice < 1 || choice > 3) {
                            throw new InvalidInputException("Invalid choice! Please enter a valid option (1-3).");
                        }
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (Exception e) {
                        System.out.println("Invalid input! Please enter a number.");
                        scanner.nextLine(); // Clear invalid input
                        continue;
                    }

                    try {
                        if (choice == 1) {
                            login();
                        } else if (choice == 2) {
                            registerUser();
                        } else if (choice == 3) {
                            System.out.println("Exiting CampusKart. Goodbye!");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                } else {
                    try {
                        showMenu();
                    } catch (Exception e) {
                        System.out.println("An error occurred while processing your request: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("A critical error occurred: " + e.getMessage());
        } finally {
            scanner.close(); // Ensure scanner is closed properly when exiting
        }
    }

private static void login() {
    System.out.print("Enter username: ");
    String username = scanner.nextLine();

    System.out.print("Enter password: ");
    String password = scanner.nextLine();

    loggedInUser = UserDAO.loginUser(username, password);


    if (loggedInUser != null) {
        System.out.println("Login successful! Welcome, " + loggedInUser.getUsername());
        // You can now use user.getUserType() if needed
    } else {
        System.out.println("Invalid username or password. Try again.");
    }
}


   private static void registerUser() {
    System.out.print("Enter username: ");
    String username = scanner.nextLine();

    System.out.print("Enter password: ");
    String password = scanner.nextLine();

    System.out.print("Enter email: ");
    String email = scanner.nextLine();

    System.out.print("Are you registering as a Moderator or Student? (M/S): ");
    String role = scanner.nextLine().trim().toUpperCase();

    System.out.print("Enter enrollment number: ");
    String enrollmentNumber = scanner.nextLine();

    boolean success = false;
    
    if (role.equals("M")) {
        success = addModerator(username, password, email, enrollmentNumber);
    } else if (role.equals("S")) {
        Student student = new Student(username, password, email, enrollmentNumber);
        success = addStudent(username,password, email, enrollmentNumber);
    } else {
        System.out.println("Invalid choice. Registration failed.");
        return;
    }

    if (success) {
        System.out.println("Registration successful!");
    } else {
        System.out.println("Registration failed. Try again.");
    }
}



    private static void showMenu() {
        if (loggedInUser instanceof Student) {
            studentMenu();
        } else if (loggedInUser instanceof Moderator) {
            moderatorMenu();
        }
    }

    private static void studentMenu() {
        while (true) {
            try {
                System.out.println("\n1. View Products");
                System.out.println("2. Buy Product");
                System.out.println("3. View My Orders");
                System.out.println("4. Add Product");
                System.out.println("5. View My Favourite Products");
                System.out.println("6. Add Product to Favourites");
                System.out.println("7. Remove Product from Favourites");
                System.out.println("8. Logout");
                System.out.print("Choose an option: ");

                int choice;
                try {
                    if (!scanner.hasNextInt()) {
                        scanner.next(); // Clear invalid input
                        throw new InvalidInputException("Invalid input! Please enter a number between 1 and 8.");
                    }
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice < 1 || choice > 8) {
                        throw new InvalidInputException("Invalid choice! Please enter a number between 1 and 8.");
                    }
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                switch (choice) {
                    case 1:
                        viewProducts();
                        break;
                    case 2:
                        buyProduct();
                        break;
                    case 3:
                        viewMyOrders();
                        break;
                    case 4:
                        addProduct();
                        break;
                    case 5:
                        viewFavouriteProducts();
                        break;
                    case 6:
                        addToFavourites();
                        break;
                    case 7:
                        removeFromFavourites();
                        break;
                    case 8:
                        loggedInUser = null;
                        System.out.println("Logged out successfully.");
                        return;
                    default: throw new InvalidInputException("Unexpected error: Invalid menu choice.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }
    private static void moderatorMenu() {
        while (true) {
            try {
                System.out.println("\n1. View All Transactions");
                System.out.println("2. Logout");
                System.out.print("Choose an option: ");

                int choice;
                try {
                    if (!scanner.hasNextInt()) {
                        scanner.next(); // Clear invalid input
                        throw new InvalidInputException("Invalid input! Please enter either 1 or 2.");
                    }
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice < 1 || choice > 2) {
                        throw new InvalidInputException("Invalid choice! Please enter 1 or 2.");
                    }
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                switch (choice) {
                    case 1 : viewAllTransactions();break;
                    case 2 : {
                        loggedInUser = null;
                        System.out.println("Logged out successfully.");
                        return;
                    }
                    default : throw new InvalidInputException("Unexpected error: Invalid menu choice.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void viewProducts() {
        try {
            List<Product> products = ProductDAO.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("\nNo products available.");
                return;
            }

            System.out.println("\nAvailable Products:");
            for (Product p : products) {
                System.out.println(p.getProductId() + ". " + p.getProductName() + " - " + p.getPrice() + " - " + p.getQuantity());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while retrieving products: " + e.getMessage());
        }
    }



    private static void buyProduct() {
        try {
            System.out.print("Enter Product ID to buy: ");

            if (!scanner.hasNextInt()) {
                scanner.next(); // Clear invalid input
                throw new InvalidInputException("Invalid input! Please enter a valid Product ID.");
            }

            int productId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Product product = ProductDAO.getProductById(productId);
            if (product == null) {
                throw new ProductNotFoundException("Product with ID " + productId + " not found!");
            }

            if (!(loggedInUser instanceof Student)) {
                throw new UnauthorizedActionException("Only students can buy products.");
            }

            if (product.getQuantity() <= 0) {
                throw new InsufficientStockException("Product is out of stock!");
            }

            // Proceed with the purchase

            Student buyer = getStudentByUsername(loggedInUser.getUsername());
            Student seller = getStudentByUsername(product.getSeller().getUsername());

            Transaction transaction = new Transaction(buyer, seller, product);
            addTransaction(transaction);
            TransactionDAO.addTransaction(productId, buyer.getClientId(), seller.getClientId());


            // Decrease quantity
            product.setQuantity(product.getQuantity() - 1);
            ProductDAO.updateProductQuantity(productId, product.getQuantity());

            System.out.println("You bought " + product.getProductName() + "!");

        } catch (ProductNotFoundException | UnauthorizedActionException | InvalidInputException | InsufficientStockException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    private static void addProduct() {
        try {
            if (!(loggedInUser instanceof Student)) {
                throw new UnauthorizedActionException("Only students can add products!");
            }

            System.out.print("Enter product name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) throw new InvalidInputException("Product name cannot be empty.");

            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) throw new InvalidInputException("Product description cannot be empty.");

            System.out.print("Enter price: ");
            if (!scanner.hasNextDouble()) {
                scanner.next(); // Clear invalid input
                throw new InvalidInputException("Invalid input! Please enter a valid price.");
            }
            double price = scanner.nextDouble();
            scanner.nextLine();
            if (price <= 0) throw new InvalidInputException("Price must be greater than zero.");

            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim();
            if (category.isEmpty()) throw new InvalidInputException("Category cannot be empty.");

            System.out.print("Enter quantity: ");
            if (!scanner.hasNextInt()) {
                scanner.next(); // Clear invalid input
                throw new InvalidInputException("Invalid input! Please enter a valid quantity.");
            }
            int quantity = scanner.nextInt();
            scanner.nextLine();
            if (quantity <= 0) throw new InvalidInputException("Quantity must be greater than zero.");
// Fetch the Student object using the logged-in user's username
            Student seller = StudentDAO.getStudentByUsername(loggedInUser.getUsername());
            if (seller == null) {
                throw new UnauthorizedActionException("Seller not found! Please ensure you have a valid student account.");
            }

// Create a new Product object with the fetched seller
            Product newProduct = new Product(name, description, price, category, quantity, seller);

            boolean success = ProductDAO.addProduct(newProduct);

            if (success) {
                System.out.println("Product added successfully!");
            } else {
                System.out.println("Failed to add product. Please try again.");
            }

        } catch (InvalidInputException | UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    private static void viewMyOrders() {
        try {
            if (loggedInUser == null) {
                throw new UnauthorizedActionException("You must be logged in to view your orders!");
            }

            List<Transaction> orders = TransactionDAO.getUserTransactions(getStudentByUsername(loggedInUser.getUsername()).getClientId()); // Fetch transactions for the logged-in user

            if (orders.isEmpty()) {
                System.out.println("\nYou have no orders.");
                return;
            }

            System.out.println("\nYour Orders:");
            for (Transaction t : orders) {
                System.out.println(t);
            }

        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while retrieving orders: " + e.getMessage());
        }
    }


    // Function to view favorite products


    // Function to add a product to favourites
    private static void addToFavourites() {
        try {
            if (loggedInUser == null) {
                throw new UnauthorizedActionException("You must be logged in to add favourite products!");
            }

            System.out.print("Enter Product ID to add to favourites: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            FavouriteDAO favouriteDAO = new FavouriteDAO();
            if (favouriteDAO.addFavourite(getStudentByUsername(loggedInUser.getUsername()).getClientId(), productId)) {
                System.out.println("Product added to favourites successfully!");
            } else {
                System.out.println("Failed to add product to favourites.");
            }

        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while adding to favourites: " + e.getMessage());
        }
    }

    // Function to remove a product from favourites
    private static void removeFromFavourites() {
        try {
            if (loggedInUser == null) {
                throw new UnauthorizedActionException("You must be logged in to remove favourite products!");
            }

            System.out.print("Enter Product ID to remove from favourites: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            FavouriteDAO favouriteDAO = new FavouriteDAO();
            if (favouriteDAO.removeFavourite(Objects.requireNonNull(getStudentByUsername(loggedInUser.getUsername())).getClientId(), productId)) {
                System.out.println("Product removed from favourites successfully!");
            } else {
                System.out.println("Failed to remove product from favourites.");
            }

        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while removing from favourites: " + e.getMessage());
        }
    }
    private static void viewFavouriteProducts() {
        try {
            if (loggedInUser == null) {
                throw new UnauthorizedActionException("You must be logged in to view your favourite products!");
            }

            FavouriteDAO favouriteDAO = new FavouriteDAO();
            List<Product> favourites = favouriteDAO.getFavouriteProductsByClient(Objects.requireNonNull(getStudentByUsername(loggedInUser.getUsername())).getClientId());

            if (favourites.isEmpty()) {
                System.out.println("\nYou have no favourite products.");
                return;
            }

            System.out.println("\nYour Favourite Products:");
            for (Product p : favourites) {
                System.out.println("ID: " + p.getProductId() + ", Name: " + p.getProductName() + ", Price: " + p.getPrice() + ", Description: " + p.getDescription());
            }

        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while retrieving favourite products: " + e.getMessage());
        }
    }


    private static void viewAllTransactions() {
        try {
            if (!(loggedInUser instanceof Moderator)) {
                throw new UnauthorizedActionException("Only moderators can view all transactions!");
            }

            List<Transaction> transactions = TransactionDAO.getAllTransactions(); // Fetch all transactions

            if (transactions.isEmpty()) {
                System.out.println("\nNo transactions available.");
                return;
            }

            System.out.println("\nAll Transactions:");
            for (Transaction t : transactions) {
                System.out.println(t);
            }

        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while retrieving transactions: " + e.getMessage());
        }
    }



}