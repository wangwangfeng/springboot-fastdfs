package com.example.springbootfastdfs.two;

import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Classname: FastdfsConnetionPool
 * @Description: TODO
 * @Date: 2022/1/11 16:49
 * @author: wwf
 */
@Slf4j
public class FastdfsConnetionPool {

    // 默认连接池大小
    public static String connection_size = "10";

    // storageClient队列
    private static LinkedBlockingQueue<StorageClient> storageClientQueue =
            new LinkedBlockingQueue<>(Integer.parseInt(connection_size));

    // 当前索引
    private static int current_index;

    private static TrackerClient trackerClient;

    @Value("classpath:fastdfs_client.conf")
    private static Resource re;

    /**
     * 初始化
     **/
    static {
        try {
            ClientGlobal.init(re.getFilename());
            trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 创建连接
    public static void createStorageClient() {
        synchronized (trackerClient) {
            if (current_index < Integer.parseInt(connection_size)) {
                try {
                    TrackerServer trackerServer = trackerClient.getConnection();
                    StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
                    StorageClient storageClient = new StorageClient(trackerServer, storageServer);
                    if (storageClientQueue.offer(storageClient)) {
                        current_index++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    // 获取连接
    public static StorageClient findStorageClient() {
        // 尝试获取一个有用的连接
        StorageClient clientInfo = storageClientQueue.poll();
        if (clientInfo == null) {
            if (current_index < Integer.parseInt(connection_size)) {
                createStorageClient();
            }
            try {
                clientInfo = storageClientQueue.poll(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return clientInfo;
    }

    // 回收连接
    public static void recycleStorageClient(StorageClient storageClient) {
        try {
            if (storageClient != null) {
                storageClientQueue.offer(storageClient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
