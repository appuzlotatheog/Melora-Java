package com.discord.musicbot.data.model;

import java.util.ArrayList;
import java.util.List;

public class UserSavedQueuesStore {
    private String userId;
    private List<SavedQueue> savedQueues = new ArrayList<>();

    public UserSavedQueuesStore() {}

    public UserSavedQueuesStore(String userId) {
        this.userId = userId;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<SavedQueue> getSavedQueues() { return savedQueues; }
    public void setSavedQueues(List<SavedQueue> savedQueues) { this.savedQueues = savedQueues; }
}
