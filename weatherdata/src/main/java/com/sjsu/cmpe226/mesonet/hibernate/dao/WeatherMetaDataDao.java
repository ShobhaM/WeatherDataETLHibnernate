package com.sjsu.cmpe226.mesonet.hibernate.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;//JM@:add for catching javax.persistence.PersistenceException

import org.hibernate.exception.ConstraintViolationException;//JM@:add for catching ConstraintViolationException

import com.sjsu.cmpe226.mesonet.constants.WeatherDataConstants;
import com.sjsu.cmpe226.mesonet.util.HibernateUtil;
import com.sjsu.cmpe226.mesonet.vo.WeatherMetaDataVO;
import com.sjsu.cmpe226.mesonet.vo.WeatherDataVO;

/**
 * @author Bhargav
 */
public class WeatherMetaDataDao {
  public boolean duplicatehandler(){
    boolean result = false;
    return result;
  }

  public WeatherMetaDataVO createMetaDataObject(HashMap<String,String> metaInfo,
                                                Integer counter){
    WeatherMetaDataVO metaDataObj = new WeatherMetaDataVO();
    // metaDataObj.setId(counter);

    metaDataObj.setPrimary_id(metaInfo.get("primary_id").toString());
    metaDataObj.setSecondary_id(metaInfo.get("secondary_id"));
    metaDataObj.setStation_name(metaInfo.get("station_name"));
    metaDataObj.setState(metaInfo.get("state"));
    metaDataObj.setCountry(metaInfo.get("country"));
    try {
      metaDataObj.setLatitude(Double.parseDouble(metaInfo.get("latitude")));
    } catch (NumberFormatException e){
      metaDataObj.setLatitude(0.0);
    }

    try {
      metaDataObj.setLongitude(Double.parseDouble(metaInfo.get("longitude")));
    } catch (NumberFormatException e){
      metaDataObj.setLongitude(0.0);
    }

    try {
      metaDataObj.setElevation(Double.parseDouble(metaInfo.get("elevation")));
    } catch (NumberFormatException e){
      metaDataObj.setElevation(0.0);
    }

    try {
      metaDataObj.setMesowest_network_id(Integer.parseInt(metaInfo.get("mesowest_network_id")));
    } catch (NumberFormatException e){
      metaDataObj.setMesowest_network_id(0);
    }
    metaDataObj.setNetwork_name(metaInfo.get("network_name"));
    metaDataObj.setStatus(metaInfo.get("status"));
    try{
      metaDataObj.setPrimary_provider_id(Integer.parseInt(metaInfo.get("primary_provider_id")));
    } catch(NumberFormatException e){
      metaDataObj.setPrimary_provider_id(0);
    }

    metaDataObj.setPrimary_provider(metaInfo.get("primary_provider"));

    try {
      metaDataObj.setSecondary_provider_id(Integer.parseInt(metaInfo.get("mesowest_network_id")));
    } catch(NumberFormatException e){
      metaDataObj.setSecondary_provider_id(0);
    }

    metaDataObj.setSecondary_id(metaInfo.get("secondary_provider"));
    try {
      metaDataObj.setTertiary_provider_id(Integer.parseInt(metaInfo.get("tertiary_provider_id")));
    } catch(NumberFormatException e){
      metaDataObj.setTertiary_provider_id(0);
    }

    metaDataObj.setTertiary_provider(metaInfo.get("tertiary_provider"));
    metaDataObj.setSecondary_id(metaInfo.get("secondary_provider"));
    try {
      metaDataObj.setWims_id(Integer.parseInt(metaInfo.get("wims_id")));
    } catch(NumberFormatException e){
      metaDataObj.setWims_id(0);
    }

    metaDataObj.setINSERT_DATE(new Date());
    metaDataObj.setLAST_UPDATE_DATE(new Date());
    return metaDataObj;
  }

  public synchronized void saveDataHeader(WeatherMetaDataVO metaDataObj,
                                          Session session,Transaction txn,Integer counter) throws InterruptedException{

    //WeatherMetaDataVO metaDataObj = createMetaDataObject(metaInfo,counter);



    //System.out.println(metaDataObj.getPrimary_id().toString()+"  "+counter);
    session.save(metaDataObj);//saveOrUpdate(metaDataObj);

    //System.out.println("begin session.flush");
    if (counter%10000 ==0) {
      session.flush();
      //System.out.println("begin session.clean");
      session.clear();
    } //Batch to DB; size = 10000
  }






  /* private void duplicateHandler(Session session ,String priID, WeatherMetaDataVO metaDataObj,
                                String station){
    Transaction tran = session.beginTransaction();
    WeatherMetaDataVO obj = (WeatherMetaDataVO) session.get(WeatherMetaDataVO.class, metaDataObj.getId());

    System.out.println("----------then delete ------------");
    session.delete(obj);
    System.out.println("----------then commit delete ------------");
    try{
      tran.commit();
      System.out.println("----------after delete ------------");

    } catch (Exception e){
      tran.rollback();
      e.printStackTrace();
    }
  }
   */

}
