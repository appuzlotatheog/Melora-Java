package com.discord.musicbot.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Per-user storage wrapper. Serialized to playlists/{userId}.json
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPlaylistStore {
    private int version = 1;
    private List<PlaylistData> playlists;

    public UserPlaylistStore() {
        this.playlists = new ArrayList<>();
    }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public List<PlaylistData> getPlaylists() { return playlists; }
    public void setPlaylists(List<PlaylistData> playlists) { this.playlists = playlists; }
}
