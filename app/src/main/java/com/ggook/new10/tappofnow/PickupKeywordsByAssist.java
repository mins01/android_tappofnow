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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
            //callback.wis = wis;

            callback.wis = mergeWordInfo(new ArrayList<WordInfo>(callback.wis.subList(0,Math.min(callback.wis.size(),10))),new ArrayList<WordInfo>(wis.subList(0,Math.min(wis.size(),10)))); //이전 내용과 합쳐서 출력. 10위,10위 합쳐서 출력
            callback.run();

        }
    }


    public PickupKeywordsByAssist(){
        super();
//        conf_scores = (JsonObject)(new JsonParser()).parse(this.jsonString_conf_scores);
        conf_scores.addProperty("android.view.view",1);
        conf_scores.addProperty("android.widget.edittext",100);
        conf_scores.addProperty("node",0);
        conf_scores.addProperty("input",200);
        Log.v("@@con",conf_scores.toString());
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
    public String getUrlFromNodeInfo(ArrayList<NodeInfo> nis,String packagename){
        for (NodeInfo ni : nis){
            if (packagename.equals("com.android.chrome") && ni.idEntry.equals("url_bar")) { //URL이 있는지 체크 안한다.
                if(ni.text.indexOf("http")!=0){ //크롬의 경우 http:// 부분이 빠져서 보임
                    ni.text = "http://"+ni.text;
                }
                Log.v("@ACT","CALL URL-com.android.chrome : "+ni.text);
                return ni.text;
            }else if (packagename.equals("com.android.browser") && ni.idEntry.equals("url")) { //URL이 있는지 체크 안한다.
                Log.v("@ACT","CALL URL-com.android.browser : "+ni.text);
                return ni.text;
            }else if (ni.idEntry.indexOf("url")==0 && ni.text.indexOf("http")==0 ) { //url이라는 id이고 http 로 시작하는 경우
                Log.v("@ACT","CALL URL-??? : "+ni.text);
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
            Log.v("@row",ni.toString()+","+Math.round(viewNode.getTextSize()));
        }



        for(int i2 =0,m2=viewNode.getChildCount();i2<m2;i2++) {
            getNodeInfo(nis,viewNode.getChildAt(i2));
        }
//        return ti;
    }
    /**
     * wis 들을 합친다. 합칠 때 순위를 기준으로 한다.
     * @param wis1
     * @param wis2
     * @return
     */
    public ArrayList<WordInfo> mergeWordInfo(ArrayList<WordInfo> wis1,ArrayList<WordInfo> wis2){
        HashMap<String,WordInfo> hmsw = new HashMap<String,WordInfo>();
        WordInfo wi = null;
//        ArrayList<WordInfo> wis_ = ((ArrayList<WordInfo>)wis1.clone());
//        wis_.addAll(wis2);
//        Log.v("@count",Integer.toString(wis1.size()));
        for(int i=0,m=wis1.size();i<m;i++){
            wi = wis1.get(i);

            WordInfo wi0;
            if(hmsw.containsKey(wi.word)){
                wi0 = hmsw.get(wi.word);
            }else{
                wi0 = new WordInfo();
                wi0.word = wi.word;
                wi0.score = 0;
                hmsw.put(wi.word,wi0);
            }
            wi0.count++;
            wi0.score += (m-i);
//            Log.v("@score",Double.toString(wi0.score)+","+wi.toString()+"::"+wi0.toString());

        }
        for(int i=0,m=wis2.size();i<m;i++){
            wi = wis2.get(i);

            WordInfo wi0;
            if(hmsw.containsKey(wi.word)){
                wi0 = hmsw.get(wi.word);
            }else{
                wi0 = new WordInfo();
                wi0.word = wi.word;
                wi0.score = 0;
                hmsw.put(wi.word,wi0);
            }
            wi0.count++;
            wi0.score += (m-i);
//            Log.v("@score",Double.toString(wi0.score)+","+wi.toString()+"::"+wi0.toString());
        }

        ArrayList<WordInfo> wis = new ArrayList<WordInfo>(hmsw.values());
        Collections.sort(wis, new Comparator<WordInfo>() {
            public int compare(WordInfo lhs, WordInfo rhs) {
                double r = rhs.score - lhs.score;
                if (r == 0.0D) {
                    r = rhs.score / (double)rhs.count - lhs.score / (double)lhs.count;
                }

                if (r < 0.0D) {
                    return -1;
                } else {
                    return r > 0.0D ? 1 : 0;
                }
            }
        });
//        wis.subList(0,Math.min(wis.size(),10));
        return wis;
    }
}