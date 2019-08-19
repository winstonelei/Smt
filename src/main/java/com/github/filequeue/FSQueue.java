package com.github.filequeue;

import com.github.filequeue.log.FileRunner;
import com.github.filequeue.log.LogEntity;
import com.github.filequeue.log.LogIndex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 完成基于文件的先进先出的读写功能
 */
public class FSQueue {
	private static final Log log = LogFactory.getLog(FSQueue.class);
	public static final String filePrefix = "fqueue";
	private int fileLimitLength = 1024 * 1024 * 100;
	private static final String dbName = "icqueue.db";
	private static final String fileSeparator = System.getProperty("file.separator");
	private String path = null;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private FileRunner deleteFileRunner;
	/**
	 * 文件操作实例
	 */
	private LogIndex db = null;
	private LogEntity writerHandle = null;
	private LogEntity readerHandle = null;
	/**
	 * 文件操作位置信息
	 */
	private int readerIndex = -1;
	private int writerIndex = -1;

	public FSQueue(String path) throws Exception {
		this(path, 1024 * 1024 * 150);
	}

	/**
	 * 在指定的目录中，以fileLimitLength为单个数据文件的最大大小限制初始化队列存储
	 * 
	 * @param dir
	 *            队列数据存储的路径
	 * @param fileLimitLength
	 *            单个数据文件的大小，不能超过2G
	 * @throws Exception
	 */
	public FSQueue(String dir, int fileLimitLength) throws Exception {
		this.fileLimitLength = fileLimitLength;
		File fileDir = new File(dir);
		if (fileDir.exists() == false && fileDir.isDirectory() == false) {
			if (fileDir.mkdirs() == false) {
				throw new IOException("create dir error");
			}
		}
		path = fileDir.getAbsolutePath();
		// 打开db
		db = new LogIndex(path + fileSeparator + dbName);
		writerIndex = db.getWriterIndex();
		readerIndex = db.getReaderIndex();
		writerHandle = createLogEntity(path + fileSeparator + filePrefix + "data_" + writerIndex + ".idb", db,
				writerIndex);
		if (readerIndex == writerIndex) {
			readerHandle = writerHandle;
		} else {
			readerHandle = createLogEntity(path + fileSeparator + filePrefix + "data_" + readerIndex + ".idb", db,
					readerIndex);

		}
		deleteFileRunner = new FileRunner(path + fileSeparator + filePrefix + "data_", fileLimitLength);
		executor.execute(deleteFileRunner);
	}

	/**
	 * 创建或者获取一个数据读写实例
	 * 
	 * @param dbpath
	 * @param db
	 * @param fileNumber
	 * @return
	 * @throws IOException
	 */
	private LogEntity createLogEntity(String dbpath, LogIndex db, int fileNumber) throws Exception
 {
		return new LogEntity(dbpath, db, fileNumber, this.fileLimitLength);
	}

	/**
	 * 一个文件的数据写入达到fileLimitLength的时候，滚动到下一个文件实例
	 * 
	 */
	private void rotateNextLogWriter() throws Exception{
		writerIndex = writerIndex + 1;
		writerHandle.putNextFile(writerIndex);
		if (readerHandle != writerHandle) {
			writerHandle.close();
		}
		db.putWriterIndex(writerIndex);
		writerHandle = createLogEntity(path + fileSeparator + filePrefix + "data_" + writerIndex + ".idb", db,
				writerIndex);
	}

	/**
	 * 向队列存储添加一个字符串
	 * 
	 * @param message
	 *            message
	 */
	public void add(String message) throws Exception {
		add(message.getBytes());
	}

	/**
	 * 向队列存储添加一个byte数组
	 * 
	 * @param message
	 */
	public void add(byte[] message) throws Exception{
		short status = writerHandle.write(message);
		if (status == LogEntity.WRITEFULL) {
			rotateNextLogWriter();
			status = writerHandle.write(message);
		}
		if (status == LogEntity.WRITESUCCESS) {
			db.incrementSize();
		}

	}
	/**
	 * 从队列存储中取出最先入队的数据，并移除它
	 * @return
	 */
	public byte[] readNextAndRemove() throws Exception{
		byte[] b = null;
		try {
			b = readerHandle.readNextAndRemove();
		} catch (Exception e) {
			int deleteNum = readerHandle.getCurrentFileNumber();
			int nextfile = readerHandle.getNextFile();
			readerHandle.close();
			FileRunner.addDeleteFile(path + fileSeparator + filePrefix + "data_" + deleteNum + ".idb");
			// 更新下一次读取的位置和索引
			db.putReaderPosition(LogEntity.messageStartPosition);
			db.putReaderIndex(nextfile);
			if (writerHandle.getCurrentFileNumber() == nextfile) {
				readerHandle = writerHandle;
			} else {
				readerHandle = createLogEntity(path + fileSeparator + filePrefix + "data_" + nextfile + ".idb", db,
						nextfile);
			}
			try {
				b = readerHandle.readNextAndRemove();
			} catch (Exception e1) {
				log.error("read new log file FileEOFException error occurred",e1);
			}
		}
		if (b != null) {
			db.decrementSize();
		}
		return b;
	}

	public void close() {
		readerHandle.close();
		writerHandle.close();
		deleteFileRunner.exit();
		executor.shutdown();
	}

	public int getQueuSize() {
		return db.getSize();
	}
}
