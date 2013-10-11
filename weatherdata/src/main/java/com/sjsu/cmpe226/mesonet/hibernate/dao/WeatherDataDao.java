package com.sjsu.cmpe226.mesonet.hibernate.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sjsu.cmpe226.mesonet.util.HibernateUtil;
import com.sjsu.cmpe226.mesonet.vo.StationWeatherWithTime;
import com.sjsu.cmpe226.mesonet.vo.WeatherDataVO;
//import com.sjsu.cmpe226.mesonet.vo.WeatherMetaDataVO;
import com.sjsu.cmpe226.mesonet.vo.WeatherMetaDataVO;

public class WeatherDataDao {

  //JM@: change name from saveDataContent to createWeatherObject, create another method for commit control
  public WeatherDataVO createWeatherObject(HashMap<String,String> contentInfo, Integer counter) {
    WeatherDataVO metaOutDataObj = new WeatherDataVO();
    //JM@: session will not be handled here;
    //Session sessionWeather = HibernateUtil.getDataSessionFactory().openSession();
    //sessionWeather.beginTransaction();
    StationWeatherWithTime swtObj =  new StationWeatherWithTime();
    swtObj.setStn(contentInfo.get("STN"));


    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd/hhmm");

    Date dt = null;
    try {
      dt = df.parse(contentInfo.get("YYMMDD/HHMM"));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();    }

    swtObj.setYYMMDDHHMM(dt);

    metaOutDataObj.setSwtObj(swtObj);//(contentInfo.get("STN"));

    metaOutDataObj.setMNET(Double.parseDouble(contentInfo.get("MNET")));
    metaOutDataObj.setSLAT(Double.parseDouble(contentInfo.get("SLAT")));
    metaOutDataObj.setSLON(Double.parseDouble(contentInfo.get("SLON")));
    metaOutDataObj.setSELV(Double.parseDouble(contentInfo.get("SELV")));
    metaOutDataObj.setTMPF(Double.parseDouble(contentInfo.get("TMPF")));
    metaOutDataObj.setSKNT(Double.parseDouble(contentInfo.get("SKNT")));
    metaOutDataObj.setDRCT(Double.parseDouble(contentInfo.get("DRCT")));
    metaOutDataObj.setGUST(Double.parseDouble(contentInfo.get("GUST")));
    metaOutDataObj.setPMSL(Double.parseDouble(contentInfo.get("PMSL")));
    metaOutDataObj.setALTI(Double.parseDouble(contentInfo.get("ALTI")));
    metaOutDataObj.setDWPF(Double.parseDouble(contentInfo.get("DWPF")));
    metaOutDataObj.setRELH(Double.parseDouble(contentInfo.get("RELH")));
    metaOutDataObj.setWTHR(Double.parseDouble(contentInfo.get("WTHR")));
    metaOutDataObj.setP24I(Double.parseDouble(contentInfo.get("P24I")));
    metaOutDataObj.setINSERT_DATE(new Date());
    metaOutDataObj.setLAST_UPDATE_DATE(new Date());

    /*if (contentInfo.get("STN").equals("SVRA2")){
      System.out.println("inside createWeatherObject:"+metaOutDataObj.hashCode()+":");
    }*/

    return metaOutDataObj;

    /*JM@ marked for batch commit;
    session.save(metaOutDataObj);
    session.getTransaction().commit();
    HibernateUtil.shutdownData();*/
  }
  public synchronized String saveWeatherDataHeader(WeatherDataVO weatherDataObj,
                                                   Session session,Transaction txn,Integer counter) throws InterruptedException{

    try{
      session.save(weatherDataObj);
      //TODO:saveOrUpdate(metaDataObj); record in report, saveOrUpdate performance is pretty bad;

      //System.out.println("begin session.flush");
      if (counter%10000 ==0) {
        session.flush();
        //System.out.println("begin session.clean");
        session.clear();
      } //Batch to DB; size = 10000
    } catch(Exception e){
      System.out.println("Exception!");
      return weatherDataObj.getSwtObj().toString();

    }
    return "-1";
  }

}