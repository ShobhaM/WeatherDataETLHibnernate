
package com.sjsu.cmpe226.mesonet.executor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.sjsu.cmpe226.mesonet.hibernate.dao.WeatherMetaDataDao;
import com.sjsu.cmpe226.mesonet.hibernate.dao.WeatherMetaDataDaoJingTest;
import com.sjsu.cmpe226.mesonet.util.HibernateUtil;
import com.sjsu.cmpe226.mesonet.util.ReadFromFile;
import com.sjsu.cmpe226.mesonet.vo.WeatherDataVO;
import com.sjsu.cmpe226.mesonet.vo.WeatherMetaDataVO;
import com.sjsu.cmpe226.mesonet.hibernate.dao.*;
import java.util.HashMap;

public class LoadDataWithHibernateJing {

  private static Logger logger = Logger.getLogger(LoadDataWithHibernate.class
    .getName());
  private final StringBuilder data = null;
  private DataInputStream in = null;
  private BufferedReader br = null;
  private final WeatherMetaDataDao weatherMetaDao = new WeatherMetaDataDao();


  public void updateOrSaveObj(HashMap<String, WeatherMetaDataVO> objsMap) throws InterruptedException{
    System.out.println("begin transactions");
    Session session = HibernateUtil.getMetaSessionFactory().openSession();
    session.beginTransaction();
    Transaction txn = session.getTransaction();
    System.out.println("begin saveDataHeader");

    int objCount = 0;
    for(String keyId: objsMap.keySet()){
      // Session session2 = HibernateUtil.getMetaSessionFactory().openSession();
      //session2.beginTransaction();
      //Transaction txn2 = session2.getTransaction();
      objCount++;
      WeatherMetaDataVO value = objsMap.get(keyId);
      weatherMetaDao.saveDataHeader(value, session, txn,objCount);
      if(objCount%1000 ==0){
        //session2.saveOrUpdate(value);
        // txn2.commit();
        //session2.close();
      }


    }

    //Jing@: After all pre-comparing commit to DB and close session;
    try {
      txn.commit();
      System.out.println("Record will be saved.");
      session.close();
      HibernateUtil.shutdownHeader();
    } catch (ConstraintViolationException  e){
      //txn.rollback();
      System.out.println("----------ConstraintViolationException: will do duplicateHandler ------------");
      //duplicateHandlerHQL(session,metaDataObj);
      HibernateUtil.shutdownHeader();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    long startTime = System.currentTimeMillis();

    LoadDataWithHibernate obj = new LoadDataWithHibernate();

    // Read data from file and save to hashMap.
    HashMap<String,WeatherMetaDataVO> objsMap =null;
    try{
      objsMap = obj.readFromFileMethod("D:/CMPE226_weatherData/weather_data/wd_data/mesowest_csv.tbl");
      //JM@: ("/Users/bhargav_sjsu/Documents/weather_data/wd_meta/mesowest_csv.tbl");
    } catch(NullPointerException E){
      System.out.println("Reached the end of this file");
    }

    // Save data to database.
    obj.updateOrSaveObj(objsMap);
    long endTime = System.currentTimeMillis();
    long timeGap= (endTime - startTime)/1000;
    System.out.println("Total use time for meta data:"+timeGap+"(secs)"+ "Total records");




  }


  public HashMap<String, WeatherMetaDataVO> readFromFileMethod(String fileName) {
    HashMap<String,WeatherMetaDataVO> uniqueObj = new HashMap<String,WeatherMetaDataVO>();
    try {
      // Initialize the file input stream
      FileInputStream fstream = new FileInputStream(fileName);
      int count = 0;
      // Get DataInputStream object
      in = new DataInputStream(fstream);
      br = new BufferedReader(new InputStreamReader(in));
      String strLine = null;

      // Read File Line By Line
      while ((strLine = br.readLine()) != null) {
        // append the content to StringBulder
        count++;

        if (count == 1)
          continue;

        HashMap<String, String> metaInfo = new HashMap<String, String>();
        String[] lineArray = strLine.split(",");

        String pri_key = lineArray[0].toString().replaceAll(" ", "");//@Jing

        if (lineArray.length == 18 && (pri_key != ""||pri_key != null) ) {//@Jing
          metaInfo.put("primary_id", lineArray[0]);
          metaInfo.put("secondary_id", lineArray[1]);
          metaInfo.put("station_name", lineArray[2]);
          metaInfo.put("state", lineArray[3]);
          metaInfo.put("country", lineArray[4]);
          metaInfo.put("latitude", lineArray[5]);
          metaInfo.put("longitude", lineArray[6]);
          metaInfo.put("elevation", lineArray[7]);
          metaInfo.put("mesowest_network_id", lineArray[8]);
          metaInfo.put("network_name", lineArray[9]);
          metaInfo.put("status", lineArray[10]);
          metaInfo.put("primary_provider_id", lineArray[11]);
          metaInfo.put("primary_provider", lineArray[12]);
          metaInfo.put("secondary_provider_id", lineArray[13]);
          metaInfo.put("secondary_provider", lineArray[14]);
          metaInfo.put("tertiary_provider_id", lineArray[15]);
          metaInfo.put("tertiary_provider", lineArray[16]);
          metaInfo.put("wims_id", lineArray[17]);

        } else if ((pri_key == ""||pri_key == null)){
          //@Jing:Check primary_id in Weathermeta_data is blank or not
          System.out.println( pri_key+ " is invalid.");
          continue;
        }

        WeatherMetaDataVO metaObj = weatherMetaDao.createMetaDataObject(metaInfo, count);
        uniqueObj.put(metaObj.getPrimary_id(), metaObj);
        //weatherMetaDao.saveDataHeader(metaObj, session, txn,count);

      }

      // Close the input stream
      in.close();

    } catch (Exception e) {
      logger.severe("Issue with reading file " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          logger.severe("Issue with reading file " + e.getMessage());
        }
      }
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          logger.severe("Issue with reading file " + e.getMessage());
        }
      }
    }
    return uniqueObj;
  }



}
