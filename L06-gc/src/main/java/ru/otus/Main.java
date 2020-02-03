package ru.otus;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

// -Xms256m
// -Xmx256m
// -Xloggc:./L06_gc/logs/SerialGC.log
// -XX:+HeapDumpOnOutOfMemoryError
// -XX:HeapDumpPath=./L06_gc/logs/dump

// -XX:+UseSerialGC
// -XX:+UseParallelGC
// -XX:+UseG1GC

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark mbean = new Benchmark();
        mbs.registerMBean(mbean, name);
        int size = 2000;
        mbean.setSize(size);
        mbean.run();
    }
}
