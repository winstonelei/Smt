package com.github.filesplitmerge;

import com.github.countableThreadPool.CountableThreadPool;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
     实现思路：将文件分成 n块，每块用不同线程去下载，


 * 文件下载工具 by winstone on 2015/11/5.
 */
public final class FileUtil {

    /**
     * 单线程下载文件
     *
     * @param url       文件的网络地址
     * @param path      保存的文件路径
     * @param threadNum 想要开启的线程数
     */
    public static void dowanload(String url, String path, int threadNum)
            throws IOException {
        System.out.println("下载中...");
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10 * 1000);
            File file = new File(path);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();
            if (file.exists())
                file.delete();
            file.createNewFile();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                int fileSize = urlConnection.getContentLength();//获取文件大小
                randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(fileSize);//设置文件大小
                randomAccessFile.close();//设置文件大小后关闭

                //获取线程要处理的块数
                int value = fileSize % threadNum;
                int unit = fileSize/threadNum;//文件平均每块大小
                int block = 0 == value ? threadNum: threadNum + 1; //如果不整除，分多一块处理
                for (int i = 0; i < block; i++) {
                    int startMark = i * unit;//开始下载位置
                    int endMark = (i + 1) * unit - 1;//下载末端位置
                    DownloadFileThread thread = new DownloadFileThread(url, file, startMark, endMark);
                    thread.start();
                }

            } else {
                System.out.println("服务器异常...");
            }
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != randomAccessFile) {
                randomAccessFile.close();
            }
        }
    }



    public static void downloadFromLocal(String soucePath, String destPath, int threadNum)
            throws IOException {
        System.out.println("下载中...");
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        CountableThreadPool pool =new CountableThreadPool(6);
        long beginTime = System.currentTimeMillis();
        try {
            File sourcFile =new File(soucePath);
            File file = new File(destPath);
            if(file.exists()){
                file.delete();
            }
            if (sourcFile.exists()) {
                int fileSize = (int)sourcFile.length();//获取文件大小
                randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(fileSize);//设置文件大小
                randomAccessFile.close();//设置文件大小后关闭

                //获取线程要处理的块数
                int value = fileSize % threadNum;
                int unit = fileSize/threadNum;//文件平均每块大小
                int block = 0 == value ? threadNum: threadNum + 1; //如果不整除，分多一块处理
                RandomAccessFile targetFile = new RandomAccessFile(file, "rw");
                for (int i = 0; i < block; i++) {
                    int startMark = i * unit;//开始下载位置
                    int endMark = (i + 1) * unit - 1;//下载末端位置

                    MergeRunnable thread = new MergeRunnable(startMark, endMark,sourcFile,targetFile);
                    //thread.start();
                    pool.execute(thread);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("耗时="+(endTime-beginTime));
                //targetFile.close();

                pool.shutdown();
            } else {
                System.out.println("服务器异常...");
            }
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != randomAccessFile) {
                randomAccessFile.close();
            }
        }
    }


    private static class MergeRunnable extends Thread {
        long startPos;
        long endPos;
        File readFile;
        RandomAccessFile targetFile;
        public MergeRunnable(long startPos, long endPos, File readFile,RandomAccessFile targetFile) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.readFile = readFile;
            this.targetFile = targetFile;
        }

        public void run() {
            try {
                targetFile.seek(startPos);
                RandomAccessFile sourceFile = new RandomAccessFile(readFile, "r");
                sourceFile.seek(startPos);
                byte[] b =new byte[(int)(endPos-startPos)];
                sourceFile.readFully(b);
                targetFile.write(b);
                sourceFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 下载文件块线程
     */
    private static class DownloadFileThread extends Thread {
        private String url;
        private File file;
        private int startMark;
        private int endMark;

        public DownloadFileThread(String url, File file, int startMark, int endMark) {
            this.url = url;
            this.file = file;
            this.startMark = startMark;
            this.endMark = endMark;
        }


        @Override
        public void run() {
            InputStream inputStream = null;
            RandomAccessFile randomAccessFile = null;
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10 * 1000);
                //请求指定文件的位置，表明当前的一块获取的开始下载位置到末端位置
                urlConnection.setRequestProperty("Range", "bytes="+startMark+"-"+endMark);
                int responseCode = urlConnection.getResponseCode();
                if (responseCode >= 200 && responseCode < 300)
                {
                    inputStream = urlConnection.getInputStream();
                    int len = 0;
                    byte[] data = new byte[4096];
                    randomAccessFile = new RandomAccessFile(file, "rwd");
                    randomAccessFile.seek(startMark);//设置往文件写入部分
                    while (-1 != (len = inputStream.read(data))) {
                        randomAccessFile.write(data, 0, len);
                    }
                    System.out.println("下载完成..."+getName());
                }
                else
                {
                    System.out.println("服务器异常...");
                }
            } catch (Exception ex) {

            }
            finally {
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != randomAccessFile) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

  //多线程下载器
    public static void main(String[] args) throws IOException {
        String source = "F:\\tmp\\ppp.mp4";
        String target = "F:\\tmp\\ppp11.mp4";//"http://www.dowei.com/d/file/mingxing/bagua/20151105/9e88df8cd5dd243b31eff7a4f7d53f89.jpg";
        FileUtil.downloadFromLocal(source,target,4);
    }
}