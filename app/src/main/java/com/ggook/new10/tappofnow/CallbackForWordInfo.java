package com.ggook.new10.tappofnow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mins01.java.PickupKeywords.TextInfo;
import com.mins01.java.PickupKeywords.WordInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CallbackForWordInfo implements Runnable {
    public ArrayList<TextInfo> tis = null;
    public ArrayList<WordInfo> wis = null;
    public Context context = null;
    public LinearLayout llr = null;
    CallbackForWordInfo() {}
    public void callUrlWithGoogle(String keyword) throws UnsupportedEncodingException {
        String enc_keyword = URLEncoder.encode(keyword,"utf-8");
        String Url = "https://www.google.com/search?q="+enc_keyword;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        context.startActivity(intent);
    }

    public void run() {

        Log.v("@callback","run");
        for(int i=0,m=wis.size();i<m && i<10;i++){
            WordInfo wi = wis.get(i);

            Button button = new Button(context);
            button.setText("["+(i+1)+"]search : "+wi.toString());
            button.setTextSize(12);
            button.setTransformationMethod(null);
            button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            button.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String keyword = wi.word;
                    try {
                        callUrlWithGoogle(keyword);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            llr.addView(button,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            Log.v("@wi-url","[url]"+Integer.toString(i+1)+". "+wi.toString());
        }


    }
}
