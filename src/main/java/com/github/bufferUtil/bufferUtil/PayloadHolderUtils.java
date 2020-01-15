/*
 * Copyright 2018 Qunar, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.bufferUtil.bufferUtil;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public final class PayloadHolderUtils {

    public static void writeString(String s, ByteBuf out) {
        byte[] bs = CharsetUtils.toUTF8Bytes(s);
        out.writeShort((short) bs.length);
        out.writeBytes(bs);
    }

    public static String readString(ByteBuf in) {
        int len = in.readShort();
        byte[] bs = new byte[len];
        in.readBytes(bs);
        return CharsetUtils.toUTF8String(bs);
    }

    public static void writeString(String s, ByteBuffer out) {
        byte[] bs = CharsetUtils.toUTF8Bytes(s);
        out.putShort((short) bs.length);
        out.put(bs);
    }

    public static void writeString(byte[] s, ByteBuf out) {
        out.writeShort((short) s.length);
        out.writeBytes(s);
    }

    public static String readString(ByteBuffer in) {
        int len = in.getShort();
        byte[] bs = new byte[len];
        in.get(bs);
        return CharsetUtils.toUTF8String(bs);
    }

    public static String readString(short len, ByteBuffer in) {
        if (len <= 0) return "";
        byte[] bs = new byte[len];
        in.get(bs);
        return CharsetUtils.toUTF8String(bs);
    }

    public static void writeBytes(byte[] bs, ByteBuf out) {
        out.writeInt(bs.length);
        out.writeBytes(bs);
    }

    public static byte[] readBytes(ByteBuf in) {
        int len = in.readInt();
        byte[] bs = new byte[len];
        in.readBytes(bs);
        return bs;
    }

    public static byte[] readBytes(ByteBuffer in) {
        int len = in.getInt();
        byte[] bs = new byte[len];
        in.get(bs);
        return bs;
    }

    public static void writeStringMap(Map<String, String> map, ByteBuf out) {
        if (map == null || map.isEmpty()) {
            out.writeShort(0);
        } else {
            if (map.size() > Short.MAX_VALUE) {
                throw new IndexOutOfBoundsException("map is too large. size=" + map.size());
            }
            out.writeShort(map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writeString(entry.getKey(), out);
                writeString(entry.getValue(), out);
            }
        }
    }

    public static Map<String, String> readStringHashMap(ByteBuf in) {
        return readStringMap(in, new HashMap<String, String>());
    }

    public static Map<String, String> readStringMap(ByteBuf in, Map<String, String> map) {
        short size = in.readShort();
        for (int i = 0; i < size; i++) {
            map.put(readString(in), readString(in));
        }
        return map;
    }


    public static void main(String[] args) {
        /*public static BackupMessage getMessage(byte[] value) throws Exception {
            return getMessage(ByteBuffer.wrap(value));
        }

        public static BackupMessage getMessage(ByteBuffer buffer) throws Exception {
            long sequence = buffer.getLong();
            // flag
            byte flag = buffer.get();
            // createTime
            final long createTime = buffer.getLong();
            // expiredTime or scheduleTime
            buffer.position(buffer.position() + Long.BYTES);
            // subject
            final String subject = PayloadHolderUtils.readString(buffer);
            // messageId
            final String messageId = PayloadHolderUtils.readString(buffer);
            // tags
            Set<String> tags = Tags.readTags(flag, buffer);
            // body
            final byte[] bodyBs = PayloadHolderUtils.readBytes(buffer);
            HashMap<String, Object> attributes = null;
            try {
                attributes = getAttributes(bodyBs, createTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            BackupMessage backupMessage = new BackupMessage(messageId, subject);
            backupMessage.setAttrs(attributes);
            tags.forEach(backupMessage::addTag);
            backupMessage.setSequence(sequence);

            return backupMessage;
        }

        private static HashMap<String, Object> getAttributes(final byte[] bodyBs, final long createTime) {
            HashMap<String, Object> attributes;
            attributes = QMQSerializer.deserializeMap(bodyBs);
            attributes.put(BaseMessage.keys.qmq_createTime.name(), createTime);
            return attributes;
        }*/
    }

}
