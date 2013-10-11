package com.sjsu.cmpe226.mesonet.vo;

import java.util.Date;

public class WeatherDataVO {

  private StationWeatherWithTime swtObj;

  //private String STN;

  //private Date YYMMDDHHMM;

  private double MNET;

  private double SLAT;

  private double SLON;

  private double SELV;

  private double TMPF;

  private double SKNT;

  private double DRCT;

  private double GUST;

  private double PMSL;

  private double ALTI;

  private double DWPF;

  private double RELH;

  private double WTHR;

  private double P24I;

  private Date LAST_UPDATE_DATE;

  private Date INSERT_DATE;


  /*for composite Id
   * public String getSTN() {
    return STN;
  }

  public void setSTN(String sTN) {
    STN = sTN;
  }

  public Date getYYMMDDHHMM() {
    return YYMMDDHHMM;
  }

  public void setYYMMDDHHMM(Date yYMMDDHHMM) {
    YYMMDDHHMM = yYMMDDHHMM;
  }
   */
  public StationWeatherWithTime getSwtObj() {
    return swtObj;
  }
  public void setSwtObj(StationWeatherWithTime newswtObj) {
    swtObj = newswtObj;
  }

  public double getMNET() {
    return MNET;
  }

  public void setMNET(double mNET) {
    MNET = mNET;
  }

  public double getSLAT() {
    return SLAT;
  }

  public void setSLAT(double sLAT) {
    SLAT = sLAT;
  }

  public double getSLON() {
    return SLON;
  }

  public void setSLON(double sLON) {
    SLON = sLON;
  }

  public double getSELV() {
    return SELV;
  }

  public void setSELV(double sELV) {
    SELV = sELV;
  }

  public double getTMPF() {
    return TMPF;
  }

  public void setTMPF(double tMPF) {
    TMPF = tMPF;
  }

  public double getSKNT() {
    return SKNT;
  }

  public void setSKNT(double sKNT) {
    SKNT = sKNT;
  }

  public double getDRCT() {
    return DRCT;
  }

  public void setDRCT(double dRCT) {
    DRCT = dRCT;
  }

  public double getGUST() {
    return GUST;
  }

  public void setGUST(double gUST) {
    GUST = gUST;
  }

  public double getPMSL() {
    return PMSL;
  }

  public void setPMSL(double pMSL) {
    PMSL = pMSL;
  }

  public double getALTI() {
    return ALTI;
  }

  public void setALTI(double aLTI) {
    ALTI = aLTI;
  }

  public double getDWPF() {
    return DWPF;
  }

  public void setDWPF(double dWPF) {
    DWPF = dWPF;
  }

  public double getRELH() {
    return RELH;
  }

  public void setRELH(double rELH) {
    RELH = rELH;
  }

  public double getWTHR() {
    return WTHR;
  }

  public void setWTHR(double wTHR) {
    WTHR = wTHR;
  }

  public double getP24I() {
    return P24I;
  }

  public void setP24I(double p24i) {
    P24I = p24i;
  }

  public Date getLAST_UPDATE_DATE() {
    return LAST_UPDATE_DATE;
  }

  public void setLAST_UPDATE_DATE(Date lAST_UPDATE_DATE) {
    LAST_UPDATE_DATE = lAST_UPDATE_DATE;
  }

  public Date getINSERT_DATE() {
    return INSERT_DATE;
  }

  public void setINSERT_DATE(Date iNSERT_DATE) {
    INSERT_DATE = iNSERT_DATE;
  }





}