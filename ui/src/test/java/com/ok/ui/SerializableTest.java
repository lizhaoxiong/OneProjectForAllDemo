package com.ok.ui;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class SerializableTest {
    @Test
    public void test() throws Exception {
        User user = new User("小名", 22);
        //序列化
        //writeUser(user);

        //反序列化
        User user2 = readUser();
        System.out.println(user2.name+user2.age);
    }

    /**
     *
     */
    private User readUser() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("E://user.txt")));
        User o = (User) ois.readObject();
        return o;
    }

    /**
     * 使用实现Serializable的序列化
     * @param user
     * @throws IOException
     */
    public void  writeUser(User user) throws IOException {
        //创建对象输出流
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("E://user.txt")));
        oos.writeObject(user);
        oos.close();
    }
}