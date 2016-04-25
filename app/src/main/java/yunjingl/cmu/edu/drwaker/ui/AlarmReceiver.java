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

        Intent ring_intent=new Intent(context,RingtonePlayingService.class);
        ring_intent.putExtra("extra",temp );
        context.startService(ring_intent);
    }
}
