package com.github.ftpDownloader;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 一句话描述该类的功能
 */
public class MyFtpUtil {
    private static final Logger logger = LogManager.getLogger(MyFtpUtil.class);
    private FTPClient ftpClient;

    public MyFtpUtil(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public boolean downloadFile(String remotePath, String fileName, String localPath) {
        boolean flag = false;
        try {
            ftpClient.changeWorkingDirectory(remotePath);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            if (null != ftpFiles && ftpFiles.length > 0) {
                for (FTPFile ftpFile : ftpFiles) {
                    if (fileName.equalsIgnoreCase(ftpFile.getName())) {
                        File localFileDirPath = new File(localPath);
                        if (!localFileDirPath.exists()) {
                            localFileDirPath.mkdirs();
                        }
                        File localFile = new File(localPath + File.separator + ftpFile.getName());
                        if (localFile.exists()) {
                            logger.info(fileName + " 文件在本地存在！");
                            long localBeginSize = localFile.length();
                            if (localBeginSize == ftpFile.getSize()) {
                                logger.info(fileName + " 文件已经下载成功！");
                                flag = true;
                            } else if (localBeginSize > ftpFile.getSize()) {
                                logger.error("文件：{}下载失败！", remotePath + "/" + ftpFile.getName());
                            } else {
                                logger.info(fileName + " 开始断点下载，已下载：{}！", localBeginSize);
                                flag = downloadByUnit(remotePath + "/" + ftpFile.getName(),
                                        localPath + File.separator + ftpFile.getName(), localBeginSize, ftpFile.getSize());
                            }

                        } else {
                            logger.info(fileName + " 开始下载！");
                            flag = downloadByUnit(remotePath + "/" + ftpFile.getName(),
                                    localPath + File.separator + ftpFile.getName(), 0, ftpFile.getSize());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean downloadByUnit(String remote, String local, long beginSize, long endSize) throws Exception {
        File localFile = new File(local);
        long waitSize = endSize - beginSize;
        FileOutputStream out = new FileOutputStream(localFile, true);
        ftpClient.setRestartOffset(beginSize);
        InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"), "iso-8859-1"));
        byte[] bytes = new byte[2048];
        int c;
        double finishSize = 0;
        double finishPercent = 0;
        while ((c = in.read(bytes)) != -1) {
            out.write(bytes, 0, c);
            finishSize += c;
            if ((finishSize / waitSize) - finishPercent > 0.01) {
                finishPercent = finishSize / waitSize;
                Thread t = Thread.currentThread();
                System.out.println("当前线程：" + t.getName() + ",文件：" + local +
                        ",当前下载进度:" + finishPercent);
            }
        }
        in.close();
        out.close();
        return ftpClient.completePendingCommand();
    }
}
