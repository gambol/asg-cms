package com.tophey.model;

import com.tophey.common.DBColumnName;
import com.tophey.common.DBTableName;
import java.io.Serializable;
import java.sql.Timestamp;
 
@DBTableName(value="category")
public class Category implements Serializable {

    private static final long serialVersionUID = -6613259079097692609L;
    @DBColumnName(value="id")
    private int id;
    @DBColumnName(value="name")
    private String name;
    @DBColumnName(value="image_url")
    private String imageUrl;
    @DBColumnName(value="description")
    private String description;
    @DBColumnName("create_date")
    private Timestamp createDate;
    @DBColumnName("display_order")
    private int displayOrder;
    @DBColumnName("is_disabled")
    private int isDisabled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(int isDisabled) {
        this.isDisabled = isDisabled;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
    
}
