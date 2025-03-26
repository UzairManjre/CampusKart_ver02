/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author uzair
 */




import campuskart_ver02.classes.Product;
import campuskart_ver02.classes.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Database.StudentDAO.getStudentById;

public class ProductDAO {
    
    public static boolean addProduct(Product product) {
        String insertProductQuery = "INSERT INTO Products (pname, pdesc, p_price, qty, category, status, s_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        System.out.println("Seller ID: " + product.getSeller().getClientId());

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(insertProductQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity());
            pstmt.setString(5, product.getCategory());
            pstmt.setString(6, "Available");  // Default status
            pstmt.setInt(7, product.getSeller().getClientId()); // Get seller's ID

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int productId = generatedKeys.getInt(1);
                        product.setProductId(productId);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
        return false;
    }
        // Method to fetch all products
  public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products";
//      System.out.println("______________________________________________________________-");
        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
//            System.out.println("++++++++++++++++++++++++");
            while (rs.next()) {
                int sellerId = rs.getInt("s_id");
                System.out.println(sellerId);
//                System.out.println("++++++++++++++++++++++++");
                Student seller = getStudentById(sellerId); // Fetch full seller details
                if (seller == null) {
                    System.err.println("Warning: No seller found for ID " + sellerId);
                    continue; // Skip this product if no seller found
                }

                System.out.println("Fetching seller for ID: " + sellerId);


                Product product = new Product(
                    rs.getInt("pr_id"),
                    rs.getString("pname"),
                    rs.getString("pdesc"),
                    rs.getDouble("p_price"),
                    rs.getString("category"),
                    rs.getInt("qty"),
                    seller
                );
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }
    // Method to fetch a product by ID
   public static Product getProductById(int productId) {
        String query = "SELECT * FROM Products WHERE pr_id = ?";
        
        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int sellerId = rs.getInt("s_id");
                Student seller = getStudentById(sellerId); // Fetch full seller details

                return new Product(
                    rs.getInt("pr_id"),
                    rs.getString("pname"),
                    rs.getString("pdesc"),
                    rs.getDouble("p_price"),
                    rs.getString("category"),
                    rs.getInt("qty"),
                    seller
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching product: " + e.getMessage());
        }
        return null;
    }
    public static boolean updateProductQuantity(int productId, int newQuantity) {
        String updateQuery = "UPDATE Products SET qty = ? WHERE pr_id = ?";

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating product quantity: " + e.getMessage());
            return false;
        }
    }

    // Method to fetch all products by a specific seller
    public static List<Product> getProductsBySeller(int sellerId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE s_id = ?";

        try (Connection conn = DatabaseConnection.initializeDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, sellerId);
            ResultSet rs = pstmt.executeQuery();
            Student seller = getStudentById(sellerId);

            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("pr_id"),
                    rs.getString("pname"),
                    rs.getString("pdesc"),
                    rs.getDouble("p_price"),
                    rs.getString("category"),
                    rs.getInt("qty"),
                   seller
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching seller's products: " + e.getMessage());
        }
        return products;
    }


}
