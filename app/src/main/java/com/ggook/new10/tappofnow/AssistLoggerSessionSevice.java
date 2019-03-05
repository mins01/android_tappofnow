package com.ggook.new10.tappofnow;

import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.service.voice.VoiceInteractionSessionService;
import android.util.Log;

public class AssistLoggerSessionSevice extends VoiceInteractionSessionService {

    @Override
    public VoiceInteractionSession onNewSession(Bundle bundle) {
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());

        return (new AssistLoggerSession(this));
    }
}
