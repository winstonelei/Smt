package com.github.ftpDownloader;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * 一句话描述该类的功能
 */
public class FtpWork implements Callable<Boolean> {
    private static final Logger logger = LogManager.getLogger(FtpWork.class);
    private String fileName;
    private FTPClient ftpClient;
    private String remotePath;
    private String localPath;

    public FtpWork(String fileName, FTPClient ftpClient, String remotePath, String localPath) {
        this.fileName = fileName;
        this.ftpClient = ftpClient;
        this.remotePath = remotePath;
        this.localPath = localPath;
    }

    @Override
    public Boolean call() {
        MyFtpUtil myFtpUtil = new MyFtpUtil(ftpClient);
        logger.info("{} start to download!", fileName);
        return myFtpUtil.downloadFile(remotePath, fileName, localPath);
    }

}
