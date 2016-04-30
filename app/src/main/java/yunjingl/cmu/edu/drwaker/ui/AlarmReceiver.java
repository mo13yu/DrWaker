package yunjingl.cmu.edu.drwaker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yunjing on 4/22/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("in receiver", "ye");
        String temp=intent.getExtras().getString("extra");
        String method=intent.getExtras().getString("wake_up_method");
        String ringtone=intent.getExtras().getString("ring_tone");

        Intent ring_intent=new Intent(context,RingtonePlayingService.class);
        ring_intent.putExtra("extra",temp);
        ring_intent.putExtra("ring_tone",ringtone);
        context.startService(ring_intent);

        Intent stopintent=new Intent();
        if(method.equals("math")){
            stopintent=new Intent(context,MathActivity.class);
        }else if(method.equals("facial")){
            stopintent=new Intent(context,SelfieActivity.class);
        }
        stopintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(stopintent);
    }
}
