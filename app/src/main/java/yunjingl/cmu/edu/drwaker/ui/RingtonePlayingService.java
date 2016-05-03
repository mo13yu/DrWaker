package yunjingl.cmu.edu.drwaker.ui;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import yunjingl.cmu.edu.drwaker.R;

/**
 * service to ring the tone according to alarm settings
 */
public class RingtonePlayingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    int id;
    boolean isRunning;
    MediaPlayer ring;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("in ring", "in ring");


        String state = intent.getExtras().getString("extra");//whether to turn on or turn down the ring tone
        String ringtone = intent.getExtras().getString("ring_tone");//which ring tone to play.

        assert state != null;
        if (state.equals("on")) {
            id = 1;
        } else if (state.equals("off")) {

            id = 0;
        } else {

            id = 0;
        }
        if (!this.isRunning && id == 1) {
            ring = createRing(ringtone);
            ring.start();
            this.isRunning = true;
            this.id = 0;
        } else if (!this.isRunning && id == 0) {
            this.isRunning = false;
            this.id = 0;
        } else if (this.isRunning && id == 1) {
            this.isRunning = true;
            this.id = 1;
        } else if (this.isRunning && id == 0) {
            ring.stop();
            ring.reset();
            this.isRunning = false;
            this.id = 0;
        }
         return START_NOT_STICKY;

    }

    public MediaPlayer createRing(String ringname) {
        if (ringname.equals("Kiss the rain")) {
            return MediaPlayer.create(getApplicationContext(), R.raw.kiss_the_rain);
        } else if (ringname.equals("River that flows in you")) {
            return MediaPlayer.create(getApplicationContext(), R.raw.river);
        } else {
            return MediaPlayer.create(getApplicationContext(), R.raw.i_just_wanna_run);
        }
    }
}

