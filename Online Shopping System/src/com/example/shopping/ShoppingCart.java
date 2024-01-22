package com.example.shopping;
import com.example.shopping.WestminsterShoppingGUI;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShoppingCart implements Iterable<Product>, Serializable {

    private List<Product> products;


    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeFromCart(String productID) {
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product item = iterator.next();
            if (item.getProductID().equals(productID)) {
                iterator.remove();
                return;
            }
        }
    }

    public void addToCart(ShoppingCart cart, Product product) {
        boolean productInCart = false;
        for (Product p : cart.getCart()) {
            if (p.getProductID().equals(product.getProductID())) {
                p.setQuantity(p.getQuantity() + 1);
                productInCart = true;
                break;
            }
        }

        if (!productInCart) {
            product.setQuantity(1);  // Set quantity to 1 for new products
            cart.getCart().add(product);
        }
    }
    public List<Product> getCart() {
        return products;
    }

    // Load the shopping cart from a file
    public void loadCart(String filename)

            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            // Read the object
            ShoppingCart loadedCart = (ShoppingCart) ois.readObject();

            // Update the current cart with the loaded one
            this.products = loadedCart.products;
        }
    }

    // Save the shopping cart to a file
    public void saveCart(String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(products);
        oos.close();
    }

    @Override
    public Iterator<Product> iterator() {
        return products.iterator();
    }
}
