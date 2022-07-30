package com.example.new_grad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisplayActivity extends Activity implements View.OnClickListener {

    Bitmap bm;
    TextView tv;
    TextToSpeech textToSpeech;
    String Bluetooth_Output;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_CODE = 100;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

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
////////////////////////////////BLUETOOTH///////////////
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
///////////
        if (ContextCompat.checkSelfPermission(DisplayActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(DisplayActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);

        } else {
            ActivityCompat.requestPermissions(DisplayActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
        }

        Log.d("BTN_ADAPTER", String.valueOf(btAdapter.getBondedDevices()));

        BluetoothDevice hc05 = btAdapter.getRemoteDevice("00:21:04:08:13:0B");
        if (ContextCompat.checkSelfPermission(DisplayActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(DisplayActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);

        } else {
            ActivityCompat.requestPermissions(DisplayActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
        }
        Log.d("HC-05",hc05.getName());

        BluetoothSocket btSocket = null;

        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);


                btSocket.connect();


                Log.d("First CONNECT", String.valueOf(btSocket.isConnected()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!btSocket.isConnected());


        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(48);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());

            byte[] buffer = new byte[12];
//          bytes = inputStream.read(buffer);

            // mmkn hena while loop
            for (int i = 0; i < 12; i++) {

                byte b = (byte) inputStream.read();
                buffer[i]=b;
                Log.d("CHAAR", String.valueOf((b)));

            }
            Bluetooth_Output= new String(buffer, StandardCharsets.UTF_8);
/////////// final text
            Log.d("Bluetooth_Read", Bluetooth_Output);

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            btSocket.close();
            Log.d("IS CONNECTEd_flag", String.valueOf(btSocket.isConnected()));
        } catch (IOException e) {
            e.printStackTrace();
        }

////////////////////////BLUETOOTH--///////////////

        tv=(TextView) findViewById(R.id.tv);


        String img21 ="";
        String filepath = getIntent().getExtras().getString("filepath", null);
        File file = new File(filepath);
        Log.d("file", String.valueOf(file)); //(debug)
        Log.d("filepath", filepath); //(debug)
        Uri uri = Uri.parse(getIntent().getExtras().getString("filepath"));

///////////////////////// here we set image view from the pervious activity///////////////
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(uri);


        /////////////////////////////////Converting imageView to bitMap then converting BitMap to Base64////////////////////////////////////

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

//        Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
        Bitmap bMap = BitmapFactory.decodeFile(filepath);
        bMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

//        tv.setText(imageString);
        Log.d("ImageeString", imageString); //(debug)

        // encodeeee hena



        postDataUsingVolley(imageString);



        new CountDownTimer(40000,1000){
            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            @Override
            public void onTick(long millisUntilFinished) {
            }
        }.start();
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));


    }
    private void postDataUsingVolley(String img) {

        RequestQueue queue= Volley.newRequestQueue(this);
        //bONDOK
//        String URL = "http://192.168.1.18:5000/";
        //mazen
        String URL ="http://192.168.1.17:5000/";
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("img", img);
//        jsonParams.put("job", job);
        Log.d("Json Request:", String.valueOf(new JSONObject(jsonParams)));

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, URL,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Json Response", String.valueOf(response).substring(10));
                        textToSpeech.speak(String.valueOf(response).substring(10)+Bluetooth_Output+"away",TextToSpeech.QUEUE_FLUSH,null);
                        new CountDownTimer(5000,1000){

                            @Override
                            public void onFinish() {
                                //cam.startPreview();
                                textToSpeech.speak(String.valueOf(response).substring(10)+Bluetooth_Output+"away",TextToSpeech.QUEUE_FLUSH,null);
                            }

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                        }.start();
                        Toast.makeText(getApplicationContext(), String.valueOf(response),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error

                        //mmkn nzwd hena speech w ntrgm el errors l users if condtions
                        textToSpeech.speak("There was An error, please try again ",TextToSpeech.QUEUE_FLUSH,null);

                        Log.d( "Error: " , String.valueOf(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String,String>();
                // headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }



        };
        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 70000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 70000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(postRequest);

    }

    @Override
    public void onClick(View v) {
//        startActivity(new Intent(this, MainActivity.class));

    }
}
