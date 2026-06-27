package com.discord.musicbot.lyrics;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LyricsCache {
    private static final Map<String, LyricsData> cache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    public static class LyricsData {
        public String query;
        public List<String> pages;
        public String source;
        public boolean isLive;
        public long timestamp;

        public LyricsData(String query, List<String> pages, String source, boolean isLive) {
            this.query = query;
            this.pages = pages;
            this.source = source;
            this.isLive = isLive;
            this.timestamp = System.currentTimeMillis();
        }
    }

    static {
        // Clean up cache entries older than 30 minutes
        cleaner.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            cache.entrySet().removeIf(entry -> now - entry.getValue().timestamp > 30 * 60 * 1000);
        }, 30, 30, TimeUnit.MINUTES);
    }

    public static void put(String id, LyricsData data) {
        if (cache.size() >= 100) {
            cache.clear(); // simple cap to prevent unbounded growth
        }
        cache.put(id, data);
    }

    public static LyricsData get(String id) {
        return cache.get(id);
    }
}
