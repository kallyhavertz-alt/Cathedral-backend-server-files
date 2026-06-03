package com.cathedralapi.cathedralbackenedapi;

import java.util.List;

public interface LiveStreamService {
    // 🎯 Returns the active 3-4 videos instead of just one
    List<LiveStreamConfig> getAllActiveStreams();

    // 🎯 Saves a new stream to the list instead of replacing the only one
    LiveStreamConfig saveNewStream(LiveStreamConfig newConfig);

    // 🎯 The background worker will call this to clean up the table
    void purgeExpiredStreams();
}