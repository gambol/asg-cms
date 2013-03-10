package com.bieshao.model;

import java.io.Serializable;

import cn.bieshao.common.DBColumnName;
import cn.bieshao.common.DBTableName;

@DBTableName(value = "comment")
public class Comment implements Serializable{

    @DBColumnName(value = "id")
    private int id;
    
    @DBColumnName(value = "comment")
    private String comment;

    @DBColumnName(value = "disabled")
    private boolean disabled;
    
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
}
