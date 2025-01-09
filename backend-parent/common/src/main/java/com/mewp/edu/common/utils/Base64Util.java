package com.mewp.edu.common.utils;

/**
 * Base64 工具类
 *
 * @author mewp
 */
public final class Base64Util {
    private static final char[] ENCODE_TABLE = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    private Base64Util() {
        throw new IllegalStateException("工具类不能实例化！");
    }

    /**
     * 编码
     *
     * @param from 待编码的字节数组
     * @return 编码后的字符串
     */
    public static String encode(byte[] from) {
        StringBuilder to = new StringBuilder((int) ((double) from.length * 1.34D) + 3);
        int num = 0;
        int currentByte = 0;

        int i;
        for (i = 0; i < from.length; i++) {
            num %= 8;
            while (num < 8) {
                switch (num) {
                    case 0:
                        currentByte = (from[i] & 0xFC) >>> 2;
                        break;
                    case 2:
                        currentByte = (from[i] & 0x03) << 4;
                        if (i + 1 < from.length) {
                            currentByte |= (from[i + 1] & 0xF0) >>> 4;
                        }
                        break;
                    case 4:
                        currentByte = (from[i] & 0x0F) << 2;
                        if (i + 1 < from.length) {
                            currentByte |= (from[i + 1] & 0xC0) >>> 6;
                        }
                        break;
                    case 6:
                        currentByte = (from[i] & 0x3F);
                        break;
                    default:
                        break;
                }
                if (num != 6 || i + 1 < from.length) {
                    to.append(ENCODE_TABLE[currentByte]);
                }
                num += 6;
            }
        }

        while (to.length() % 4 != 0) {
            to.append("=");
        }
        return to.toString();
    }
}
