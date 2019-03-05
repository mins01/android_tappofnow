package com.ggook.new10.tappofnow;

import android.app.Activity;
import android.app.Application;
import android.app.ListActivity;
import android.app.assist.AssistContent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class SubActivity extends ListActivity  {

    private Application myApp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
//        registerAssist();
//        finish();

    }


    public  void registerAssist(){
        Log.v("@@","onReady");
        Application myApp = getApplication();
        Application.OnProvideAssistDataListener fn = new Application.OnProvideAssistDataListener() {
            @Override
            public void onProvideAssistData(Activity activity, Bundle bundle) {
                Log.v("@@","registerOnProvideAssistDataListener.onProvideAssistData");
            }
        };
        myApp.unregisterOnProvideAssistDataListener(fn);
        myApp.registerOnProvideAssistDataListener(fn);
        Log.v("@@","registerOnProvideAssistDataListener");
    }

    @Override
    public void onProvideAssistContent(AssistContent outContent) {
        super.onProvideAssistContent(outContent);

        String structuredJson = null;
        try {
            structuredJson = new JSONObject()
                    .put("@type", "MusicRecording")
                    .put("@id", "https://example.com/music/recording")
                    .put("name", "Album Title")
                    .put("description", "Album Title description")

                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("@@","onProvideAssistContent");
        outContent.setStructuredData(structuredJson);



    }

    @Override
    public void onProvideAssistData(Bundle data) {
        super.onProvideAssistData(data);
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.v("@@","onPointerCaptureChanged");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
    }



}

