package com.github.fileUtils;

import java.io.*;
import java.util.Base64;

/**
 * 写文件工具类
 **/
public class AppendFile {
    public static void method1(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void method2(String fileName, String content) {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 追加文件：使用RandomAccessFile
     *
     * @param fileName 文件名
     * @param content  追加的内容
     */
    public static void method3(String fileName, String content) {
        RandomAccessFile randomFile = null;
        try {
            // 打开一个随机访问文件流，按读写方式
            randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void test(String path,String line) {
        //  File firstFile = new File("D:\\tmp\\ip\\1.txt");
        File secondFile = new File(path);
        //BufferedReader in = null;
        BufferedWriter out = null;
        try {
            //加入编码字符集
            //    in = new BufferedReader(new InputStreamReader(new FileInputStream(firstFile), "gbk"));
            //加入编码字符集
            // out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(secondFile,true), "gbk"));
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(secondFile,true), "UTF-8"));
            //String line = "";
            out.write(line + "\r\n");
           /* while ((line = in.readLine()) != null) {
                System.out.println(line);

            }*/
        } catch (FileNotFoundException e) {
            System.out.println("file is not fond");
        } catch (IOException e) {
            System.out.println("Read or write Exceptioned");
        } finally {
       /*     if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeFileByBytes(byte[] bytes, String fileName) {

        try {
            //writePath 为最终文件路径名 如：D://test.txt
            FileOutputStream fos = new FileOutputStream(fileName);

            fos.write(bytes);

            fos.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void main(String[] args) {
        try {
            File file = new File("d://text.txt");
            if (file.createNewFile()) {
                System.out.println("Create file successed");
            }
            method1("d://text.txt", "123");
            method2("d://text.txt", "123");
            method3("d://text.txt", "123");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void base64StringToPDF(String base64sString, String filePath) {
        BufferedInputStream bin = null;
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        try {
            //将base64编码的字符串解码成字节数组
            byte[] bytes = Base64.getDecoder().decode(base64sString);
            //apache公司的API
            //byte[] bytes = Base64.decodeBase64(base64sString);
            //创建一个将bytes作为其缓冲区的ByteArrayInputStream对象
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            //创建从底层输入流中读取数据的缓冲输入流对象
            bin = new BufferedInputStream(bais);
            //指定输出的文件
            File file = new File(filePath);
            //创建到指定文件的输出流
            fout =new FileOutputStream(file);
            //为文件输出流对接缓冲输出流对象
            bout = new BufferedOutputStream(fout);

            byte[] buffers = new byte[1024];
            int len = bin.read(buffers);
            while (len != -1) {
                bout.write(buffers, 0, len);
                len = bin.read(buffers);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
            bout.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bin.close();
                fout.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

