/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biemian.db.common;

import java.io.File;

/**
 *
 * @author xiang.fu
 */
public class PathConst {
    
    private static final String CONFIG_DIR_PATH = System.getProperty("user.home") + File.separator + ".papasky_crawler" + File.separator + "config";
    
    private static final String DB_CONFIG_NAME = "db.xml";
    public static final String DB_CONFIG_PATH = CONFIG_DIR_PATH + File.separator + DB_CONFIG_NAME;
    
    
}
