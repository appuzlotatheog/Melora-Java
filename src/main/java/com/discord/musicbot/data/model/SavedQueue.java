package com.discord.musicbot.data.model;

import java.util.ArrayList;
import java.util.List;

public class SavedQueue {
    private String name;
    private long createdAt;
    private List<PlaylistTrack> tracks = new ArrayList<>();

    public SavedQueue() {}

    public SavedQueue(String name, long createdAt, List<PlaylistTrack> tracks) {
        this.name = name;
        this.createdAt = createdAt;
        this.tracks = tracks;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public List<PlaylistTrack> getTracks() { return tracks; }
    public void setTracks(List<PlaylistTrack> tracks) { this.tracks = tracks; }
}
