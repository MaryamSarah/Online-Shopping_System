package com.example.shopping;

public abstract class Product {
    public String productID;
    public String productName;
    public int numberOfAvailableItems;
    public int price;
    private int quantity=0;
    public Product(){
        this.productID=productID;
        this.productName=productName;
        this.numberOfAvailableItems=numberOfAvailableItems;
        this.price=price;
    }

    public Product(String name, int id, int price, int availableItems) {
        this.productID=productID;
        this.productName=productName;
        this.numberOfAvailableItems=numberOfAvailableItems;
        this.price=price;
    }

    public void setProductID(String productID){
        this.productID=productID;
    }
    public void setProductName(String productName){
        this.productName=productName;
    }
    public void setNumberOfAvailableItems(int numberOfAvailableItems){
        this.numberOfAvailableItems=numberOfAvailableItems;
    }
    public void setPrice(int price){
        this.price=price;
    }
    public String getProductID(){
        return this.productID;
    }
    public String getProductName(){
        return this.productName;
    }
    public int getNumberOfAvailableItems(){
        return this.numberOfAvailableItems;
    }
    public int setPrice(){
        return this.price;
    }
    public int getPrice(){
        return this.price;
    }

    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

}