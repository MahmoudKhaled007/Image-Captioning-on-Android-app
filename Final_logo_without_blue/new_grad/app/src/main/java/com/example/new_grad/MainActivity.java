package com.example.new_grad;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity implements EZCamCallback, View.OnLongClickListener{

    private static final int REQUEST_CODE = 100;
    private TextureView textureView;
    TextToSpeech textToSpeech;
    private EZCam cam;
    private SimpleDateFormat dateFormat;

    private final String TAG = "CAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        textureView = (TextureView) findViewById(R.id.textureView);
        dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());

//call back ez cam

        cam = new EZCam(this);
        cam.setCameraCallback(this);
//LENS CHANGE
        String id = cam.getCamerasList().get(CameraCharacteristics.LENS_FACING_BACK);
        cam.selectCamera(id);
        askPermission();


    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
            cam.open(CameraDevice.TEMPLATE_PREVIEW, textureView);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.CAMERA}, REQUEST_CODE);

        }
    }


@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                textToSpeech.speak("Camera Ready",TextToSpeech.QUEUE_FLUSH,null);

//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                cam.open(CameraDevice.TEMPLATE_PREVIEW, textureView);

            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                textToSpeech.speak("camera permission denied",TextToSpeech.QUEUE_FLUSH,null);

            }
        }
    }
    @Override
    public void onCameraReady() {
        cam.setCaptureSetting(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY);
        cam.startPreview();

//mmkn n4el el timer dh
        new CountDownTimer(4000,100){

            @Override
            public void onFinish() {
                //cam.startPreview();

                cam.takePicture();
                textToSpeech.speak("Photo Taken, please wait.",TextToSpeech.QUEUE_FLUSH,null);

            }

            @Override
            public void onTick(long millisUntilFinished) {

            }

        }.start();

        textureView.setOnLongClickListener(this);
    }

    @Override
    public void onPicture(Image image) {
        cam.stopPreview();
        try {
            String filename = "image_"+dateFormat.format(new Date())+".jpg";
            File file = new File(getFilesDir(), filename);
            EZCam.saveImage(image, file);

            Intent intent = new Intent(this, DisplayActivity.class);
            intent.putExtra("filepath", file.getAbsolutePath());
            startActivity(intent);
            finish();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());

        }
    }
// mmkn nzwdha fe text to speech
    @Override
    public void onCameraDisconnected() {
        Log.e(TAG, "Camera disconnected");
    }

    @Override
    public void onError(String message) {
        Log.e(TAG, message);
    }

    @Override
    protected void onDestroy() {
        cam.close();
        super.onDestroy();
    }


    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
