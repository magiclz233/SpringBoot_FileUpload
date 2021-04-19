package com.magic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


/**
 * @author magic
 */
@RestController
public class FileUploadController {

public static final Logger LOG = LoggerFactory.getLogger(FileUploadController.class);

@RequestMapping("/upload")
public String upload(@RequestParam("file")MultipartFile file, Model model){
    if(file.isEmpty()){
        return "文件为空";
    }
    String fileName = file.getOriginalFilename();
    LOG.info("上传的文件名为："+fileName);
    String suffixName = fileName.substring(fileName.lastIndexOf("."));
    LOG.info("文件的后缀名："+suffixName);
    String filePath = "E:/upload/";
    String path = filePath+fileName;
    File file1 = new File(path);
    if(!file1.getParentFile().exists()){
        file1.getParentFile().mkdirs();
    }
    try {
        file.transferTo(file1);
        return "上传成功";
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "上传失败";
}

@PostMapping("/batch")
public String handleFileUpload(HttpServletRequest request){
    List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
    MultipartFile file = null;
    BufferedOutputStream stream = null;
    for (int i = 0; i < files.size(); i++) {
        file = files.get(i);
        String filePath = "E:/upload/";
        if(!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                stream = new BufferedOutputStream(new FileOutputStream
                        (new File(filePath + file.getOriginalFilename())));//设置文件路径以及名字
                stream.write(bytes);
                stream.close();
            } catch (IOException e) {
                stream = null;
                return "第"+i+"个文件上传失败==>"+e.getMessage();
            }
        }else {
            return "第"+i+"个文件上传失败,文件为空";
        }
    }
    return "success";
}

    /**
     * 只是测试，给固定的地方放一张magic.jpg图片，然后点击下载按钮进行下载
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "magic.jpg";// 文件名
        if (fileName != null) {
            //设置文件路径
            File file = new File("E:/upload/magic.jpg");
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "下载失败";
    }






}
