package com.example.springbootfastdfs.controller;

import com.example.springbootfastdfs.one.FileClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.UUID;

/**
 * @Classname: TestController
 * @Description: TODO
 * @Date: 2022/1/11 14:24
 * @author: wwf
 * 已经上传的图片
 * http://172.168.253.183/group1/M00/00/00/rKj9t2HdP-uADJcFAA58T1dKwJs825.png
 */
@Controller
@RequestMapping(value = "/")
@Slf4j
public class TestOneController {

    @Autowired
    public FileClient fileClient;

    /**
     * @Description: 上传图片：http://localhost:8080/upload
     **/
    @GetMapping(value = "/upload")
    @ResponseBody
    public String upload() {
        File file = new File("D://01.png");
        String result = fileClient.uploadFile(file);
        log.info("上传文件：{}", result);
        return result;
    }

    /**
     * @Description: 下载图片：http://localhost:8080/download
     * @param: groupName   group1
     * @param fileUrl      M00/00/00/rKj9t2HdP-uADJcFAA58T1dKwJs825.png   注意前面没有 /
     **/
    @PostMapping(value = "/download")
    @ResponseBody
    public String download(String groupName, String fileUrl) {
        String ext_name = "png";
        if (fileUrl != null) {
            if (fileUrl.contains(".")) {
                ext_name = fileUrl.substring(fileUrl.indexOf(".") + 1);
            }
        }
        String fileName = "D://" + UUID.randomUUID().toString() + "." + ext_name;
        fileClient.downloadFile(groupName, fileUrl, fileName);
        log.info("下载文件：{}", fileName);
        return fileName;
    }

    /**
     * @Description: 删除图片：http://localhost:8080/delete
     * @param: groupName   group1
     * @param fileUrl      M00/00/00/rKj9t2HdP-uADJcFAA58T1dKwJs825.png   注意前面没有 /
     **/
    @PostMapping(value = "/delete")
    @ResponseBody
    public String delete(String groupName, String fileUrl) {
        fileClient.deleteFile(groupName, fileUrl);
        log.info("删除文件！");
        return "删除文件！";
    }

}
