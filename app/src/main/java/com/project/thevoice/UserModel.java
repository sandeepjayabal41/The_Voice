package com.project.thevoice;

public class UserModel
{
    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    private String Firstname, Lastname, Email;

    public UserModel(String firstname, String lastname, String email) {
        Firstname = firstname;
        Lastname = lastname;
        Email = email;
    }



}
