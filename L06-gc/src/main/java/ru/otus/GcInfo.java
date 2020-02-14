package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GcInfo {
    private static Map<String, MemRegion> memRegions;

    public GcInfo() {
        memRegions = new HashMap<String, MemRegion>(ManagementFactory.getMemoryPoolMXBeans().size());
        for (MemoryPoolMXBean mBean : ManagementFactory.getMemoryPoolMXBeans()) {
            memRegions.put(mBean.getName(), new MemRegion(mBean.getName(), mBean.getType() == MemoryType.HEAP));
        }
    }

    private class MemRegion {
        private boolean heap;
        private String name;

        public MemRegion(String name, boolean heap) {
            this.heap = heap;
            this.name = name;
        }

        public boolean isHeap() {
            return heap;
        }
    }

    private List<GcStats> youngGenList = new ArrayList<>();
    private List<GcStats> oldGenList = new ArrayList<>();

    public void addNotification(GarbageCollectionNotificationInfo info) {
        Map<String, MemoryUsage> memBefore = info.getGcInfo().getMemoryUsageBeforeGc();
        Map<String, MemoryUsage> memAfter = info.getGcInfo().getMemoryUsageAfterGc();

        GcStats stats = new GcStats();
        stats.setName(info.getGcName());
        stats.setDuration(info.getGcInfo().getDuration());
        long timeOperation = info.getGcInfo().getEndTime() - info.getGcInfo().getStartTime();
        stats.setSpeed(timeOperation > 0 ? Math.abs(returnMemUsage(memBefore) - returnMemUsage(memAfter)) / timeOperation : 0);

        if (getType(info).equals("Young Generation")) {
            youngGenList.add(stats);
        } else {
            oldGenList.add(stats);
        }
    }

    private static long returnMemUsage(Map<String, MemoryUsage> memUsage) {
        long memSpace = 0;
        for (Map.Entry<String, MemoryUsage> entry : memUsage.entrySet()) {
            if (memRegions.get(entry.getKey()).isHeap()) {
                memSpace += (entry.getValue().getUsed() >> 10);
            }
        }
        return memSpace;
    }

    private String getType(GarbageCollectionNotificationInfo info) {
        String type = info.getGcAction();
        if (type.equals("end of minor GC")) {
            type = "Young Generation";
        } else if (type.equals("end of major GC")) {
            type = "Old Generation";
        }
        return type;
    }

    public void printData() {
        System.out.println("Printing GC stats once a minute:");

        if (youngGenList.isEmpty() && oldGenList.isEmpty()) {
            System.out.println("No statistics for the minute");
            return;
        }

        if (!youngGenList.isEmpty()) {
            String youngGenName = youngGenList.get(0).getName();
            long speedOperationYoungGen = youngGenList.get(0).getSpeed();
            long youngGenDuration = 0;
            for (GcStats stats : youngGenList) {
                youngGenDuration += stats.getDuration();
            }
            long averageYoungGenDuration = youngGenDuration / youngGenList.size();

            System.out.println(String.format("%s. Name : %s, Count %s, Total duration : %s ms, Avg duration : %s ms, Speed of operation: %s KB/ms",
                    "Young Generation", youngGenName, youngGenList.size(), youngGenDuration, averageYoungGenDuration, speedOperationYoungGen));
        }

        if (!oldGenList.isEmpty()) {
            String oldGenName = oldGenList.get(0).getName();
            long speedOperationOldGen = oldGenList.get(0).getSpeed();

            long oldGenDuration = 0;
            for (GcStats stats : oldGenList) {
                oldGenDuration += stats.getDuration();
            }
            long averageOldGenDuration = oldGenDuration / oldGenList.size();
            System.out.println(String.format("%s. Name : %s, Count %s, Total duration : %s ms, Avg duration : %s ms, Speed of operation: %s KB/ms",
                    "Old Generation", oldGenName, oldGenList.size(), oldGenDuration, averageOldGenDuration, speedOperationOldGen));
        }
        youngGenList.clear();
        oldGenList.clear();
    }
}
