package com.krajetum.jqnap.utils;

/**
 *  Disclaimer:
 *  This class is the result of a porting from a javascript script made by QNAP
 */
public class EzEncode {
    /* encode function start */
    String ezEncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    int ezDecodeChars[] = new int[]{
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
            -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

    String utf16to8(String str)
    {
        String out;
        int i, len;
        char c;
        out = "";
        len = str.length();
        for (i=0; i<len; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                out += str.charAt(i);
            }
            else if (c > 0x07FF) {
                out += (char)(0xE0 | ((c >> 12) & 0x0F));
                out += (char)(0x80 | ((c >> 6) & 0x3F));
                out += (char)(0x80 | ((c >> 0) & 0x3F));

            }
            else {
                out += (char)(0xC0 | ((c >>6) & 0x1F));
                out += (char)(0x80 | ((c >>0) & 0x3F));
            }
        }
        return out;
    }
    String utf8to16(String str) {
        String out;
        int i, len;
        char c;
        char char2, char3;

        out = "";
        len = str.length();
        i = 0;
        while(i < len) {
            c = str.charAt(i++);
            switch(c >> 4)
            {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                // 0xxxxxxx
                out += str.charAt(i-1);
                break;
                case 12: case 13:
                // 110x xxxx 10xx xxxx
                char2 = str.charAt(i++);
                out += (char)(((c & 0x1F) << 6) | (char2 & 0x3F));
                break;
                case 14:
                    // 1110 xxxx10xx xxxx10xx xxxx
                    char2 = str.charAt(i++);
                    char3 = str.charAt(i++);
                    out += (char)(((c & 0x0F) << 12) |
                            ((char2 & 0x3F) << 6) |
                            ((char3 & 0x3F) << 0));
            }
        }
        return out;
    }

    String ezEncode(String str)
    {
        String out;
        int i, len;
        int c1, c2, c3;

        len = str.length();
        i = 0;
        out = "";
        while(i < len)
        {
            c1 = str.charAt(i++) & 0xff;
            if(i == len)
            {
                out += ezEncodeChars.charAt(c1 >> 2);
                out += ezEncodeChars.charAt((c1 & 0x3) << 4);
                out += "==";
                break;
            }
            c2 = str.charAt(i++);
            if(i == len)
            {
                out += ezEncodeChars.charAt(c1 >> 2);
                out += ezEncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
                out += ezEncodeChars.charAt((c2 & 0xF) << 2);
                out += "=";
                break;
            }
            c3 = str.charAt(i++);
            out += ezEncodeChars.charAt(c1 >> 2);
            out += ezEncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
            out += ezEncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
            out += ezEncodeChars.charAt(c3 & 0x3F);
        }
        return out;
    }
    public String encode(String string){
        return ezEncode(utf8to16(string));
    }

}
