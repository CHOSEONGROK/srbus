package com.example.srbus.data.remote.arrBus;

import org.simpleframework.xml.Element;

public class Header {
    @Element
    private String headerCd;
    @Element
    private String headerMsg;
    @Element
    private String itemCount;

    public String getHeaderCd() { return headerCd; }
    public void setHeaderCd(String headerCd) { this.headerCd = headerCd; }

    public String getHeaderMsg() { return headerMsg; }
    public void setHeaderMsg(String headerMsg) { this.headerMsg = headerMsg; }

    public String getItemCount() { return itemCount; }
    public void setItemCount(String itemCount) { this.itemCount = itemCount; }

    @Override
    public String toString() {
        return "Header{" +
                "headerCd='" + headerCd + '\'' +
                ", headerMsg='" + headerMsg + '\'' +
                ", itemCount='" + itemCount + '\'' +
                '}';
    }
}
