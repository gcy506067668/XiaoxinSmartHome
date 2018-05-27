package online.letmesleep.iotbasedhome.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

import online.letmesleep.iotbasedhome.R;

/**
 * Created by Letmesleep on 2018/4/14.
 */

public class SoundPoolUtil {
    public static final int START_LISTENER = 1;
    public static final int STOP_LISTENER = 3;
    public static final int CANCEL_LISTENER = 2;
    public static final int ERROR_LISTENER = 5;
    public static final int SUCCESS_LISTENER = 4;
    private static HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private static SoundPool soundPool =new SoundPool.Builder().setMaxStreams(5).build();

    public static void initSoundPool(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                soundID.put(1,soundPool.load(context,R.raw.bdspeech_recognition_start,1));
                soundID.put(2,soundPool.load(context,R.raw.bdspeech_speech_end,1));
                soundID.put(3,soundPool.load(context,R.raw.bdspeech_recognition_cancel,1));
                soundID.put(4,soundPool.load(context,R.raw.bdspeech_recognition_success,1));
                soundID.put(5,soundPool.load(context,R.raw.bdspeech_recognition_error,1));
            }
        }).start();

    }
    /***
     *
     * @param which
     */
    public static void play(int which){
        soundPool.play(soundID.get(which),1,1,0,0,1);
        }

}
