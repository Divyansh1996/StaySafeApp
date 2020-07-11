package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;


public class HardwareButtonReceiver extends Service {
    static int count = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void CapturePhoto(final int CameraId) {
        System.out.println("Camera Id = " + CameraId);
        Log.d("kkkk", "Preparing to take photo");
        Camera camera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        Camera.getCameraInfo(CameraId, cameraInfo);

        try {
            camera = Camera.open(CameraId);
        } catch (RuntimeException e) {
            Log.d("kkkk", "Camera not available: " + 1);
            camera = null;
        }
        try {
            if (null == camera) {
                Log.d("kkkk", "Could not get camera instance");
            } else {
                Log.d("kkkk", "Got the camera, creating the dummy surface texture");
                try {
                    camera.setPreviewTexture(new SurfaceTexture(0));
                    camera.startPreview();
                } catch (Exception e) {
                    Log.d("kkkk", "Could not set the surface preview texture");
                    e.printStackTrace();
                }
                camera.takePicture(null, null, new Camera.PictureCallback() {


                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                            File myfile;
                            File picsDir;
                            if (CameraId == 0) {
                                picsDir = new File(MainActivity.mContext.getExternalFilesDir(null).getAbsolutePath(), "FrontCamera_Directory");
                                picsDir.mkdir();
                                myfile = new File(picsDir, "FrontCamera" + count + ".png");
                            } else {
                                picsDir = new File(MainActivity.mContext.getExternalFilesDir(null).getAbsolutePath(), "BackCamera_Directory");
                                picsDir.mkdir();
                                myfile = new File(picsDir, "BackCamera" + count + ".png");
                            }
                            ++count;
                            System.out.println("Entering try block");
                            try {
                                myfile.createNewFile();
                                FileOutputStream fos = new FileOutputStream(myfile);
                                fos.write(data);
                                System.out.println("Image Saving");
                                fos.close();
                                Log.d("kkkk", "image saved");
                                System.out.println("Camera = "+camera);
                            } catch (Exception error) {
                                System.out.println("Exception = " + error);
                                error.printStackTrace();
                                Log.d("kkkk", "Image could not be saved");
                            }
                        camera.stopPreview();
                        camera.release();
                    }
                });
            }
        } catch (Exception e) {
            camera.release();
        }
    }
}











