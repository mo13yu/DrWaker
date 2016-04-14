package yunjingl.cmu.edu.drwaker.entities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yunjing on 4/12/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

     Log.e("in receiver", "ye");

        Intent ring_intent=new Intent(context,RingtonePlayingService.class);
        context.startService(ring_intent);
    }
}
