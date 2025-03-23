/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campuskart_ver02.classes;

import AbstactClasses.UserDetails;
import Exceptions.UnauthorizedActionException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author uzair
 */

public class Transaction {
    private Student buyer;
    private Student seller;
    private Product product;

    // List to store all transactions
    private static List<Transaction> transactionList = new ArrayList<>();

    public Transaction(Student buyer, Student seller, Product product) {
        this.buyer = buyer;
        this.seller = seller;
        this.product = product;
        transactionList.add(this);
    }

    public Student getBuyer() {
        return buyer;
    }

    public Student getSeller() {
        return seller;
    }

    public Product getProduct() {
        return product;
    }

    public static List<Transaction> getUserTransactions(Student user) {
        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction t : transactionList) {
            if (t.getBuyer().equals(user) || t.getSeller().equals(user)) {
                userTransactions.add(t);
            }
        }
        return userTransactions;
    }

    public static List<Transaction> getAllTransactions(UserDetails user) throws UnauthorizedActionException {
        if (user instanceof Moderator) {  // Now valid check
            return new ArrayList<>(transactionList);
        }
        throw new UnauthorizedActionException("Unauthorized action! You do not have the required permissions to perform this action.");
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "Buyer=" + buyer.getUsername() +
                ", Seller=" + seller.getUsername() +
                ", Product=" + product.getProductName() +
                '}';
    }
}
