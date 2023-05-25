package com.example.coffeeshop_onlinedb;

import java.util.ArrayList;

public class Drink {

    int PID;
    String Drink_Name;
    String Drink_Description;
    float Drink_Price;
    String Drink_Category;

    static ArrayList<String> categories = new ArrayList<>();




    public Drink(int drinkID, String drink_Name, String drink_Description, float drink_Price, String drink_Category) {
        PID = drinkID;
        Drink_Name = drink_Name;
        Drink_Price = drink_Price;
        Drink_Description = drink_Description;
        Drink_Category = drink_Category;

    }

    public String getDrink_Category() {
        return Drink_Category;
    }

    public void setDrink_Category(String drink_Category) {
        Drink_Category = drink_Category;
    }

    public int getDrink_ImageID() {
        return PID;
    }

    public String getDrink_Name() {
        return Drink_Name;
    }

    public String getDrink_Description() {
        return Drink_Description;
    }

    public float getDrink_Price() {
        return Drink_Price;
    }

    @Override
    public String toString() {
        return Drink_Name + " - " + Drink_Price + "$\n" + Drink_Description;
    }
}
