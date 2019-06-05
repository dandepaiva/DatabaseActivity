package com.example.DatabaseActivity.data;

import com.example.DatabaseActivity.MyDBHandler;
import com.example.DatabaseActivity.Product;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository {

    private Repository(){ }
    private static class Singleton{
        private static final Repository INSTANCE = new Repository();
    }

    public static Repository getInstance(){
        return Singleton.INSTANCE;
    }

    private Executor executor = Executors.newFixedThreadPool(3);


    public void insertProduct(final Product product, final UpdateUI updateUI) {
        Runnable add_runnable = new Runnable() {
            @Override
            public void run() {
                MyDBHandler.addProduct(product);
                updateUI.updateProgress(product.getProductName());
            }
        };

        executor.execute(add_runnable);
    }

    public Void findProduct(final String productName, final UpdateRecyclerView updateRV){
        Runnable find_runnable = new Runnable() {
            @Override
            public void run() {
                ArrayList<Product> productsList = MyDBHandler.findProduct(productName);
                updateRV.updateRecyclerView(productsList);
            }
        };
        executor.execute(find_runnable);
        return null;
    }

    public interface UpdateUI {
        void updateProgress(String text);
    }

    public interface UpdateRecyclerView {
        void updateRecyclerView(ArrayList<Product> products);
    }
}
