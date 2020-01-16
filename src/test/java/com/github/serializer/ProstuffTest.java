package com.github.serializer;

import com.github.batch.Job;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ProstuffTest {
    private byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    @Test
    public void testSerizable(){

       Job job = new Job();
       job.setName("wang");
       job.setId(12);

      // byte[] bytes = ObjectToByte(job);
        byte[] bytes =  ProtoStuffUtil.serialize(job);

        Job job2 =ProtoStuffUtil.deserialize(bytes,Job.class);

        System.out.println(job2.getName());
    }
}
