package com.example.DatabaseActivity.employeeVisualizer;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Employee {
    // Name format due to Gson
    long id;
    @SerializedName("employee_name")
    String employeeName;
    @SerializedName("employee_salary")
    int employeeSalary;
    @SerializedName("employee_age")
    int employeeAge;
    @SerializedName("employee_image")
    String profileImage;

    public Employee(long id, String employeeName, int employeeSalary, int employeeAge, String profileImage) {
        this.id = id;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
        this.employeeAge = employeeAge;
        this.profileImage = profileImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employee_name) {
        this.employeeName = employee_name;
    }

    public int getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(int employee_salary) {
        this.employeeSalary = employee_salary;
    }

    public int getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(int employee_age) {
        this.employeeAge = employee_age;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profile_image) {
        this.profileImage = profile_image;
    }

    @NonNull
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employeeName='" + employeeName + '\'' +
                ", employeeSalary=" + employeeSalary +
                ", employeeAge=" + employeeAge +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
