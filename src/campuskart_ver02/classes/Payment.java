/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campuskart_ver02.classes;

/**
 *
 * @author uzair
 */

import java.sql.Timestamp;

public class Payment {
    private int payId;
    private int transactionId;
    private double amount;
    private String paymentStatus;
    private String paymentMethod;
    private Timestamp paymentDate;

    // Constructor
    public Payment(int payId, int transactionId, double amount, String paymentStatus, String paymentMethod, Timestamp paymentDate) {
        this.payId = payId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    // Getters
    public int getPayId() {
        return payId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    // Setters
    public void setPayId(int payId) {
        this.payId = payId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    // ToString for debugging
    @Override
    public String toString() {
        return "Payment{" +
                "payId=" + payId +
                ", transactionId=" + transactionId +
                ", amount=" + amount +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
