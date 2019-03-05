package com.ggook.new10.tappofnow;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService  extends android.accessibilityservice.AccessibilityService{
    private static final String TAG = "@@";
    private static MyAccessibilityService thisService;

    public static AccessibilityService getInstance() {
        return thisService;
    }

    // 이벤트 발생 시 호출
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.e(TAG,"Catch Event Package Name : "+accessibilityEvent.getPackageName());
        Log.e(TAG,"Catch Event TEXT : "+ accessibilityEvent.getText());
        Log.e(TAG,"Catch Event ContentDescription : "+ accessibilityEvent.getContentDescription());
        Log.e(TAG,"Catch Event getSource : "+ accessibilityEvent.getSource());

    }

    // 접근성 관한을 가지고 연결이 디면 호출
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        thisService = this;
        Log.e(TAG,"onServiceConnected()");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; //전체 이벤트 가져오기
        info.feedbackType = AccessibilityServiceInfo.DEFAULT |AccessibilityServiceInfo.FEEDBACK_HAPTIC;
        info.notificationTimeout = 100;// milisecond
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG,"onInterrupt()");
    }

    @Override
    protected boolean onGesture(int gestureId) {
        Log.w("@@","onGesture : "+String.valueOf(gestureId));
        return super.onGesture(gestureId);

    }
}
