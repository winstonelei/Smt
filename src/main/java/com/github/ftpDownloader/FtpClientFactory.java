package com.github.ftpDownloader;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * FTP连接对象工厂类类
 */
public class FtpClientFactory extends BasePooledObjectFactory<FTPClient> {
    private static Logger logger = LogManager.getLogger(FtpClientFactory.class);
    private FtpClientConfig ftpClientConfig;
    private static final String PASSIVE_MODE = "true";

    public FtpClientFactory(FtpClientConfig ftpClientConfig) {
        this.ftpClientConfig = ftpClientConfig;
    }


    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(ftpClientConfig.getEncoding());
        ftpClient.setConnectTimeout(ftpClientConfig.getClientTimeout());
        ftpClient.connect(ftpClientConfig.getHost(), ftpClientConfig.getPort());
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            logger.warn("ftp server {} refused connection!", ftpClientConfig.getServerName());
            return null;
        }
        boolean result = ftpClient.login(ftpClientConfig.getUsername(), ftpClientConfig.getPassword());
        if (!result) {
            logger.error("ftp server {} login failed,username:{}",
                    ftpClientConfig.getServerName(), ftpClientConfig.getUsername());
            return null;
        }
        ftpClient.setFileType(ftpClientConfig.getTransferFileType());
        ftpClient.setBufferSize(1024);
        ftpClient.changeWorkingDirectory(ftpClientConfig.getRemotePath());
        if (ftpClientConfig.getPassiveMode().equals(PASSIVE_MODE)) {
            ftpClient.enterLocalPassiveMode();
        }
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    /**
     * 释放ftp客户端的连接
     *
     * @param ftpClient ftp连接对象
     */
    public void destroy(FTPClient ftpClient) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean validateObject(FTPClient ftpClient) {
        try {
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            logger.error("Failed to validate client {}!", e.getMessage());
            return false;
        }
    }
}
