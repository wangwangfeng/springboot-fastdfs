package com.example.springbootfastdfs.controller;

import com.example.springbootfastdfs.two.FastDfsClient;
import com.example.springbootfastdfs.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

/**
 * @Classname: TestTwoController
 * @Description: TODO
 * @Date: 2022/1/12 9:54
 * @author: wwf
 */
@RequestMapping(value = "/two")
@Controller
@Slf4j
public class TestTwoController {

    /**
     * @Description: http://localhost:8080/two/upload
     * 返回值：[group1, M00/00/00/rKj9t2HeRNuAb2XVAEYRW_EEwEg203.png]
     **/
    @GetMapping(value = "/upload")
    @ResponseBody
    public String upload() {
        log.info("上传文件");
        File file = new File("D://02.png");
        String[] result = FastDfsClient.upload(FileUtil.getFileBuff(file), FileUtil.getFileExtName("02.png"), null);
        return Arrays.toString(result);
    }

    @PostMapping(value = "/download")
    @ResponseBody
    public String download(String groupName, String fileUrl) {
        log.info("下载文件");
        String ext_name = "png";
        if (fileUrl != null) {
            if (fileUrl.contains(".")) {
                ext_name = fileUrl.substring(fileUrl.indexOf(".") + 1);
            }
        }
        String fileName = "D://" + UUID.randomUUID().toString() + "." + ext_name;
        byte[] fbyte = FastDfsClient.download(groupName, fileUrl);
        FileUtil.writeByteToFile(fbyte, fileName);
        return fileName;
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public void delete(String groupName, String fileUrl) {
        log.info("删除文件");
        FastDfsClient.deleteFile(groupName, fileUrl);
    }


}
