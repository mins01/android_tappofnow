package com.ggook.new10.tappofnow;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mins01.java.PickupKeywords.TextInfo;
import com.mins01.java.PickupKeywords.WordInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static android.support.v4.content.ContextCompat.startActivity;

public class AssistLoggerSession extends VoiceInteractionSession {
    private View view;
    private ArrayList<String> selectedContentList = new ArrayList<>() ;

    public AssistLoggerSession(Context context) {
        super(context);
        Log.v("@@","AssistLoggerSession.AssistLoggerSession");

    }
    @Override
    public void onHandleScreenshot(@Nullable Bitmap screenshot) {
        super.onHandleScreenshot(screenshot);
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());

        //스크린샷을 처리하는 부분입니다.

    }

    @Override
    public View onCreateContentView() {
        super.onCreateContentView();
//        view = getLayoutInflater().inflate(R.layout.activity_main,null);
        view = getLayoutInflater().inflate(R.layout.assist_main,null);
//        view = new CustomView(getContext());
        setContentView(view);

        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());

        Button btnTest = view.findViewById(R.id.btnTest);
        Button btnAssistSetting = view.findViewById(R.id.btnAssistSetting);
        Button btnExit = view.findViewById(R.id.btnExit);
        Button btnTest2 = view.findViewById(R.id.btnTest2);

        btnTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
                if(lastStructure != null){
//                    getHint(lastStructure.getWindowNodeAt(0).getRootViewNode());
                    getContentText(lastStructure);
                }

            }
        });
        btnAssistSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(this, AssistLoggerSession.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
                Intent intent= new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(getContext(),intent,null);

                finish();
            }
        });

        btnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyAccessibilityService.getInstance() != null){
                    MyAccessibilityService.getInstance().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    Log.d("@@","AccessibilityService.GLOBAL_ACTION_BACK");
                }else{
                    Log.d("@@","MyAccessibilityService.getInstance() == null");
                }

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // runChrome();
                finish();
            }
        });
        return view;
    }

    public void getContentText(AssistStructure structure){
        selectedContentList.clear();
//        HashMap<String,Integer> contentMap = new HashMap<String,Integer>();
        TextView tv = (TextView) view.findViewById(R.id.textView2);
        ArrayList<String> contentList = new ArrayList<>();
//        getText(contentList,structure.getWindowNodeAt(0).getRootViewNode());
        PickupKeywordsByAssist pkba = new PickupKeywordsByAssist();
        pkba.numeric_multiple = 0.1;
        ArrayList<NodeInfo> nis = null;
        ArrayList<TextInfo> tis = null;
        ArrayList<WordInfo> wis = null;

        String url = null;
//        Runnable r2 = () -> { System.out.println("222");};


        CallbackForWordInfo callback = new CallbackForWordInfo();


        try {
            nis = pkba.getNodeInfoByViewNode(structure.getWindowNodeAt(0).getRootViewNode());
            url = pkba.getUrlFromNodeInfo(nis);
            if(url != null &&  !url.isEmpty()){ //URL이 포함 안된 경우
                Log.v("@@","Found Url");
                pkba.setUrlWithCallback(url,callback);
            }else{
                tis = pkba.nodeInfoToTextInfo(nis);
                wis = pkba. getWords(tis);
                callback.wis = wis;
                callback.run();
            }


//            for(int i=0,m=tis.size();i<m;i++){
//                Log.v("@text",tis.get(i).toString());
//            }
//            for(int i=0,m=wis.size();i<m;i++){
//                Log.v("@word",wis.get(i).toString());
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        JSONArray jsonSelectedContent = new JSONArray (selectedContentList);
//        Log.v("@@","jsonSelectedContent="+jsonSelectedContent.toString().length()+"\t"+jsonSelectedContent.toString());

    }


    @TargetApi(Build.VERSION_CODES.O)
    public void getHint(AssistStructure.ViewNode viewNode){
        if (viewNode.getVisibility() != View.VISIBLE) {
//            Log.v("@@ ",viewNode.getClassName()+":hidden");
            return;
        }
        String getIdEntry = "",getText="",getContentDescription="";
        if(viewNode.getIdEntry() != null){
            getIdEntry = viewNode.getIdEntry();
        }
        if(viewNode.getText() != null){
            getText = viewNode.getText().toString();
        }
        if(viewNode.getContentDescription() != null){
            getContentDescription = viewNode.getContentDescription().toString();
        }
        Log.v("@@",viewNode.getClassName()+"\t/"+getIdEntry+"\t/"+getText+"\t/"+getContentDescription);

        for(int i2 =0,m2=viewNode.getChildCount();i2<m2;i2++) {
            getHint(viewNode.getChildAt(i2));
        }

    }

    public void runChrome(){
        Context ct = getContext();
        PackageManager pm  = ct.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("com.android.chrome");
        ct.startActivity(intent);


    }

    private Bundle lastData;
    private AssistStructure lastStructure;
    private AssistContent lastContent;
    @Override
    public void onHandleAssist(@Nullable Bundle data, @Nullable AssistStructure structure, @Nullable AssistContent content) {
        super.onHandleAssist(data, structure, content);

        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());

        Log.v("@@","CALL PACKAGE : "+structure.getActivityComponent().getPackageName());
        ((TextView) view.findViewById(R.id.textView2)).setText("");
        ((TextView) view.findViewById(R.id.textView2)).setText(structure.getActivityComponent().getPackageName());

        lastData = data;
        lastStructure = structure;
        lastContent = content;
        Toast.makeText(getContext(),R.string.msg_blocked,Toast.LENGTH_LONG).show();

        if(data==null){
            Log.v("@@","data is null");
        }else{
            Log.v("@@","data is not null");
        }

        if(structure==null){
            Log.v("@@","structure is null");
        }else{
            Log.v("@@","structure is not null");
            AssistStructure.WindowNode wn = structure.getWindowNodeAt(0);
            Log.v("@@","wn.getTitle().toString() : "+wn.getTitle().toString());
//            getHint(wn.getRootViewNode());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                long endTime = structure.getAcquisitionEndTime();
                long startTime = structure.getAcquisitionStartTime();
                boolean homeActivity = structure.isHomeActivity();
            }else{
                long endTime = 0;
                long startTime = 0;
                boolean homeActivity = false;
            }

            ComponentName cn = structure.getActivityComponent();

            int wnc = structure.getWindowNodeCount();
            int dec = structure.describeContents();
        }



        try {
            // Fetch structured data
            JSONObject structuredData = null;
            if (content != null) {
                String v = content.getStructuredData();
                if(v != null){
                    structuredData = new JSONObject(content.getStructuredData());
                    // Display description as Toast
                    Toast.makeText(
                            getContext(),
                            structuredData.optString("description"),
                            Toast.LENGTH_LONG
                    ).show();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = content.getIntent();
    }

    @Override
    public void onHandleAssistSecondary(@Nullable Bundle data, @Nullable AssistStructure structure, @Nullable AssistContent content, int index, int count) {
        super.onHandleAssistSecondary(data, structure, content, index, count);
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());

        Log.v("@@",String.valueOf(index)+":"+String.valueOf(count));
    }
}
