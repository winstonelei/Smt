package com.github.zipFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by winstone on 2016/12/6.
 * 压缩文件工具类
 */
public class ZipFileUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtils.class);

    /**
     * 压缩文件
     * @param sourceDir
     * @param zipFilename
     * @throws IOException
     */
    public static void compressZipFile(String sourceDir, String zipFilename) throws IOException {
        if (!validateZipFilename(zipFilename)) {
            throw new RuntimeException("Zipfile must end with .zip");
        }
        ZipOutputStream zipFile = null;
        try {
            zipFile = new ZipOutputStream(new FileOutputStream(zipFilename));
            compressDirectoryToZipfile(normDir(new File(sourceDir).getParent()), normDir(sourceDir), zipFile);
        } finally {
            IOUtils.closeQuietly(zipFile);
        }
    }

    /**
     * 解压缩文件
     * @param zipFileName
     * @param outputFolder
     * @throws IOException
     */
    public static void decompressZipfileToDirectory(String zipFileName, File outputFolder) throws IOException {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(zipFileName));
            ZipEntry zipEntry = null;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                logger.info("decompressing " + zipEntry.getName() + " is directory:" + zipEntry.isDirectory() + " available: " + zipInputStream.available());

                File temp = new File(outputFolder, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    temp.mkdirs();
                } else {
                    temp.getParentFile().mkdirs();
                    temp.createNewFile();
                    temp.setLastModified(zipEntry.getTime());
                    FileOutputStream outputStream = new FileOutputStream(temp);
                    try {
                        IOUtils.copy(zipInputStream, outputStream);
                    } finally {
                        IOUtils.closeQuietly(outputStream);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(zipInputStream);
        }
    }

    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException {
        for (File sourceFile : new File(sourceDir).listFiles()) {
            if (sourceFile.isDirectory()) {
                compressDirectoryToZipfile(rootDir, sourceDir + normDir(sourceFile.getName()), out);
            } else {
                ZipEntry entry = new ZipEntry(normDir(StringUtils.isEmpty(rootDir) ? sourceDir : sourceDir.replace(rootDir, "")) + sourceFile.getName());
                entry.setTime(sourceFile.lastModified());
                out.putNextEntry(entry);
                FileInputStream in = new FileInputStream(sourceDir + sourceFile.getName());
                try {
                    IOUtils.copy(in, out);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
        }
    }

    /**
     * 校验文件名称
     * @param filename
     * @return
     */
    private static boolean validateZipFilename(String filename) {
        if (!StringUtils.isEmpty(filename) && filename.trim().toLowerCase().endsWith(".zip")) {
            return true;
        }

        return false;
    }

    /**
     * 是否正常文件夹
     * @param dirName
     * @return
     */
    private static String normDir(String dirName) {
        if (!StringUtils.isEmpty(dirName) && !dirName.endsWith(File.separator)) {
            dirName = dirName + File.separator;
        }
        return dirName;
    }

/*    public static void main(String args[]){
        try {
            //compressZipFile("F:\\test\\nihao","F:\\test\\nihao.zip");
            decompressZipfileToDirectory("F:\\test\\nihao.zip",new File("F:\\test\\"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

}
