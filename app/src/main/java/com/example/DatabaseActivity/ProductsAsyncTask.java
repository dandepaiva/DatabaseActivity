package com.example.DatabaseActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;


public class ProductsAsyncTask extends AsyncTask<Void, Integer, Void> {
    public static final String TAG = "ProductsAsyncTask";
    private LoaderComunication loaderComunication;
    int counter;

    WeakReference<Activity> activityMain;

    public ProductsAsyncTask(Activity activityMain) {
        this.activityMain = new WeakReference<>(activityMain);
        if(activityMain instanceof LoaderComunication){
            loaderComunication = (LoaderComunication) activityMain;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i=counter; i < 1000; i++) {
            Log.d(TAG, "doInBackground() called with count: " + i);

            Product findProduct = new Product(MyApplication.getContext().getString(R.string.async_throttle, i), i);
            publishProgress(i);
            MyDBHandler.addProduct(findProduct);
            counter++;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer...progress){
        //Log.d(TAG, "onProgressUpdate() called with: progress = [" + loaderComunication + "]");
        if (loaderComunication != null){
            loaderComunication.sendProgress(progress[0]);
        }
    }


    @Override
    protected void onPostExecute(Void result) {

    }
}
