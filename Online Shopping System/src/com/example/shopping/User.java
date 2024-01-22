package com.example.shopping;

public class User {

    private String username;
    private String password;
    private ShoppingCart cart;
    public User(String username, String password){
        this.username=username;
        this.password=password;
        this.cart=new ShoppingCart();
    }
    public void setUserName(String username){
        this.username=username;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getUserName(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public ShoppingCart getCart() {
        return cart;
    }
}
