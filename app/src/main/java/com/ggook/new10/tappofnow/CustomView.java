package com.ggook.new10.tappofnow;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewStructure;

public class CustomView extends android.support.constraint.ConstraintLayout {
    public CustomView(Context context) {
        super(context);
        init((AttributeSet) null);

    }
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }


    private void init(AttributeSet attrs){
        inflate(getContext(), R.layout.assist_main,this);
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());

    }


    @Override
    public void onProvideVirtualStructure(ViewStructure structure) {
        super.onProvideVirtualStructure(structure);
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());

    }
}
