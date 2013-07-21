package com.tophey.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tophey.utils.FileOperateUtil;

@Controller
@RequestMapping("/image/")
public class PicUploadController {

    @RequestMapping(value="uploadImage", method = RequestMethod.POST)
    public @ResponseBody String upload(HttpServletRequest request,   
            HttpServletResponse response) throws Exception{   
        Map<String, Object> map = new HashMap<String, Object>();  
        
  
        String[] params = new String[] { "alais" };  
        Map<String, Object[]> values = new HashMap<String, Object[]>();  
  
        // 其实FileOperateUtil支持一次上传多个文件。这里为了简单，还是只返回一个好了
        List<Map<String, Object>> result = FileOperateUtil.upload(request);  
        
        if (result.size() != 1) {
            return null;
        } else {
            Map m = result.get(0);
            return (String)m.get(FileOperateUtil.STORENAME);
        }
    }  
    
    @RequestMapping("uptest")
    public String index(HttpServletRequest request, HttpServletResponse response){
        System.out.println("hehe");
       return "uploadTest";
    }
    
    @RequestMapping("display")
    public String display(@RequestParam(required=true) String name,
            HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        FileOperateUtil.download(request, response, name, "image/jpeg");  
         
        return null;
    }
}
