package org.geekbang.event;

import java.util.Observable;
import java.util.Observer;

/**
 * @author mao  2021/5/28 3:22
 */
public class ObserverDemo {
    public static void main(String[] args) {
        Observable observable = new EventObservable();
        // 添加监听者
        observable.addObserver(new EventObserver());
        // 发布事件, 通知监听者
        observable.notifyObservers("hello world...");
    }


    // 被观察者
    static class EventObservable extends Observable {
        @Override
        public void setChanged() {
            super.setChanged();
        }

        @Override
        public void notifyObservers(Object msg) {
            // 打开监听开关
            setChanged();
            super.notifyObservers(msg);
            // 关闭监听开关
            clearChanged();
        }
    }

    // 观察者
    static class EventObserver implements Observer {

        @Override
        public void update(Observable o, Object msg) {
            System.out.println("收到事件: " + msg);
        }
    }
}
