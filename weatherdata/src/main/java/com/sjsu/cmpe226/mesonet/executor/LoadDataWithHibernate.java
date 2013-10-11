
package com.sjsu.cmpe226.mesonet.executor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
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
import com.sjsu.cmpe226.mesonet.vo.StationWeatherWithTime;
import com.sjsu.cmpe226.mesonet.vo.WeatherDataVO;
import com.sjsu.cmpe226.mesonet.vo.WeatherMetaDataVO;
import com.sjsu.cmpe226.mesonet.hibernate.dao.*;
import java.util.HashMap;

public class LoadDataWithHibernate {

  private static Logger logger = Logger.getLogger(LoadDataWithHibernate.class
    .getName());
  private final StringBuilder data = null;
  private DataInputStream in = null;


  private BufferedReader br = null;
  private final WeatherMetaDataDao weatherMetaDao = new WeatherMetaDataDao();
  private final WeatherDataDao weatherDao = new WeatherDataDao();

  public void updateOrSaveWeatherObj(HashMap<String, WeatherDataVO> objsMapWeather) throws InterruptedException{
    System.out.println("begin txns for WeahterData ");
    Session session = HibernateUtil.getDataSessionFactory().openSession();
    session.beginTransaction();
    Transaction txn = session.getTransaction();
    System.out.println("begin save process for WeahterData ");
    int objCount = 0;
    for(String keyId: objsMapWeather.keySet()){

      objCount++;
      //System.out.println("keyId: "+keyId);
      WeatherDataVO value = objsMapWeather.get(keyId);
      weatherDao.saveWeatherDataHeader(value, session, txn, objCount);


    }

    //Jing@: After all pre-comparing commit to DB and close session;
    try {
      txn.commit();
      //System.out.println("Record will be saved.");
      session.close();
      HibernateUtil.shutdownHeader();
    } catch (ConstraintViolationException  e){
      //txn.rollback();
      System.out.println("----------ConstraintViolationException: will do duplicateHandler ------------");
      //duplicateHandlerHQL(session,metaDataObj);
      HibernateUtil.shutdownHeader();
    }
  }


  public void updateOrSaveMetaObj(HashMap<String, WeatherMetaDataVO> objsMap)
      throws InterruptedException{
    System.out.println("begin txns for MetaData ");
    Session session = HibernateUtil.getMetaSessionFactory().openSession();
    session.beginTransaction();
    Transaction txn = session.getTransaction();

    int objCount = 0;
    for(String keyId: objsMap.keySet()){

      objCount++;
      WeatherMetaDataVO value = objsMap.get(keyId);
      weatherMetaDao.saveDataHeader(value, session, txn,objCount);

    }

    //Jing@: After all pre-comparing commit to DB and close session;
    try {
      txn.commit();
      //System.out.println("Record will be saved.");
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

    //JM@: load meta data
    LoadDataWithHibernate objLoader = new LoadDataWithHibernate();

    // Read data from file and save to hashMap.
    HashMap<String,WeatherMetaDataVO> objsMap =null;
    try{
      //:tempmarked:
      objsMap = objLoader.readFromFileMethod("D:/CMPE226_weatherData/weather_data/wd_data/mesowest_csv.tbl");
      //JM@: ("/Users/bhargav_sjsu/Documents/weather_data/wd_meta/mesowest_csv.tbl");
    } catch(NullPointerException E){
      System.out.println("Reached the end of this file for MetaData.");
    }

    //JM@ Save meta data to database.
    //: tempmarked:
    objLoader.updateOrSaveMetaObj(objsMap);
    long endTime = System.currentTimeMillis();
    long timeGap= (endTime - startTime)/1000;
    System.out.println("Total use time for meta data:"+timeGap+"(secs)"+ "Total records");


    //JM@: for weather data loading

    long startTimeWeather = System.currentTimeMillis();
    LoadDataWithHibernate objWeatherLoader = new LoadDataWithHibernate();
    HashMap<String,WeatherDataVO> objsMapWeather =null;
    try{
      objsMapWeather = objWeatherLoader
          .readFromOutDataFileMethod("D:/CMPE226_weatherData/weather_data" +
              "/wd_data/mesowest.out.20130903T1545.tbl");
    } catch(NullPointerException E) {
      System.out.println("Reached the end of this file for WeatherData.");
    }
    objWeatherLoader.updateOrSaveWeatherObj(objsMapWeather);


    long endTimeWeather = System.currentTimeMillis();
    long timeGapWeather = (endTimeWeather - startTimeWeather)/1000;
    System.out.println("Total use time for meta data:"+timeGapWeather+"(secs)"+ "Total records");

  }

  //JM@: method for reading meta data
  public HashMap<String, WeatherMetaDataVO> readFromFileMethod(String fileName) {
    HashMap<String,WeatherMetaDataVO> uniqueObjMap = new HashMap<String,WeatherMetaDataVO>();
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
        uniqueObjMap.put(metaObj.getPrimary_id(), metaObj);
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
    return uniqueObjMap;
  }

  //JM@ method from Shobha, for incrementally reading in weather data
  public HashMap<String, WeatherDataVO> readFromOutDataFileMethod(String filename) {
    HashMap<String, WeatherDataVO> uniqueWeatherDataMap
    =  new  HashMap<String, WeatherDataVO>();

    WeatherDataDao weatherDataDao = new WeatherDataDao();
    WeatherDataVO weatherObj;
    try {
      FileInputStream fipstream = new FileInputStream(filename);
      System.out.println("Total size of the file to read "
          + fipstream.available());

      int count = 0;

      in = new DataInputStream(fipstream);
      br = new BufferedReader(new InputStreamReader(in));

      String strLine = null;



      while ((strLine = br.readLine()) != null) {
        count++;

        if (count <= 4)
          continue;
        //TODO: JM@:delete it after we use unix deal with pre-clean-deuplicate

        HashMap<String, String> contentDataInfo = new HashMap<String, String>();
        Scanner sc = new Scanner(strLine);
        ArrayList<String> lineArray = new ArrayList<String>();

        while(sc.hasNext())
        {
          lineArray.add(sc.next());
        }

        String station =lineArray.get(0).toString().replace(" ", "");
        String timestamp =lineArray.get(1).toString();

        if ((lineArray.size() == 16)
            &&(lineArray.get(0) !=null)&& (lineArray.get(0) !="")
            &&(lineArray.get(1) !=null)&& (lineArray.get(1) !="")) {
          contentDataInfo.put("STN", lineArray.get(0));
          contentDataInfo.put("YYMMDD/HHMM", lineArray.get(1));
          contentDataInfo.put("MNET", lineArray.get(2));
          contentDataInfo.put("SLAT", lineArray.get(3));
          contentDataInfo.put("SLON", lineArray.get(4));
          contentDataInfo.put("SELV", lineArray.get(5));
          contentDataInfo.put("TMPF", lineArray.get(6));
          contentDataInfo.put("SKNT", lineArray.get(7));
          contentDataInfo.put("DRCT", lineArray.get(8));
          contentDataInfo.put("GUST", lineArray.get(9));
          contentDataInfo.put("PMSL", lineArray.get(10));
          contentDataInfo.put("ALTI", lineArray.get(11));
          contentDataInfo.put("DWPF", lineArray.get(12));
          contentDataInfo.put("RELH", lineArray.get(13));
          contentDataInfo.put("WTHR", lineArray.get(14));
          contentDataInfo.put("P24I", lineArray.get(15));
          //System.out.println( lineArray.get(0)+" and  "+lineArray.get(1)+";count="+count+ " for 1st hashmap.");



        } else {
          System.out.println( lineArray.get(0)+" and  "+lineArray.get(1)+ " is invalid.");
          continue;
        }

        weatherObj = weatherDataDao.createWeatherObject(contentDataInfo, count);
        uniqueWeatherDataMap.put(weatherObj.getSwtObj().toString(),weatherObj);


      }

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
    return uniqueWeatherDataMap;
  }


}
