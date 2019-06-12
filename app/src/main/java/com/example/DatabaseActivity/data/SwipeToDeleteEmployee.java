package com.example.DatabaseActivity.data;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeToDeleteEmployee extends ItemTouchHelper.SimpleCallback {
    private InteractListener swipeListener;


    public SwipeToDeleteEmployee(InteractListener swipeListener) {
        super(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT); // ItemTouchHelper.LEFT to be added later
        this.swipeListener = swipeListener;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (swipeListener != null)
        {
            swipeListener.onDelete(position);
        }
    }



    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }
}
