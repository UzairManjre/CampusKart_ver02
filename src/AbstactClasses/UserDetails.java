/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbstactClasses;

/**
 *
 * @author uzair
 */

public abstract class UserDetails {
    protected String username;
    protected String enrollmentNumber;
    protected String password;
    protected String email;
    protected String role; // Student or Mod

    public UserDetails(String username, String enrollmentNumber, String password, String email, String role) {
        this.username = username;
        this.enrollmentNumber = enrollmentNumber;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public abstract void displayUserDetails();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
