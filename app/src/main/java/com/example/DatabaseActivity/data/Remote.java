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
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                //final ArrayList<Employee> employeesList = new ArrayList<>();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        //Listener
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                Type dataListType = new TypeToken<List<Employee>>() {}.getType();
                                ArrayList<Employee> lss = gson.fromJson(response, dataListType);


//                                JSONArray jsonArray = null;
//
//                                // Create JSON array with the string given at the URL
//                                try {
//                                    jsonArray = new JSONArray(response);
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                // Return ("terminate") Listener if jsonArray is null or empty
//                                if (jsonArray == null || jsonArray.length() == 0){
//                                    return;
//                                }
//
//                                // Read each JSONObject in the JSONArray
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    try {
//                                        // Each JSONObject represents an Employee
//                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                        String objectContent = String.valueOf(jsonObject);
//                                        Employee employeeToAdd = gson.fromJson(objectContent, Employee.class);
//
//                                        /*Long id = jsonObject.getLong("id");
//                                        String employeeName = jsonObject.getString("employeeName");
//                                        int employeeSalary = jsonObject.getInt("employeeSalary");
//                                        int employeeAge = jsonObject.getInt("employeeAge");
//                                        String profileImage = jsonObject.getString("profileImage");
//
//                                        // Create an Object Employee with the data in the JSONObject
//                                        Employee employeeToAdd = new Employee(id, employeeName,
//                                                employeeSalary, employeeAge, profileImage);*/
//
//                                        // Add the Employee to an ArrayList
//                                        employeesList.add(employeeToAdd);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }

                                // SendData from Remote to Activity
                                for (SendData callback : sendDataSet) {
                                    callback.sendData(lss);
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



    public interface SendData{
        void sendData(ArrayList<Employee> employeeList);
    }

    private static class Singleton {
        private static final Remote INSTANCE = new Remote();
    }
}
