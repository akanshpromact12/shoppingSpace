package com.promact.akansh.shoppingappdemo.Model;

/**
 * Created by Akansh on 13-10-2017.
 */

public class Users {
    private String username;
    private String password;
    private String mobileno;

    public Users() {

    }

    public Users(String username, String password, String mobileno) {
        this.username = username;
        this.password = password;
        this.mobileno = mobileno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
}
