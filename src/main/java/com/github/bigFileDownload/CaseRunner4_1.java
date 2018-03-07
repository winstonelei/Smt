/*
https://github.com/Viscent/javamtia
http://www.broadview.com.cn/31065
*/
package com.github.bigFileDownload;


import com.github.ftpUpload.Debug;

public class CaseRunner4_1 {

  public static void main(String[] args) throws Exception {
    if (0 == args.length) {
      args = new String[] { "https://download.jetbrains.com/python/pycharm-professional-2017.3.exe", "4", "3" };
    }
    main0(args);
  }

  public static void main0(String[] args) throws Exception {
    final int argc = args.length;
    BigFileDownloader downloader = new BigFileDownloader(args[0]);

    // 下载线程数
    int workerThreadsCount = argc >= 2 ? Integer.valueOf(args[1]) : 4;
    long reportInterval = argc >= 3 ? Integer.valueOf(args[2]) : 2;

    Debug.info("downloading %s%nConfig:worker threads:%s,reportInterval:%s s.",
        args[0], workerThreadsCount, reportInterval);

    downloader.download(workerThreadsCount, reportInterval * 1000);
  }
}
