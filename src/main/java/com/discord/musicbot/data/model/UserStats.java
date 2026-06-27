package com.discord.musicbot.data.model;

import java.util.HashMap;
import java.util.Map;

public class UserStats {
    private static final int MAX_MAP_SIZE = 500;

    private long totalListeningTimeMs = 0;
    private long totalTracksPlayed = 0;
    private long djPoints = 0;
    private Map<String, Long> favoriteArtists = new HashMap<>();
    private Map<String, Long> favoriteTracks = new HashMap<>();
    private Map<String, Long> commandUses = new HashMap<>();

    public synchronized long getDjPoints() { return djPoints; }
    public synchronized void incrementDjPoints() { this.djPoints++; }
    public synchronized void addDjPoints(long points) { this.djPoints = Math.max(0, this.djPoints + points); }

    public synchronized long getTotalListeningTimeMs() { return totalListeningTimeMs; }
    public synchronized void addListeningTime(long ms) { this.totalListeningTimeMs += ms; }

    public synchronized long getTotalTracksPlayed() { return totalTracksPlayed; }
    public synchronized void incrementTracksPlayed() { this.totalTracksPlayed++; }

    public synchronized Map<String, Long> getFavoriteArtists() { return new HashMap<>(favoriteArtists); }
    public synchronized void incrementArtist(String artist) {
        if (artist == null || artist.isEmpty()) return;
        favoriteArtists.put(artist, favoriteArtists.getOrDefault(artist, 0L) + 1);
        enforceSizeLimit(favoriteArtists);
    }

    public synchronized Map<String, Long> getFavoriteTracks() { return new HashMap<>(favoriteTracks); }
    public synchronized void incrementTrack(String track) {
        if (track == null || track.isEmpty()) return;
        favoriteTracks.put(track, favoriteTracks.getOrDefault(track, 0L) + 1);
        enforceSizeLimit(favoriteTracks);
    }

    public synchronized Map<String, Long> getCommandUses() { return new HashMap<>(commandUses); }
    public synchronized void incrementCommand(String command) {
        if (command == null || command.isEmpty()) return;
        commandUses.put(command, commandUses.getOrDefault(command, 0L) + 1);
        enforceSizeLimit(commandUses);
    }

    private void enforceSizeLimit(Map<String, Long> map) {
        if (map.size() > MAX_MAP_SIZE) {
            String lowestKey = null;
            long lowestVal = Long.MAX_VALUE;
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                if (entry.getValue() < lowestVal) {
                    lowestVal = entry.getValue();
                    lowestKey = entry.getKey();
                }
            }
            if (lowestKey != null) {
                map.remove(lowestKey);
            }
        }
    }
}
