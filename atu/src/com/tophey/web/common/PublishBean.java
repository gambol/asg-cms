/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.web.common;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;


/**
 *
 * @author zhenbao.zhou
 */
public class PublishBean {

    private int id;
    
    @Size(min = 1, max = 20)
    private String server_name;
     
    @Size(min = 7, max = 200)
    @URL
    private String url = "http://";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
   
    @URL
    private String banner = "http://";
    
    @Size(max = 2000)
    private String desc;
    
    private String network;
    
    private int category;

    public String getBanner() {
        return banner;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }


    
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
  

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("properties name=");
        if (server_name != null) {
            sb.append("'").append(server_name).append("', ");
        } else {
            sb.append(server_name).append(", ");
        }
        
        sb.append("categoryId:");
        sb.append(category);

        return sb.toString();
    }
}
