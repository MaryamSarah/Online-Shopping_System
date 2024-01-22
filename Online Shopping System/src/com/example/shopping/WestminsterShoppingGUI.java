package com.example.shopping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import javax.swing.table.TableColumn;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.util.*;
import java.util.List;

//https://stackoverflow.com/questions/36252770/display-alternate-tostring-method-with-custom-cell-editor
//https://pt.stackoverflow.com/questions/124930/scrollbar-em-um-jtable

public class WestminsterShoppingGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel productPanel;
    private JPanel productDetailsPanel;
    private JPanel shoppingCartPanel;
    private JTable shoppingcartTable;
    private JComboBox<String> productTypeDropdown;
    private JLabel text;
    private JTable productTable;
    private JLabel itemsAvailableLabel;
    private WestminsterShoppingManager manager;
    private JFrame frame;
    private static ShoppingCart cart;

    public WestminsterShoppingGUI(WestminsterShoppingManager manager, ShoppingCart cart) {
        this.manager = manager;
        this.cart=cart;
        setTitle("Westminster Shopping Center");


        //----------------Main Panel for displaying product table, buttons, and product details panel-------------------
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        mainPanel.revalidate(); // Notify the layout manager of changes
        mainPanel.repaint(); // Repaint the component to apply the color

        //------------------------------ Top panel for buttons----------------------------------------------------------
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Dropdown for product categories
        productTypeDropdown = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        productTypeDropdown.setPreferredSize(new Dimension(80, 30));
        text = new JLabel("Select the product category");
        topPanel.add(productTypeDropdown);
        topPanel.add(text, FlowLayout.LEFT);
        productTypeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the product table based on the selected category
                updateProductTable();
            }
        });

        //Shopping Cart button in top panel
        JButton button=new JButton("Shopping Cart");
        topPanel.add(button, BorderLayout.EAST);


        //---------------------------------Shopping cart panel for user-------------------------------------------------
        frame=new JFrame("Shopping Cart");
        shoppingCartPanel = new JPanel();
        shoppingCartPanel.setLayout(new BorderLayout());
        shoppingCartPanel.setPreferredSize(new Dimension(1000,300));
        frame.add(shoppingCartPanel, BorderLayout.NORTH);


        // Adding a button panel with the "Buy from Cart" button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Creating a shopping cart table
        shoppingcartTable = new JTable();
        shoppingCartPanel.add(shoppingcartTable, BorderLayout.CENTER);

        // Add event listener to the "Shopping Cart" button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new window for the shopping cart
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the cart window

                frame.pack();
                frame.setSize(500, 400);  // Change here
                frame.setVisible(true);

                // Update the shopping cart table
                /*updateShoppingCartTable();*/
            }
        });

        // Creating a scrollpanel for the shopping cart table
        shoppingcartTable = new JTable();
        shoppingcartTable.setRowHeight(70);
        JScrollPane scrollPane1=new JScrollPane(shoppingcartTable);
        scrollPane1.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5)); // Margins around the table
        shoppingCartPanel.add(scrollPane1, BorderLayout.CENTER);
        scrollPane1.setVisible(true);
        Dimension preferredSize1 = new Dimension(500,100);
        scrollPane1.setPreferredSize(preferredSize1);

        // Add a button for displaying the success dialog
        JButton buyButton = new JButton("Buy Items");
        buttonPanel.add(buyButton, BorderLayout.SOUTH); // Add it below the "Remove from Cart" button

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display a confirmation dialog
                int result = JOptionPane.showConfirmDialog(shoppingCartPanel, "Confirm purchase of items in the cart?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(shoppingCartPanel, "Items successfully purchased!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        shoppingCartPanel.add(buttonPanel, BorderLayout.EAST);




        //------------------------------------Product Panel for product table-------------------------------------------
        productPanel = new JPanel(new BorderLayout());
        mainPanel.add(productPanel, BorderLayout.CENTER);

        //Product Table
        productTable = new JTable();
        productTable.setRowHeight(45);
        // Create a titled border for visual clarity (optional)
        productPanel.setBorder(BorderFactory.createTitledBorder("Products"));
        // Create a JScrollPane with margins
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setVisible(true);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 8, 10)); // Margins around the table
        productPanel.add(scrollPane, BorderLayout.CENTER);
        Dimension preferredSize = new Dimension(500,150);
        scrollPane.setPreferredSize(preferredSize);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);



        //-----------------------Product Details Panel for displaying individual product details-------------------------
        productDetailsPanel = new JPanel();
        itemsAvailableLabel = new JLabel();
        productDetailsPanel.add(itemsAvailableLabel);
        mainPanel.add(productDetailsPanel, BorderLayout.SOUTH);
        productDetailsPanel.setLayout(new GridLayout(0, 1)); // One column, variable rows
        productDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 18)); // Top, left, bottom, right
        productDetailsPanel.setPreferredSize(new Dimension(200, 250));
        ShoppingCart finalCart = cart;
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Product selectedProduct = manager.productlist.get(selectedRow);
                    // Clear any previous product details
                    productDetailsPanel.removeAll();

                    // Create labels for product details
                    JLabel idLabel = new JLabel("ID: " + selectedProduct.getProductID());
                    JLabel categoryLabel = new JLabel("Category: " + (selectedProduct instanceof Electronics ? "Electronics" : "Clothing"));
                    JLabel nameLabel = new JLabel("Name: " + selectedProduct.getProductName());

                    JLabel itemsAvailableLabel = new JLabel("Items Available: " + selectedProduct.getNumberOfAvailableItems());
                    int availableItems = selectedProduct.getNumberOfAvailableItems();

                    if (availableItems < 3) {
                        itemsAvailableLabel.setForeground(Color.RED);
                    } else {
                        itemsAvailableLabel.setForeground(Color.BLACK);
                    }

                    // Add common labels
                    productDetailsPanel.add(idLabel);
                    productDetailsPanel.add(categoryLabel);
                    productDetailsPanel.add(nameLabel);
                    productDetailsPanel.add(itemsAvailableLabel);

                    // Conditional labels for Electronics and Clothing
                    if (selectedProduct instanceof Electronics) {
                        Electronics electronics = (Electronics) selectedProduct;
                        JLabel brandLabel = new JLabel("Brand: " + electronics.getBrand());
                        JLabel warrantyLabel = new JLabel("Warranty: " + electronics.getWarrantyPeriod() + " years");
                        productDetailsPanel.add(brandLabel);
                        productDetailsPanel.add(warrantyLabel);
                    } else if (selectedProduct instanceof Clothing) {
                        Clothing clothing = (Clothing) selectedProduct;
                        JLabel sizeLabel = new JLabel("Size: " + clothing.getSize());
                        JLabel colourLabel = new JLabel("Colour: " + clothing.getColor());
                        productDetailsPanel.add(sizeLabel);
                        productDetailsPanel.add(colourLabel);
                    }

                    // Add the "Add to Shopping Cart" button
                    JButton addToCartButton = new JButton("Add to Shopping Cart");
                    addToCartButton.setPreferredSize(new Dimension(50, 40)); // Example size
                    addToCartButton.setHorizontalAlignment(SwingConstants.CENTER); // Center text
                    productDetailsPanel.add(addToCartButton);
                    // Action listener for the "Add to Shopping Cart" button
                    addToCartButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int selectedRow = productTable.getSelectedRow();
                            if (selectedRow >= 0) {
                                Product selectedProduct = manager.productlist.get(selectedRow);

                                // Check the available quantity before adding to the cart
                                int availableQuantity = selectedProduct.getNumberOfAvailableItems();
                                if (availableQuantity > 0) {
                                    // Find if the product already exists in the cart
                                    boolean productInCart = false;
                                    for (Product product : cart.getCart()) {
                                        if (product.getProductID().equals(selectedProduct.getProductID())) {
                                            // Increment quantity if product is already in the cart
                                            product.setQuantity(product.getQuantity() + 1);
                                            productInCart = true;
                                            break;
                                        }
                                    }

                                    if (!productInCart) {
                                        // Add the product to the shopping cart with quantity 1
                                        cart.addToCart(cart, selectedProduct);
                                    }

                                    // Update the shopping cart table
                                    updateShoppingCartTable();

                                    // Decrease the available quantity
                                    selectedProduct.setNumberOfAvailableItems(availableQuantity - 1);

                                    // Update the product table to reflect the reduced quantity
                                    updateProductTable();

                                    try {
                                        cart.saveCart("shopping_cart.data"); // Save the cart to file
                                    } catch (IOException ex) {
                                        System.err.println("Error saving shopping cart: " + ex.getMessage());
                                    }
                                } else {
                                    // Notify the user that the product is out of stock
                                    JOptionPane.showMessageDialog(WestminsterShoppingGUI.this, "Sorry, this item is out of stock.");
                                }
                            }
                        }
                    });



                    // Layout the labels
                    productDetailsPanel.revalidate();
                    productDetailsPanel.repaint();

                }
            }
        });
        updateProductTable();

    }

    public void setShoppingCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public void updateShoppingCartTable() {
        // Create the table model with the desired columns
        DefaultTableModel model = new DefaultTableModel(new String[]{"Product", "Quantity", "Price (£)"}, 0);
        shoppingcartTable.setModel(model);

        // Center the contents of all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        shoppingcartTable.setDefaultRenderer(Object.class, centerRenderer);
        shoppingcartTable.getColumnModel().getColumn(0).setCellRenderer(new MultiLineCellRenderer());

        // Create info panel with BoxLayout (Y_AXIS) if not already created
        JPanel infoPanel = (JPanel) shoppingCartPanel.getClientProperty("infoPanel");
        if (infoPanel == null) {
            infoPanel = new JPanel(new GridLayout(0, 1));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 8, 10));
            shoppingCartPanel.add(infoPanel, BorderLayout.SOUTH);
            shoppingCartPanel.putClientProperty("infoPanel", infoPanel);  // Store infoPanel as a client property
        } else {
            // Clear previous components
            infoPanel.removeAll();
        }

        double totalCost = 0.0;
        double initialTotal = 0.0;
        double firstTimeDiscount = 0.0;
        double threeItemDiscount = 0.0;
        double totalDiscount = firstTimeDiscount + threeItemDiscount;
        Map<String, Integer> categoryCounts = new HashMap<>();

        // Add rows to the table model and update totalCost
        for (Product product : cart.getCart()) {
            // Handle potential null quantity
            Integer quantity = product.getQuantity();
            if (quantity == null) {
                quantity = 0;
            }
            initialTotal += product.getPrice() * quantity;

            String category = getCategory(product);
            int count = categoryCounts.getOrDefault(category, 0) + quantity;
            categoryCounts.put(category, count);

            if (count >= 3 && isSameCategoryCount(categoryCounts, category, count)) {
                // Apply discount only if three items of the same category are present
                threeItemDiscount += product.getPrice() * 0.2 * quantity;
            }

            // Apply first-time purchase discount (if applicable)
            if (manager.isFirstTimeUser) {
                firstTimeDiscount += product.getPrice() * 0.1 * quantity;
                totalCost += product.getPrice() * 0.9 * quantity;  // Apply 10% discount
            } else {
                totalCost += product.getPrice() * quantity;
            }

            String quantityString = quantity == null ? "0" : String.valueOf(quantity);

            // Construct product information string based on type
            String productInfo = "";
            if (product instanceof Electronics) {
                Electronics electronics = (Electronics) product;
                productInfo = product.getProductName() + "\n" + electronics.getBrand() + "\n" + electronics.getWarrantyPeriod();
            } else if (product instanceof Clothing) {
                Clothing clothing = (Clothing) product;
                productInfo = product.getProductName() + "\n" + clothing.getColor() + "\n" + clothing.getSize();
            } else {
                // Handle other product types if needed
                productInfo = product.getProductName() + "\n" + product.getProductID();
            }

            // Add row to the table model
            double productPrice = product.getPrice() * (double) Integer.parseInt(quantityString);
            model.addRow(new Object[]{productInfo, quantityString, productPrice});
        }

        JLabel initialTotalLabel = new JLabel("Initial Total: £" + initialTotal);
        JLabel discount1Label = new JLabel("First Time Purchase Discount: -£" + firstTimeDiscount);
        JLabel discount2Label = new JLabel("Three Item in same Category Purchase: -£" + threeItemDiscount);
        JLabel totalLabel = new JLabel(" Final Total: £" + (totalCost - threeItemDiscount)); // Deduct threeItemDiscount from totalCost
        infoPanel.add(initialTotalLabel);
        infoPanel.revalidate();
        infoPanel.add(discount1Label);
        infoPanel.revalidate();
        infoPanel.add(discount2Label);
        infoPanel.revalidate();
        infoPanel.add(totalLabel);
        infoPanel.revalidate();

        // Apply right alignment and borders to labels
        Border marginBorder = BorderFactory.createEmptyBorder(0, 10, 0, 90);
        for (Component component : infoPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setBorder(marginBorder);
            }
        }

        // Revalidate and repaint relevant containers
        infoPanel.revalidate();
        infoPanel.repaint();
        shoppingCartPanel.revalidate();
        shoppingCartPanel.repaint();
    }


    private boolean isSameCategoryCount(Map<String, Integer> categoryCounts, String category, int count) {
        int sameCategoryCount = categoryCounts.getOrDefault(category, 0);

        // Check if there are three or more items in the same category
        return sameCategoryCount >= 3 && count == sameCategoryCount;
    }
    private String getCategory(Product product) {
        return product instanceof Electronics ? "Electronics" : "Clothing";
    }


//put items of product info column into separate lines
    class MultiLineCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JTextArea textArea = new JTextArea(value.toString());
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            // Additional styling for textArea if needed
            return textArea;
        }
    }

    public void updateProductTable() {
        // Center panel for product table:
        String[] columnNames = {"Product ID", "Product Name", "Category", "Price (£)", "Info"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        productTable.setModel(model);
        // Adjust the "Info" column width
        TableColumn infoColumn = productTable.getColumnModel().getColumn(4); // Index 5 for the "Info" column
        infoColumn.setPreferredWidth(400); // Adjust width as needed

        // Center row elements
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for(int x=0;x<model.getColumnCount();x++){
            productTable.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }

        // Color headers
        JTableHeader header = productTable.getTableHeader();
        header.setBackground(Color.decode("#4ADEDE"));

        for(Product product : manager.productlist) {
            String category = product instanceof Electronics ? "Electronics" : "Clothing";
            if (productTypeDropdown.getSelectedItem().equals("All") || productTypeDropdown.getSelectedItem().equals(category)) {
                //Check if the product is already in the table
                boolean productExists = false;
                for (int row = 0; row < model.getRowCount(); row++) {
                    if (product.getProductID().equals(model.getValueAt(row, 0))) {
                        productExists = true;
                        break;
                    }
                }
                // Add the product row with appropriate information based on category:(only if doesn't exist)
                if (!productExists) {
                    String info = "";
                    if (product instanceof Electronics) {
                        Electronics electronics = (Electronics) product;
                        info = electronics.getBrand() + " | " + electronics.getWarrantyPeriod() + " years";
                    } else if (product instanceof Clothing) {
                        Clothing clothing = (Clothing) product;
                        info = clothing.getColor() + " | " + clothing.getSize();
                    }
                    model.addRow(new Object[]{product.getProductID(), product.getProductName(), category, product.getPrice(), info});
                }
            }
        }

        // Display the GUI:
        pack();
        setSize(1000, 600); // Adjust size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        WestminsterShoppingGUI myGUI = new WestminsterShoppingGUI(manager, cart);
        myGUI.setVisible(true);
    }

}