/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.common;

import java.io.File;

/**
 *
 * @author xiang.fu
 */
public class PathConst {
    
    private static final String CONFIG_DIR_PATH = System.getProperty("user.home") + File.separator + ".sifu" + File.separator + "config";
    
    private static final String DB_CONFIG_NAME = "db.xml";
    public static final String DB_CONFIG_PATH = CONFIG_DIR_PATH + File.separator + DB_CONFIG_NAME;
    
    public static final String UPLOAD_PIC_PATH = System.getProperty("user.home") + File.separator + "topheyUploadPath" + File.separator;
}