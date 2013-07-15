package com.tophey.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
@RequestMapping("/user/")
public class PicUploadController {

    @RequestMapping(value="uploadImage", method = RequestMethod.POST)
    public String upload(HttpServletRequest request,   
            HttpServletResponse response) throws Exception{   
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", -10);
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存缓冲区，超过后写入临时文件
        factory.setSizeThreshold(10240000);
        // 设置临时文件存储位置
        String base = "/tmp/upload";
        File file = new File(base);
        if(!file.exists())
             file.mkdirs();
        
        factory.setRepository(file);
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置单个文件的最大上传值
        upload.setFileSizeMax(10002400000l);
        // 设置整个request的最大值
        upload.setSizeMax(10002400000l);
        upload.setHeaderEncoding("UTF-8");
        String ret2 = null;
        try {
                List items = upload.parseRequest(request);
                FileItem item = null;
                String fileName = null;
                for (int i = 0 ;i < items.size(); i++){
                        item = (FileItem) items.get(i);
                        fileName = base + item.getName();
                        // 保存文件
                        if (!item.isFormField() && item.getName().length() > 0) {
                                String key = item.getName();
                                File f = new File(fileName);
                                item.write(new File(fileName));
                        }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
                                

        return null;
    }  
    
    @RequestMapping("uptest")
    public String index(HttpServletRequest request, HttpServletResponse response){
        System.out.println("hehe");
       return "uploadTest";
    }

}
