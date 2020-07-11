package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {
    private Service mNetworkReceiver;
    public static Context mContext;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Location location;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

   /* }

    @Override
    public void onResume() {
        super.onResume();

        mNetworkReceiver = new HardwareButtonReceiver();
        View.OnClickListener contactsClickListener = new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {*/

                playsound();
                SmsManager smsManager = SmsManager.getDefault();
                String message="Please Help me Call the police Immediately Because I am in trouble and you will receive Necessary details in whatsapp";
                smsManager.sendTextMessage("+919479330185", null, message, null, null);
                flashLightOn();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flashLightOff();

                for(int i=0;i<5;i++){
                    HardwareButtonReceiver hardwareButtonReceiver=new HardwareButtonReceiver();
                    hardwareButtonReceiver.CapturePhoto(0);
                }
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

                ArrayList<Uri> Uri_files = new ArrayList<Uri>();
                for (int i = 0; i < 5; i++) {
                    String Filename = getContext().getExternalFilesDir(null).getAbsolutePath() + "/FrontCamera_Directory/FrontCamera" + i + ".png";
                    File imageFile = new File(Filename);
                    Uri_files.add(Uri.fromFile(imageFile));
                }

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                String toNumber = "+91 94793 30185"; // contains spaces.
                toNumber = toNumber.replace("+", "").replace(" ", "");

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                //String Filename = getContext().getExternalFilesDir(null).getAbsolutePath() + "/FrontCamera_Directory/FrontCamera"+i+".png";
                //File imageFile = new File(Filename);
                sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri_files);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                //sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("image/png");
                startActivity(sendIntent);

            //}


        //};
        //Button btnContacts = (Button) findViewById(R.id.btn_contacts);
        //btnContacts.setOnClickListener(contactsClickListener);
    }

    public Context getContext() {
        return mContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            }
        } catch (CameraAccessException e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }

   private void playsound(){
       Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
       MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
       mp.start();
   }

}

