package com.example.DatabaseActivity.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.DatabaseActivity.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class Remote {
    private Executor executor = Executors.newFixedThreadPool(3);
    private String TAG = "REMOTE EXECUTION";

    private Set<SendData> sendDataSet = new HashSet<>();

    private Remote() {
    }

    static Remote getInstance() {
        return Singleton.INSTANCE;
    }

    void getRemote(final SendData sendData) {
        addSendDataSet(sendData);

        Runnable remoteRunnable = new Runnable() {
            @Override
            public void run() {

                RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
                final String url = "http://dummy.restapiexample.com/api/v1/employees";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        //Listener
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                Type dataListType = new TypeToken<List<Employee>>() {
                                }.getType();
                                ArrayList<Employee> employeesList = gson.fromJson(response, dataListType);


                                // SendData from Remote to Activity
                                for (SendData callback : sendDataSet) {
                                    callback.sendData(employeesList);
                                }

                            }
                        },
                        //Error Listener
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                            }
                        });
                queue.add(stringRequest);
            }
        };

        executor.execute(remoteRunnable);
    }

    void addSendDataSet(SendData sendData) {
        sendDataSet.add(sendData);
    }

    void removeSendDataSet(SendData sendData) {
        sendDataSet.remove(sendData);
    }


    public interface SendData {
        void sendData(ArrayList<Employee> employeeList);
    }

    private static class Singleton {
        private static final Remote INSTANCE = new Remote();
    }
}
