package com.example.dell.alarmalert_csdn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CallAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,AlarmAlert.class);
        Bundle bundle = new Bundle();
        //String content = intent.getStringExtra("content");
        //Log.e("content===sadsad",content);
        bundle.putString("STR_CALLER","");
        intent1.putExtras(bundle);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //如果正在启动的Activity的Task已经在运行的话，那么，新的Activity将不会启动
        //1. 新活动会成为历史栈中的新任务（一组活动）的开始。
        //2. 通常用于具有"launcher"行为的活动：让用户完成一系列事情，完全独立于之前的活动。
        //3. 如果新活动已存在于一个为它运行的任务中，那么不会启动，只会把该任务移到屏幕最前。
        //4. 如果新活动要返回result给启动自己的活动，就不能用这个flag
        context.startActivity(intent1);
    }
}
