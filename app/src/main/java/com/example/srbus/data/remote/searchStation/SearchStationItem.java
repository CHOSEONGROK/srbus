package com.example.srbus.data.remote.searchStation;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.example.srbus.R;
import com.example.srbus.data.local.searchStationHistory.SearchStationHistory;
import org.jetbrains.annotations.Nullable;
import org.simpleframework.xml.Element;

import java.io.Serializable;

public class SearchStationItem implements Serializable {
    private final String TAG = "ArrBusItem";

    private int id = -1;
    @Element(required = false) private String arsId;
    @Element(required = false) private String posX;
    @Element(required = false) private String posY;
    @Element(required = false) private String stId;
    @Element(required = false) private String stNm;
    @Element(required = false) private String tmX;
    @Element(required = false) private String tmY;

    private boolean isFavorite = false;

    public int getFavoriteColor(Context context) {
        if (isFavorite) {
            return ContextCompat.getColor(context, R.color.yellow500);
        } else {
            return ContextCompat.getColor(context, R.color.gray300);
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getArsId() { return arsId; }
    public void setArsId(String arsId) { this.arsId = arsId; }

    public String getPosX() { return posX; }
    public void setPosX(String posX) { this.posX = posX; }

    public String getPosY() { return posY; }
    public void setPosY(String posY) { this.posY = posY; }

    public String getStId() { return stId; }
    public void setStId(String stId) { this.stId = stId; }

    public String getStNm() { return stNm; }
    public void setStNm(String stNm) { this.stNm = stNm; }

    public String getTmX() { return tmX; }
    public void setTmX(String tmX) { this.tmX = tmX; }

    public String getTmY() { return tmY; }
    public void setTmY(String tmY) { this.tmY = tmY; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    @Nullable
    public SearchStationHistory convert() {
        return new SearchStationHistory(null, arsId, posX, posY, stId, stNm, tmX, tmY);
    }

    @Override
    public String toString() {
        return "ArrBusItem{" +
                "TAG='" + TAG + '\'' +
                ", arsId='" + arsId + '\'' +
                ", posX='" + posX + '\'' +
                ", posY='" + posY + '\'' +
                ", stId='" + stId + '\'' +
                ", stNm='" + stNm + '\'' +
                ", tmX='" + tmX + '\'' +
                ", tmY='" + tmY + '\'' +
                "}";
    }
}
