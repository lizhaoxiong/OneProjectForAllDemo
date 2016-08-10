package com.ok.utils.utils.test;

import android.view.KeyEvent;

import com.ok.utils.utils.log.Logger;

import java.io.IOException;

/**
 * 模拟系统信号
 */
public class AnalogSignalUtil {

    private static final String TAG = "TweenAnimView";

    public static final class KeyEventSignal {

        public static void up() {
            analogMenuEvent(KeyEvent.KEYCODE_DPAD_UP);
        }

        public static void down() {
            analogMenuEvent(KeyEvent.KEYCODE_DPAD_DOWN);
        }

        public static void left() {
            analogMenuEvent(KeyEvent.KEYCODE_DPAD_LEFT);
        }

        public static void right() {
            analogMenuEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
        }

        public static void center() {
            analogMenuEvent(KeyEvent.KEYCODE_DPAD_CENTER);
        }

        private static void analogMenuEvent(int keyCode) {
            if (keyCode < 0) {
                keyCode = KeyEvent.KEYCODE_MENU;
            }
            try {
                String keyCommand = "input keyevent " + keyCode;
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(keyCommand);
            } catch (IOException e) {
                Logger.e(e.getMessage());
            }
        }

    }
}
