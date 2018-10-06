package com.github.ftpDownloader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一句话描述该类的功能
 * need test
 */
public class MyjFtpDownJob {
    private static final Logger logger = LogManager.getLogger(MyjFtpDownJob.class);

    public static void main(String[] args) throws Exception {
        CommandParamParser paramParser = CommandParamParser.commandLineParamAnalysis(args);
        List<String> workDayList = workDayList(paramParser);
       // FtpPropertiesCommonUtils ftpConfig = FtpPropertiesCommonUtils.getInstance();
        FtpClientConfig ftpConfig = new FtpClientConfig();
        //ftpConfig.setRemotePath();
        FtpClientFactory ftpClientFactory = new FtpClientFactory(ftpConfig);
        FtpClientPool ftpClientPool = new FtpClientPool(15, ftpClientFactory);
        FTPClient ftpClient = ftpClientPool.borrowObject();
        logger.info("the number of ftpClient num is :{},alive num is :{}",
                ftpClientPool.getNumIdle(), ftpClientPool.getNumActive());
        for (String day : workDayList) {
            String serverPath = "";//ftpConfig.getRemote().concat(day.replaceAll("-", ""));
            ftpClient.changeWorkingDirectory(serverPath);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            ftpClientPool.returnObject(ftpClient);
            logger.info("the number of ftpClient num is :{},alive num is :{}",
                    ftpClientPool.getNumIdle(), ftpClientPool.getNumActive());
            String[] fileNamArr = new String[ftpFiles.length];
            for (int i = 0; i < ftpFiles.length; i++) {
                fileNamArr[i] = ftpFiles[i].getName();
            }
            ExecutorService pool = new ThreadPoolExecutor(5, 15,
                    3600L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
                    new DefaultThreadFactory());

            List<Future> list = new ArrayList<>();
            for (String name : fileNamArr) {
                if (name.startsWith(".")) {
                    continue;
                }
                Callable c = new FtpWork(name, ftpClientPool.borrowObject(),
                        serverPath, paramParser.getLocalPath().concat(File.separator)
                        .concat(day.replaceAll("-", "")));
                Future f = pool.submit(c);

                list.add(f);
            }
            pool.shutdown();
            for (Future f : list) {
                System.out.println(f.get());
            }
        }
    }

    private static List<String> workDayList(CommandParamParser paramParser) {
        List<String> dayList = new ArrayList<>();
        if (StringUtils.isBlank(paramParser.getStartDay())) {
            dayList.add(TimeCommonUtils.getNowDateStr());
        }
        if (StringUtils.isNotBlank(paramParser.getStartDay()) && StringUtils.isNotBlank(paramParser.getEndDay())) {
            dayList.addAll(TimeCommonUtils.getDateDayListStartAndEnd(paramParser.getStartDay(), paramParser.getEndDay()));
        }
        return dayList;
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);

            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
