package com.rjstudio.dogi.Utilities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.rjstudio.dogi.R;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by r0man on 2018/5/1.
 */

public class VoiceUtility {

    private static SoundPool soundPool;
    private static VoiceUtility voiceUtility;
    private HashMap<Integer,Integer> soundID = new HashMap<Integer,Integer>();

    private  VoiceUtility(Context context) throws InterruptedException {

        soundPool = new SoundPool(19, AudioManager.STREAM_SYSTEM,3);
        //HashMap

        // Number
        soundID.put(1,soundPool.load(context,R.raw.i1,1));
        soundID.put(2,soundPool.load(context,R.raw.i2,1));
        soundID.put(3,soundPool.load(context,R.raw.i3,1));
        soundID.put(4,soundPool.load(context,R.raw.i4,1));
        soundID.put(5,soundPool.load(context,R.raw.i5,1));
        soundID.put(6,soundPool.load(context,R.raw.i6,1));
        soundID.put(7,soundPool.load(context,R.raw.i7,1));
        soundID.put(8,soundPool.load(context,R.raw.i8,1));
        soundID.put(9,soundPool.load(context,R.raw.i9,1));
        soundID.put(10,soundPool.load(context,R.raw.i10,1));

        // After midnight or prior to midnight
        soundID.put(101,soundPool.load(context,R.raw.morning,1));
        soundID.put(102,soundPool.load(context,R.raw.afternoon,1));


        //Other
        soundID.put(103,soundPool.load(context,R.raw.bi,1));
        soundID.put(104,soundPool.load(context,R.raw.bibu,1));
        soundID.put(105,soundPool.load(context,R.raw.du,1));
        soundID.put(106,soundPool.load(context,R.raw.dudu,1));

        //Minuter and Hour
        soundID.put(200,soundPool.load(context,R.raw.nowis,1));
        soundID.put(201,soundPool.load(context,R.raw.drop,1));
        soundID.put(202,soundPool.load(context,R.raw.fen,1));

        Thread.sleep(3000);


    }
    public static VoiceUtility getInstance(Context context)
    {
        if (voiceUtility == null)
        {
            try {
                voiceUtility = new VoiceUtility(context);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return voiceUtility;

    }

    public void playTest(Context context)
    {
        if (context == null)
        {
            Log.d(TAG, "playTest: context is null");
        }

        final int bi = soundPool.load(context,R.raw.bi,1);
        final int nowIs = soundPool.load(context,R.raw.nowis,1);
        final int dudu = soundPool.load(context,R.raw.dudu,1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {


                if (sampleId == dudu)
                {
                    soundPool.play(bi,1,1,0,0,1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    soundPool.play(nowIs,1,1,0,0,1);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    soundPool.play(dudu,1,1,0,0,1);

                }

            }
        });




    }

    public void getCurrentTime(int HOUR,int MINUTER,int SECONDE,int DAY) throws InterruptedException {
        // xian zai shi
        soundPool.play(soundID.get(200),1,1,0,0,1.2f);
        Thread.sleep(1600);

        getNumber(HOUR);

        // dian
        soundPool.play(soundID.get(201),1,1,0,0,1);
        Thread.sleep(1200);

        getNumber(MINUTER);

        soundPool.play(soundID.get(202),1,1,0,0,1);
        Thread.sleep(1000);
    }

    public void getNumber(int number) throws InterruptedException {
        //1 - 60
        int SHI = 0;
        int GE = 0;
        Log.d(TAG, "getNumber: "+number);
        if(number > 9)
        {
            SHI = number / 10;
            GE  = number % 10;
        }
        else
        {
            SHI = 0;
            GE = number;
        }

        Log.d(TAG, "getNumber: SHI="+SHI+":"+GE);
        if(SHI != 0)
        {
            switch(SHI)
            {
                case 1 :
                    break;
                case 2 :
                    soundPool.play(soundID.get(2),1,1,0,0,1.1f);
                    break;
                case 3 :
                    soundPool.play(soundID.get(3),1,1,0,0,1.2f);
                    break;
                case 4:
                    soundPool.play(soundID.get(4),1,1,0,0,1);
                    break;
                case 5 :
                    soundPool.play(soundID.get(5),1,1,0,0,1);
                    break;
                case 6 :
                    soundPool.play(soundID.get(6),1,1,0,0,1);
                    break;
                case 7 :
                    soundPool.play(soundID.get(7),1,1,0,0,1);
                    break;
                case 8 :
                    soundPool.play(soundID.get(8),1,1,0,0,1);
                    break;
                case 9 :
                    soundPool.play(soundID.get(9),1,1,0,0,1);
                    break;

            }
            Thread.sleep(500);
            soundPool.play(soundID.get(10),1,1,0,0,1.17f);
            Thread.sleep(620);
        }

        if(GE != 0)
        {
            switch(GE)
            {
                case 1 :
                    soundPool.play(soundID.get(1),1,1,0,0,1.1f);
                    break;
                case 2 :
                    soundPool.play(soundID.get(2),1,1,0,0,1.1f);
                    break;
                case 3 :
                    soundPool.play(soundID.get(3),1,1,0,0,1.2f);
                    break;
                case 4 :
                    soundPool.play(soundID.get(4),1,1,0,0,1.1f);
                    break;
                case 5 :
                    soundPool.play(soundID.get(5),1,1,0,0,1);
                    break;
                case 6 :
                    soundPool.play(soundID.get(6),1,1,0,0,1.1f);
                    break;
                case 7 :
                    soundPool.play(soundID.get(7),1,1,0,0,1.1f);
                    break;
                case 8 :
                    soundPool.play(soundID.get(8),1,1,0,0,1.1f);
                    break;
                case 9 :
                    soundPool.play(soundID.get(9),1,1,0,0,1.1f);
                    break;
            }
        }

        Thread.sleep(1000);

    }

    public void numberTest() throws InterruptedException {
        for (int i = 10 ; i < 60 ; i++)
        {
            getNumber(i);
        }
    }


}
