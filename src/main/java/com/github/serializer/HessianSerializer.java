package com.github.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.github.spi.MySPI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hession序列化
 */
@MySPI("hessian")
public class HessianSerializer implements  ObjectSerializer {

    /**
     * 序列化
     * @param object
     * @return
     * @throws Exception
     */
    @Override
    public byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            Hessian2Output hos = new Hessian2Output(baos);
            hos.writeObject(object);
            hos.flush();
            hos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * 反序列化
     * @param param
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    @Override
    public <T> T deserialize(byte[] param, Class<T> t) throws Exception {
        ByteArrayInputStream bios;
        try {
            bios = new ByteArrayInputStream(param);
            Hessian2Input his = new Hessian2Input(bios);
            return (T) his.readObject();
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }
}
