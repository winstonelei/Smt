package com.github.ftpDownloader;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ftp client连接池
 */
public class FtpClientPool implements ObjectPool<FTPClient> {
    private static final int DEFAULT_POOL_SIZE = 10;
    private final BlockingQueue<FTPClient> pool;
    private int poolSize;
    private final FtpClientFactory factory;

    public FtpClientPool(FtpClientFactory factory) throws Exception {
        this(DEFAULT_POOL_SIZE, factory);
    }

    public FtpClientPool(int poolSize, FtpClientFactory factory) throws Exception {
        this.factory = factory;
        this.poolSize = poolSize;
        pool = new ArrayBlockingQueue<>(poolSize);
        initPool(poolSize);
    }

    private void initPool(int maxPoolSize) throws Exception {
        for (int i = 0; i < maxPoolSize; i++) {
            addObject();
        }
    }

    @Override
    public FTPClient borrowObject() throws Exception {
        FTPClient ftpClient = pool.take();
        if (null == ftpClient) {
            ftpClient = factory.create();
        } else if (!factory.validateObject(ftpClient)) {
            //使对象在池中失效
            invalidateObject(ftpClient);
            //制造新对象
            ftpClient = factory.create();
        }
        return ftpClient;
    }


    @Override
    public void returnObject(FTPClient ftpClient) throws Exception {
        addObject();
    }

    @Override
    public void invalidateObject(FTPClient ftpClient) {
        //废弃对象
        pool.remove(ftpClient);
    }

    @Override
    public void addObject() throws Exception {
        pool.offer(factory.create(), 60, TimeUnit.SECONDS);
    }

    @Override
    public int getNumIdle() {
        //空闲对象个数
        return pool.size();
    }

    @Override
    public int getNumActive() {

        //活跃对象个数
        return this.poolSize - pool.size();
    }

    @Override
    public void clear() {
        //清除池，池可用
        pool.clear();
    }

    @Override
    public void close() {
        //关闭池，池不可用
        while (pool.iterator().hasNext()) {
            FTPClient client;
            try {
                client = pool.take();
                factory.destroy(client);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
