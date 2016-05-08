package com.ok.mvvm.event;

import android.text.Editable;
import android.text.TextWatcher;

import com.ok.mvvm.bean.User;

/**
 * Created by Administrator on 2016/5/1.
 */
public class UserEvent {
    public User user;
    public UserEvent(User user){this.user=user;}
    public TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            user.username = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            user.password = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
