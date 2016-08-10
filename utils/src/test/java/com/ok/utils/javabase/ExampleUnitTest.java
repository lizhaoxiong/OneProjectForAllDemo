package com.ok.utils.javabase;

import com.ok.utils.javabase.User;

import org.junit.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /**
     * java的引用类型：
     * 强应用：=
     * 软引用：SoftReference
     * 弱引用：WeakReference  universal image loader
     * 虚引用：PhantomReference LeakCanary
     *
     * 对象的可及性：由最强引用类型决定
     * 强可及对象，永远不会被GC回收
     * 弱可及对象，当内存不足时被GC回收
     *
     */
    @Test
    public void TestReference(){
        //String str = "abc";//没有被回收，"abc"存在于常量池中跟堆没有关系
        String str = new String("abc");//创建了两个对象，一个是常量池"abc"，一个是堆内存String对象

        //创建一个软引用，引用到str
        SoftReference<String> srf = new SoftReference<>(str);
        //创建一个弱引用，引用到str
        WeakReference<String> wrf = new WeakReference<>(str);

        str = null;//去掉强引用
        srf.clear();//清除软引用链
        //wrf.clear();//清除弱引用链
        System.gc();//主动GC,GC回收器会回收掉堆中弱引用

        String srfString = srf.get();
        String wrfString = wrf.get();


        System.out.println("软引用获取的对象"+srfString);
        System.out.println("弱引用获取的对象"+wrfString);
    }

    /**
     * HashMap<Integer,User></>按照User里面的age倒序排序
     */
    @Test
    public void TestSort(){
        HashMap<Integer,User> hashMap = new HashMap<>();

        User user = new User("zhangsan",22);

        User user2 = new User("lisi",23);

        User user3 = new User("wangwu",21);

        hashMap.put(1,user);
        hashMap.put(2,user2);
        hashMap.put(3,user3);


        System.out.println("排序前"+hashMap);
        HashMap<Integer,User> sortedHashMap = sortHashMap(hashMap);
        System.out.println("排序后"+sortedHashMap);
    }

    private HashMap<Integer,User> sortHashMap(HashMap<Integer, User> hashMap) {
        //HashMap是无序的，必须使用有序的子类LinkedHashMap
        LinkedHashMap<Integer,User> sortedHashMap = new LinkedHashMap<>();
        //HashMap是没有方法排序的，因为他是无序的,那么就把他转换成Set
        Set<Map.Entry<Integer, User>> entrySet = hashMap.entrySet();
        //set转换成list
        ArrayList<Map.Entry<Integer, User>> list = new ArrayList<>(entrySet);
        //我们知道这个排序方法，可是里面需要传入list
        Collections.sort(list,new Comparator<Map.Entry<Integer,User>>(){
            @Override
            public int compare(Map.Entry<Integer, User> lhs, Map.Entry<Integer, User> rhs) {
                //按照age排序
                //return rhs.getValue().age-lhs.getValue().age;
                //按照姓名的首字母排序
                return  rhs.getValue().name.compareTo(lhs.getValue().name);
            }
        });

        //将排好序的list转换成LinkedHashMap,遍历一一put
        for(int i=0;i<list.size();i++){
            Map.Entry<Integer, User> userEntry = list.get(i);
            sortedHashMap.put(userEntry.getKey(),userEntry.getValue());
        }
        return sortedHashMap;
    }
}