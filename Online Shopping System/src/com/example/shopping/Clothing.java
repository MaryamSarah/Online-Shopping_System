package com.example.shopping;

import java.io.Serializable;

public class Clothing extends Product implements Serializable {
    public String color;
    public String size;
    public Clothing(String color, String size) {
        this.color = color;
        this.size = size;
    }
    public Clothing(String color, String size, String productID, String productName, int numberOfAvailableItems, int price){
        this.color=color;
        this.size=size;
        this.productID=productID;
        this.productName=productName;
        this.numberOfAvailableItems=numberOfAvailableItems;
        this.price=price;
    }

    public void setColor(String color){
        this.color=color;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public String getColor(){
        return this.color;
    }
    public String getSize(){
        return this.size;
    }

    @Override
    public String toString() {
        return "Clothing{" +
                "color='" + getColor() + '\'' +
                ", size='" + getSize() + '\'' +
                ", productID=" + getProductID() +
                ", productName='" + getProductName() + '\'' +
                ", numberOfAvailableItems=" + getNumberOfAvailableItems() +
                ", price=" + getPrice() +
                '}';

    }

}
