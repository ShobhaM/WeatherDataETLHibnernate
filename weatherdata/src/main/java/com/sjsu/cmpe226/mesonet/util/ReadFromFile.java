package com.sjsu.cmpe226.mesonet.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * 
 * @author <a href="Bhargav@leantaas.com">Bhargav</a>
 */
public class ReadFromFile {

    private static Logger          logger = Logger.getLogger(ReadFromFile.class.getName());
    private StringBuilder   data   = null;
    private DataInputStream in     = null;
    private BufferedReader  br     = null;

    /**
     * Method used to read data from file
     * 
     * @param data
     */
    public StringBuilder readFromFileMethod(String fileName) {
        try {
            // Initialize the file input stream
            FileInputStream fstream = new FileInputStream(fileName);
            data = new StringBuilder();

            // Get DataInputStream object
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine = null;

            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // append the content to StringBulder
                data.append(strLine);
                data.append("\n");
            }

            // Close the input stream
            in.close();

        } catch (Exception e) {
            logger.severe("Issue with reading file " + e.getMessage());
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

        return data;
    }
    
}
