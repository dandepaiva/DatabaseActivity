package com.example.DatabaseActivity.data;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeToDeleteEmployee extends ItemTouchHelper.SimpleCallback {
    private EmployeeAdapter employeeAdapter;


    public SwipeToDeleteEmployee(EmployeeAdapter employeeAdapter) {
        super(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT); // ItemTouchHelper.LEFT to be added later
        this.employeeAdapter = employeeAdapter;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        employeeAdapter.onDelete(position);
    }



    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }
}
