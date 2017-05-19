package com.github.scheduler;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by winstone on 2016/12/6.
 * 文件channel
 */
public class FileChannel extends AbstractMemoryChannel {

    private String filePath = System.getProperty("java.io.tmpdir");

    private String fileUrlAllName = "urls.txt";

    private String fileCursor = "cursor.txt";

    private PrintWriter fileUrlWriter;

    private PrintWriter fileCursorWriter;

    private AtomicInteger cursor = new AtomicInteger();

    private AtomicBoolean inited = new AtomicBoolean(false);

    private BlockingQueue<Object> queue;

    private Set<String> urls;

    private ScheduledExecutorService flushThreadPool;

    public FileChannel(String filePath) {
        if (!filePath.endsWith("/") && !filePath.endsWith("\\")) {
            filePath += "/";
        }
        this.filePath = filePath;
        initDuplicateRemover();
    }


    public void initDuplicateRemover(){
        if (!inited.get()) {
            init();
        }
        //urls.add("xxx");
        return ;
    }

    private void flush() {
        fileUrlWriter.flush();
        fileCursorWriter.flush();
    }

    private void init() {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        readFile();
        initWriter();
        initFlushThread();
        inited.set(true);
    }


    private void initFlushThread() {
        flushThreadPool = Executors.newScheduledThreadPool(1);
        flushThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                flush();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void initWriter() {
        try {
            fileUrlWriter = new PrintWriter(new FileWriter(getFileName(fileUrlAllName), true));
            fileCursorWriter = new PrintWriter(new FileWriter(getFileName(fileCursor), false));
        } catch (IOException e) {
            throw new RuntimeException("init cache scheduler error", e);
        }
    }
    private void readFile() {
        try {
            queue = new LinkedBlockingQueue<Object>();
            urls = new LinkedHashSet<String>();
            readCursorFile();
            readUrlFile();
            // ();
        } catch (FileNotFoundException e) {
            //init
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readUrlFile() throws IOException {
        String line;
        BufferedReader fileUrlReader = null;
        try {
            fileUrlReader = new BufferedReader(new FileReader(getFileName(fileUrlAllName)));
            int lineReaded = 0;
            while ((line = fileUrlReader.readLine()) != null) {
                urls.add(line.trim());
                lineReaded++;
                if (lineReaded > cursor.get()) {
                    queue.add(line);
                }
            }
        } finally {
            if (fileUrlReader != null) {
                IOUtils.closeQuietly(fileUrlReader);
            }
        }
    }
    private void readCursorFile() throws IOException {
        BufferedReader fileCursorReader = null;
        try {
            fileCursorReader = new BufferedReader(new FileReader(getFileName(fileCursor)));
            String line;
            //read the last number
            while ((line = fileCursorReader.readLine()) != null) {
                cursor = new AtomicInteger(NumberUtils.toInt(line));
            }
        } finally {
            if (fileCursorReader != null) {
                IOUtils.closeQuietly(fileCursorReader);
            }
        }
    }

    public void close() throws IOException {
        flushThreadPool.shutdown();
        fileUrlWriter.close();
        fileCursorWriter.close();
    }

    private String getFileName(String filename) {
        return filePath  + filename;
    }

    @Override
    public boolean offer(Object event) {
        queue.add(event);
        fileUrlWriter.println(event);
        return true;
    }

    @Override
    public Object poll(long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public Object poll() {
        if (!inited.get()) {
            init();
        }
        fileCursorWriter.println(cursor.incrementAndGet());
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
