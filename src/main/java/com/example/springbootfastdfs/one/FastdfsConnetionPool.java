package com.example.springbootfastdfs.one;

import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Classname: FastdfsConnetionPool
 * @Description: Fastdfs连接池
 * @Date: 2022/1/11 13:19
 * @author: wwf
 */
@Slf4j
public class FastdfsConnetionPool {

    // 最大连接数
    private final int maxSize = 5;

    private static final String CONF_NAME = "fastdfs_client.conf";

    // 被使用的
    private ConcurrentHashMap<StorageClient1, Object> usedConnetionPool = null;

    // 空闲的连接
    private ArrayBlockingQueue<StorageClient1> idleConnectionPool = null;

    private Object obj = new Object();

    private static FastdfsConnetionPool instacne = new FastdfsConnetionPool();

    public static FastdfsConnetionPool getFastdfsConnetionPool() {
        return instacne;
    }

    // 取出连接
    public StorageClient1 getConnection(int waitTime) {
        StorageClient1 storageClient1 = null;
        try {
            storageClient1 = idleConnectionPool.poll(waitTime, TimeUnit.SECONDS);
            if (storageClient1 != null) {
                usedConnetionPool.put(storageClient1, obj);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return storageClient1;
    }

    // 回收连接
    public void recycleConnection(StorageClient1 storageClient1) {
        if (usedConnetionPool.remove(storageClient1) != null) {
            idleConnectionPool.add(storageClient1);
        }
    }

    // 如果连接无效则抛弃，新建连接来补充到池里
    public void drop(StorageClient1 storageClient1) {
        if (usedConnetionPool.remove(storageClient1) != null) {
            TrackerServer trackerServer = null;
            TrackerClient trackerClient = new TrackerClient();
            try {
                trackerServer = trackerClient.getConnection();
                StorageClient1 newStorageClient11 = new StorageClient1(trackerServer, null);
                idleConnectionPool.add(newStorageClient11);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (trackerServer != null) {
                    try {
                        trackerServer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 单例
    public FastdfsConnetionPool() {
        usedConnetionPool = new ConcurrentHashMap<>();
        idleConnectionPool = new ArrayBlockingQueue<>(maxSize);
        init(maxSize);
    }

    // 初始化连接池
    private void init(int size) {
        initClientGlobal();
        TrackerServer trackerServer = null;
        TrackerClient trackerClient = new TrackerClient();
        try {
            // 只需要一个trackerServer连接
            trackerServer = trackerClient.getConnection();
            StorageServer storageServer = null;
            StorageClient1 storageClient1 = null;
            for (int i = 0; i < size; i++) {
                storageClient1 = new StorageClient1(trackerServer, storageServer);
                idleConnectionPool.add(storageClient1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initClientGlobal() {
        String path = this.getClass().getResource("/").getPath() + CONF_NAME;
        try {
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
