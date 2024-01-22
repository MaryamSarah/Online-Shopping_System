package com.example.shopping;

import java.io.Serializable;

public class Electronics extends Product implements Serializable {
    public String brand;
    public int warrantyPeriod;

    public Electronics(String brand, int warrantyPeriod) {
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    public Electronics(String brand, int warrantyPeriod, String productID, String productName, int numberOfAvailableItems, int price) {
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
        this.productID = productID;
        this.productName = productName;
        this.numberOfAvailableItems = numberOfAvailableItems;
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getBrand() {
        return this.brand;
    }

    public int getWarrantyPeriod() {
        return this.warrantyPeriod;
    }

    @Override
    public String toString() {
        return "Electronics{" +
                "brand='" + getBrand() + '\'' +
                ", warrantyPeriod=" + getWarrantyPeriod() +
                ", productID=" + getProductID() +
                ", productName='" + getProductName() + '\'' +
                ", numberOfAvailableItems=" + getNumberOfAvailableItems() +
                ", price=" + getPrice() +
                '}';
    }
}
