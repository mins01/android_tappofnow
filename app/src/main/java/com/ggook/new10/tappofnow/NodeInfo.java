package com.ggook.new10.tappofnow;
import com.mins01.java.PickupKeywords.TextInfo;
public class NodeInfo extends  TextInfo{
    public String idEntry = "";
    public String toString() {
        return this.idEntry+","+this.tag + "," + this.text + "," + Long.toString(this.score, 10);
    }
}
