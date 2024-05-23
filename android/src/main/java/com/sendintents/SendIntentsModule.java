package com.sendintents;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.app.Activity;
import android.content.pm.PackageManager;

@ReactModule(name = SendIntentsModule.NAME)
public class SendIntentsModule extends ReactContextBaseJavaModule {
  public static final String NAME = "SendIntents";

  private static final String LOG_NAME = NAME + "Module";

  private Promise mActivityPromise;

  private static final int APP_ALL_FILES_ACCESS_REQUEST = 101;
  private static final int ALL_FILES_ACCESS_REQUEST = 102;

  private static final String E_CURRENT_ACTIVITY_DOES_NOT_EXIST = "E_CURRENT_ACTIVITY_DOES_NOT_EXIST";

  private static final String E_ACTIVITY_CANCELLED = "E_PICKER_CANCELLED";
  private static final String E_FAILED_TO_START_ACTIVITY = "E_FAILED_TO_START_ACTIVITY";
  private static final String E_NO_ACTIVITY_RESULT_DATA_FOUND = "E_NO_ACTIVITY_RESULT_DATA_FOUND";

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      if (requestCode == APP_ALL_FILES_ACCESS_REQUEST) {
        if (mActivityPromise != null) {
          if (resultCode == Activity.RESULT_CANCELED) {
            Log.d(LOG_NAME, "Activity  cancelled");
            mActivityPromise.reject(E_ACTIVITY_CANCELLED, "Activity cancelled");
          } else if (resultCode == Activity.RESULT_OK) {
            Log.d(LOG_NAME, "Activity is ok");
            Uri uri = intent.getData();
            if (uri == null) {
              Log.d(LOG_NAME, "Activity is ok. But uri =  null");
              mActivityPromise.reject(E_NO_ACTIVITY_RESULT_DATA_FOUND, "No activity result data found");
            } else {
              Log.d(LOG_NAME, "Activity is ok. uri =  " + uri.toString());
              mActivityPromise.resolve(uri.toString());
            }
          }
          mActivityPromise = null;
        } else {
          Log.w(LOG_NAME, "No activity promise set during activity result");
        }
      }
    }
  };

  public SendIntentsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(mActivityEventListener);

  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  /**
   * check if permission has been granted for external storage
   * 
   * @param promise Promise of boolean indicating if the permission has been
   *                granted or not.
   */
  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void checkPermissionForExternalStorage(Promise promise) {
    int version = Build.VERSION.SDK_INT;
    Log.d(LOG_NAME, "Build.Version.SDK_INT " + version);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
      Log.d(LOG_NAME, "Checking for permissions");
      if (!Environment.isExternalStorageManager()) {
        Log.d(LOG_NAME, "Trying to get permission of external storage manager");
        promise.resolve(false);
      } else {
        Log.d(LOG_NAME, "isExternalStorageManager already approved");
        promise.resolve(true);
      }
    } else {
      promise.resolve(false);
    }
  }

  @ReactMethod
  public void sendIntent(String action,
      String uriString,
      String[] categories,
      String data,
      int activityCode, Promise promise) {
    Activity currentActivity = getCurrentActivity();
    if (currentActivity == null) {
      promise.reject(E_CURRENT_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
      return;
    }
    // Store the promise to resolve/reject when picker returns data
    mActivityPromise = promise;
    try {
      Uri uri = Uri.parse(uriString);
      Intent intent = new Intent(action, uri);
      for (String category : categories) {
        intent.addCategory(category);
      }
      // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
      intent.setData(Uri.parse(data));
      Log.d(LOG_NAME, "About to start activity for action " + action);
      currentActivity.startActivityForResult(intent, activityCode);
    } catch (Exception ex) {
      Log.w(LOG_NAME, "Received exception during " + action, ex);
      mActivityPromise.reject(E_FAILED_TO_START_ACTIVITY, ex);
      mActivityPromise = null;
    }
  }

  /**
   * request permission to use external storage
   * 
   * @param packageName The packageName of the application requesting the
   *                    permission to use external storage
   * @param promise     Promise of boolean implying if the permission has been
   *                    granted or otherwise
   */
  @ReactMethod
  public void requestPermissionToManageAppAllFiles(String packageName, Promise promise) {
    int version = Build.VERSION.SDK_INT;
    Log.d(LOG_NAME, "Build.Version.SDK_INT " + version);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
      Log.d(LOG_NAME, "Requesting permissions");
      if (!Environment.isExternalStorageManager()) {
        Log.d(LOG_NAME, "About to open intent settings page for ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
        String uriString = "package:" + packageName;
        // (Intent.CATEGORY_HOME);
        String[] categories = { Intent.CATEGORY_DEFAULT };
        sendIntent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uriString, categories, uriString,
            APP_ALL_FILES_ACCESS_REQUEST, promise);
      } else {
        Log.d(LOG_NAME, "isExternalStorageManager already approved");
        promise.resolve("isExternalStorageManager already approved");
      }
    } else {
      Log.d(LOG_NAME, "Build.Version.SDK_INT" + version);
      promise.resolve("Build.Version.SDK_INT" + version);
    }
  }

  /**
   * request permission to use [ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION]
   * 
   * @param packageName The packageName of the application requesting the
   *                    permission to use external storage
   * @param promise     Promise of boolean implying if the permission has been
   *                    granted or otherwise
   */
  @ReactMethod
  public void requestPermissionToManageAllFiles(String packageName, Promise promise) {
    int version = Build.VERSION.SDK_INT;
    Log.d(LOG_NAME, "Build.Version.SDK_INT " + version);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
      Log.d(LOG_NAME, "Requesting permissions");
      if (!Environment.isExternalStorageManager()) {
        Log.d(LOG_NAME, "About to open intent settings page for ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION");
        String uriString = "package:" + packageName;
        // (Intent.CATEGORY_HOME);
        String[] categories = { Intent.CATEGORY_DEFAULT };
        sendIntent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION, uriString, categories, uriString,
            ALL_FILES_ACCESS_REQUEST, promise);
      } else {
        Log.d(LOG_NAME, "isExternalStorageManager already approved");
        promise.resolve("isExternalStorageManager already approved");
      }
    } else {
      Log.d(LOG_NAME, "Build.Version.SDK_INT" + version);
      promise.resolve("Build.Version.SDK_INT" + version);
    }
  }
}
