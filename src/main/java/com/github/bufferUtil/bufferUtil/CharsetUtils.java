
package com.github.bufferUtil.bufferUtil;

import com.google.common.base.Strings;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class CharsetUtils {
    public static final Charset UTF8 = Charset.forName("utf-8");

    private static final byte[] EMPTY_BYTES = new byte[0];

    public static byte[] toUTF8Bytes(final String s) {
        try {
            return Strings.isNullOrEmpty(s) ? EMPTY_BYTES : s.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String toUTF8String(final byte[] bs) {
        try {
            return bs == null || bs.length == 0 ? "" : new String(bs, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
