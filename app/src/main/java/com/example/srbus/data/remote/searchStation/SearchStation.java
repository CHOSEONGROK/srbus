package com.example.srbus.data.remote.searchStation;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "ServiceResult")
public class SearchStation implements Serializable {

    @Element(required = false) private String comMsgHeader;
    @Element(required = false) private Header msgHeader;
    @Element(required = false) private Body msgBody = new Body();

    public String getComMsgHeader() { return comMsgHeader; }
    public void setComMsgHeader(String comMsgHeader) { this.comMsgHeader = comMsgHeader; }

    public Header getMsgHeader() { return msgHeader; }
    public void setMsgHeader(Header msgHeader) { this.msgHeader = msgHeader; }

    public Body getMsgBody() { return msgBody; }
    public void setMsgBody(Body msgBody) { this.msgBody = msgBody; }

    @Override
    public String toString() {
        return "SearchStation{" +
                "comMsgHeader='" + comMsgHeader + '\'' +
                ", msgHeader=" + msgHeader +
                ", msgBody=" + msgBody +
                '}';
    }
}