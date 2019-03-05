package com.ggook.new10.tappofnow;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;


public class MyA11yService extends android.accessibilityservice.AccessibilityService{

    private static final String TAG = "@@";

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

        Log.e(TAG,"onServiceConnected()");

        super.onServiceConnected();
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
    public void onCreate() {
        super.onCreate();
        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
    }

//    @Override
//    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
//        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
//
//        // 사용자 조작 이벤트가 온다
//        // 조작 대상의 View의 정보가 담긴 객체 취득
//        AccessibilityNodeInfo nodeInfo = accessibilityEvent.getSource();
//
//        // 뒤는 원하는 대로...
//        // 클릭 이벤트를 발생시킨다
////        nodeInfo.performAction(AccessibilityEvent.TYPE_VIEW_CLICKED);
//
//        // 홈 버튼을 누르는 것도 가능
////        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
//        Log.d("@@","GLOBAL_ACTION_HOME");
//
//    }
//
//    @Override
//    public void onInterrupt() {
//        Log.v("@@",this.getClass().getName()+"."+new Object(){}.getClass().getEnclosingMethod().getName());
//
//    }

    @Override public boolean onGesture(int gestureId) {
        Log.d("@@","onGesture"+String.valueOf(gestureId));
        switch (gestureId) {
            case GESTURE_SWIPE_UP_AND_DOWN:
                Log.d("@@","Performing gesture.");
                performGlobalAction(GLOBAL_ACTION_BACK);
                return true;

            default:
                return false;
        }
    }
}
