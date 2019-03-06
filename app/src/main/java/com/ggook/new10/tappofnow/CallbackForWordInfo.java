package com.ggook.new10.tappofnow;

import android.util.Log;
import android.widget.TextView;

import com.mins01.java.PickupKeywords.TextInfo;
import com.mins01.java.PickupKeywords.WordInfo;

import java.util.ArrayList;

public class CallbackForWordInfo implements Runnable {
    public ArrayList<TextInfo> tis = null;
    public ArrayList<WordInfo> wis = null;
    TextView textViewOutput = null;
    CallbackForWordInfo() {}
    public void run() {
        String text = (String) textViewOutput.getText();
        text+="\n";
        for(int i=0,m=wis.size();i<m && i<10;i++){
            WordInfo wi = wis.get(i);
            Log.v("@wi",wi.toString());
            text+=Integer.toString(i+1)+". "+wi.toString()+"\n";
        }
        textViewOutput.setText(text);
    }
}
