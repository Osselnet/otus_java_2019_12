package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.util.ArrayList;
import java.util.List;

public class GcInfo {
    public GcInfo() {
    }

    private List<GcStats> youngGenList = new ArrayList<>();
    private List<GcStats> oldGenList = new ArrayList<>();

    public void addNotification(GarbageCollectionNotificationInfo info) {
        if (getType(info).equals("Young Generation")) {
            GcStats stats = new GcStats();
            stats.setName(info.getGcName());
            stats.setDuration(info.getGcInfo().getDuration());
            youngGenList.add(stats);
        } else {
            GcStats stats = new GcStats();
            stats.setName(info.getGcName());
            stats.setDuration(info.getGcInfo().getDuration());
            oldGenList.add(stats);
        }
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
            long youngGenDuration = 0;
            for (GcStats stats : youngGenList) {
                youngGenDuration += stats.getDuration();
            }
            long averageYoungGenDuration = youngGenDuration / youngGenList.size();

            System.out.println(String.format("%s. Name : %s, Count %s, Total duration : %s ms, Avg duration : %s ms",
                    "Young Generation", youngGenName, youngGenList.size(), youngGenDuration, averageYoungGenDuration));
        }

        if (!oldGenList.isEmpty()) {
            String olgGenName = oldGenList.get(0).getName();
            long oldGenDuration = 0;
            for (GcStats stats : oldGenList) {
                oldGenDuration += stats.getDuration();
            }
            long averageOldGenDuration = oldGenDuration / oldGenList.size();
            System.out.println(String.format("%s. Name : %s, Count %s, Total duration : %s ms, Avg duration : %s ms",
                    "Old Generation", olgGenName, oldGenList.size(), oldGenDuration, averageOldGenDuration));
        }
        youngGenList.clear();
        oldGenList.clear();
    }
}
