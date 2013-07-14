/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.web.common;

/**
 *
 * @author xiang.fu
 */
public class RankPageQuery {

    private String categoryId;
    private int start;
    private int size;

    public RankPageQuery() {
        categoryId = "1";
        start = 0;
        size = 10;
    }
    
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
