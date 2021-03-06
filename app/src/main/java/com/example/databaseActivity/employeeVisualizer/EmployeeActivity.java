package com.example.databaseActivity.employeeVisualizer;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.databaseActivity.R;

import java.util.ArrayList;

/*  TODO
 *   RecyclerView to show Employees
 *   EmployeeAdapter
 *
 */
public class EmployeeActivity extends Activity implements Remote.SendData, InteractListener {
    private static final String TAG = "EmployeeActivity";
    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity_layout);
        final Button accessEmployee = findViewById(R.id.show_employees_button);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        employeeAdapter = new EmployeeAdapter();
        recyclerView.setAdapter(employeeAdapter);

        /*
         * Attach the ItemTouchHelper to the recyclerView
         */
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new SwipeToDeleteCallback(this));
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
    public void sendData(final ArrayList<Employee> employeeList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                employeeAdapter.update(employeeList);
            }
        });
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
     * shows a Snack bar to allow undo deletion
     * @param position Position of the deleted element in the array
     */
    @Override
    public void onDelete(int position) {
        employeeAdapter.onDelete(position);
        Log.d(TAG, "onDelete() called with: position = [" + position + "]");

        Snackbar snackbar = Snackbar.make(recyclerView,
                getString(R.string.snackbar_delete_text, position),
                Snackbar.LENGTH_INDEFINITE);

        // Listener for the Undo Button in the snack bar
        snackbar.setAction(R.string.snackbar_undo_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        employeeAdapter.undoDelete();
                    }
                });
        snackbar.show();
    }
}
