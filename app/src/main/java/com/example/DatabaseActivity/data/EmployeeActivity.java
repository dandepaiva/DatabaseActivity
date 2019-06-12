package com.example.DatabaseActivity.data;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.DatabaseActivity.R;

import java.util.ArrayList;

/*  TODO
 *   RecyclerView to show Employees
 *   EmployeeAdapter
 *
 */
public class EmployeeActivity extends Activity implements Remote.SendData, DeleteClickListener {
    private static final String TAG = "EmployeeActivity";
    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private UndoClickListener undoClickListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity_layout);
        final Button accessEmployee = findViewById(R.id.button);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        employeeAdapter = new EmployeeAdapter();
        recyclerView.setAdapter(employeeAdapter);

        /**
         * Attach the ItemTouchHelper to the recyclerView
         */
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new SwipeToDeleteEmployee(employeeAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //---------------------------------------------------


        accessEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remote.getInstance().getRemote(EmployeeActivity.this);
            }
        });
    }

    @Override
    public void sendData(ArrayList<Employee> employeeList) {
        employeeAdapter.update(employeeList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        employeeAdapter.setCallback(this);
        Remote.getInstance().addSendDataSet(this);
    }

    @Override
    protected void onPause() {
        Remote.getInstance().removeSendDataSet(this);
        employeeAdapter.invalidate();
        super.onPause();
    }

    /**
     * shows a Snackbar to allow undo deletion
     * @param position Position of the deleted element in the array
     */
    @Override
    public void onDelete(int position) {
        Log.d(TAG, "onDelete() called with: position = [" + position + "]");
        undoClickListener = employeeAdapter.employeeAdapter;
        View contextView = findViewById(R.id.recyclerView);
        Snackbar snackbar = Snackbar.make(contextView, "Deleted the employee in position: " + position, Snackbar.LENGTH_LONG);
        /**
         * Listener for the Undo Button in the snackbar
         */
        snackbar.setAction("Undo action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(undoClickListener!=null) {
                            /**
                             * if Undo is pressed, warn Adapter
                             * to update the RecyclerView
                             */
                            undoClickListener.undoDelete();
                        }
                    }
                });
        snackbar.show();
    }
}
