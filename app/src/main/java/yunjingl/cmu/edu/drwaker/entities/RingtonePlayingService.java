package yunjingl.cmu.edu.drwaker.entities;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import yunjingl.cmu.edu.drwaker.R;

/**
 * Created by yunjing on 4/13/16.
 */
public class RingtonePlayingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Log.e("in ring", "in ring");
        MediaPlayer ring=MediaPlayer.create(this, R.raw.kiss_the_rain);
        ring.start();
        return START_NOT_STICKY;
    }
}
