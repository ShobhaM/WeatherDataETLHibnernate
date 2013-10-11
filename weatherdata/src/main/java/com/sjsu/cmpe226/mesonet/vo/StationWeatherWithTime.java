package com.sjsu.cmpe226.mesonet.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Arrays;

//JM@: create new class for composite Id
public class StationWeatherWithTime implements Serializable {
  private static final long serialVersionUID = 7526472295622776147L;
  private String stn;
  private Date YYMMDDHHMM;

  //JM@: override hashCode and equals
  public boolean equals(StationWeatherWithTime stationWeatherObjectB) {

    if((this.stn.equals(stationWeatherObjectB.stn))&&
        (this.YYMMDDHHMM.equals(stationWeatherObjectB.YYMMDDHHMM))){
      return true;
    } else return false;

  }




  public String getStn(){
    return stn;
  }

  public Date getYYMMDDHHMM(){
    return YYMMDDHHMM;
  }

  public void setStn(String newStn){
    stn= newStn;
  }

  public void setYYMMDDHHMM(Date newYYMMDDHHMM){
    YYMMDDHHMM= newYYMMDDHHMM;
  }

  @Override
  public String toString(){
    return stn+":"+YYMMDDHHMM;
  }
}
