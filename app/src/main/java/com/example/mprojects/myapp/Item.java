package com.example.mprojects.myapp;

public class Item {
    private String name;
    private String phone;
    private String ID;
    private String check="0";

    public Item(String ID,String name,String phone,String check)
    {
        this.setName(name);
        this.setPhone(phone);
        this.setID(ID);
        this.setCheck(check);


    }
    //Getter and Setter

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
