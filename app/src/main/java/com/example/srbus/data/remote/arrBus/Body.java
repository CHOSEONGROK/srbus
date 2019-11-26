package com.example.srbus.data.remote.arrBus;

import org.simpleframework.xml.ElementList;

import java.io.Serializable;
import java.util.ArrayList;

public class Body implements Serializable {

    @ElementList(entry = "itemList", inline = true, required = false)
    private ArrayList<ArrBusItem> itemList = new ArrayList<>();

    public ArrayList<ArrBusItem> getItemList() { return itemList; }
    public void setItemList(ArrayList<ArrBusItem> itemList) { this.itemList = itemList;}

    @Override
    public String toString() {
        return "Body{" +
                "itemList=" + itemList +
                '}';
    }
}