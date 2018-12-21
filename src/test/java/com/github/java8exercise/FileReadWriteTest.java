package com.github.java8exercise;

import com.github.multiQuery.Person;
import com.github.serializer.KryoSerializer;
import com.github.serializer.ObjectSerializer;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class FileReadWriteTest {

    private void makeDir(String filePath) {
        synchronized (Java8Prictase.class) {
            File rootPathFile = new File(filePath);
            if (!rootPathFile.exists()) {
                boolean result = rootPathFile.mkdir();
                if (!result) {
                    throw new RuntimeException("cannot create root path, the path to create is:" + filePath);
                }
            } else if (!rootPathFile.isDirectory()) {
                throw new RuntimeException("rootPath is not directory");
            }
        }
    }

    private String getFullFileName(final String filePath,final String id) {
        return String.format("%s/%s", filePath, id);
    }

    private void writeFile(final Person person) {
        makeDir("D:\\tmp\\");
        String file = getFullFileName("D:\\tmp\\",person.getName());
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "rw");
            ObjectSerializer serializer = new KryoSerializer();
            try (FileChannel channel = raf.getChannel()) {
                byte[] content = convert(person, serializer);
                ByteBuffer buffer = ByteBuffer.allocate(content.length);
                buffer.put(content);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                channel.force(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] convert(final Person person, final ObjectSerializer objectSerializer)throws  Exception{
        return objectSerializer.serialize(person);
    }

    public static Person transformBean(final byte[] contents, final ObjectSerializer objectSerializer) throws Exception {
        final Person adapter = objectSerializer.deserialize(contents, Person.class);
        return adapter;
    }

    @Test
    public void writeFileTest(){
        Person p3 = new Person();
        p3.setAge(21);
        p3.setName("lisi");
        writeFile(p3);
    }

    @Test
    public void readFileTest() throws Exception {
        File file = new File("D:\\tmp\\lisi");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            ObjectSerializer serializer = new KryoSerializer();
            Person person = transformBean(content, serializer);
            System.out.println(person.getName());
        }
    }

}
