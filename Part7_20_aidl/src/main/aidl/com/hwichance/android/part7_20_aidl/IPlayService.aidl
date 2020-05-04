// IPlayService.aidl
package com.hwichance.android.part7_20_aidl;

// Declare any non-default types here with import statements

interface IPlayService {
    int currentPosition();
    int getMaxDuration();
    void start();
    void stop();
    int getMediaStatus();
}
