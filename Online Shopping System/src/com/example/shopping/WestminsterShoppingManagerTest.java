package com.example.shopping;

import com.example.shopping.WestminsterShoppingManager;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class WestminsterShoppingManagerTest {
    private WestminsterShoppingManager manager;

    private final String TEST_PRODUCTS_FILE_PATH = "testProducts.txt";

    @BeforeEach
    void setUp() {
        // Set up any required initialization before each test
    }

    @AfterEach
    void tearDown() {
        // Clean up any resources after each test
        File testFile = new File(TEST_PRODUCTS_FILE_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void addProduct_validInput_productAdded() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        ByteArrayInputStream in = new ByteArrayInputStream("e\nTestProduct\nP001\n50\n10\nSony\n2\nn".getBytes());
        System.setIn(in);

        shoppingManager.addProduct();

        assertEquals(1, shoppingManager.productlist.size());
        assertEquals("TestProduct", shoppingManager.productlist.get(0).getProductName());
    }

    @Test
    void removeProduct_validProductId_productRemoved() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        Product testProduct = new Electronics("Sony", 2, "P001", "TestProduct", 10, 50);
        shoppingManager.productlist.add(testProduct);

        ByteArrayInputStream in = new ByteArrayInputStream("P001\nn".getBytes());
        System.setIn(in);

        shoppingManager.removeProduct();

        assertEquals(0, shoppingManager.productlist.size());
    }

    @Test
    void printProduct_nonEmptyList_productsPrinted() {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        Product testProduct = new Electronics("Sony", 2, "P001", "TestProduct", 10, 50);
        shoppingManager.productlist.add(testProduct);

        ByteArrayInputStream in = new ByteArrayInputStream("\n".getBytes());
        System.setIn(in);

        // Redirect System.out to capture printed output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        shoppingManager.printProduct();

        String expectedOutput = "**Product:** TestProduct\n" +
                " - ID: P001\n" +
                " - Price: £50\n" +
                " - Available Items: 10\n" +
                " - Category: Electronics\n" +
                "-------------\n" +
                "Press 'Enter' to return to the main menu...\n";

        assertEquals(expectedOutput, outContent.toString());
    }

//    @Test
//    void saveProduct_validProducts_productsSavedToFile() throws IOException {
//        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
//        Product testProduct = new Electronics("Sony", 2, "P001", "TestProduct", 10, 50);
//        shoppingManager.productlist.add(testProduct);
//
//        // Set the file path to the test file
//        shoppingManager.CW 01\Products.txt(TEST_PRODUCTS_FILE_PATH);
//
//        shoppingManager.saveProduct();
//
//        // Read the content of the test file
//        String fileContent = Files.readString(Path.of(TEST_PRODUCTS_FILE_PATH));
//
//        String expectedContent = "Electronics{Sony, 2, P001, TestProduct, 10, 50}\n";
//
//        assertEquals(expectedContent, fileContent);
//    }
//
//    @Test
//    void loadProducts_validFile_productsLoaded() {
//        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
//
//        // Set the file path to the test file
//        shoppingManager.setProductsFilePath(TEST_PRODUCTS_FILE_PATH);
//
//        // Create a test file with product data
//        String testFileContent = "Electronics{Sony, 2, P001, TestProduct, 10, 50}\n";
//        try {
//            Files.writeString(Path.of(TEST_PRODUCTS_FILE_PATH), testFileContent);
//        } catch (IOException e) {
//            fail("Failed to create test file.");
//        }
//
//        shoppingManager.loadProducts();
//
//        assertEquals(1, shoppingManager.productlist.size());
//        assertEquals("TestProduct", shoppingManager.productlist.get(0).getProductName());
//    }
}
