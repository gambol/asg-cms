/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.bieshao.utils;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author xiang.fu
 */
public class ConfigUtils {

    private static final String TOPHEY_CONFIG_ID = "TopHey Config";

    public static void saveProp(Properties prop, String filePath) throws IOException {
        if (prop == null) {
            return;
        }
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        prop.storeToXML(new FileOutputStream(file), TOPHEY_CONFIG_ID);
    }

    public static Properties loadProp(String filePath) throws IOException {
        File file = new File(filePath);
        Properties p = new Properties();
        p.loadFromXML(new FileInputStream(file));
        return p;
    }

    public static void savePlainProp(Properties prop, String filePath) throws IOException {
        if (prop == null) {
            return;
        }
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        prop.store(new FileOutputStream(file), TOPHEY_CONFIG_ID);
    }

    public static Properties loadPlainProp(String filePath) throws IOException {
        File file = new File(filePath);
        Properties p = new Properties();
        p.load(new FileInputStream(file));
        return p;
    }
}
