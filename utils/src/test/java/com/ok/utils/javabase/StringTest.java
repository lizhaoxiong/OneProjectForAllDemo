package com.ok.utils.javabase;

import org.junit.Test;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author li zhaoxiong
 * @version 1.0.0
 * @description
 * @modify
 */
public class StringTest {
    @Test
    public void testStringByte(){
        //??????1????8?????UTF-8 3???ISO-8859?1?
        String str = "?abc";
        int length = str.getBytes().length;
        System.out.println(length);
    }

}
