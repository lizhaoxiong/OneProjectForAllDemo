package com.ok.ui.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

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
public class User implements Parcelable{

    public String name;
    public int age;

    public User(String name,int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name+age;
    }

    /**
     * 描叙
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 序列化
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    /**
     * 反范序列化
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            User user = new User(in.readString(),in.readInt());
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


}
