/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campuskart_ver02.classes;

import AbstactClasses.ProductDetails;
import Interfaces.ProductAction;

/**
 *
 * @author uzair
 */

public class Product extends ProductDetails implements ProductAction {
    private static int productCounter = 0;  // Static counter for unique product IDs
    private int productId;  // Instance variable for each product's ID

    public Product(String productName, String description, double price, String category, int quantity, Student seller) {
        super(productName, description, price, category, quantity, seller);
    }
    public Product(int productId,String productName, String description, double price, String category, int quantity, Student seller) {
        super(productName, description, price, category, quantity, seller);
        this.productId = productId;
    }
    @Override
    public void addProduct() {
        System.out.println("Product added: " + productName);

    }

    @Override
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    @Override
    public void updateProduct(String newDescription, double newPrice, int newQuantity) {
        this.description = newDescription;
        this.price = newPrice;
        this.quantity = newQuantity;
        System.out.println("Product updated: " + productName);
    }

    @Override
    public void deleteProduct() {
        System.out.println("Product deleted: " + productName);
    }

    @Override
    public void buyProduct() {
        if (quantity > 0) {
            quantity--;
            System.out.println("Product bought: " + productName);
        } else {
            System.out.println("Out of stock: " + productName);
        }
    }

    @Override
    public void sellProduct() {
        quantity++;
        System.out.println("Product restocked: " + productName);
    }

    public Student getSeller() {
        return seller;
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }
}
