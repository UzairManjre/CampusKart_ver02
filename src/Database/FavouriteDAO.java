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


import campuskart_ver02.classes.Favourite;
import campuskart_ver02.classes.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Database.ProductDAO.getProductById;

public class FavouriteDAO {
    
    private Connection conn;

    public FavouriteDAO() {
        conn = DatabaseConnection.initializeDB();
    }

    // Add a product to a user's favourites
    public boolean addFavourite(int clientId, int productId) {
        String query = "INSERT INTO Favourites (c_id, product_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding favourite: " + e.getMessage());
            return false;
        }
    }

    // Remove a product from a user's favourites
    public boolean removeFavourite(int clientId, int productId) {
        String query = "DELETE FROM Favourites WHERE c_id = ? AND product_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing favourite: " + e.getMessage());
            return false;
        }
    }

    // Get all favourite products of a user
// Get all favourite products of a user with product details
    public List<Product> getFavouriteProductsByClient(int clientId) {
        List<Product> favouriteProducts = new ArrayList<>();
        String query = "SELECT p.pr_id, p.pname, p.p_price, p.pdesc " +
                "FROM Favourites f " +
                "JOIN Products p ON f.product_id = p.pr_id " +
                "WHERE f.c_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = getProductById(rs.getInt("pr_id"));

                favouriteProducts.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching favourite products: " + e.getMessage());
        }
        return favouriteProducts;
    }

}
