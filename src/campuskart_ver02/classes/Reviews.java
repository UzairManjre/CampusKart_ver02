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

public class Reviews {
    private final int r_id;
    private final Product product;
    private final Student student;
    private int rating;
    private String comment;
    private Timestamp timestamp;

    public Reviews(int r_id, Product product, Student student, int rating, String comment, Timestamp timestamp) {
        this.r_id = r_id;
        this.product = product;
        this.student = student;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public int getReviewId() { return r_id; }
    public Product getProduct() { return product; }
    public Student getStudent() { return student; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public Timestamp getTimestamp() { return timestamp; }

    public void setRating(int rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
