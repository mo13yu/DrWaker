package yunjingl.cmu.edu.drwaker.ui;

/**
 * Created by yunjing on 4/22/16.
 */
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import yunjingl.cmu.edu.drwaker.R;
import yunjingl.cmu.edu.drwaker.adapter.*;

public class RingtonePlayingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    int id;
    boolean isRunning;
    MediaPlayer ring;
    boolean startring;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Log.e("in ring", "in ring");

        startring=true;
        String state=intent.getExtras().getString("extra");
        String ringtone=intent.getExtras().getString("ring_tone");
        boolean loc_switch=intent.getExtras().getBoolean("loc_switch");

        if(loc_switch){
            String la=intent.getExtras().getString("loc_la");
            String lo=intent.getExtras().getString("loc_lo");
            startring=SetLocation.compare(la,lo);
        }

        if(startring){
            assert state!=null;
            if(state.equals("on")){
                id=1;
            }else if(state.equals("off")){

                id=0;
            }else{

                id=0;
            }
            if (!this.isRunning&&id==1){
                ring=createRing(ringtone);
                // ring=MediaPlayer.create(this, R.raw.kiss_the_rain);
                ring.start();
                this.isRunning=true;
                this.id=0;
            }else if(!this.isRunning&&id==0){
                this.isRunning=false;
                this.id=0;
            }else if(this.isRunning&&id==1){
                this.isRunning=true;
                this.id=1;
            }else if(this.isRunning&&id==0){
                ring.stop();
                ring.reset();
                this.isRunning=false;
                this.id=0;
            }
             
        }
        return START_NOT_STICKY;




    }
    public MediaPlayer createRing(String ringname){
        if(ringname.equals("Kiss the rain")){
            return MediaPlayer.create(getApplicationContext(), R.raw.kiss_the_rain);
        }else if(ringname.equals("River that flows in you")){
            return MediaPlayer.create(getApplicationContext(), R.raw.river);
        }else{
            return MediaPlayer.create(getApplicationContext(), R.raw.i_just_wanna_run);
        }
    }
}

