package com.example.loginauthentication.CustomerFoodPanel;

public class CustomerPendingOrders1 {

    private String GrandTotalPrice,Name,Note;

    public CustomerPendingOrders1( String grandTotalPrice, String mobileNumber, String name,String note) {
        GrandTotalPrice = grandTotalPrice;
        Name = name;
        Note=note;

    }

    public CustomerPendingOrders1()
    {

    }


    public String getGrandTotalPrice() {
        return GrandTotalPrice;
    }

    public void setGrandTotalPrice(String grandTotalPrice) {
        GrandTotalPrice = grandTotalPrice;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
