package org.shawn.games.Serendipity.Search.Debug;

import java.util.HashMap;
import java.util.Map;

public class Debugger {
    private static Map<String, AveragedEntry> averages = new HashMap<>();
    private static Map<String, FrequencyEntry> counters = new HashMap<>();

    public static void dbg_mean_of(String name, long value) {
        if (!averages.containsKey(name)) {
            averages.put(name, new AveragedEntry());
        }

        averages.get(name).add(value);
    }

    public static void dbg_hit_on(String name, boolean hit) {
        if (!counters.containsKey(name)) {
            counters.put(name, new FrequencyEntry());
        }

        counters.get(name).add(hit);
    }

    public static void print() {
        for (Map.Entry<String, AveragedEntry> entry : averages.entrySet()) {
            System.out.printf("Average of <%s>\t | %s\n", entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, FrequencyEntry> entry : counters.entrySet()) {
            System.out.printf("Hits of <%s>\t | %s\n", entry.getKey(), entry.getValue());
        }
    }
}
