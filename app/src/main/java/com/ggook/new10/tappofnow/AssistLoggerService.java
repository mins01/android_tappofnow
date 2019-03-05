package com.ggook.new10.tappofnow;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.util.Log;

public class AssistLoggerService extends VoiceInteractionService{
//    private Application myApp;

    @Override
    public void onReady() {
        super.onReady();
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
//        registerAssist();
//        getApplicationContext().startService(new Intent(getApplicationContext(),MyA11yService.class));

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
    }




}
