package com.ggook.new10.tappofnow;

import android.util.Log;

import com.mins01.java.PickupKeywords.TextInfo;
import com.mins01.java.PickupKeywords.WordInfo;

import java.util.ArrayList;

public class CallbackForWordInfo implements Runnable {
    public ArrayList<TextInfo> tis = null;
    public ArrayList<WordInfo> wis = null;
    CallbackForWordInfo() {}
    public void run() {
        for(int i=0,m=wis.size();i<m;i++){
            WordInfo wi = wis.get(i);
            Log.v("@wi",wi.toString());
        }

    }
}
