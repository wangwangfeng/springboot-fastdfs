package com.example.springbootfastdfs.one.impl;

import com.example.springbootfastdfs.one.FastdfsConnetionPool;
import com.example.springbootfastdfs.one.FileClient;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @Classname: FileClientImpl
 * @Description: TODO
 * @Date: 2022/1/11 13:58
 * @author: wwf
 */
@Service
public class FileClientImpl implements FileClient {

    @Override
    public String uploadFile(File file) {
        byte[] buff = getFileBuff(file);
        String file_ext_name = getFileExtName(file.getName());
        return upload(buff, file_ext_name, null);
    }

    @Override
    public String uploadFile(File file, String file_ext_name) {
        byte[] buff = getFileBuff(file);
        return upload(buff, file_ext_name, null);
    }

    @Override
    public String uploadFile(byte[] buff, String file_ext_name) {
        return upload(buff, file_ext_name, null);
    }

    @Override
    public byte[] downloadFile(String groupName, String fileUrl) {
        return download(groupName, fileUrl);
    }

    @Override
    public void downloadFile(String groupName, String fileUrl, String filePath) {
        writeByteToFile(download(groupName, fileUrl), filePath);
    }

    @Override
    public void deleteFile(String groupName, String fileUrl) {
        delete(groupName, fileUrl);
    }

    // 文件转字节数组
    private byte[] getFileBuff(File file) {
        byte[] buff = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            buff = new byte[inputStream.available()];
            inputStream.read(buff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buff;
    }

    // 字节数组转文件
    private void writeByteToFile(byte[] fileByte, String fileName) {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(fileByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 通过文件名称获取文件拓展名
    private String getFileExtName(String name) {
        String ext_name = null;
        if (name != null) {
            if (name.contains(".")) {
                ext_name = name.substring(name.indexOf(".") + 1);
            }
        }
        return ext_name;
    }

    // 文件上传
    private String upload(byte[] buff, String file_ext_name, NameValuePair[] mate_list) {
        String upPath = null;
        StorageClient1 storageClient1 = null;
        FastdfsConnetionPool pool = FastdfsConnetionPool.getFastdfsConnetionPool();
        storageClient1 = pool.getConnection(10);
        try {
            upPath = storageClient1.upload_file1(buff, file_ext_name, mate_list);
            pool.recycleConnection(storageClient1);
        } catch (Exception e) {
            // 如果出现了IO异常应该销毁此连接
            pool.drop(storageClient1);
            e.printStackTrace();
        }
        return upPath;
    }

    // 文件下载
    private byte[] download(String groupName, String fileUrl) {
        byte[] group1s = null;
        StorageClient1 storageClient1 = null;
        FastdfsConnetionPool pool = FastdfsConnetionPool.getFastdfsConnetionPool();
        storageClient1 = pool.getConnection(10);
        try {
            group1s = storageClient1.download_file(groupName, fileUrl);
            pool.recycleConnection(storageClient1);
        } catch (Exception e) {
            pool.drop(storageClient1);
            e.printStackTrace();
        }
        return group1s;
    }

    // 文件删除
    private void delete(String groupName, String fileUrl) {
        StorageClient1 storageClient1 = null;
        FastdfsConnetionPool pool = FastdfsConnetionPool.getFastdfsConnetionPool();
        storageClient1 = pool.getConnection(10);
        try {
            int i = storageClient1.delete_file(groupName, fileUrl);
            if (i == 0) {
                System.out.println("删除文件成功");
            }
            pool.recycleConnection(storageClient1);
        } catch (Exception e) {
            pool.drop(storageClient1);
            e.printStackTrace();
        }
    }

}
