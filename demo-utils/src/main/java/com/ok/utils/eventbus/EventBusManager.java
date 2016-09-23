package com.ok.utils.eventbus;

import com.ok.utils.eventbus.subscriber.HttpSubscriber;
import com.ok.utils.utils.log.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * EventBusManager封装了EventBus,提供创建，发送，注册，注销
 */
public class EventBusManager {
    private EventBus mEventBus;

    private static EventBusManager Instance;

    public EventBusManager() {
        mEventBus = EventBus.getDefault();

    }

    public static EventBusManager getInstance() {
        if (Instance == null) {
            synchronized (EventBusManager.class) {
                if (Instance == null) {
                    Instance = new EventBusManager();
                }
            }
        }
        return Instance;
    }

    public void post(Object object) {
        if (object == null) {
            LogUtils.w("EventBusManager", "post null object");
            return;
        }
        mEventBus.post(object);
    }


    public void register(Object object) {
        if(object==null)return;
        try {
            if (!mEventBus.isRegistered(object)) {
                mEventBus.register(object);
            }
        } catch (Exception e) {

        }
    }

    public void unregister(Object object) {
        if(object==null)return;
        try {
            if (mEventBus.isRegistered(object)) {
                mEventBus.unregister(object);
            }
        } catch (Exception e) {

        }
    }


    public void registerDefault() {
        register(new HttpSubscriber());
    }


}
