package com.github.bufferUtil;

public  class ByteUtil {
    public static byte IntToByte(int i) {
        return (byte) i;
    }

    public static int BytesToInt(byte abyte0[], int offset) {
        return (int) getNumber(abyte0, offset, 2);
    }

    public static byte[] IntToBytes(int i) {
        byte abyte0[] = new byte[2];
        abyte0[1] = (byte) (0xff & i);
        abyte0[0] = (byte) ((0xff00 & i) >> 8);
        return abyte0;
    }

    public static byte[] IntToBytes4(int i) {
        byte abyte0[] = new byte[4];
        abyte0[3] = (byte) (0xff & i);
        abyte0[2] = (byte) ((0xff00 & i) >> 8);
        abyte0[1] = (byte) ((0xff0000 & i) >> 16);
        abyte0[0] = (byte) ((0xff000000 & i) >> 24);
        return abyte0;
    }

    public static byte[] LongToBytes8(long l) {
        byte abyte0[] = new byte[8];
        abyte0[7] = (byte) (int) (255L & l);
        abyte0[6] = (byte) (int) ((65280L & l) >> 8);
        abyte0[5] = (byte) (int) ((0xff0000L & l) >> 16);
        abyte0[4] = (byte) (int) ((0xff000000L & l) >> 24);
        abyte0[3] = (byte) (int) ((0xff00000000L & l) >> 32);
        abyte0[2] = (byte) (int) ((0xff0000000000L & l) >> 40);
        abyte0[1] = (byte) (int) ((0xff000000000000L & l) >> 48);
        abyte0[0] = (byte) (int) ((0xff00000000000000L & l) >> 56);
        return abyte0;
    }

    public static long Bytes8ToLong(byte abyte0[], int offset) {
        return (255L & (long) abyte0[offset]) << 56 | (255L & (long) abyte0[offset + 1]) << 48 | (255L & (long) abyte0[offset + 2]) << 40
                | (255L & (long) abyte0[offset + 3]) << 32 | (255L & (long) abyte0[offset + 4]) << 24 | (255L & (long) abyte0[offset + 5]) << 16
                | (255L & (long) abyte0[offset + 6]) << 8 | (255L & (long) abyte0[offset + 7]);
    }

    public static void LongToBytes4(long l, byte abyte0[]) {
        abyte0[3] = (byte) (int) (255L & l);
        abyte0[2] = (byte) (int) ((65280L & l) >> 8);
        abyte0[1] = (byte) (int) ((0xff0000L & l) >> 16);
        abyte0[0] = (byte) (int) ((0xffffffffff000000L & l) >> 24);
    }

    public static void IntToBytes(int i, byte abyte0[]) {
        abyte0[1] = (byte) (0xff & i);
        abyte0[0] = (byte) ((0xff00 & i) >> 8);
    }

    public static void IntToBytes4(int i, byte abyte0[]) {
        abyte0[3] = (byte) (0xff & i);
        abyte0[2] = (byte) ((0xff00 & i) >> 8);
        abyte0[1] = (byte) ((0xff0000 & i) >> 16);
        abyte0[0] = (byte) (int) ((0xffffffffff000000L & (long) i) >> 24);
    }

    public static int Bytes4ToInt(byte abyte0[], int offset) {
        return (0xff & abyte0[offset]) << 24 | (0xff & abyte0[offset + 1]) << 16 | (0xff & abyte0[offset + 2]) << 8 | 0xff & abyte0[offset + 3];
    }

    public static long Bytes4ToLong(byte abyte0[], int offset) {
        return (255L & (long) abyte0[offset + 0]) << 24 | (255L & (long) abyte0[offset + 1]) << 16 | (255L & (long) abyte0[offset + 2]) << 8 | 255L & (long) abyte0[offset + 3];
    }

    /**
     * @param p 字节数组
     * @param off 偏移量
     * @param len 从偏移量开始往后读取长度
     * @return
     * 
     */
     public static final long getNumber(byte[] p, int off, int len) {
        long ret = 0;
        int done = off + len;
        for (int i = off; i < done; i++)
            ret = ((ret << 8) & 0xffffffff) + (p[i] & 0xff);
        return ret;
    }

    /**
     * @param p
     * @return
     */
    static public final long getNumber(byte[] p) {
        long ret = 0;
        int done = p.length;
        for (int i = 0; i < done; i++)
            ret = ((ret << 8) & 0xffffffff) + (p[i] & 0xff);
        return ret;
    }

    public static void main(String[] args) {
        byte[] t = new byte[] { 3, -1, 54, -2, 34, 6, 7, 5, 0, 7, 4 };
        System.out.println(getNumber(t, 2, 2));
    }

    public static int computerPackageLengthLevel(Double packageLength) {
        if (packageLength < 65) {
            return 1;
        } else if (packageLength <= 129) {
            return 2;
        } else if (packageLength < 257) {
            return 3;
        } else if (packageLength < 513) {
            return 4;
        } else if (packageLength < 1024) {
            return 5;
        } else {
            return 6;
        }
    }
}
