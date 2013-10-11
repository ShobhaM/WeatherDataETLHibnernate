package com.sjsu.cmpe226.mesonet.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * 
 * @author <a href="Bhargav@leantaas.com">Bhargav</a>
 */
public class WriteToFile {

    private Logger logger = Logger.getLogger(WriteToFile.class.getName());
    private BufferedWriter out = null;
    
    /**
     * Method used to write data to file
     * @param data
     */
    public boolean writeToFileMethod(StringBuilder data,String fileName){
        boolean flag = false;
        
        try{
        out = new BufferedWriter(new FileWriter(fileName));
        out.write(data.toString());
        out.close();
        flag =  true;
        }catch(IOException e){
            logger.severe("Issue with writing file "+e.getMessage());
        }finally{
            if(out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    logger.severe("Issue with writing file "+e.getMessage());
                }
        }
        
        return flag;
    }
    
    /**
     * Method used to append data to file
     * @param data
     */
    public synchronized boolean appendToFileMethod(StringBuilder data,String fileName){
        boolean flag = false;
        
        try{
        out = new BufferedWriter(new FileWriter(fileName,true));
        out.write(data.toString());
        out.close();
        flag =  true;
        }catch(IOException e){
            logger.severe("Issue with writing file "+e.getMessage());
        }finally{
            if(out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    logger.severe("Issue with writing file "+e.getMessage());
                }
        }
        
        return flag;
    }
    
}
