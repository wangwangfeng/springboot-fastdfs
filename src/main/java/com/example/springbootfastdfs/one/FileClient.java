package com.example.springbootfastdfs.one;

import java.io.File;

public interface FileClient {

    String uploadFile(File file);

    String uploadFile(File file, String file_ext_name);

    String uploadFile(byte[] buff, String file_ext_name);

    /**
     * @param: groupName 组名 "group1"
     * @param fileUrl 上传返回去掉组名的路径 "M00/00/03/O8va9WAPi26AfrBAAAB61Abzts85829501.png"
     * @return: byte[]
     **/
    byte[] downloadFile(String groupName, String fileUrl);

    void downloadFile(String groupName, String fileUrl, String filePath);

    void deleteFile(String groupName, String fileUrl);

}
