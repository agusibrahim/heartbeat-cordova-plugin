package com.litekey.cordova.plugins.heartbeat;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class HeartBeatPlugin extends CordovaPlugin {

    private static final String TAG = HeartBeatPlugin.class.getSimpleName();
    public static final int REQUEST_CODE = 0x03A070A73;
    
    private CallbackContext callback;

    @Override
    public boolean execute(String action, JSONArray data,
            CallbackContext callbackContext) throws JSONException {
        this.callback = callbackContext;
       

        if (action.equals("take")) {
            take();
            PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
            r.setKeepCallback(true);
            callbackContext.sendPluginResult(r);            
        } else {
            return false;
        }
        return true;
    }

    private void take() {
        Intent intent = new Intent(this.cordova.getActivity().getApplicationContext(), CameraActivity.class);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());
        this.cordova.startActivityForResult((CordovaPlugin) this, intent, REQUEST_CODE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i(TAG, "REQUEST_CODE: " + REQUEST_CODE + ", RESULT_OK: " + Activity.RESULT_OK);
        Log.i(TAG, "ActivityResult: " + requestCode + " : " + resultCode);
        if(requestCode == REQUEST_CODE){            
            if(resultCode == Activity.RESULT_OK){
                String bpm = Integer.toString(intent.getIntExtra("bpm", 0));
                Log.i(TAG, "Result: " + bpm);               
                this.callback.sendPluginResult(new PluginResult(PluginResult.Status.OK, bpm));
            } else {
                this.callback.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
            }
        }       
    }
    
}
