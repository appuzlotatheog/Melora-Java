package com.discord.musicbot.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single playlist (or the hidden favorites playlist).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistData {
    private String id;          // UUID
    private String name;        // Display name (e.g. "Chill Vibes" or "favorites_123456")
    private String userId;      // Owner user ID
    private List<PlaylistTrack> tracks;
    private long createdAt;     // epoch millis
    private long updatedAt;     // epoch millis
    private boolean isMewsic = false;

    public PlaylistData() {
        this.tracks = new ArrayList<>();
    }

    public PlaylistData(String id, String name, String userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.tracks = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<PlaylistTrack> getTracks() { return tracks; }
    public void setTracks(List<PlaylistTrack> tracks) { this.tracks = tracks; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public boolean isFavorites() {
        return name != null && name.startsWith("favorites_");
    }

    public long getTotalDuration() {
        return tracks.stream().mapToLong(PlaylistTrack::getDuration).sum();
    }

    public boolean isMewsic() { return isMewsic; }
    public void setMewsic(boolean mewsic) { isMewsic = mewsic; }
}
