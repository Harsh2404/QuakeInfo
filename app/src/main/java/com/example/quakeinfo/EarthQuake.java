package com.example.quakeinfo;

public class EarthQuake {
    private double mamplitude;
    private  String mplace;
    private  long mtimeinms;
    private  String mdetail;

    public EarthQuake(double mamplitude, String mplace, long mtimeinms,String mdetail) {
        this.mamplitude = mamplitude;
        this.mplace = mplace;
        this.mtimeinms = mtimeinms;
        this.mdetail=mdetail;
    }

    public double getMamplitude() {
        return mamplitude;
    }

    public String getMplace() {
        return mplace;
    }

    public long getMmtimeinms() {
        return mtimeinms;
    }
    public String getMdetail(){
        return mdetail;
    }


}
