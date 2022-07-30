package com.example.new_grad;

import android.media.Image;

public interface EZCamCallback {
    void onCameraReady();
    void onPicture(Image image);
    void onError(String message);
    void onCameraDisconnected();


}
