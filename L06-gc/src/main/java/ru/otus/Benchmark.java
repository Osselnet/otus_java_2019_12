package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Benchmark implements BenchmarkMBean {

    private volatile int size = 100;
    private static GcInfo gcInfo = new GcInfo();

    void run() throws InterruptedException {

        installGCMonitoring();
        //Print GC data every 60 seconds
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                gcInfo.printData();
            }
        }, 60000, 60000);

        Timer t2 = new Timer();
        t2.schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 190000);

        List<String> array = new ArrayList<>();
        while (true) {
            for (int i = 0; i < size; i++) {
                array.add(new String(new char[i]));
            }
            for (int i = 0; i < size / 2; i++) {
                array.remove(i);
            }
            Thread.sleep(500);
        }
    }

    static void installGCMonitoring() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcBean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    gcInfo.addNotification(info);
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }
}
