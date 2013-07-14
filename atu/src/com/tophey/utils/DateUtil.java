/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author root
 */
public class DateUtil {
   public static Timestamp getCurrentTimestamp() {                                                                                                
        return new Timestamp(new Date().getTime());                                                                                                
    }                                                                                                                                              
    
   public static String getTimeStr(Date d){
       SimpleDateFormat dateFormat = new SimpleDateFormat(
               "yyyy-MM-dd- HH:mm:ss");
       return dateFormat.format(d);
   }
      
}
