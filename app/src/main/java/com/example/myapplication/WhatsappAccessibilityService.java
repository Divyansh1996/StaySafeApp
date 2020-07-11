package com.example.myapplication;


import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

import static android.content.ContentValues.TAG;


public class WhatsappAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent (AccessibilityEvent event) {
        System.out.println("Called By WhatsappAccessibilityService");
        if (getRootInActiveWindow () == null) {
            return;
        }

        AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap (getRootInActiveWindow ());

        // Whatsapp Message EditText id
       /* List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/entry");
        if (messageNodeList == null || messageNodeList.isEmpty ()) {
            return;
        }*/

        // check if the whatsapp message EditText field is filled with text and ending with your suffix (explanation above)
        /*AccessibilityNodeInfoCompat messageField = messageNodeList.get (0);
        if (messageField.getText () == null || messageField.getText ().length () == 0
                || !messageField.getText ().toString ().endsWith (getApplicationContext ().getString (R.string.whatsapp_suffix))) { // So your service doesn't process any message, but the ones ending your apps suffix
            return;
        }*/

        // Whatsapp send button id
        List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/send");
        if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty ()) {
            return;
        }

        AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get (0);
        if (!sendMessageButton.isVisibleToUser ()) {
            return;
        }

        // Now fire a click on the send button
        sendMessageButton.performAction (AccessibilityNodeInfo.ACTION_CLICK);

        // Now go back to your app by clicking on the Android back button twice:
        // First one to leave the conversation screen
        // Second one to leave whatsapp
        try {
            Thread.sleep (500); // hack for certain devices in which the immediate back click is too fast to handle
            performGlobalAction (GLOBAL_ACTION_BACK);
            Thread.sleep (500);  // same hack as above
        } catch (InterruptedException ignored) {}
        performGlobalAction (GLOBAL_ACTION_BACK);
    }

    @Override
    public void onInterrupt() {
        System.out.println("Hello there");
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG,"Service Connected");

    }
    public boolean isAccessibilityOn (Context context, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName () + "/" + clazz.getCanonicalName ();
        try {
            accessibilityEnabled = Settings.Secure.getInt (context.getApplicationContext ().getContentResolver (), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {  }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter (':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString (context.getApplicationContext ().getContentResolver (), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString (settingValue);
                while (colonSplitter.hasNext ()) {
                    String accessibilityService = colonSplitter.next ();

                    if (accessibilityService.equalsIgnoreCase (service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

