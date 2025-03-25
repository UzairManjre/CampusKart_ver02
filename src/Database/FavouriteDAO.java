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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public List<Favourite> getFavouritesByClient(int clientId) {
        List<Favourite> favourites = new ArrayList<>();
        String query = "SELECT * FROM Favourites WHERE c_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                favourites.add(new Favourite(rs.getInt("c_id"), rs.getInt("product_id")));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching favourites: " + e.getMessage());
        }
        return favourites;
    }
}
