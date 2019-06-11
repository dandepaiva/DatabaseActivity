package com.example.DatabaseActivity.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.DatabaseActivity.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Remote {
    private Executor executor = Executors.newFixedThreadPool(3);
    private String TAG = "REMOTEEXECUTION";

    private Set<SendData> sendDataSet = new HashSet<>();
    private Remote() {
    }

    public static Remote getInstance() {
        return Singleton.INSTANCE;
    }

    public void getRemote(final SendData sendData) {
        addSendDataSet(sendData);

        Runnable remoteRunnable = new Runnable() {
            @Override
            public void run() {

                RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
                String url = "http://dummy.restapiexample.com/api/v1/employees";
                final ArrayList<Employee> employeesList = new ArrayList<>();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (jsonArray == null || jsonArray.length() == 0){
                                    return;
                                }

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);



                                        Long id = jsonObject.getLong("id");
                                        String employeeName = jsonObject.getString("employee_name");
                                        int employeeSalary = jsonObject.getInt("employee_salary");
                                        int employeeAge = jsonObject.getInt("employee_age");
                                        String profileImage = jsonObject.getString("profile_image");

                                        Employee addToList = new Employee(id, employeeName,
                                                employeeSalary, employeeAge, profileImage);

                                        Log.d(TAG, addToList.toString());
                                        employeesList.add(addToList);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                for (SendData callback : sendDataSet) {
                                    callback.sendData(employeesList);
                                }

                            }
                        },
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

    public void addSendDataSet(SendData sendData) {
        sendDataSet.add(sendData);
    }

    public void removeSendDataSet(SendData sendData) {
        sendDataSet.remove(sendData);
    }



    public interface SendData{
        void sendData(ArrayList<Employee> employeeList);
    }

    private static class Singleton {
        private static final Remote INSTANCE = new Remote();
    }
}
