package com.github.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.spi.MySPI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 实现kryo方式序列化
 */
@MySPI("kryo")
public class KryoSerializer implements  ObjectSerializer {


    /**
     * kryo实现序列化
     * @param object
     * @return
     * @throws Exception
     */
    @Override
    public byte[] serialize(Object object) throws Exception {
        byte[] bytes= null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Kryo kryo = new Kryo();
            Output output = new Output(outputStream) ;
            kryo.writeObject(output,object);
            bytes = output.toBytes();
            output.flush();
        } catch (KryoException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
            }
        }
        return bytes;
    }

    /**
     * 实现反序列化
     * @param param
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    @Override
    public <T> T deserialize(byte[] param, Class<T> t) throws Exception {
       T object = null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(param);
            Kryo kryo = new Kryo();
            Input input = new Input(inputStream);
            object = kryo.readObject(input,t);
            input.close();
        } catch (KryoException e) {
            e.printStackTrace();
        }
         return  object;
    }
}
