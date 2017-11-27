package com.ait.android.shoppinglist.data;

//import io.realm.RealmObject;
//import io.realm.annotations.PrimaryKey;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {

    @PrimaryKey
    private String itemID;

    private String category;
    private String name;
    private String description;
    private String price;
    private boolean purchased;

    public Item() {

    }

    public Item(String category, String name, String description, String price, boolean purchased) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
