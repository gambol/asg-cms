/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.bieshao.utils;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author root
 */
public class DateUtil {
   public static Timestamp getCurrentTimestamp() {                                                                                                
        return new Timestamp(new Date().getTime());                                                                                                
    }                                                                                                                                              
         
}
