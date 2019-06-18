package com.example.databaseActivity.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.databaseActivity.LoaderCommunication;

import java.lang.ref.WeakReference;


public class ProductsAsyncTask extends AsyncTask<Void, Integer, Void> {
    private static final String TAG = "ProductsAsyncTask";
    private LoaderCommunication loaderCommunication;

    public ProductsAsyncTask(Activity activityMain) {
        WeakReference<Activity> activityMain1 = new WeakReference<>(activityMain);
        if (activityMain instanceof LoaderCommunication) {
            loaderCommunication = (LoaderCommunication) activityMain;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
    /*      for (int i = 0; i < 1000; i++) {
            Log.d(TAG, "doInBackground() called with count: " + i);

            Product findProduct = new Product(MyApplication.getContext().getString(R.string.async_throttle, i), i);
            publishProgress(i);
            MyDBHandler.addProduct(findProduct);
        }
    */
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if (loaderCommunication != null) {
            loaderCommunication.sendProgress(progress[0]);
        }
    }


    @Override
    protected void onPostExecute(Void result) {

    }
}
