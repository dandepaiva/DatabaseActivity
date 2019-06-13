package com.example.DatabaseActivity.employeeVisualizer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.DatabaseActivity.MyApplication;
import com.example.DatabaseActivity.R;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} implementation
 * <p>
 * Show employees in a RecyclerView
 */
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> implements InteractListener {
    private static final String TAG = "EmployeeAdapter";
    protected ArrayList<Employee> employeeArrayList;

    Employee recentlyDeleted;
    int recentlyDeletedPosition;

    private InteractListener callback;

    /**
     * constructor
     */
    public EmployeeAdapter() {
        this.employeeArrayList = new ArrayList<Employee>();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View line = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_recycler_view, parent, false);

        return new EmployeeViewHolder(line);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder employeeViewHolder, int i) {
        employeeViewHolder.onBind(employeeArrayList.get(i), callback);
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
         * notify the EmployeeAdapter
         */
        employeeArrayList.remove(position);
        notifyItemRemoved(position);

    }

    public void setCallback(InteractListener callback) {
        this.callback = callback;
    }

    public void invalidate() {
        callback = null;
    }

    public void undoDelete() {
        Log.d(TAG, "undoDelete() called");

        employeeArrayList.add(recentlyDeletedPosition, recentlyDeleted);
        notifyItemInserted(recentlyDeletedPosition);
    }

    /**
     * {@link RecyclerView.ViewHolder} implementation
     * <p>
     * ViewHolder for this Adapter
     */
    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        public final TextView employeeName;
        public final TextView employeeAge;
        public final TextView employeeId;
        public final TextView employeeSalary;
        public final ImageButton trashcanButton;

        /**
         * constructor
         *
         * @param itemView View inside RecyclerView to show employee
         */
        public EmployeeViewHolder(View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employee_name);
            employeeAge = itemView.findViewById(R.id.employee_age);
            employeeId = itemView.findViewById(R.id.employee_id);
            employeeSalary = itemView.findViewById(R.id.employee_salary);
            trashcanButton = itemView.findViewById(R.id.trash_button);
        }

        /**
         * called when binding the ViewHolder
         *
         * @param employee Employee to bind
         * @param callback     InteractListener Interface, communication between ViewHolder
         *                     and Adapter
         */
        public void onBind(Employee employee, final InteractListener callback) {
            Context context = MyApplication.getContext();
            employeeName.setText(context.getString(R.string.bind_employee_name, employee.getEmployeeName()));
            employeeAge.setText(context.getString(R.string.bind_employee_age, employee.getEmployeeAge()));
            employeeId.setText(context.getString(R.string.bind_employee_id, employee.getId()));
            employeeSalary.setText(context.getString(R.string.bind_employee_salary, employee.getEmployeeSalary()));

            trashcanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    /**
                     * callback is an instance of the interface
                     */
                    if (callback != null) {
                        callback.onDelete(position);
                    }
                }
            });
        }
    }
}