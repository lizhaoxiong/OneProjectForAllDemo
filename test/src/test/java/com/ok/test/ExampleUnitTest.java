package com.ok.test;

import android.os.Handler;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    //equals 和 == 区别，是equals可以被复写自定义比较，而==只能比较值
    @Test
    public void testEquals(){
        String str1 = "he";
        String str2 = str1 + new String("llow");
        System.out.println(str2);
        System.out.println(str1.equals("he"));
        System.out.println(str1.equals(new String("he")));

    }

    @Test
    public void testSwitch(){
        String i = "";
        switch (i){
            case "100":

        }
    }
}