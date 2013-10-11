package com.sjsu.cmpe226.mesonet.hibernate.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class WeatherMetaDataDaoJingTest {
	public boolean duplicatehandler(){
		boolean result = false;		
		
		return result;
	}

	
    public synchronized void saveDataHeader(HashMap<String,String> metaInfo,Integer counter) throws InterruptedException{
        
        Session session = HibernateUtil.getMetaSessionFactory().openSession();
        
        session.beginTransaction();
        Transaction txn = session.getTransaction(); //Jing@
       
        
        WeatherMetaDataVO metaDataObj = new WeatherMetaDataVO();
        metaDataObj.setId(counter);    
       
        metaDataObj.setPrimary_id(metaInfo.get("primary_id").toString());
        metaDataObj.setSecondary_id(metaInfo.get("secondary_id"));
        metaDataObj.setStation_name(metaInfo.get("station_name"));
        metaDataObj.setState(metaInfo.get("state"));
        metaDataObj.setCountry(metaInfo.get("country"));        
        metaDataObj.setCountry(metaInfo.get("latitude"));
        metaDataObj.setCountry(metaInfo.get("longitude"));
        metaDataObj.setCountry(metaInfo.get("elevation"));
        metaDataObj.setCountry(metaInfo.get("mesowest_network_id"));
        metaDataObj.setCountry(metaInfo.get("network_name"));
        metaDataObj.setCountry(metaInfo.get("status"));
        metaDataObj.setCountry(metaInfo.get("primary_provider_id"));
        metaDataObj.setCountry(metaInfo.get("primary_provider"));
        metaDataObj.setCountry(metaInfo.get("secondary_provider_id"));
        metaDataObj.setCountry(metaInfo.get("secondary_provider"));
        metaDataObj.setCountry(metaInfo.get("tertiary_provider_id"));        
        metaDataObj.setCountry(metaInfo.get("tertiary_provider"));
        metaDataObj.setCountry(metaInfo.get("wims_id"));        
        metaDataObj.setINSERT_DATE(new Date());
        metaDataObj.setLAST_UPDATE_DATE(new Date());   
        
        try {
        	 session.save(metaDataObj);
        	 
        	 if (counter%10 ==0) {
        		 session.flush();
        		 session.clear();
        	 } //Batch to DB; size = 10
                       	  
        	           
             txn.commit();
             System.out.println(metaDataObj.getPrimary_id().toString() + " will be saved.");             
        
        	 HibernateUtil.shutdownHeader();
        	
        } catch (ConstraintViolationException  e){        	
        	txn.rollback();
        	System.out.println("----------ConstraintViolationException: will do duplicateHandler ------------");
        	duplicateHandlerHQL(session,metaDataObj);          	
            HibernateUtil.shutdownHeader();        	
        }
        
    }
 
    private void duplicateHandler(Session session ,String priID, WeatherMetaDataVO metaDataObj,
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
    
    public boolean duplicateHandlerHQL(Session session, WeatherMetaDataVO metaDataObj){
    	
    	String hql = "delete from WeatherMetaDataVO  where primary_id =";
    	hql = hql+"\'"+metaDataObj.getPrimary_id().toString()+"\'";    	
    	System.out.println("----------"+hql+"------------");
    	session.createQuery(hql).executeUpdate();    	
    	
    	Transaction txnSec = session.beginTransaction();            
        session.save(metaDataObj);              
        txnSec.commit(); 
        
        return true; 
    }
    
    public boolean duplicateHandlerHQL(Session session, WeatherDataVO DataObj){
    	boolean r = false;
    	//TODO: Should ID be the primary key for weatherData?
    	String hql = "delete from WeatherDataVO  where id =";
    	hql = hql+"\'"+DataObj.getIdNo()+"\'";    	
    	System.out.println("----------"+hql+"------------");
    	session.createQuery(hql).executeUpdate();    	
    	//insert new one
    	Transaction txnSec = session.beginTransaction();            
        session.save(DataObj);              
        txnSec.commit(); 
        
        return r=true; 
    }
    
}
