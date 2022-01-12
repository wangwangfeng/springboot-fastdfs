package com.example.springbootfastdfs.two;

import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname: FastDfsClient
 * @Description: TODO
 * @Date: 2022/1/11 17:04
 * @author: wwf
 */
@Slf4j
public class FastDfsClient {

    /**
     * 上传文件
     *
     * @param fileByte
     * @param fileExtName
     * @param valuePairs
     * @return
     */
    public static String[] upload(byte[] fileByte, String fileExtName, NameValuePair[] valuePairs) {
        StorageClient storageClient = FastdfsConnetionPool.findStorageClient();
        String[] uploadResults = null;
        try {
            uploadResults = storageClient.upload_file(fileByte, fileExtName, valuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FastdfsConnetionPool.recycleStorageClient(storageClient);
        }
        return uploadResults;
    }

    /**
     * 文件下载
     *
     * @param groupName
     * @param fileUrl
     * @return
     */
    public static byte[] download(String groupName, String fileUrl) {
        byte[] group1s = null;
        try {
            StorageClient storageClient = FastdfsConnetionPool.findStorageClient();
            group1s = storageClient.download_file(groupName, fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return group1s;
    }

    /**
     * 删除文件
     *
     * @param groupName
     * @param fileUrl
     */
    public static void deleteFile(String groupName, String fileUrl) {
        try {
            StorageClient storageClient = FastdfsConnetionPool.findStorageClient();
            final int group = storageClient.delete_file(groupName, fileUrl);
            if (group == 0) {
                log.info("删除文件成功：");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件元数据
     *
     * @param groupName
     * @param fileId    文件ID
     * @return
     */
    public static Map<String, String> getFileMetadata(String groupName, String fileId) {
        try {
            StorageClient storageClient = FastdfsConnetionPool.findStorageClient();
            NameValuePair[] metaList = storageClient.get_metadata(groupName, fileId);
            if (metaList != null) {
                HashMap<String, String> map = new HashMap<>();
                for (NameValuePair metaItem : metaList) {
                    map.put(metaItem.getName(), metaItem.getValue());
                }
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
