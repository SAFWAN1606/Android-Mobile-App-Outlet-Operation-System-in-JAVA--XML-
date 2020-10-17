package com.outlet.outletoperationsystem.Model;

public class User {
    private String Password;
    private String Email;
    private String Name;
    private String Phone;


    private String IsStaff;

    public User() {
    }

    public User(String password, String email, String name) {
        Password = password;
        Email = email;
        Name = name;
        IsStaff="false";
    }
    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
