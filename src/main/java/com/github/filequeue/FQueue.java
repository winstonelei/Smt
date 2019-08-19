package com.github.filequeue;

import com.github.filequeue.exception.FileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基于文件系统的持久化队列
 * 
 */
public class FQueue extends AbstractQueue<byte[]> implements Queue<byte[]>,
		java.io.Serializable {
	private static final long serialVersionUID = -5960741434564940154L;
	private FSQueue fsQueue = null;
	final Logger log = LoggerFactory.getLogger(FQueue.class);
	private Lock lock = new ReentrantReadWriteLock().writeLock();

	public FQueue(String path) throws Exception {
		fsQueue = new FSQueue(path, 1024 * 1024 * 300);
	}

	public FQueue(String path, int logsize) throws Exception {
		fsQueue = new FSQueue(path, logsize);
	}

	@Override
	public Iterator<byte[]> iterator() {
		throw new UnsupportedOperationException("iterator Unsupported now");
	}

	@Override
	public int size() {
		return fsQueue.getQueuSize();
	}

	@Override
	public boolean offer(byte[] e) {
		try {
			lock.lock();
			fsQueue.add(e);
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			lock.unlock();
		}
		return false;
	}

	@Override
	public byte[] peek() {
		throw new UnsupportedOperationException("peek Unsupported now");
	}

	@Override
	public byte[] poll() {
		try {
			lock.lock();
			return fsQueue.readNextAndRemove();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		} finally {
			lock.unlock();
		}
	}

	public void close() {
		if (fsQueue != null) {
			fsQueue.close();
		}
	}
}
