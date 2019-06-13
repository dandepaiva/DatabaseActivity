package com.example.DatabaseActivity.employeeVisualizer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.DatabaseActivity.MyApplication;
import com.example.DatabaseActivity.R;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private InteractListener interactListener;

    private Drawable icon;
    private ColorDrawable background;

    SwipeToDeleteCallback(InteractListener swipeListener) {
        super(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
        interactListener = swipeListener;

        icon = ContextCompat.getDrawable(MyApplication.getContext(), R.drawable.ic_delete);

        background = new ColorDrawable(Color.argb(180, 8, 116, 231));
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 70;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + iconMargin;
        int iconBottom = iconTop + icon.getIntrinsicHeight();


        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft()+ iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(
                    itemView.getLeft(),
                    itemView.getTop()+26,
                    itemView.getLeft() + ((int) dX) - backgroundCornerOffset,
                    itemView.getBottom());

        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(
                    itemView.getRight() + ((int) dX) + backgroundCornerOffset,
                    itemView.getTop()+26,
                    itemView.getRight(),
                    itemView.getBottom());

        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(canvas);
        icon.draw(canvas);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (interactListener != null) {
            interactListener.onDelete(position);
        }
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }
}
