package com.sendintents;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import android.content.pm.PackageManager;

@ReactModule(name = SendIntentsModule.NAME)
public class SendIntentsModule extends ReactContextBaseJavaModule {
  public static final String NAME = "SendIntents";

  public SendIntentsModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b * 2);
  }  

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void checkPermission(Promise promise) {
    int version = Build.VERSION.SDK_INT;
    Log.d("SendIntentsModule", "Build.Version.SDK_INT " + version);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
      Log.d("SendIntentsModule", "Checking for permissions");
      if (!Environment.isExternalStorageManager()) {
        Log.d("SendIntentsModule", "Trying to get permission of external storage manager");
        promise.resolve(false);
      } else {
        Log.d("SendIntentsModule", "isExternalStorageManager already approved");
        promise.resolve(true);
      }
    } else {
      promise.resolve(false);
    }
  }

  @ReactMethod
  public void requestPermission(String packageName, Promise promise) {
    int version = Build.VERSION.SDK_INT;
    Log.d("SendIntentsModule", "Build.Version.SDK_INT " + version);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
      Log.d("SendIntentsModule", "Requesting permissions");
      if (!Environment.isExternalStorageManager()) {
        Log.d("SendIntentsModule", "Requesting permission of external storage manager");
        try {
          Uri uri = Uri.parse("package:" +packageName);
          Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
          intent.addCategory("android.intent.category.DEFAULT");
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          intent.setData(Uri.parse(String.format("package:%s", packageName)));
          getReactApplicationContext().startActivity(intent);
          promise.resolve(true);
        } catch (Exception ex) {
          Intent intent = new Intent();
          intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          getReactApplicationContext().startActivity(intent);
          promise.resolve(true);
        }
      } else {
        Log.d("SendIntentsModule", "isExternalStorageManager already approved");
        promise.resolve(true);
      }
    } else {
      promise.resolve(false);
      Log.d("SendIntentsModule", "Build.Version.SDK_INT" + version);
    }
  }  
}
