package com.discord.musicbot.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a single track stored in a playlist or favorites.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistTrack {
    private String title;
    private String author;
    private long duration;
    private String uri;
    private String source;
    private String encodedTrack; // Base64 Lavaplayer encoded track (nullable)

    public PlaylistTrack() {}

    public PlaylistTrack(String title, String author, long duration, String uri, String source, String encodedTrack) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.uri = uri;
        this.source = source;
        this.encodedTrack = encodedTrack;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getEncodedTrack() { return encodedTrack; }
    public void setEncodedTrack(String encodedTrack) { this.encodedTrack = encodedTrack; }
}
