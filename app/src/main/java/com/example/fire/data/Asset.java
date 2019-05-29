package com.example.fire.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "asset_table")
public class Asset {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String category;

    private String balance;

    private String annualReturn;

    @Ignore
    public Asset(int id, String name, String category, String balance, String annualReturn) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.balance = balance;
        this.annualReturn = annualReturn;
    }

    public Asset(String name, String category, String balance, String annualReturn) {
        this.name = name;
        this.category = category;
        this.balance = balance;
        this.annualReturn = annualReturn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setAnnualReturn(String annualReturn) {
        this.annualReturn = annualReturn;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getBalance() {
        return balance;
    }

    public String getAnnualReturn() {
        return annualReturn;
    }
}
