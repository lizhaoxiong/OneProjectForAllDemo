/**
 * Copyright 2014 Zhenguo Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ok.utils.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 字符串操作工具包 结合android.text.TextUtils使用
 *
 * @author jingle1267@163.com
 */
public final class StringUtil {

    /**
     * Don't let anyone instantiate this class.
     */
    private StringUtil() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 字符串转成float
     * @param str
     * @return
     */
    public static float parseStr2Float(String str) {
        if (str == null) {
            return -1;
        }
        try {
            return Float.parseFloat(str);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 字符串转成Long
     * @param str
     * @return
     */
    public static long parseStr2Long(String str) {
        if (str == null) {
            return -1;
        }
        try {
            return Long.parseLong(str);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str != null && str.length() > 0) {
            return false;
        }
        return true;
    }


    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param data 要转换的字节数组
     * @return 转换后的结果
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 计算字符个数，一个汉字算两个
     * @param s
     * @return
     */
    public static int countWord(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int n = s.length(), a = 0, b = 0;
        int len = 0;
        char c;
        for (int i = 0; i < n; i++) {
            c = s.charAt(i);
            if (Character.isSpaceChar(c)) {
                ++b;
            }
            else if (isAscii(c)) {
                ++a;
            }
            else {
                ++len;
            }
        }
        return len + (int) Math.ceil((a + b) / 2.0);
    }
    public static boolean isAscii(char c) {
        return c <= 0x7f;
    }

    /**
     * 过滤文本中的html脚本信息
     * @param inputString
     * @return
     */
    public static String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        Pattern p_html1;
        Matcher m_html1;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String regEx_html1 = "<[^>]+";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(htmlStr);
            htmlStr = m_html1.replaceAll(""); // 过滤html标签

            textStr = htmlStr;

        }
        catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }
    /**
     * 判断字符串是否是合法的16进制串
     * @author: Xue Wenchao
     * @param str
     * @return
     * @return: boolean
     * @date: 2014-1-21 上午10:13:23
     */
    public static boolean isHexString(String str) {
        if (str == null) {
            return false;
        }
        return Pattern.matches("^[0-9a-fA-F]++$", str);
    }

    /**
     *  按字节截取字符串
     * @param str
     * @param subSLength
     * @return
     */
    public static String subStr(String str, int subSLength){
        if(TextUtils.isEmpty(str)){
            return "";
        }
        final int len = str.length();
        StringBuffer newStr = new StringBuffer();
        int counter = 0;

        for (int i = 0; i < len; i++) {
            char sigleItem = str.charAt(i);
            if(counter<subSLength){
                newStr.append(sigleItem);
            }
            if (isAlphanumeric(sigleItem)) {
                counter++;
            }
            else if (Character.isLetter(sigleItem)) {
                counter = counter + 2;
            }
            else {
                counter++;
            }

        }
        if(counter<=subSLength){
            return str;
        }else{
            newStr.append("...");
            return newStr.toString();
        }
    }

    /**
     * 判断字符是否为英文字母或者阿拉伯数字.
     *
     * @param ch char字符
     * @return true or false
     */
    public static boolean isAlphanumeric(char ch) {
        // 常量定义
        final int DIGITAL_ZERO = 0;
        final int DIGITAL_NINE = 9;
        final char MIN_LOWERCASE = 'a';
        final char MAX_LOWERCASE = 'z';
        final char MIN_UPPERCASE = 'A';
        final char MAX_UPPERCASE = 'Z';

        if ((ch >= DIGITAL_ZERO && ch <= DIGITAL_NINE) || (ch >= MIN_LOWERCASE && ch <= MAX_LOWERCASE)
                || (ch >= MIN_UPPERCASE && ch <= MAX_UPPERCASE)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static int calculateCharLength(String src) {
        int counter = -1;
        if (src != null) {
            counter = 0;
            final int len = src.length();
            for (int i = 0; i < len; i++) {
                char sigleItem = src.charAt(i);
                if (isAlphanumeric(sigleItem)) {
                    counter++;
                }
                else if (Character.isLetter(sigleItem)) {
                    counter = counter + 2;
                }
                else {
                    counter++;
                }
            }
        }
        else {
            counter = -1;
        }

        return counter;
    }

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    /**
     * 将给定的字符串中所有给定的关键字标红
     *
     * @param sourceString 给定的字符串
     * @param keyword      给定的关键字
     * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml()转换为Spanned对象再传递给TextView对象
     */
    public static String keywordMadeRed(String sourceString, String keyword) {
        String result = "";
        if (sourceString != null && !"".equals(sourceString.trim())) {
            if (keyword != null && !"".equals(keyword.trim())) {
                result = sourceString.replaceAll(keyword,
                        "<font color=\"red\">" + keyword + "</font>");
            } else {
                result = sourceString;
            }
        }
        return result;
    }

    /**
     * 为给定的字符串添加HTML红色标记，当使用Html.fromHtml()方式显示到TextView 的时候其将是红色的
     *
     * @param string 给定的字符串
     * @return
     */
    public static String addHtmlRedFlag(String string) {
        return "<font color=\"red\">" + string + "</font>";
    }

}
