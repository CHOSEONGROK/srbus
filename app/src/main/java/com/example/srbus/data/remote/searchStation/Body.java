package com.example.srbus.data.remote.searchStation;

import org.simpleframework.xml.ElementList;

import java.util.ArrayList;

public class Body {

    @ElementList(entry = "itemList", inline = true, required = false)
    private ArrayList<SearchStationItem> itemList = new ArrayList<>();

    public ArrayList<SearchStationItem> getItemList() { return itemList; }
    public void setItemList(ArrayList<SearchStationItem> itemList) { this.itemList = itemList;}

    @Override
    public String toString() {
        return "Body{" +
                "itemList=" + itemList +
                '}';
    }
}