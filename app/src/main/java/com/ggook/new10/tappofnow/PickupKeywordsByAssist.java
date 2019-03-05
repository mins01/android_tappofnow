package com.ggook.new10.tappofnow;

import android.app.assist.AssistStructure;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mins01.java.PickupKeywords.PickupKeywords;
import com.mins01.java.PickupKeywords.TextInfo;
import com.mins01.java.PickupKeywords.WordInfo;

import java.net.URL;
import java.util.ArrayList;

public class PickupKeywordsByAssist extends PickupKeywords {
    public String jsonString_conf_scores = "{\"android.view.view\":1,\"android.widget.edittext\":100,\"node\":1,\"h1\":50,\"h2\":40,\"h3\":30,\"h4\":20,\"h5\":10,\"h6\":10,\"title\":100,\"span\":5,\"a\":1,\"li\":5,\"meta-description\":50,\"meta-keywords\":50,\"meta-og:title\":100,\"meta-og:description\":25}";

    public Runnable callback = null;

    private class GetUrlTask extends AsyncTask<String, String, String> {
        public PickupKeywordsByAssist pkba = null;
        public CallbackForWordInfo callback = null;
        protected String doInBackground(String... strUrls) {
            Log.v("##","doInBackground");
            int count = strUrls.length;
            Log.v("##",Integer.toString(count));
            Log.v("##",strUrls[0]);

            String html ="";
            for (int i = 0; i < count; i++) {
                try {
                    Log.v("##","getHTML");
                    html = pkba.getHTML(strUrls[i]);
//                    Log.v("##",html+"2");
//                    Log.v("##",strUrls[i]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return html;
        }

        protected void onProgressUpdate(String... text) {
//            Log.v("##","onProgressUpdate");
//            Log.v("##",text[0]);
        }
        protected void onPostExecute(String html) {
            Log.v("##","onPostExecute");
//            Log.v("##",html);
            pkba.setHTML(html);
            ArrayList<TextInfo> tis = pkba.getTexts();
            ArrayList<WordInfo> wis = pkba.getWords(tis);
//            callback.tis = tis;
            callback.wis = wis;
            callback.run();

        }
    }


    public PickupKeywordsByAssist(){
        super();
        conf_scores = (JsonObject)(new JsonParser()).parse(this.jsonString_conf_scores);
        numeric_multiple = 0.1;
    }
    public void setUrlWithCallback(String url,CallbackForWordInfo callback) throws Exception{
        GetUrlTask gut = new GetUrlTask();
        gut.pkba = this;
        gut.callback = callback;
        gut.execute(url);
    }

    /**
     * NodeInfo에서 URL이 있는지 체크
     * @param nis
     * @return
     */
    public String getUrlFromNodeInfo(ArrayList<NodeInfo> nis){
        for (NodeInfo ni : nis){
            if (ni.idEntry.equals("url") && ni.text.indexOf("http") == 0) { //URL이 있는지 체크 안한다.
                Log.v("@ACT","CALL URL:"+ni.text);
                return ni.text;
            }
        }
        Log.v("@ACT","No- URL");
        return null;
    }
    public ArrayList<NodeInfo> getNodeInfoByViewNode(AssistStructure.ViewNode viewNode) throws Exception {
        ArrayList<NodeInfo> nis= new ArrayList<NodeInfo>();
        getNodeInfo(nis,viewNode);
        return nis;
    }
    public ArrayList<TextInfo> nodeInfoToTextInfo(ArrayList<NodeInfo> nis){
        ArrayList<TextInfo> tis = new ArrayList<TextInfo>();
        for (NodeInfo ni : nis){
            tis.add((TextInfo) ni);
        }
        return tis;
    }

    public void getNodeInfo(ArrayList<NodeInfo> nis, AssistStructure.ViewNode viewNode){
        if (viewNode.getVisibility() != View.VISIBLE) {
//            Log.v("@@ ",viewNode.getClassName()+":hidden");
            return;
        }
        String getIdEntry = "",getText="",getContentDescription="",getClassName="node";
        if(viewNode.getIdEntry() != null){
            getIdEntry = viewNode.getIdEntry();
        }
        if(viewNode.getText() != null && viewNode.getText().length() > 0){
            getText = viewNode.getText().toString().trim();
        }
        if(viewNode.getContentDescription() != null){
            getContentDescription = viewNode.getContentDescription().toString();
        }
        if(viewNode.getClassName() != null){
            getClassName = viewNode.getClassName().toLowerCase();
        }

//        int tss = viewNode.getTextSelectionStart();
//        int tse = viewNode.getTextSelectionEnd();
        int score = conf_scores.has(getIdEntry) ? conf_scores.get(getIdEntry).getAsInt():1;
        NodeInfo ni = null;

        if(getText != "" && getText.length()>0 && viewNode.getTop() >= 0 && viewNode.getLeft() >= 0){
//            Log.v("@1",getText);
            ni = new NodeInfo();
            ni.tag = getClassName;
            ni.text = getText;
            ni.idEntry = getIdEntry;
            ni.score = Math.round(viewNode.getWidth()*viewNode.getHeight()/100)*score;

            nis.add(ni);
//            Log.v("@raw",viewNode.getClassName()+"\t/"+getIdEntry+"\t/"+getText+"\t/"+getContentDescription+"\t/"+viewNode.getWidth()+"/\t"+viewNode.getHeight()+"/\t"+viewNode.getTop()+"/\t"+viewNode.getLeft()+"\t/"+viewNode.getTextSize());
//            Log.v("@row",ni.toString()+","+Math.round(viewNode.getTextSize()));
        }



        for(int i2 =0,m2=viewNode.getChildCount();i2<m2;i2++) {
            getNodeInfo(nis,viewNode.getChildAt(i2));
        }
//        return ti;
    }
}