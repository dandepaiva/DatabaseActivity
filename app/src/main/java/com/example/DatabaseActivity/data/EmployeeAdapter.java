package com.example.DatabaseActivity.data;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.DatabaseActivity.MyApplication;
import com.example.DatabaseActivity.R;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} implementation
 *
 * Show employees in a RecyclerView
 */
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> implements DeleteClickListener, UndoClickListener{
    private static final String TAG = "EmployeeAdapter";
    protected ArrayList<Employee> employeeArrayList;
    private DeleteClickListener callback;
    public EmployeeAdapter employeeAdapter = this;

    Employee recentlyDeleted;
    int recentlyDeletedPosition;

    /**
     * constructor
     */
    public EmployeeAdapter() {
        this.employeeArrayList = new ArrayList<Employee>();
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View line = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_recycler_view,parent, false);

        EmployeeViewHolder employeeViewHolder = new EmployeeViewHolder(line);
        return employeeViewHolder;
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder employeeViewHolder, int i) {
        employeeViewHolder.onBind(employeeArrayList.get(i), this);
    }

    @Override
    public int getItemCount() {
        return employeeArrayList.size();
    }

    public void update(ArrayList<Employee> employeeArrayList) {
        this.employeeArrayList = employeeArrayList;
        notifyDataSetChanged();
    }

    @Override
    public void onDelete(int position) {
        /**
         * Save the employee being deleted to have the undo option
         */
        recentlyDeleted = employeeArrayList.get(position);
        recentlyDeletedPosition = position;
        /**
         * remove employee from the array
         * notify the RecyclerView
         */
        employeeArrayList.remove(position);
        notifyItemRemoved(position);
        if (callback != null) {
            /**
             * calls the onDelete of the EmployeeActivity
             */
            callback.onDelete(recentlyDeletedPosition);
        }
    }


    public void setCallback(DeleteClickListener callback) {
        this.callback = callback;
    }

    public void invalidate() {
        callback = null;
    }

    @Override
    public void undoDelete() {
        Log.d(TAG, "undoDelete() called");

        employeeArrayList.add(recentlyDeletedPosition, recentlyDeleted);
        notifyItemInserted(recentlyDeletedPosition);
    }

    /**
     * {@link RecyclerView.ViewHolder} implementation
     *
     * ViewHolder for this Adapter
     */
    public static class EmployeeViewHolder extends RecyclerView.ViewHolder{
        public TextView employeeName;
        public TextView employeeAge;
        public TextView employeeId;
        public TextView employeeSalary;
        public ImageButton button;

        /**
         * constructor
         * @param itemView View inside RecyclerView to show employee
         */
        public EmployeeViewHolder(View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employee_name);
            employeeAge = itemView.findViewById(R.id.employee_age);
            employeeId = itemView.findViewById(R.id.employee_id);
            employeeSalary = itemView.findViewById(R.id.employee_salary);
            button = itemView.findViewById(R.id.trash_button);
        }

        /**
         * called when binding the ViewHolder
         * @param employeeBind Employee to
         * @param callback DeleteClickListener Interface, communication between ViewHolder
         *                 and Adapter
         */
        public void onBind(Employee employeeBind, final DeleteClickListener callback){
            employeeName.setText("name: " + employeeBind.getEmployeeName());
            employeeAge.setText("age: " + employeeBind.getEmployeeAge());
            employeeId.setText("id: " + employeeBind.getId());
            employeeSalary.setText("salary: " + employeeBind.getEmployeeSalary());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    /**
                     * callback is an instance of the interface
                     */
                    if (callback!=null){
                        callback.onDelete(position);
                    }
                }
            });
        }
    }
}