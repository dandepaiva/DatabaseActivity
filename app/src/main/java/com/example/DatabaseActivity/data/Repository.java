package com.example.DatabaseActivity.data;

import com.example.DatabaseActivity.MyDBHandler;
import com.example.DatabaseActivity.Product;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository {

    private Executor executor = Executors.newFixedThreadPool(3);

    /**
     * Bill Pugh Singleton Implementation
     */
    private Repository() {
    }

    public static Repository getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * insert a product using a new thread
     *
     * @param product  Product to be added to the database
     * @param updateUI Interface to communicate the results to the UIThread
     */
    public void insertProduct(final Product product, final UpdateUI updateUI) {
        Runnable addRunnable = new Runnable() {
            @Override
            public void run() {
                MyDBHandler.addProduct(product);
                updateUI.updateProgress(product.getProductName());
            }
        };

        executor.execute(addRunnable);
    }

    /**
     * find a product in the database using a new thread
     *
     * @param productName String of the name of the product to lookup
     * @param updateRV    Interface to communicate the results to the UIThread
     */
    public void findProduct(final String productName, final ProductsArrayList updateRV) {
        Runnable findRunnable = new Runnable() {
            @Override
            public void run() {
                ArrayList<Product> productsList = MyDBHandler.findProduct(productName);
                updateRV.productsArrayList(productsList);
            }
        };
        executor.execute(findRunnable);
    }

    /**
     * delete a product from the database using a new thread
     *
     * @param productName   String with the name of the product to delete
     * @param deleteSuccess Interface to communicate the results to the UIThread
     */
    public void deleteProduct(final String productName, final IsSuccess deleteSuccess) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                deleteSuccess.isSuccess(MyDBHandler.deleteProduct(productName));
            }
        };
        executor.execute(deleteRunnable);
    }

    /**
     *
     */
    public interface UpdateUI {
        /**
         * @param text String with the text you want to communicate
         */
        void updateProgress(String text);
    }

    public interface ProductsArrayList {
        /**
         * @param products Array of Products to communicate
         */
        void productsArrayList(ArrayList<Product> products);
    }

    public interface IsSuccess {
        /**
         * @param success Boolean to communicate
         */
        void isSuccess(boolean success);

    }

    private static class Singleton {
        private static final Repository INSTANCE = new Repository();
    }
}
