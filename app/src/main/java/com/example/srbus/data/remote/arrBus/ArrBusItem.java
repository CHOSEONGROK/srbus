package com.example.srbus.data.remote.arrBus;

import android.content.Context;
import com.example.srbus.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.util.HashMap;

public class ArrBusItem implements Serializable {
    private final String TAG = "ArrBusItem";

    public enum RouteType {공용, 공항, 마을, 간선, 지선, 순환, 광역, 인천, 경기, 폐지}

    @Element(required = false) private String adirection;
    @Element(required = false) private String arrmsg1;
    @Element(required = false) private String arrmsg2;
    @Element(required = false) private String arrmsgSec1;
    @Element(required = false) private String arrmsgSec2;
    @Element(required = false) private String arsId;
    @Element(required = false) private String busRouteId;
    @Element(required = false) private String busType1;
    @Element(required = false) private String busType2;
    @Element(required = false) private String deTourAt;
    @Element(required = false) private String firstTm;
    @Element(required = false) private String gpsX;
    @Element(required = false) private String gpsY;
    @Element(required = false) private String isArrive1;
    @Element(required = false) private String isArrive2;
    @Element(required = false) private String isFullFlag1;
    @Element(required = false) private String isFullFlag2;
    @Element(required = false) private String isLast1;
    @Element(required = false) private String isLast2;
    @Element(required = false) private String lastTm;
    @Element(required = false) private String nextBus;
    @Element(required = false) private String nxtStn;
    @Element(required = false) private String posX;
    @Element(required = false) private String posY;
    @Element(required = false) private String repTm1;
    @Element(required = false) private String rerdieDiv1;
    @Element(required = false) private String rerdieDiv2;
    @Element(required = false) private String rerideNum1;
    @Element(required = false) private String rerideNum2;
    @Element(required = false) private String routeType;
    @Element(required = false) private String rtNm;
    @Element(required = false) private String sectNm;
    @Element(required = false) private String sectOrd1;
    @Element(required = false) private String sectOrd2;
    @Element(required = false) private String stId;
    @Element(required = false) private String stNm;
    @Element(required = false) private String staOrd;
    @Element(required = false) private String stationNm1;
    @Element(required = false) private String stationNm2;
    @Element(required = false) private String stationTp;
    @Element(required = false) private String term;
    @Element(required = false) private String traSpd1;
    @Element(required = false) private String traSpd2;
    @Element(required = false) private String traTime1;
    @Element(required = false) private String traTime2;
    @Element(required = false) private String vehId1;
    @Element(required = false) private String vehId2;

    private boolean isFavorite = false;

    private int arrmsg1_min = 0;
    private int arrmsg1_sec = 0;
    private int arrmsg1_station = 0;

    private int arrmsg2_min = 0;
    private int arrmsg2_sec = 0;
    private int arrmsg2_station = 0;

    /**
     * Getter and Setter.
     */
    public String getAdirection() { return adirection; }
    public void setAdirection(String adirection) { this.adirection = adirection; }

    public String getArrmsg1() { return arrmsg1; }
    public void setArrmsg1(String arrmsg1) {
        this.arrmsg1 = arrmsg1;
        if (arrmsg1.contains("[막차]")) {
            arrmsg1 = arrmsg1.substring(5);
        }
        if (arrmsg1.contains("분") || arrmsg1.contains("초")) {
            if (arrmsg1.contains("분")) {
                arrmsg1_min = Integer.valueOf(arrmsg1.substring(0, arrmsg1.indexOf('분')));
            } else {
                arrmsg1_min = 0;
            }
            if (arrmsg1.contains("초")) {
                arrmsg1_sec = Integer.valueOf(arrmsg1.substring(arrmsg1.indexOf('분') + 1, arrmsg1.indexOf('초')));
            } else {
                arrmsg1_sec = 0;
            }
            if (arrmsg1.contains("[") && arrmsg1.contains("]")) {
                arrmsg1_station = Integer.valueOf(arrmsg1.substring(arrmsg1.indexOf('[') + 1, arrmsg1.indexOf('번')));
            } else {
                arrmsg1_station = 0;
            }
        } else if (arrmsg1.equals("곧 도착") || arrmsg1.equals("출발대기") || arrmsg1.equals("운행종료")) {
            arrmsg1_min = 0;
            arrmsg1_sec = 0;
            arrmsg1_station = 0;
        }
//        Log.d(TAG, "arrmsg1=" + arrmsg1 + ", arrmsg1_min=" + arrmsg1_min  + ", arrmsg1_sec=" + arrmsg1_sec + ", arrmsg1_station=" + arrmsg1_station);
    }

    public String getArrmsg2() { return arrmsg2; }
    public void setArrmsg2(String arrmsg2) {
        this.arrmsg2 = arrmsg2;
        if (arrmsg2.contains("[막차]")) {
            arrmsg2 = arrmsg2.substring(5);
        }
        if (arrmsg2.contains("분") || arrmsg2.contains("초")) {
            if (arrmsg2.contains("분")) {
                arrmsg2_min = Integer.valueOf(arrmsg2.substring(0, arrmsg2.indexOf('분')));
            } else {
                arrmsg2_min = 0;
            }
            if (arrmsg2.contains("초")) {
                arrmsg2_sec = Integer.valueOf(arrmsg2.substring(arrmsg2.indexOf('분') + 1, arrmsg2.indexOf('초')));
            } else {
                arrmsg2_sec = 0;
            }
            if (arrmsg2.contains("[") && arrmsg2.contains("]")) {
                arrmsg2_station = Integer.valueOf(arrmsg2.substring(arrmsg2.indexOf('[') + 1, arrmsg2.indexOf('번')));
            } else {
                arrmsg2_station = 0;
            }
        } else if (arrmsg1.equals("곧 도착") || arrmsg1.equals("출발대기") || arrmsg1.equals("운행종료")) {
            arrmsg1_min = 0;
            arrmsg1_sec = 0;
            arrmsg1_station = 0;
        }
//        Log.d(TAG, "arrmsg2=" + arrmsg2 + ", arrmsg2_min=" + arrmsg2_min  + ", arrmsg2_sec=" + arrmsg2_sec + ", arrmsg2_station=" + arrmsg2_station);
    }

    public String getArrmsgSec1() { return arrmsgSec1; }
    public void setArrmsgSec1(String arrmsgSec1) { this.arrmsgSec1 = arrmsgSec1; }

    public String getArrmsgSec2() { return arrmsgSec2; }
    public void setArrmsgSec2(String arrmsgSec2) { this.arrmsgSec2 = arrmsgSec2; }

    public String getArsId() { return arsId; }
    public void setArsId(String arsId) { this.arsId = arsId; }

    public String getBusRouteId() { return busRouteId; }
    public void setBusRouteId(String busRouteId) { this.busRouteId = busRouteId; }

    public String getBusType1() { return busType1; }
    public void setBusType1(String busType1) { this.busType1 = busType1; }

    public String getBusType2() { return busType2; }
    public void setBusType2(String busType2) { this.busType2 = busType2; }

    public String getDeTourAt() { return deTourAt; }
    public void setDeTourAt(String deTourAt) { this.deTourAt = deTourAt; }

    public String getFirstTm() { return firstTm; }
    public void setFirstTm(String firstTm) { this.firstTm = firstTm; }

    public String getGpsX() { return gpsX; }
    public void setGpsX(String gpsX) { this.gpsX = gpsX; }

    public String getGpsY() { return gpsY; }
    public void setGpsY(String gpsY) { this.gpsY = gpsY; }

    public String getIsArrive1() { return isArrive1; }
    public void setIsArrive1(String isArrive1) { this.isArrive1 = isArrive1; }

    public String getIsArrive2() { return isArrive2; }
    public void setIsArrive2(String isArrive2) { this.isArrive2 = isArrive2; }

    public String getIsFullFlag1() { return isFullFlag1; }
    public void setIsFullFlag1(String isFullFlag1) { this.isFullFlag1 = isFullFlag1; }

    public String getIsFullFlag2() { return isFullFlag2; }
    public void setIsFullFlag2(String isFullFlag2) { this.isFullFlag2 = isFullFlag2; }

    public String getIsLast1() { return isLast1; }
    public void setIsLast1(String isLast1) { this.isLast1 = isLast1; }

    public String getIsLast2() { return isLast2; }
    public void setIsLast2(String isLast2) { this.isLast2 = isLast2; }

    public String getLastTm() { return lastTm; }
    public void setLastTm(String lastTm) { this.lastTm = lastTm; }

    public String getNextBus() { return nextBus; }
    public void setNextBus(String nextBus) { this.nextBus = nextBus; }

    public String getNxtStn() { return nxtStn; }
    public void setNxtStn(String nxtStn) { this.nxtStn = nxtStn; }

    public String getPosX() { return posX; }
    public void setPosX(String posX) { this.posX = posX; }

    public String getPosY() { return posY; }
    public void setPosY(String posY) { this.posY = posY; }

    public String getRepTm1() { return repTm1; }
    public void setRepTm1(String repTm1) { this.repTm1 = repTm1; }

    public String getRerdieDiv1() { return rerdieDiv1; }
    public void setRerdieDiv1(String rerdieDiv1) { this.rerdieDiv1 = rerdieDiv1; }

    public String getRerdieDiv2() { return rerdieDiv2; }
    public void setRerdieDiv2(String rerdieDiv2) { this.rerdieDiv2 = rerdieDiv2; }

    public String getRerideNum1() { return rerideNum1; }
    public void setRerideNum1(String rerideNum1) { this.rerideNum1 = rerideNum1; }

    public String getRerideNum2() { return rerideNum2; }
    public void setRerideNum2(String rerideNum2) { this.rerideNum2 = rerideNum2; }

    public String getRouteType() { return routeType; }
    public void setRouteType(String routeType) { this.routeType = routeType; }

    public String getRtNm() { return rtNm; }
    public void setRtNm(String rtNm) { this.rtNm = rtNm; }

    public String getSectNm() { return sectNm; }
    public void setSectNm(String sectNm) { this.sectNm = sectNm; }

    public String getSectOrd1() { return sectOrd1; }
    public void setSectOrd1(String sectOrd1) { this.sectOrd1 = sectOrd1; }

    public String getSectOrd2() { return sectOrd2; }
    public void setSectOrd2(String sectOrd2) { this.sectOrd2 = sectOrd2; }

    public String getStId() { return stId; }
    public void setStId(String stId) { this.stId = stId; }

    public String getStNm() { return stNm; }
    public void setStNm(String stNm) { this.stNm = stNm; }

    public String getStaOrd() { return staOrd; }
    public void setStaOrd(String staOrd) { this.staOrd = staOrd; }

    public String getStationNm1() { return stationNm1; }
    public void setStationNm1(String stationNm1) { this.stationNm1 = stationNm1; }

    public String getStationNm2() { return stationNm2; }
    public void setStationNm2(String stationNm2) { this.stationNm2 = stationNm2; }

    public String getStationTp() { return stationTp; }
    public void setStationTp(String stationTp) { this.stationTp = stationTp; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public String getTraSpd1() { return traSpd1; }
    public void setTraSpd1(String traSpd1) { this.traSpd1 = traSpd1; }

    public String getTraSpd2() { return traSpd2; }
    public void setTraSpd2(String traSpd2) { this.traSpd2 = traSpd2; }

    public String getTraTime1() { return traTime1; }
    public void setTraTime1(String traTime1) { this.traTime1 = traTime1; }

    public String getTraTime2() { return traTime2; }
    public void setTraTime2(String traTime2) { this.traTime2 = traTime2; }

    public String getVehId1() { return vehId1; }
    public void setVehId1(String vehId1) { this.vehId1 = vehId1; }

    public String getVehId2() { return vehId2; }
    public void setVehId2(String vehId2) { this.vehId2 = vehId2; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getArrmsg1_min() { return arrmsg1_min; }
    public void setArrmsg1_min(int arrmsg1_min) { this.arrmsg1_min = arrmsg1_min; }

    public int getArrmsg1_sec() { return arrmsg1_sec; }
    public void setArrmsg1_sec(int arrmsg1_sec) { this.arrmsg1_sec = arrmsg1_sec; }

    public int getArrmsg1_station() { return arrmsg1_station; }
    public void setArrmsg1_station(int arrmsg1_station) { this.arrmsg1_station = arrmsg1_station; }

    public int getArrmsg2_min() { return arrmsg2_min; }
    public void setArrmsg2_min(int arrmsg2_min) { this.arrmsg2_min = arrmsg2_min; }

    public int getArrmsg2_sec() { return arrmsg2_sec; }
    public void setArrmsg2_sec(int arrmsg2_sec) { this.arrmsg2_sec = arrmsg2_sec; }

    public int getArrmsg2_station() { return arrmsg2_station; }
    public void setArrmsg2_station(int arrmsg2_station) { this.arrmsg2_station = arrmsg2_station; }

    /**
     *
     */
    @NotNull
    public String getFirstArrBusRemainingTime() {
        String res = "";
        if (arrmsg1_min > 0 || arrmsg1_sec > 0) {
            if (arrmsg1_min > 0) {
                res = arrmsg1_min + "분 ";
            }
            res += arrmsg1_sec + "초";
        } else if (arrmsg1.equals("곧 도착")) {
            res = arrmsg1;
        } else if (arrmsg1.equals("운행종료")) {
            res = arrmsg1;
        } else if (arrmsg1.equals("출발대기")) {
            res = arrmsg1;
        }
        return res;
    }
    @Nullable
    public String getFirstArrBusRemainingStation() {
        String res = null;
        if (arrmsg1_station > 0) {
            res = arrmsg1.substring(arrmsg1.indexOf('[') + 1, arrmsg1.indexOf(']'));
            res = res.replaceAll(" ", "");
//            res = res.replaceAll("\\p{Z}", "");
        }
        return res;
    }
    @NotNull
    public String getSecondArrBusRemainingTime() {
        String res = "";
        if (arrmsg2_min > 0 || arrmsg2_sec > 0) {
            if (arrmsg2_min > 0) {
                res = arrmsg2_min + "분 ";
            }
            res += arrmsg2_sec + "초";
        } else if (arrmsg2.equals("곧 도착")) {
            res = arrmsg2;
        } else if (arrmsg2.equals("운행종료")) {
            res = arrmsg2;
        } else if (arrmsg2.equals("출발대기")) {
            res = arrmsg2;
        }
        return res;
    }
    @Nullable
    public String getSecondArrBusRemainingStation() {
        String res = null;
        if (arrmsg2_station > 0) {
            res = arrmsg2.substring(arrmsg2.indexOf('[') + 1, arrmsg2.indexOf(']'));
            res = res.replaceAll(" ", "");
//            res = res.replaceAll("\\p{Z}", "");
        }
        return res;
    }

    public void decreaseArrBusRemainingTimeByOneSecond() {
        if (arrmsg1_sec > 0) {
            arrmsg1_sec -= 1;
        } else if (arrmsg1_min > 0) {
            arrmsg1_sec = 59;
            arrmsg1_min -= 1;
        } else {
            arrmsg1_sec = 0;
            arrmsg1_min = 0;
        }
//        Log.d(TAG, "arrmsg1_min=" + arrmsg1_min + ", arrmsg1_sec=" + arrmsg1_sec);

        if (arrmsg2_sec > 0) {
            arrmsg2_sec -= 1;
        } else if (arrmsg2_min > 0) {
            arrmsg2_sec = 59;
            arrmsg2_min -= 1;
        } else {
            arrmsg2_sec = 0;
            arrmsg2_min = 0;
        }
//        Log.d(TAG, "arrmsg1_min=" + arrmsg2_min + ", arrmsg1_sec=" + arrmsg2_sec);
    }

    public String getFirstBusFullOrNot() {
        if (isFullFlag1.equals("1")) {
            return "혼잡";
        } else {
            return "여유";
        }
    }

    public String getSecondBusFullOrNot() {
        if (isFullFlag2.equals("1")) {
            return "혼잡";
        } else {
            return "여유";
        }
    }

    public String getRouteTypeString() {
        switch (routeType) {
            case "0":
                return RouteType.공용.name();
            case "1":
                return RouteType.공항.name();
            case "2":
                return RouteType.마을.name();
            case "3":
                return RouteType.간선.name();
            case "4":
                return RouteType.지선.name();
            case "5":
                return RouteType.순환.name();
            case "6":
                return RouteType.광역.name();
            case "7":
                return RouteType.인천.name();
            case "8":
                return RouteType.경기.name();
            case "9":
                return RouteType.폐지.name();
            default:
                return RouteType.폐지.name();
        }
    }

    public int getRouteTypeTextColor(Context context) {
        switch (routeType) {
            case "0":
                return context.getResources().getColor(R.color.black);
            case "1":
                return context.getResources().getColor(R.color.teal500);
            case "2":
                return context.getResources().getColor(R.color.green900);
            case "3":
                return context.getResources().getColor(R.color.blue500);
            case "4":
                return context.getResources().getColor(R.color.green500);
            case "5":
                return context.getResources().getColor(R.color.black);
            case "6":
                return context.getResources().getColor(R.color.black);
            case "7":
                return context.getResources().getColor(R.color.red500);
            case "8":
                return context.getResources().getColor(R.color.teal500);
            case "9":
                return context.getResources().getColor(R.color.black);
            default:
                return context.getResources().getColor(R.color.black);
        }
    }

    public int getFavoriteColorFilter(Context context) {
        if (isFavorite) {
            return context.getResources().getColor(R.color.yellow500);
        } else {
            return context.getResources().getColor(R.color.gray300);
        }
    }

    @Override
    public String toString() {
        return "ArrBusItem{" +
                "adirection='" + adirection + '\'' +
                ", arrmsg1='" + arrmsg1 + '\'' +
                ", arrmsg2='" + arrmsg2 + '\'' +
                ", arrmsgSec1='" + arrmsgSec1 + '\'' +
                ", arrmsgSec2='" + arrmsgSec2 + '\'' +
                ", arsId='" + arsId + '\'' +
                ", busRouteId='" + busRouteId + '\'' +
                ", busType1='" + busType1 + '\'' +
                ", busType2='" + busType2 + '\'' +
                ", deTourAt='" + deTourAt + '\'' +
                ", firstTm='" + firstTm + '\'' +
                ", gpsX='" + gpsX + '\'' +
                ", gpsY='" + gpsY + '\'' +
                ", isArrive1='" + isArrive1 + '\'' +
                ", isArrive2='" + isArrive2 + '\'' +
                ", isFullFlag1='" + isFullFlag1 + '\'' +
                ", isFullFlag2='" + isFullFlag2 + '\'' +
                ", isLast1='" + isLast1 + '\'' +
                ", isLast2='" + isLast2 + '\'' +
                ", nextBus='" + nextBus + '\'' +
                ", nxtStn='" + nxtStn + '\'' +
                ", posX='" + posX + '\'' +
                ", posY='" + posY + '\'' +
                ", repTm1='" + repTm1 + '\'' +
                ", rerdieDiv1='" + rerdieDiv1 + '\'' +
                ", rerdieDiv2='" + rerdieDiv2 + '\'' +
                ", rerideNum1='" + rerideNum1 + '\'' +
                ", rerideNum2='" + rerideNum2 + '\'' +
                ", routeType='" + routeType + '\'' +
                ", rtNm='" + rtNm + '\'' +
                ", sectNm='" + sectNm + '\'' +
                ", sectOrd1='" + sectOrd1 + '\'' +
                ", stId='" + stId + '\'' +
                ", stNm='" + stNm + '\'' +
                ", staOrd='" + staOrd + '\'' +
                ", stationNm1='" + stationNm1 + '\'' +
                ", stationNm2='" + stationNm2 + '\'' +
                ", stationTp='" + stationTp + '\'' +
                ", term='" + term + '\'' +
                ", traSpd1='" + traSpd1 + '\'' +
                ", traSpd2='" + traSpd2 + '\'' +
                ", traTime1='" + traTime1 + '\'' +
                ", traTime2='" + traTime2 + '\'' +
                ", vehId1='" + vehId1 + '\'' +
                ", vehId2='" + vehId2 + '\'' +
                "}";
    }
}
