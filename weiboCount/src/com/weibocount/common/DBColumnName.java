/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author xiang.fu
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBColumnName {
    
    String value();
    
}
