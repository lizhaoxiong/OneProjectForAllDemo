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
public class SortTest {

    @Test
    public void maopaoSort(){
        //冒泡排序
        int [] arr = {39,21,100,53,34,90};
        for(int x=0;x<arr.length-1;x++){
            for(int y = 0;y<arr.length-1-x;y++){//-1防止越界-x减少比较次数
                if(arr[y]>arr[y+1]){
                    int temp =  arr[y];
                    arr[y] = arr[y+1];
                    arr[y+1] = temp;
                }
            }
        }

        for(int i =0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
    }

    @Test
    public void findTwo(){
        //二分查找 数组必须有序
        int [] arr = {1,3,5,9,10,33,44};
        
    }
}
