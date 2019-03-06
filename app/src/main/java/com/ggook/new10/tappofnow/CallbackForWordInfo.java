package com.ggook.new10.tappofnow;

import android.util.Log;
import android.widget.TextView;

import com.mins01.java.PickupKeywords.TextInfo;
import com.mins01.java.PickupKeywords.WordInfo;

import java.util.ArrayList;

public class CallbackForWordInfo implements Runnable {
    public ArrayList<TextInfo> tis = null;
    public ArrayList<WordInfo> wis = null;
    TextView tv = null;
    CallbackForWordInfo() {}
    public void run() {
        tv.append("RUN");
        Log.v("@callback","run");
//        textViewOutput.append("\n");
        String text = "\n";
        for(int i=0,m=wis.size();i<m && i<10;i++){
            WordInfo wi = wis.get(i);

//            textViewOutput.append("[url]"+Integer.toString(i+1)+". "+wi.toString()+"\n");
            text +="[url]"+Integer.toString(i+1)+". "+wi.toString()+"\n";
            Log.v("@wi-url","[url]"+Integer.toString(i+1)+". "+wi.toString());
        }
        tv.append(text);

    }
}
