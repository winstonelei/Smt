package com.github.monitor;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * FileSystemEventMonitor watches a specific directory and execute
 * {@link #actions} if the directory is modified
 * 
 *
 */
public class FileSystemEventMonitor implements Monitor<File> {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemEventMonitor.class);

    private Path[] paths;

    private boolean isRecursive;

    private WatchService watcher;

    private Map<WatchKey, Path> keys;

    private Map<String, Action<? super File>> actions;

    Predicate<? super File> filter;

    public FileSystemEventMonitor(Path[] paths, Predicate<? super File> filter, boolean isRecursive) {
        super();
        this.paths = paths;
        this.isRecursive = isRecursive;
        this.filter = filter;
        actions = new ConcurrentHashMap<String, Action<? super File>>();
    }

    @Override
    public void init() throws Exception {

        keys = new HashMap<WatchKey, Path>();

        try {
            watcher = FileSystems.getDefault().newWatchService();
            if (isRecursive) {
                for (Path path : paths) {
                    register(path);
                }
            } else {
                for (Path path : paths) {
                    registerAll(path);
                }
            }

        } catch (IOException e) {
            throw new Exception("Failed to create FileWatchService", e);
        }
    }

    private void executeActions(File file) {
        if (actions != null) {
            for (Entry<String, Action<? super File>> action : actions.entrySet()) {
                action.getValue().execute(file);
            }
        }
    }

    @Override
    public void destroy() {
        if (watcher != null) {
            try {
                watcher.close();
            } catch (IOException e) {
                LOG.error("Failed to close FileWatchService");
            }
        }
    }

    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }

    private void registerAll(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void run() {
        while (true) {

            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                LOG.info("Close FileSystemEventMonitor {}", this);
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                LOG.warn("WatchKey not found in key-path map");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                Kind<?> kind = event.kind();

                if (kind == OVERFLOW) {
                    LOG.warn("OVERFLOW event is not supported yet");
                    continue;
                }

                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) (event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (isRecursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException e) {
                        LOG.error("Failed to create FileWatchService", e);
                    }
                }

                File file = child.toFile();
                if (filter == null || filter.apply(file)) {
                    executeActions(file);
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    public Path[] getPaths() {
        return paths;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public void setRecursive(boolean isRecursive) {
        this.isRecursive = isRecursive;
    }

    public Map<String, Action<? super File>> getActions() {
        return actions;
    }

    @Override
    public void addAction(String name, Action<? super File> action) {
        this.actions.put(name, action);
    }

}
